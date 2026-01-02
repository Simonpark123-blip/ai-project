package project.ai.customAi.service.NN;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.pojo.NN.NetworkParameter.FullwordNetworkParameterV2;
import project.ai.customAi.pojo.NN.TrainingParameter.FullwordTrainingParameterV2;
import project.ai.customAi.service.AiAlgorithm;
import project.ai.customAi.service.fullword.FeatureCalculation;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
                String inputWord = FullwordTrainingParameterV2.testInput.get(i).get(0);
                String expectedTarget = FullwordTrainingParameterV2.testInput.get(i).get(1);

                double bestScore = Double.NEGATIVE_INFINITY;
                String bestCandidate = null;

                for (String dictCandidate : FullwordTrainingParameterV2.dictionary) {
                    // normalize
                    String candNorm = dictCandidate.toLowerCase(Locale.ROOT);
                    double[] features = featureCalculation.extractFeatures(inputWord.toLowerCase(Locale.ROOT), candNorm);

                    // forward + read score
                    double score = neuronalNetwork.predictScore(features);
                    if (score > bestScore) {
                        bestScore = score;
                        bestCandidate = dictCandidate;
                    }
                }

                boolean match = expectedTarget.equalsIgnoreCase(bestCandidate);
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
            return result;
        } catch (Exception e) {
            log.error("Error processing NNFullwordAlgV2", e);
            throw e;
        }
    }
}