package project.ai.customAi.pojo.neuronalNetwork;

import project.ai.customAi.pojo.data.CharData;

public class AlphanumericNetworkParameter {

    public static final int numberOfInputSignals = CharData.inputs[0].length() * 26;
    public static final int numberOfNeuronsInHiddenLayer = CharData.inputs[0].length() * 26;
    public static final int numberOfNeuronsInOutputLayer = CharData.targets[0].length() * 26;

}
