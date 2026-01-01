package project.ai.customAi.pojo.NN.NetworkParameter;

import project.ai.customAi.pojo.BaseNetworkParameter;

public class FullwordNetworkParameter implements BaseNetworkParameter {

    public static final int fixedWordSize = 5; // input-words + 1 for useful generated words

    public static final int numberOfInputSignals = fixedWordSize;
    public static final int numberOfNeuronsInHiddenLayer = fixedWordSize * 25;
    public static final int numberOfNeuronsInOutputLayer = fixedWordSize;

}
