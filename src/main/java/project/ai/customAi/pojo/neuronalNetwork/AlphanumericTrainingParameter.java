package project.ai.customAi.pojo.neuronalNetwork;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import project.ai.customAi.pojo.data.CharData;
import project.ai.customAi.service.perceptron.ActivationFunction;

import java.util.Arrays;
import java.util.Random;

@Data
@Slf4j
@AllArgsConstructor
public class AlphanumericTrainingParameter /* = Hyperparameter */{

    public static final int numberOfEpochs = 1000;
    public static final double learningRate = 0.01;
    public static final ActivationFunction activationFunction = ActivationFunction.SIGMOID;
    public static final double faultTolerance = 0.1;
    public static final double[][] inputs = createOneHotVector(CharData.inputs);
    public static final double[][] targets = createOneHotVector(CharData.targets);

    private static final Random rand = new Random();
    private static final int randomTestDataIndex = rand.nextInt(CharData.targets.length);
    public static final double[][] testInputs = createOneHotVector(pickRandom(CharData.inputs));
    public static final double[][] testTargets = createOneHotVector(pickRandom(CharData.targets));

    private static double[][] createOneHotVector(String[] inputs){
        double[][] result = new double[inputs.length][]; // inputs.length == count of test-data
        // iterate through test-data
        for (int i = 0; i < inputs.length; i++) {
            int sizeOfString = inputs[i].length();
            result[i] = new double[26 * sizeOfString];
            // iterate through chars of string
            for(int charIndex = 0; charIndex < sizeOfString; charIndex++){
                // convert to ASCII
                int asciiIndex = inputs[i].charAt(charIndex);
                // normalize ('A' as ASCII == 65 -> 65-65 = 0)
                int oneHotIndex = asciiIndex - 'A';
                result[i][charIndex * 26 + oneHotIndex] = 1.0;
            }
        }
        return result;
    }

    private static String[] pickRandom(String[] data){
        return new String[]{data[randomTestDataIndex]};
    }

}