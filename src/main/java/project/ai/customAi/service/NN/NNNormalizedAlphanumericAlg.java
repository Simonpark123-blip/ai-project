package project.ai.customAi.service.NN;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.pojo.NN.TrainingParameter.AlphanumericNormalizedTrainingParameter;
import project.ai.customAi.pojo.NN.NetworkParameter.AlphanumericNormalizedNetworkParameter;
import project.ai.customAi.service.AiAlgorithm;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("nnNormalizedAlphanumeric")
public class NNNormalizedAlphanumericAlg implements AiAlgorithm {

    @Override
    public Map<String, String> handleAlgorithm(String logicalOperation, Map<String, String> data) {
        try {
            Instant started = Instant.now();

            AlphanumericNormalizedFFN neuronalNetwork = new AlphanumericNormalizedFFN(
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

            return null;
        } catch (Exception e) {
            log.error("Error processing CSV", e);
            throw e;
        }
    }

}
