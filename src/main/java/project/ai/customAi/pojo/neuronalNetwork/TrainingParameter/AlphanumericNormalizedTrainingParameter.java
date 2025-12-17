package project.ai.customAi.pojo.neuronalNetwork.TrainingParameter;

import lombok.extern.slf4j.Slf4j;
import project.ai.customAi.pojo.BaseTrainingParameter;
import project.ai.customAi.pojo.data.CharData;
import project.ai.customAi.service.perceptron.ActivationFunction;

import java.util.Arrays;
import java.util.Random;

@Slf4j
public class AlphanumericNormalizedTrainingParameter implements BaseTrainingParameter {

    public static final long RANDOM_SEED = 42L;
    public static final Random rand = new Random(RANDOM_SEED);

    public static final int numberOfEpochs = 30000;
    public static final double learningRate = 0.1;
    public static final ActivationFunction activationFunction = ActivationFunction.TANH;
    public static final double faultTolerance = 0.04;

    public static final double[][] inputs = createNormalizedData(CharData.inputs);
    public static final double[][] targets = createNormalizedData(CharData.targets);

    public static final double[][] testInputs = createNormalizedData(new String[]{"TU"});
    public static final double[][] testTargets = createNormalizedData(new String[]{"UV"});

    private static double[][] createNormalizedData(String[] data){
        double[][] normalizedData = new double[data.length][];
        for(int i = 0; i < data.length; i++){
            String s = data[i];
            normalizedData[i] = new double[s.length()];
            for(int j = 0; j < s.length(); j++){
                // [-1, 1]
                normalizedData[i][j] = 2.0 * getAsciiRepresentation(s.charAt(j)) / 25.0 - 1.0;
            }
        }
        log.info("Normalized array: {}", Arrays.deepToString(normalizedData));
        return normalizedData;
    }


    private static int getAsciiRepresentation(char c){
        return c - 'A';
    }

}
