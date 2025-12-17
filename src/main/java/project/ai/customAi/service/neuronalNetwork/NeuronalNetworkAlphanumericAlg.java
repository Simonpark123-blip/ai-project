package project.ai.customAi.service.neuronalNetwork;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.pojo.neuronalNetwork.NetworkParameter.AlphanumericNetworkParameter;
import project.ai.customAi.pojo.neuronalNetwork.TrainingParameter.AlphanumericTrainingParameter;
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
@Profile("nnAlphanumeric")
public class NeuronalNetworkAlphanumericAlg implements AiAlgorithm {

    @Override
    public Map<String, String> handleAlgorithm(String logicalOperation, Map<String, String> data) {
        try {
            Instant started = Instant.now();

            AlphanumericFFN neuronalNetwork = new AlphanumericFFN(
                    AlphanumericNetworkParameter.numberOfInputSignals,
                    AlphanumericNetworkParameter.numberOfNeuronsInHiddenLayer,
                    AlphanumericNetworkParameter.numberOfNeuronsInOutputLayer
            );

            DisplayMachineLearning.showWeights(neuronalNetwork.getWeightsOfHiddenLayer(), neuronalNetwork.getWeightsOfOutputLayer());
            neuronalNetwork.testAllInputsAndShowResults(AlphanumericTrainingParameter.inputs, AlphanumericTrainingParameter.targets);

            neuronalNetwork.trainWithSupervisedLearning();

            DisplayMachineLearning.showWeights(neuronalNetwork.getWeightsOfHiddenLayer(), neuronalNetwork.getWeightsOfOutputLayer());
            neuronalNetwork.testAllInputsAndShowResults(AlphanumericTrainingParameter.inputs, AlphanumericTrainingParameter.targets);

            neuronalNetwork.testAllInputsAndShowResults(AlphanumericTrainingParameter.testInputs, AlphanumericTrainingParameter.testTargets);

            Instant finished = Instant.now();
            log.info("Algorithm execution time: {}ms", Duration.between(started, finished).toMillis());
            
            return null;
        } catch (Exception e) {
            log.error("Error processing CSV", e);
            throw e;
        }
    }

}
