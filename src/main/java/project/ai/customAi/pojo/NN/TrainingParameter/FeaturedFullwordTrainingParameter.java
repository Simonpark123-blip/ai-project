package project.ai.customAi.pojo.NN.TrainingParameter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.ai.customAi.pojo.BaseTrainingParameter;
import project.ai.customAi.service.perceptron.ActivationFunction;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class FeaturedFullwordTrainingParameter implements BaseTrainingParameter {

    public static final double learningRate = 0.015;
    public static final ActivationFunction activationFunction = ActivationFunction.SIGMOID;
    public static final double faultTolerance = 0.1;

    public static List<String> fullDictionary = retrieveDictionaryData();

    static List<String> retrieveDictionaryData() {
        List<String> dictionaryData = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(Objects.requireNonNull(FeaturedFullwordTrainingParameter.class.getClassLoader().getResource("data/index.dic")).getPath()))) {
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
            List.of("Baumm", "Baum"),
            List.of("Paull", "Paul"),
            List.of("Jaava", "Java"),
            List.of("Lofe", "Love"),
            List.of("Lif", "Life"),
            List.of("Tesst", "Test"),
            List.of("Maill", "Mail"),
            List.of("Boott", "Boot"),
            List.of("Windd", "Wind"),
            List.of("Hausz", "Haus"),
            List.of("Autoo", "Auto"),
            List.of("Codee", "Code"),
            List.of("Baumhous", "Baumhaus"),
            List.of("Autobaan", "Autobahn"),
            List.of("Mailserwer", "Mailserver"),
            List.of("Bootshauss", "Bootshaus"),
            List.of("Hausverwaltug", "Hausverwaltung"),
            List.of("Reifendruk", "Reifendruck"),
            List.of("Quellcod", "Quellcode"),
            List.of("Softwareentwicklng", "Softwareentwicklung"),
            List.of("Programmierspraache", "Programmiersprache"),
            List.of("Rechtschreibkorektur", "Rechtschreibkorrektur"),
            List.of("Datenverarbeitng", "Datenverarbeitung"),
            List.of("Informationsverarbietung", "Informationsverarbeitung"),
            List.of("Benutzeroberflache", "Benutzeroberfläche"),
            List.of("Fehlerbehandlng", "Fehlerbehandlung"),
            List.of("Datenbankverbindng", "Datenbankverbindung"),
            List.of("Systemkonfiguraton", "Systemkonfiguration"),
            List.of("Anwendungsentwiclung", "Anwendungsentwicklung"),
            List.of("Testframwork", "Testframework"),
            List.of("Entwicklungsumbegung", "Entwicklungsumgebung"),
            List.of("Autoversichrung", "Autoversicherung"),
            List.of("Baumkron", "Baumkrone"),
            List.of("Baumstam", "Baumstamm"),
            List.of("Baumateriall", "Baumaterial")
    );

    @AllArgsConstructor
    static class TrainingSet {
        String input;
        String dictionary;
        double label;
    }

    public static List<String> dictionary = List.of(
            "Baum","Paul","Java","Love","Life","Test","Mail","Boot","Wind","Haus","Auto","Code",
            "Baumhaus","Autobahn","Mailserver","Bootshaus","Hausverwaltung","Reifendruck",
            "Quellcode","Softwareentwicklung","Programmiersprache","Rechtschreibkorrektur",
            "Datenverarbeitung","Informationsverarbeitung","Benutzeroberfläche","Fehlerbehandlung",
            "Datenbankverbindung","Systemkonfiguration","Anwendungsentwicklung","Testframework",
            "Entwicklungsumgebung","Autoversicherung","Baumkrone","Baumstamm","Baumaterial"
    );

    static List<TrainingSet> trainingSets = List.of(
            new TrainingSet("Baum","Baum",1.0),
            new TrainingSet("Paul","Paul",1.0),
            new TrainingSet("Java","Java",1.0),
            new TrainingSet("Love","Love",1.0),
            new TrainingSet("Life","Life",1.0),
            new TrainingSet("Test","Test",1.0),
            new TrainingSet("Mail","Mail",1.0),
            new TrainingSet("Boot","Boot",1.0),
            new TrainingSet("Wind","Wind",1.0),
            new TrainingSet("Haus","Haus",1.0),
            new TrainingSet("Auto","Auto",1.0),
            new TrainingSet("Code","Code",1.0),
            new TrainingSet("Baumhaus","Baumhaus",1.0),
            new TrainingSet("Quellcode","Quellcode",1.0),
            new TrainingSet("Softwareentwicklung","Softwareentwicklung",1.0),
            new TrainingSet("Programmiersprache","Programmiersprache",1.0),
            new TrainingSet("Rechtschreibkorrektur","Rechtschreibkorrektur",1.0),
            new TrainingSet("Datenverarbeitung","Datenverarbeitung",1.0),
            new TrainingSet("Testframework","Testframework",1.0),
            new TrainingSet("Benutzeroberfläche","Benutzeroberfläche",1.0),

            new TrainingSet("Bamm","Baum",0.9),
            new TrainingSet("Boum","Baum",0.85),
            new TrainingSet("Bam","Baum",0.8),
            new TrainingSet("Pall","Paul",0.9),
            new TrainingSet("Pual","Paul",0.85),
            new TrainingSet("Paol","Paul",0.8),
            new TrainingSet("Pol","Paul",0.7),
            new TrainingSet("jawa","Java",0.9),
            new TrainingSet("Jvaa","Java",0.85),
            new TrainingSet("javva","Java",0.8),
            new TrainingSet("jav","Java",0.75),
            new TrainingSet("Jav","Java",0.75),
            new TrainingSet("Lvoe","Love",0.85),
            new TrainingSet("Lov","Love",0.75),
            new TrainingSet("Lyfe","Life",0.7),
            new TrainingSet("Lief","Life",0.8),
            new TrainingSet("Tset","Test",0.85),
            new TrainingSet("Tes","Test",0.7),
            new TrainingSet("Cdoe","Code",0.85),
            new TrainingSet("Cod","Code",0.7),
            new TrainingSet("Mial","Mail",0.85),
            new TrainingSet("Meil","Mail",0.8),
            new TrainingSet("Mai","Mail",0.75),
            new TrainingSet("Btoot","Boot",0.75),
            new TrainingSet("Bot","Boot",0.7),
            new TrainingSet("Bott","Boot",0.8),
            new TrainingSet("Aotu","Auto",0.85),
            new TrainingSet("Aut","Auto",0.7),
            new TrainingSet("Gava","Java",0.6),
            new TrainingSet("Jafa","Java",0.7),
            new TrainingSet("Jana","Java",0.6),
            new TrainingSet("Jave","Java",0.65),
            new TrainingSet("Javo","Java",0.6),
            new TrainingSet("Lafe","Life",0.5),
            new TrainingSet("Luve","Love",0.75),
            new TrainingSet("Wint","Wind",0.7),
            new TrainingSet("Wynd","Wind",0.6),

            new TrainingSet("Jiva","Java",0.45),
            new TrainingSet("Jove","Java",0.4),
            new TrainingSet("JawaScript","Java",0.2),
            new TrainingSet("Javscript","Java",0.35),
            new TrainingSet("JavaScript","Java",0.0),
            new TrainingSet("JavaFX","Java",0.2),
            new TrainingSet("JavaBeans","Java",0.0),
            new TrainingSet("Lover","Love",0.2),
            new TrainingSet("Loving","Love",0.2),
            new TrainingSet("Liefes","Life",0.3),
            new TrainingSet("Lifely","Life",0.3),
            new TrainingSet("Tests","Test",0.4),
            new TrainingSet("Testlauf","Test",0.0),
            new TrainingSet("Testframework","Test",0.0),
            new TrainingSet("Mailbox","Mail",0.0),
            new TrainingSet("Mailserver","Mail",0.0),
            new TrainingSet("Bootshaus","Boot",0.0),
            new TrainingSet("Booting","Boot",0.2),
            new TrainingSet("Windkraft","Wind",0.0),
            new TrainingSet("Windig","Wind",0.1),
            new TrainingSet("Hausverwaltung","Haus",0.0),
            new TrainingSet("Hauswand","Haus",0.0),
            new TrainingSet("Autobahn","Auto",0.0),
            new TrainingSet("Autohaus","Auto",0.0),
            new TrainingSet("Reifendruck","Reif",0.0),
            new TrainingSet("Reifend","Reif",0.2),
            new TrainingSet("Reife","Reif",0.6),

            new TrainingSet("Quellcode","Quellcode",1.0),
            new TrainingSet("Quellcod","Quellcode",0.85),
            new TrainingSet("Quellkode","Quellcode",0.6),
            new TrainingSet("Softwareentwicklung","Softwaredevelopment",0.0),
            new TrainingSet("Softwareentwickler","Softwareentwicklung",0.2),
            new TrainingSet("Software","Softwareentwicklung",0.2),
            new TrainingSet("Entwicklungsumgebung","Entwicklungsumgebung",1.0),
            new TrainingSet("Entwicklungs","Entwicklungsumgebung",0.2),
            new TrainingSet("Programmiersprache","Programmiersprache",1.0),
            new TrainingSet("Programiersprache","Programmiersprache",0.9),
            new TrainingSet("Programmiersprahe","Programmiersprache",0.75),
            new TrainingSet("Programmierung","Programmiersprache",0.2),
            new TrainingSet("Programmierer","Programmiersprache",0.1),
            new TrainingSet("Rechtschreibkorrektur","Rechtschreibkorrektur",1.0),
            new TrainingSet("Rechtschreibkorrekturr","Rechtschreibkorrektur",0.9),
            new TrainingSet("Rechtschreib","Rechtschreibkorrektur",0.35),
            new TrainingSet("Rechtschreibung","Rechtschreibkorrektur",0.25),
            new TrainingSet("Autoversicherung","Autoversicherung",1.0),
            new TrainingSet("Autoversicherungn","Autoversicherung",0.8),
            new TrainingSet("Autoversicherng","Autoversicherung",0.6),
            new TrainingSet("Baumkrone","Baumkrone",1.0),
            new TrainingSet("Baumkrn","Baumkrone",0.55),
            new TrainingSet("Baumstamm","Baumstamm",1.0),
            new TrainingSet("Baumstam","Baumstamm",0.6),
            new TrainingSet("Baumhaus","Baumhaus",1.0),
            new TrainingSet("Baumhausn","Baumhaus",0.7),
            new TrainingSet("Baumaterial","Baum",0.0),

            new TrainingSet("Testumgebung","Test",0.0),
            new TrainingSet("Testdaten","Test",0.0),
            new TrainingSet("Testbericht","Test",0.0),
            new TrainingSet("Mailkonto","Mail",0.0),
            new TrainingSet("Mailbox128","Mail",0.0),
            new TrainingSet("Mailing","Mail",0.1),
            new TrainingSet("Codebasis","Code",0.0),
            new TrainingSet("Codeanalyse","Code",0.0),
            new TrainingSet("CodeReview","Code",0.0),
            new TrainingSet("Datenbankverbindung","Datenbankverbindung",1.0),
            new TrainingSet("Datenbank","Datenbankverbindung",0.2),
            new TrainingSet("Datenbankzugang","Datenbankverbindung",0.0),
            new TrainingSet("Datenverarbeitungssystem","Datenverarbeitung",0.0),
            new TrainingSet("Informationsverarbeitung","Informationsverarbeitung",1.0),
            new TrainingSet("Informations","Informationsverarbeitung",0.1),
            new TrainingSet("Systemkonfiguration","Systemkonfiguration",1.0),
            new TrainingSet("Systemconf","Systemkonfiguration",0.6),
            new TrainingSet("Anwendungsentwicklung","Anwendungsentwicklung",1.0),
            new TrainingSet("Anwendungsentwickler","Anwendungsentwicklung",0.2),
            new TrainingSet("Benutzeroberfläche","Benutzeroberfläche",1.0),
            new TrainingSet("Benutzeroberflächen","Benutzeroberfläche",0.6),
            new TrainingSet("Fehlerbehandlung","Fehlerbehandlung",1.0),
            new TrainingSet("Fehlerbehandl","Fehlerbehandlung",0.6),
            new TrainingSet("Fehleranalyse","Fehlerbehandlung",0.2),

            new TrainingSet("","Java",0.0),
            new TrainingSet(" ","Java",0.0),
            new TrainingSet("1234","Test",0.0),
            new TrainingSet("!!!","Mail",0.0),
            new TrainingSet("aaaaaaaa","Java",0.0),
            new TrainingSet("qwertz","Code",0.0),
            new TrainingSet("loremipsum","Life",0.0),
            new TrainingSet("j","Java",0.25),
            new TrainingSet("ja","Java",0.5),
            new TrainingSet("jav","Java",0.75),
            new TrainingSet("javaa","Java",0.8),
            new TrainingSet("JAVA","Java",0.95),
            new TrainingSet("java","Java",0.98),
            new TrainingSet("JaVa","Java",0.9),

            new TrainingSet("Baue","Baum",0.6),
            new TrainingSet("Baume","Baum",0.65),
            new TrainingSet("Baums","Baum",0.45),
            new TrainingSet("Baumwald","Baum",0.0),
            new TrainingSet("Baumkranz","Baum",0.0),
            new TrainingSet("Baumrinde","Baum",0.0),
            new TrainingSet("Reifendrucktest","Reif",0.0),
            new TrainingSet("Reifentest","Reif",0.0),
            new TrainingSet("Windenergie","Wind",0.0),
            new TrainingSet("Windstille","Wind",0.0),
            new TrainingSet("Mailingliste","Mail",0.0),
            new TrainingSet("Mail-forward","Mail",0.0),
            new TrainingSet("Autoupdate","Auto",0.0),
            new TrainingSet("Autoteile","Auto",0.0),
            new TrainingSet("Bootswerft","Boot",0.0),
            new TrainingSet("Bootcamp","Boot",0.0),
            new TrainingSet("Codegenerator","Code",0.0),
            new TrainingSet("Codefragment","Code",0.0),
            new TrainingSet("Testdatenbank","Test",0.0),
            new TrainingSet("Testcase","Test",0.0),
            new TrainingSet("Loveletter","Love",0.0),
            new TrainingSet("Lovesong","Love",0.0),
            new TrainingSet("Lifeguard","Life",0.0),
            new TrainingSet("Lifeboat","Life",0.0),

            new TrainingSet("Reiph","Reif",0.5),
            new TrainingSet("Reifung","Reif",0.25),
            new TrainingSet("Reifer","Reif",0.3),
            new TrainingSet("Winded","Wind",0.1),
            new TrainingSet("Windy","Wind",0.1),
            new TrainingSet("Hausmeister","Haus",0.0),
            new TrainingSet("Hausarbeit","Haus",0.0),
            new TrainingSet("Autokauf","Auto",0.0),
            new TrainingSet("Autofahren","Auto",0.0),
            new TrainingSet("CodeReview","Code",0.0),
            new TrainingSet("CodeQuality","Code",0.0),
            new TrainingSet("Mailadresse","Mail",0.0),
            new TrainingSet("Mailclient","Mail",0.0),
            new TrainingSet("Bootfahrt","Boot",0.0),
            new TrainingSet("Bootrennen","Boot",0.0),
            new TrainingSet("Windhose","Wind",0.0),

            new TrainingSet("Tst","Test",0.55),
            new TrainingSet("Teest","Test",0.5),
            new TrainingSet("C0de","Code",0.4),
            new TrainingSet("K0de","Code",0.4),
            new TrainingSet("Mailz","Mail",0.4),
            new TrainingSet("Maal","Mail",0.45),
            new TrainingSet("Autto","Auto",0.5),
            new TrainingSet("Aauto","Auto",0.45),
            new TrainingSet("B0ot","Boot",0.45),
            new TrainingSet("Bo0t","Boot",0.45),

            new TrainingSet("Programmierumgebung","Programmiersprache",0.1),
            new TrainingSet("Programmierwerkzeug","Programmiersprache",0.0),
            new TrainingSet("Softwarearchitektur","Softwareentwicklung",0.0),
            new TrainingSet("Softwaretester","Softwareentwicklung",0.1),
            new TrainingSet("Informationssicherheit","Informationsverarbeitung",0.0),
            new TrainingSet("Datenverarbeitungszentrum","Datenverarbeitung",0.0),
            new TrainingSet("Konfigurationsdatei","Systemkonfiguration",0.0),
            new TrainingSet("Benutzerverwaltung","Benutzeroberfläche",0.0),
            new TrainingSet("Fehlerdiagnose","Fehlerbehandlung",0.0),
            new TrainingSet("Fehlerprotokoll","Fehlerbehandlung",0.0)
    );

    public static List<List<String>> input = trainingSets.stream()
            .map(ts -> List.of(ts.input, ts.dictionary))
            .collect(Collectors.toList());

    public static double[][] targets = trainingSets.stream()
            .map(ts -> new double[]{ ts.label })
            .toArray(double[][]::new);

}