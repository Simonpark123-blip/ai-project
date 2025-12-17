package project.ai.customAi.pojo.perceptron;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.ai.customAi.pojo.BaseTrainingParameter;
import project.ai.customAi.service.perceptron.ActivationFunction;

@Data
@AllArgsConstructor
public class TrainingParameter implements BaseTrainingParameter/* = Hyperparameter */{

    private int numberOfEpochs;
    private double learningRate;
    private ActivationFunction activationFunction;
    private double[][] inputs;
    private double[] targets;

}