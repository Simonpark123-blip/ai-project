package project.ai.customAi.pojo.neuronalNetwork;

import project.ai.customAi.pojo.data.CharData;

public class AlphanumericNormalizedNetworkParameter {

    public static final int numberOfInputSignals = CharData.inputs[0].length();
    public static final int numberOfNeuronsInHiddenLayer = CharData.inputs[0].length() * 10;
    public static final int numberOfNeuronsInOutputLayer = CharData.targets[0].length();

}
