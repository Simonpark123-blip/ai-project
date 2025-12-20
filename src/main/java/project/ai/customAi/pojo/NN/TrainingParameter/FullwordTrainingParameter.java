package project.ai.customAi.pojo.NN.TrainingParameter;

import lombok.extern.slf4j.Slf4j;
import project.ai.customAi.pojo.BaseTrainingParameter;
import project.ai.customAi.pojo.data.Wordset;
import project.ai.customAi.pojo.NN.NetworkParameter.FullwordNetworkParameter;
import project.ai.customAi.service.fullword.FullwordPreperator;
import project.ai.customAi.service.perceptron.ActivationFunction;

import java.util.*;

@Slf4j
public class FullwordTrainingParameter implements BaseTrainingParameter {

    private static final char[] VOWELS = {'a','e','i','o','u'};

    public static final int numberOfEpochs = 10_000;
    public static final double learningRate = 0.1;
    public static final ActivationFunction activationFunction = ActivationFunction.TANH;
    public static final double faultTolerance = 0.04;
    public static final int numberOfGeneratedWords = 9; // one element is original
    public static final int numberOfTotalWords = (numberOfGeneratedWords + 1) * Wordset.input.length;

    public static final double[][] inputs = createNormalizedData(generateInputs());
    public static final double[][] targets = createNormalizedData(Wordset.input);
    public static final double[][] testInputs = createNormalizedData(Wordset.testInput);
    public static final double[][] testTargets = createNormalizedData(Wordset.input);

    private static String[] generateInputs() {
        List<String> inputList = new ArrayList<>();
        for (String word : Wordset.input) {
            word = FullwordPreperator.cleanWord(word).toLowerCase(Locale.ROOT);
            inputList.add(word);
            for (int i = 0; i < numberOfGeneratedWords; i++) {
                inputList.add(generateTypo(word));
            }
        }
        log.info("Generated input examples: {}", inputList);
        return inputList.toArray(new String[0]);
    }

    private static String generateTypo(String word) {
        Random rnd = new Random();
        int action = rnd.nextInt(3);
        int pos = rnd.nextInt(word.length());

        // 0 = delete, 1 = insert, 2 = substitute
        return switch (action) {
            case 0 -> {
                if (word.length() == 1) yield word;
                yield word.substring(0, pos) + word.substring(pos + 1);
            }
            case 1 -> {
                char v = VOWELS[rnd.nextInt(VOWELS.length)];
                yield word.substring(0, pos) + v + word.substring(pos);
            }
            case 2 -> {
                char c = (char) ('a' + rnd.nextInt(26));
                yield word.substring(0, pos) + c + word.substring(pos + 1);
            }
            default -> word;
        };
    }

    private static double[][] createNormalizedData(String[] input) {
        double[][] normalizedData = new double[numberOfTotalWords][];
        String[] data = new String[numberOfTotalWords];
        if(input.length == numberOfTotalWords){
            data = input;
        }
        else {
            for(int i = 0; i < numberOfTotalWords; i++){
                data[i] = input[i/(numberOfGeneratedWords + 1)];
            }
        }

        for (int i = 0; i < numberOfTotalWords; i++) {
            log.info("Uncleaned word to process: {}", data[i]);
            String word = FullwordPreperator.cleanWord(data[i]).toLowerCase(Locale.ROOT);
            log.info("Cleaned word to process: {}", word);
            StringBuilder padded = new StringBuilder(word);
            while (padded.length() < FullwordNetworkParameter.numberOfInputSignals) {
                padded.append("_");
            }
            log.info("Word with padding: {}", padded);
            normalizedData[i] = new double[padded.length()];
            for (int j = 0; j < padded.length(); j++) {
                char ch = padded.charAt(j);
                normalizedData[i][j] = (ch == '_') ? -1.0 : 2.0 * (ch - 'a') / 25.0 - 1.0;
            }
            log.info("Normalized word with padding: {}", Arrays.deepToString(normalizedData));
        }
        log.info("Normalized array (length {}): {}", normalizedData.length, Arrays.deepToString(normalizedData));
        return normalizedData;
    }
}