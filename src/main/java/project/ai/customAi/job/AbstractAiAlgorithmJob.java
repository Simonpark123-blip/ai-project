package project.ai.customAi.job;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.CommandLineRunner;
import project.ai.customAi.logConstant.Prefix;
import project.ai.customAi.logConstant.PrefixConstant;
import project.ai.customAi.service.AiAlgorithm;
import project.ai.customAi.service.straightForward.StraightForwardAlg;

import java.io.BufferedReader;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractAiAlgorithmJob implements CommandLineRunner {

    private final AiAlgorithm algorithm;
    private final PrefixConstant prefixText;

    public static final String COMMA_DELIMITER = ",";
    public static final URL CSV_FILE_PATH = StraightForwardAlg.class.getClassLoader().getResource("data/QaA_catalogue.csv");

    protected AbstractAiAlgorithmJob(AiAlgorithm algorithm, PrefixConstant prefixText) {
        this.algorithm = algorithm;
        this.prefixText = prefixText;
    }

    @Override
    public void run(String... args) throws Exception {
        MDC.put("prefix", prefixText.getLabel());
        try {
            log.info(Prefix.clrStartup);
            log.info("{} {}", Prefix.clrArgs, args);

            String keyword = "Test";/*Arrays.stream(args)
                    .filter(arg -> arg.contains("keyword="))
                    .map(arg -> arg.substring(arg.indexOf("=") + 1))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Keyword must be set"));*/

            Map<String, String> data = prepareData();

            Map<String, String> result = algorithm.handleAlgorithm(keyword, data);

            log.info("{}: {}",  Prefix.clrResult, result);
            log.info(Prefix.clrShutdown);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        MDC.clear();
    }

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

}