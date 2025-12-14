package project.ai.customAi.service.neuronalNetwork;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.pojo.neuronalNetwork.AlphanumericNormalizedNetworkParameter;
import project.ai.customAi.pojo.neuronalNetwork.AlphanumericNormalizedTrainingParameter;
import project.ai.customAi.pojo.perceptron.TrainingParameter;
import project.ai.customAi.service.AiAlgorithm;
import project.ai.customAi.service.perceptron.ActivationFunction;
import project.ai.customAi.service.perceptron.LogicalOperation;
import project.ai.customAi.service.perceptron.MachineLearning;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("neuronalNetworkNormalizedAlphanumericUsage")
public class NeuronalNetworkNormalizedAlphanumericAlg implements AiAlgorithm {

    @Override
    public Map<String, String> handleAlgorithm(String logicalOperation, Map<String, String> data) {
        try {
            Instant started = Instant.now();
// TODO: feedforwardNetwork abstrahieren
            AlphanumericNormalizedFeedForwardNetwork neuronalNetwork = new AlphanumericNormalizedFeedForwardNetwork(
                    AlphanumericNormalizedNetworkParameter.numberOfInputSignals,
                    AlphanumericNormalizedNetworkParameter.numberOfNeuronsInHiddenLayer,
                    AlphanumericNormalizedNetworkParameter.numberOfNeuronsInOutputLayer
            );

            DisplayMachineLearning.showWeights(neuronalNetwork.getWeightsOfHiddenLayer(), neuronalNetwork.getWeightsOfOutputLayer());
            neuronalNetwork.testAllInputsAndShowResults(AlphanumericNormalizedTrainingParameter.inputs, AlphanumericNormalizedTrainingParameter.targets);

            neuronalNetwork.trainWithSupervisedLearning();

            DisplayMachineLearning.showWeights(neuronalNetwork.getWeightsOfHiddenLayer(), neuronalNetwork.getWeightsOfOutputLayer());
            neuronalNetwork.testAllInputsAndShowResults(AlphanumericNormalizedTrainingParameter.inputs, AlphanumericNormalizedTrainingParameter.targets);

            neuronalNetwork.testAllInputsAndShowResults(AlphanumericNormalizedTrainingParameter.testInputs, AlphanumericNormalizedTrainingParameter.testTargets);

            Instant finished = Instant.now();
            log.info("Algorithm execution time: {}ms", Duration.between(started, finished).toMillis());

            /*int epochs = data != null && data.containsKey("epochs") ? Integer.parseInt(data.get("epochs")) : 100;
            MachineLearning machineLearning = getMachineLearning(LogicalOperation.valueOf(logicalOperation), epochs);

            // do algorithm
            log.info("Weights for neuron before training: {}, bias: {}", ProcessMonitoring.lastWeights,
                    ProcessMonitoring.lastWeights[ProcessMonitoring.lastWeights.length - 1]);
            // forward-pass
            machineLearning.testAllInputsAndShowResults();

            // backward-pass
            machineLearning.trainWithSupervisedLearning();

            log.info("Weights for neuron after training: {}, bias: {}", ProcessMonitoring.lastWeights,
                    ProcessMonitoring.lastWeights[ProcessMonitoring.lastWeights.length - 1]);
            // forward-pass
            double percentage = machineLearning.testAllInputsAndShowResults();

            return Map.of("percentage", String.valueOf(percentage));*/
            return null;
        } catch (Exception e) {
            log.error("Error processing CSV", e);
            throw e;
        }
    }

    private static MachineLearning getMachineLearning(LogicalOperation logicalOperation, int epochs) {
        double[][] inputs = new double[][]{
                                    {0, 0},
                                    {1, 0},
                                    {0, 1},
                                    {1, 1}
                            };

        double[] targets = switch (logicalOperation) {
            case OR -> new double[]{0, 1, 1, 1};
            case XOR -> new double[]{0, 1, 1, 0};
            default -> new double[]{0, 0, 0, 1};
        };

        return new MachineLearning(
                new TrainingParameter(epochs, 0.5, ActivationFunction.HEAVISIDE, inputs, targets)
        );
    }
}
