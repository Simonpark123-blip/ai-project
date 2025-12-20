package project.ai.customAi.service.NN;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.pojo.NN.NetworkParameter.AlphanumericNetworkParameter;
import project.ai.customAi.pojo.NN.TrainingParameter.AlphanumericTrainingParameter;
import project.ai.customAi.service.AiAlgorithm;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("nnAlphanumeric")
public class NNAlphanumericAlg implements AiAlgorithm {

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
