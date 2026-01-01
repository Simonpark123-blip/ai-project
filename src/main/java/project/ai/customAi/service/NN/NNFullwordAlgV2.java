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

            double[][] inputs = new double[FullwordTrainingParameterV2.input.size()][];
            for(int i = 0; i < FullwordTrainingParameterV2.input.size(); i++) {
                String input = FullwordTrainingParameterV2.input.get(i).getFirst();
                String candidate = FullwordTrainingParameterV2.input.get(i).getLast();

                inputs[i] = featureCalculation.extractFeatures(input, candidate);
                log.info("editDistance is {} for candidate {} and input {}", inputs[i][0], candidate, input);
                log.info("lengthDiff is {} for candidate {} and input {}", inputs[i][1], candidate, input);
                log.info("sameLength is {} for candidate {} and input {}", inputs[i][2], candidate, input);
                log.info("overlappingChars is {} for candidate {} and input {}", inputs[i][3], candidate, input);
                log.info("overlappingNGrams is {} for candidate {} and input {}", inputs[i][4], candidate, input);
                log.info("matchingPrefixChars is {} for candidate {} and input {}", inputs[i][5], candidate, input);
                log.info("matchingSuffixChars is {} for candidate {} and input {}", inputs[i][6], candidate, input);
                log.info("vowelPatternMatch is {} for candidate {} and input {}", inputs[i][7], candidate, input);
            }

            neuronalNetwork.testAllInputsAndShowResults(inputs, FullwordTrainingParameterV2.targets);

            neuronalNetwork.trainWithSupervisedLearning(inputs);

            DisplayMachineLearning.showWeights(neuronalNetwork.getWeightsOfHiddenLayer(), neuronalNetwork.getWeightsOfOutputLayer());
            neuronalNetwork.testAllInputsAndShowResults(inputs, FullwordTrainingParameterV2.targets);

            //neuronalNetwork.testAllInputsAndShowResults(FullwordTrainingParameter.testInputs, FullwordTrainingParameter.testTargets);

            Instant finished = Instant.now();
            log.info("Algorithm execution time: {}ms", Duration.between(started, finished).toMillis());

            return null;
        } catch (Exception e) {
            log.error("Error processing CSV", e);
            throw e;
        }
    }

}
