package project.ai.customAi.pojo.NN.TrainingParameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import project.ai.customAi.pojo.BaseTrainingParameter;
import project.ai.customAi.service.perceptron.ActivationFunction;

import java.util.List;

@Slf4j
public class FullwordTrainingParameterV2 implements BaseTrainingParameter {

    public static final int numberOfEpochs = 500000;
    public static final double learningRate = 0.01;
    public static final ActivationFunction activationFunction = ActivationFunction.SIGMOID;
    public static final double faultTolerance = 0.01;//adjust this value to be greater than some value

    @AllArgsConstructor
    static class TrainingSet {
        String input;
        String dictionary;
        double label; // 1.0 = plausible / correct, 0.0 = incorrect / implausible
    }

    static TrainingSet trainingSetName = new TrainingSet("Pall", "Paul", 1.0);      // kleiner Tippfehler
    static TrainingSet trainingSetNature = new TrainingSet("XYZ", "Baum", 0.0);       // völlig falsches Wort
    static TrainingSet trainingSetProg = new TrainingSet("jawa", "Java", 1.0);      // Tippfehler in Programmierkontext
    static TrainingSet trainingSetSwap = new TrainingSet("Jvaa", "Java", 1.0);      // Buchstaben vertauscht
    static TrainingSet trainingSetExtra = new TrainingSet("javva", "Java", 1.0);     // Buchstabe doppelt
    static TrainingSet trainingSetMissing = new TrainingSet("jav", "Java", 1.0);       // Buchstabe fehlt
    static TrainingSet trainingSetFar = new TrainingSet("Lava", "Java", 0.0);      // ähnlich klingend, aber falsch
    static TrainingSet trainingSetSimilar = new TrainingSet("Jura", "Java", 1.0);      // phonetisch ähnlich
    static TrainingSet trainingSetEmpty = new TrainingSet("", "Java", 0.0);          // leeres Wort
    static TrainingSet trainingSetExact = new TrainingSet("Java", "Java", 1.0);      // korrekt
    // TODO: dictionary -> durch dic iterieren und für input das wahrscheinlichste wort (höchster score) finden
    public static List<List<String>> input = List.of(
            List.of(trainingSetName.input, trainingSetName.dictionary),
            List.of(trainingSetNature.input, trainingSetNature.dictionary),
            List.of(trainingSetProg.input, trainingSetProg.dictionary),
            List.of(trainingSetSwap.input, trainingSetSwap.dictionary),
            List.of(trainingSetExtra.input, trainingSetExtra.dictionary),
            List.of(trainingSetMissing.input, trainingSetMissing.dictionary),
            List.of(trainingSetFar.input, trainingSetFar.dictionary),
            List.of(trainingSetSimilar.input, trainingSetSimilar.dictionary),
            List.of(trainingSetEmpty.input, trainingSetEmpty.dictionary),
            List.of(trainingSetExact.input, trainingSetExact.dictionary)
    );

    public static double[][] targets = {
            { trainingSetName.label },
            { trainingSetNature.label },
            { trainingSetProg.label },
            { trainingSetSwap.label },
            { trainingSetExtra.label },
            { trainingSetMissing.label },
            { trainingSetFar.label },
            { trainingSetSimilar.label },
            { trainingSetEmpty.label },
            { trainingSetExact.label }
    };

}