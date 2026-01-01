package project.ai.customAi.pojo.NN.NetworkParameter;

import project.ai.customAi.pojo.BaseNetworkParameter;

public class FullwordNetworkParameterV2 implements BaseNetworkParameter {

    public static final int numberOfInputSignals = 7;
    public static final int numberOfNeuronsInHiddenLayer = numberOfInputSignals * 3;
    public static final int numberOfNeuronsInOutputLayer = 1;

}
