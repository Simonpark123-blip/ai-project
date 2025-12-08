package project.ai.customAi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.ai.customAi.service.perceptron.ActivationFunction;

@Data
@AllArgsConstructor
public class TrainingParameter /* = Hyperparameter */{

    private int numberOfEpochs;
    private double learningRate;
    private ActivationFunction activationFunction;
    private double[][] inputs;
    private double[] targets;

}