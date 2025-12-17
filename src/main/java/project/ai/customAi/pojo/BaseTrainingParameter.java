package project.ai.customAi.pojo;

import project.ai.customAi.service.perceptron.ActivationFunction;

public interface BaseTrainingParameter {

    int numberOfEpochs = 10_000;
    double learningRate = 0.1;
    ActivationFunction activationFunction = ActivationFunction.ONLYSUM;
    double faultTolerance = 0.1;
    double[][] inputs = new double[0][0];
    double[][] targets = new double[0][0];

}
