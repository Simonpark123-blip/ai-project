package project.ai.customAi.pojo.NN.NetworkParameter;

import project.ai.customAi.pojo.BaseNetworkParameter;
import project.ai.customAi.pojo.NN.TrainingParameter.TrainingParameter;

public class NetworkParameter implements BaseNetworkParameter {

    public static final int numberOfInputSignals = TrainingParameter.inputs[0].length;
    public static final int numberOfNeuronsInHiddenLayer = TrainingParameter.inputs[0].length;
    public static final int numberOfNeuronsInOutputLayer = TrainingParameter.targets[0].length;

}
