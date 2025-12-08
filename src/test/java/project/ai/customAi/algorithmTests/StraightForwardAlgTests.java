package project.ai.customAi.algorithmTests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import project.ai.customAi.service.StraightForwardAlg;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class StraightForwardAlgTests {

    private StraightForwardAlg straightForwardAlg;

    public static final String COMMA_DELIMITER = ",";
    public static final URL CSV_FILE_PATH = StraightForwardAlg.class.getClassLoader().getResource("data/QaA_catalogue.csv");
    public static Map<String, String> data  = new HashMap<>();

    private Map<String, String> prepareData() throws Exception {
        try {
            assert CSV_FILE_PATH != null;
            try (BufferedReader reader = Files.newBufferedReader(new File(CSV_FILE_PATH.toURI()).toPath())) {
                // prepare
                Map<String, String> questionAndAnswer = new HashMap<>();

                reader.lines()
                        .skip(1)
                        .map(line -> line.split(COMMA_DELIMITER))
                        .forEach(parts -> questionAndAnswer.put(parts[0], parts[1]));

                return questionAndAnswer;
            }
        } catch (Exception e) {
            log.error("Error processing CSV", e);
            throw e;
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        straightForwardAlg = new StraightForwardAlg();
        data = prepareData();
    }

    @Test
    public void testStraightForwardAlg_returns_fullMatchResult() {
        //prepare
        String keyword = "Spieler";
        Map<String, String> expectedResult = Map.of("Wie viele Spieler hat eine Fußballmannschaft auf dem Feld?", "11");

        //act
        Map<String, String> actualResult = straightForwardAlg.handleAlgorithm(keyword, data);

        //check
        assertEquals(expectedResult, actualResult, "fullMatchResult");
    }

    @Test
    public void testStraightForwardAlg_returns_closeMatchResult() {
        //prepare
        String keyword = "Spiler";
        Map<String, String> expectedResult = Map.of("Wie viele Spieler hat eine Fußballmannschaft auf dem Feld?", "11");

        //act
        Map<String, String> actualResult = straightForwardAlg.handleAlgorithm(keyword, data);

        //check
        assertEquals(expectedResult, actualResult, "closeMatchResult");
    }

    @Test
    public void testStraightForwardAlg_returns_wrongMatchResult() {
        //prepare
        String keyword = "Spoiler";
        Map<String, String> expectedResult = Map.of();

        //act
        Map<String, String> actualResult = straightForwardAlg.handleAlgorithm(keyword, data);

        //check
        assertEquals(expectedResult, actualResult, "wrongMatchResult");
    }

    @Test
    public void testStraightForwardAlg_returns_wrongSynonymResult() {
        //prepare
        String keyword = "Fußballspieler";
        Map<String, String> expectedResult = Map.of("Wie viele Spieler hat eine Fußballmannschaft auf dem Feld?", "11");

        //act
        Map<String, String> actualResult = straightForwardAlg.handleAlgorithm(keyword, data);

        //check
        assertEquals(expectedResult, actualResult, "wrongSynonymResult");
    }

}
