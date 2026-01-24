package project.ai.customAi.pojo.NN.NetworkParameter;

import project.ai.customAi.pojo.BaseNetworkParameter;

public class FeaturedFullwordNetworkParameter implements BaseNetworkParameter {

    public static final int numberOfInputSignals = 4;
    public static final int numberOfNeuronsInHiddenLayer = numberOfInputSignals * 3;
    public static final int numberOfNeuronsInOutputLayer = 1;

}
