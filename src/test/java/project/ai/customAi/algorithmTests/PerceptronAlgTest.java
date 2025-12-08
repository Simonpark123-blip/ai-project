package project.ai.customAi.algorithmTests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import project.ai.customAi.service.perceptron.LogicalOperation;
import project.ai.customAi.service.perceptron.PerceptronAlg;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PerceptronAlgTest {

    private PerceptronAlg perceptronAlg;

    @BeforeEach
    void setUp() {
        perceptronAlg = new PerceptronAlg();
    }

    @Test
    public void testPerceptronAlg_returns_LogicalAndResultUnderfitting() {
        //prepare
        String logicalOperation = LogicalOperation.AND.name();
        Map<String, String> data = Map.of("epochs", String.valueOf(25));
        Map<String, String> expectedOutput = Map.of("percentage", "100.0");

        //act
        Map<String, String> actualResult = perceptronAlg.handleAlgorithm(logicalOperation, data);

        //check
        assertEquals(expectedOutput, actualResult, "inAccuratePercentageUnderfitting");
    }

    @Test
    public void testPerceptronAlg_returns_LogicalAndResultHighEpochs() {
        //prepare
        String logicalOperation = LogicalOperation.AND.name();
        Map<String, String> data = Map.of("epochs", String.valueOf(100));
        Map<String, String> expectedOutput = Map.of("percentage", "100.0");

        //act
        Map<String, String> actualResult = perceptronAlg.handleAlgorithm(logicalOperation, null);

        //check
        assertEquals(expectedOutput, actualResult, "accuratePercentageHighEpochs");
    }

    @Test
    public void testPerceptronAlg_returns_LogicalOrResult() {
        //prepare
        String logicalOperation = LogicalOperation.OR.name();
        Map<String, String> expectedOutput = Map.of("percentage", "100.0");

        //act
        Map<String, String> actualResult = perceptronAlg.handleAlgorithm(logicalOperation, null);

        //check
        assertEquals(expectedOutput, actualResult, "accuratePercentage");
    }

    @Test
    public void testPerceptronAlg_returns_LogicalXorResult() {
        //prepare
        String logicalOperation = LogicalOperation.XOR.name();
        Map<String, String> expectedOutput = Map.of("percentage", "100.0");

        //act
        Map<String, String> actualResult = perceptronAlg.handleAlgorithm(logicalOperation, null);

        //check
        assertEquals(expectedOutput, actualResult, "inaccuratePercentage");
    }

    // -> usage of neural network

}
