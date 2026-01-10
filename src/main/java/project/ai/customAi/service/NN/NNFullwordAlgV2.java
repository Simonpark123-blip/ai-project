package project.ai.customAi.service.NN;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.pojo.NN.NetworkParameter.FullwordNetworkParameterV2;
import project.ai.customAi.pojo.NN.TrainingParameter.FullwordTrainingParameterV2;
import project.ai.customAi.service.AiAlgorithm;
import project.ai.customAi.service.fullword.FeatureCalculation;
import project.ai.customAi.service.fullword.FullwordPreperator;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("nnFullwordV2")
public class NNFullwordAlgV2 implements AiAlgorithm {

    private final FeatureCalculation featureCalculation;

    @Override
    public Map<String, String> handleAlgorithm(String logicalOperation, Map<String, String> data) {
        try {
            Instant started = Instant.now();

            FullwordFFNV2 neuronalNetwork = new FullwordFFNV2(
                    FullwordNetworkParameterV2.numberOfInputSignals,
                    FullwordNetworkParameterV2.numberOfNeuronsInHiddenLayer,
                    FullwordNetworkParameterV2.numberOfNeuronsInOutputLayer
            );

            DisplayMachineLearning.showWeights(neuronalNetwork.getWeightsOfHiddenLayer(), neuronalNetwork.getWeightsOfOutputLayer());

            // Build training inputs
            double[][] inputs = new double[FullwordTrainingParameterV2.input.size()][];
            for (int i = 0; i < FullwordTrainingParameterV2.input.size(); i++) {
                String inputWord = FullwordTrainingParameterV2.input.get(i).get(0);
                String candidateWord = FullwordTrainingParameterV2.input.get(i).get(1);

                double[] features = featureCalculation.extractFeatures(inputWord.toLowerCase(Locale.ROOT), candidateWord.toLowerCase(Locale.ROOT));
                inputs[i] = features;

                log.info("Built features for training pair input='{}' candidate='{}' -> {}", inputWord, candidateWord, features);
            }

            neuronalNetwork.testAllInputsAndShowResults(inputs, FullwordTrainingParameterV2.targets);

            neuronalNetwork.trainWithSupervisedLearning(inputs);

            DisplayMachineLearning.showWeights(neuronalNetwork.getWeightsOfHiddenLayer(), neuronalNetwork.getWeightsOfOutputLayer());
            neuronalNetwork.testAllInputsAndShowResults(inputs, FullwordTrainingParameterV2.targets);

            // For each input, iterate entire dictionary, compute score for each dictionary word,
            //    pick the highest-scoring dictionary word and compare it with the expected target from training data
            int correct = 0;
            int total = FullwordTrainingParameterV2.testInput.size();

            for (int i = 0; i < total; i++) {
                String inputWord = FullwordPreperator.cleanWord(FullwordTrainingParameterV2.testInput.get(i).get(0));
                String expectedTarget = FullwordPreperator.cleanWord(FullwordTrainingParameterV2.testInput.get(i).get(1));

                double bestScore = Double.NEGATIVE_INFINITY;
                String bestCandidate = null;

                for (String dictCandidate : FullwordTrainingParameterV2.dictionary) {
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

            String inputWord = FullwordPreperator.cleanWord("Abbel");//gehen, testn, opfel,
            double bestScore = Double.NEGATIVE_INFINITY;

            // prefilter using levenshtein-distance
            List<String> candidates = FullwordTrainingParameterV2.fullDictionary
                    .stream()
                    .filter(w -> featureCalculation.isDistanceLE(inputWord, w, 2))
                    .limit(200)
                    .toList();

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
            log.error("Error processing NNFullwordAlgV2", e);
            throw e;
        }
    }
}