package project.ai.customAi.service.neuronalNetwork;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DisplayMachineLearning {

    public static void showWeights(double[][] weightsOfInputLayer, double[][] weightsOfOutputLayer) {
        showWeightsWithComment("Weights and bias of hidden layer:", weightsOfInputLayer);
        showWeightsWithComment("Weights and bias of output layer:", weightsOfOutputLayer);
    }

    private static void showWeightsWithComment(String comment, double[][] weights) {
        log.info(comment);
        for (int i = 0; i < weights.length; i++) {
            log.info("Neuron {}: {}", i+1, weights[i]);
        }
    }

}