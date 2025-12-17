package project.ai.customAi.pojo;

public interface BaseNetworkParameter {

    int numberOfInputSignals = BaseTrainingParameter.inputs[0].length;
    int numberOfNeuronsInHiddenLayer = BaseTrainingParameter.inputs[0].length;
    int numberOfNeuronsInOutputLayer = BaseTrainingParameter.targets[0].length;

}
