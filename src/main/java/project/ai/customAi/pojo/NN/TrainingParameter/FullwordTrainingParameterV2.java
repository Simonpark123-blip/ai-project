package project.ai.customAi.pojo.NN.TrainingParameter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.ai.customAi.pojo.BaseTrainingParameter;
import project.ai.customAi.service.perceptron.ActivationFunction;

import java.io.*;
import java.util.*;

@Slf4j
public class FullwordTrainingParameterV2 implements BaseTrainingParameter {

    public static final int numberOfEpochs = 500000;
    public static final double learningRate = 0.001;
    public static final ActivationFunction activationFunction = ActivationFunction.SIGMOID;
    public static final double faultTolerance = 0.1;

    @AllArgsConstructor
    static class TrainingSet {
        String input;
        String dictionary;
        double label;
    }

    public static List<String> dictionary = List.of(
            "Baum",
            "Paul",
            "Java",
            "Love",
            "Laub",
            "Test",
            "Life",
            "Reif",
            "Haus",
            "Auto",
            "Code",
            "Boot",
            "Mail",
            "Wind"
    );

    public static List<String> fullDictionary = retrieveDictionaryData();

    static List<String> retrieveDictionaryData() {
        List<String> dictionaryData = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(Objects.requireNonNull(FullwordTrainingParameterV2.class.getClassLoader().getResource("data/index.dic")).getPath()))) {
            String line = br.readLine();

            while (line != null) {
                dictionaryData.add(line.split("/")[0]);
                line = br.readLine();
            }
            return dictionaryData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<String>> testInput = List.of(
            List.of("Bamm", "Baum"),     // Doppelbuchstabe
            List.of("Boum", "Baum"),     // Vokalwechsel
            List.of("Pual", "Paul"),     // Vertauschung
            List.of("Pau",  "Paul"),     // fehlender Buchstabe

            List.of("Gava", "Java"),     // phonetisch
            List.of("Jafa", "Java"),     // Konsonantenwechsel
            List.of("Jvaa", "Java"),     // Swap

            List.of("Lief", "Life"),     // Vokalwechsel
            List.of("Live", "Life"),     // phonetisch
            List.of("Lyfe", "Life"),     // starker Vokalwechsel

            List.of("Laab", "Laub"),     // Doppelvokal
            List.of("Lub",  "Laub"),     // Auslassung

            List.of("Tets", "Test"),     // Swap
            List.of("Tast", "Test"),     // Vokalfehler

            List.of("Huas", "Haus"),     // Swap
            List.of("Houz", "Haus"),     // phonetisch

            List.of("Aoto", "Auto"),     // Swap
            List.of("Oto",  "Auto"),     // Auslassung

            List.of("Cdoe", "Code"),     // Swap
            List.of("Kood", "Code"),     // phonetisch

            List.of("Bott", "Boot"),     // Doppelkonsonant
            List.of("Bot",  "Boot"),     // fehlender Buchstabe

            List.of("Meil", "Mail"),     // phonetisch
            List.of("Mial", "Mail"),     // Swap

            List.of("Wint", "Wind"),     // Konsonant nah
            List.of("Wynd", "Wind")      // starker Vokalwechsel
    );

    static TrainingSet ts1  = new TrainingSet("Paul", "Paul", 1.0);
    static TrainingSet ts2  = new TrainingSet("Pall", "Paul", 0.9);
    static TrainingSet ts3  = new TrainingSet("Paol", "Paul", 0.8);
    static TrainingSet ts4  = new TrainingSet("Pol",  "Paul", 0.75);
    static TrainingSet ts5  = new TrainingSet("Baul", "Paul", 0.2);

    static TrainingSet ts6  = new TrainingSet("Java",  "Java", 1.0);
    static TrainingSet ts7  = new TrainingSet("jawa",  "Java", 0.9);
    static TrainingSet ts8  = new TrainingSet("Jvaa",  "Java", 0.85);
    static TrainingSet ts9  = new TrainingSet("javva", "Java", 0.8);
    static TrainingSet ts10 = new TrainingSet("jav",   "Java", 0.75);

    static TrainingSet ts11 = new TrainingSet("Lava",  "Java", 0.2);
    static TrainingSet ts12 = new TrainingSet("Jura",  "Java", 0.2);
    static TrainingSet ts13 = new TrainingSet("Reif",  "Java", 0.0);

    static TrainingSet ts14 = new TrainingSet("Love", "Love", 1.0);
    static TrainingSet ts15 = new TrainingSet("Lvoe", "Love", 0.85);
    static TrainingSet ts16 = new TrainingSet("Lov",  "Love", 0.75);

    static TrainingSet ts17 = new TrainingSet("Life", "Love", 0.2);
    static TrainingSet ts18 = new TrainingSet("Reif", "Love", 0.0);

    static TrainingSet ts19 = new TrainingSet("Baum", "Baum", 1.0);
    static TrainingSet ts20 = new TrainingSet("Bam",  "Baum", 0.8);
    static TrainingSet ts21 = new TrainingSet("Laub", "Baum", 0.1);

    static TrainingSet ts22 = new TrainingSet("Test", "Test", 1.0);
    static TrainingSet ts23 = new TrainingSet("Tset", "Test", 0.85);
    static TrainingSet ts24 = new TrainingSet("Tes",  "Test", 0.7);

    static TrainingSet ts25 = new TrainingSet("",     "Java", 0.0);
    static TrainingSet ts26 = new TrainingSet("XYZ",  "Java", 0.0);

    public static List<List<String>> input = List.of(
            List.of(ts1.input, ts1.dictionary),
            List.of(ts2.input, ts2.dictionary),
            List.of(ts3.input, ts3.dictionary),
            List.of(ts4.input, ts4.dictionary),
            List.of(ts5.input, ts5.dictionary),

            List.of(ts6.input, ts6.dictionary),
            List.of(ts7.input, ts7.dictionary),
            List.of(ts8.input, ts8.dictionary),
            List.of(ts9.input, ts9.dictionary),
            List.of(ts10.input, ts10.dictionary),
            List.of(ts11.input, ts11.dictionary),
            List.of(ts12.input, ts12.dictionary),
            List.of(ts13.input, ts13.dictionary),

            List.of(ts14.input, ts14.dictionary),
            List.of(ts15.input, ts15.dictionary),
            List.of(ts16.input, ts16.dictionary),
            List.of(ts17.input, ts17.dictionary),
            List.of(ts18.input, ts18.dictionary),

            List.of(ts19.input, ts19.dictionary),
            List.of(ts20.input, ts20.dictionary),
            List.of(ts21.input, ts21.dictionary),

            List.of(ts22.input, ts22.dictionary),
            List.of(ts23.input, ts23.dictionary),
            List.of(ts24.input, ts24.dictionary),

            List.of(ts25.input, ts25.dictionary),
            List.of(ts26.input, ts26.dictionary)
    );

    public static double[][] targets = {
            {ts1.label}, {ts2.label}, {ts3.label}, {ts4.label}, {ts5.label},
            {ts6.label}, {ts7.label}, {ts8.label}, {ts9.label}, {ts10.label},
            {ts11.label}, {ts12.label}, {ts13.label},
            {ts14.label}, {ts15.label}, {ts16.label}, {ts17.label}, {ts18.label},
            {ts19.label}, {ts20.label}, {ts21.label},
            {ts22.label}, {ts23.label}, {ts24.label},
            {ts25.label}, {ts26.label}
    };
}