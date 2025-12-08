package project.ai.customAi.pojo;

import project.ai.customAi.service.ActivationFunction;

public class TrainingParameter {

    public static final int numberOfEpochs = 100;
    public static final double learningRate = 0.5;
    public static final ActivationFunction activationFunction = ActivationFunction.HEAVISIDE;
    public static final double[][] inputs = LogicalAndData.inputs;
    public static final double[] targets = LogicalAndData.targets;

}