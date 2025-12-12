package project.ai.customAi.pojo.neuronalNetwork;

public class NetworkParameter {

    public static final int numberOfInputSignals = TrainingParameter.inputs[0].length;
    public static final int numberOfNeuronsInHiddenLayer = TrainingParameter.inputs[0].length;
    public static final int numberOfNeuronsInOutputLayer = TrainingParameter.targets[0].length;

}
