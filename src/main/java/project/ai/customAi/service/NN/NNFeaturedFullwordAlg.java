package project.ai.customAi.service.NN;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import project.ai.customAi.pojo.NN.NetworkParameter.FeaturedFullwordNetworkParameter;
import project.ai.customAi.pojo.NN.TrainingParameter.FeaturedFullwordTrainingParameter;
import project.ai.customAi.service.AiAlgorithm;
import project.ai.customAi.service.fullword.FeatureCalculation;
import project.ai.customAi.service.fullword.FullwordPreperator;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class NNFeaturedFullwordAlg implements AiAlgorithm {

    private final FeatureCalculation featureCalculation;

    public NNFeaturedFullwordAlg(){
        featureCalculation = new FeatureCalculation();
    }

    @Override
    public Map<String, String> handleAlgorithm(String logicalOperation, Map<String, String> data, AtomicInteger epochs) {
        try {
            Instant started = Instant.now();

            FeaturedFullwordFFN neuronalNetwork = new FeaturedFullwordFFN(
                    FeaturedFullwordNetworkParameter.numberOfInputSignals,
                    FeaturedFullwordNetworkParameter.numberOfNeuronsInHiddenLayer,
                    FeaturedFullwordNetworkParameter.numberOfNeuronsInOutputLayer
            );

            DisplayMachineLearning.showWeights(neuronalNetwork.getWeightsOfHiddenLayer(), neuronalNetwork.getWeightsOfOutputLayer());

            // Build training inputs
            double[][] inputs = new double[FeaturedFullwordTrainingParameter.input.size()][];
            for (int i = 0; i < FeaturedFullwordTrainingParameter.input.size(); i++) {
                String inputWord = FeaturedFullwordTrainingParameter.input.get(i).get(0);
                String candidateWord = FeaturedFullwordTrainingParameter.input.get(i).get(1);

                double[] features = featureCalculation.extractFeatures(inputWord.toLowerCase(Locale.ROOT), candidateWord.toLowerCase(Locale.ROOT));
                inputs[i] = features;

                log.info("Built features for training pair input='{}' candidate='{}' -> {}", inputWord, candidateWord, features);
            }

            neuronalNetwork.testAllInputsAndShowResults(inputs, FeaturedFullwordTrainingParameter.targets);

            neuronalNetwork.trainWithSupervisedLearning(inputs, epochs);

            DisplayMachineLearning.showWeights(neuronalNetwork.getWeightsOfHiddenLayer(), neuronalNetwork.getWeightsOfOutputLayer());
            neuronalNetwork.testAllInputsAndShowResults(inputs, FeaturedFullwordTrainingParameter.targets);

            // For each input, iterate entire dictionary, compute score for each dictionary word,
            //    pick the highest-scoring dictionary word and compare it with the expected target from training data
            int correct = 0;
            int total = FeaturedFullwordTrainingParameter.testInput.size();

            for (int i = 0; i < total; i++) {
                String inputWord = FullwordPreperator.cleanWord(FeaturedFullwordTrainingParameter.testInput.get(i).get(0));
                String expectedTarget = FullwordPreperator.cleanWord(FeaturedFullwordTrainingParameter.testInput.get(i).get(1));

                double bestScore = Double.NEGATIVE_INFINITY;
                String bestCandidate = null;

                for (String dictCandidate : FeaturedFullwordTrainingParameter.dictionary) {
                    // normalize
                    String candNorm = FullwordPreperator.cleanWord(dictCandidate);
                    double[] features = featureCalculation.extractFeatures(inputWord.toLowerCase(Locale.ROOT), candNorm);

                    // forward + read score
                    double score = neuronalNetwork.predictScore(features);
                    if (score > bestScore) {
                        bestScore = score;
                        bestCandidate = dictCandidate;
                    }
                }

                assert bestCandidate != null;
                boolean match = expectedTarget.equals(FullwordPreperator.cleanWord(bestCandidate));
                if (match) correct++;

                log.info("Input='{}' expected='{}' => predicted='{}' (score={}) match={}", inputWord, expectedTarget, bestCandidate, bestScore, match);
            }

            double accuracy = total == 0 ? 0.0 : (double) correct / total;
            log.info("Prediction accuracy on {} cases: {} ({} / {})", total, accuracy, correct, total);

            Instant finished = Instant.now();
            log.info("Algorithm execution time: {}ms", Duration.between(started, finished).toMillis());

            Map<String, String> result = new HashMap<>();
            result.put("accuracy", String.valueOf(accuracy));
            result.put("correct", String.valueOf(correct));
            result.put("total", String.valueOf(total));

            String inputWord = FullwordPreperator.cleanWord("verseentlic");//gehen, testn, opfel, abbel, vegadiv, negetiv
            double bestScore = Double.NEGATIVE_INFINITY;

            // prefilter using levenshtein-distance
            List<String> candidates = new ArrayList<>();
            int distance = 2;
            while(candidates.isEmpty()) {
                int finalDistance = distance;
                candidates = FeaturedFullwordTrainingParameter.fullDictionary
                        .stream()
                        .map(String::toLowerCase)
                        .distinct()
                        .filter(w -> featureCalculation.isDistanceLE(inputWord, w, finalDistance))
                        .limit(200)
                        .toList();

                distance++;
            }

            @Data
            @AllArgsConstructor
            class Candidate {
                private String value;
                private double score;
            }

            List<Candidate> bestCandidates = new ArrayList<>();
            for (String candidate : candidates) {
                // normalize
                String candNorm = FullwordPreperator.cleanWord(candidate);
                double[] features = featureCalculation.extractFeatures(inputWord.toLowerCase(Locale.ROOT), candNorm);

                // forward + read score
                double score = neuronalNetwork.predictScore(features);
                Candidate c = new Candidate(candidate, score);
                log.debug("{}", score);
                if (score > bestScore) {
                    bestScore = score;
                }
                bestCandidates.add(c);
            }

            bestCandidates.sort(Comparator.comparing(Candidate::getScore).reversed());
            log.info("Best candidate is: {}", bestCandidates.getFirst().getValue());
            log.info("Best score is: {}", bestScore);
            log.info("All {} good candidates (best -> worst): {}", bestCandidates.size(), bestCandidates.toArray());

            return result;
        } catch (Exception e) {
            log.error("Error processing NNFeaturedFullwordAlg", e);
            throw e;
        }
    }
}