package project.ai.customAi.service.perceptron;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.pojo.ProcessMonitoring;
import project.ai.customAi.pojo.TrainingParameter;
import project.ai.customAi.service.AiAlgorithm;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("perceptronUsage")
public class PerceptronAlg implements AiAlgorithm {

    @Override
    public Map<String, String> handleAlgorithm(String logicalOperation, Map<String, String> data) {
        try {
            int epochs = data != null && data.containsKey("epochs") ? Integer.parseInt(data.get("epochs")) : 100;
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

            return Map.of("percentage", String.valueOf(percentage));
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
