package project.ai.customAi.pojo.neuronalNetwork;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.ai.customAi.pojo.data.LogicalAndData;
import project.ai.customAi.service.perceptron.ActivationFunction;

@Data
@AllArgsConstructor
public class TrainingParameter /* = Hyperparameter */{

    public static final int numberOfEpochs = 200000;
    public static final double learningRate = 0.1;
    public static final ActivationFunction activationFunction = ActivationFunction.SIGMOID;
    public static final double faultTolerance = 0.1;
    public static final double[][] inputs = LogicalAndData.inputs;
    public static final double[][] targets = LogicalAndData.targets;

}