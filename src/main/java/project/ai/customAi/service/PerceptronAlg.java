package project.ai.customAi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("perceptronUsage")
public class PerceptronAlg implements AiAlgorithm {

    @Override
    public Map<String, String> handleAlgorithm(String keyword, Map<String, String> data) {
        try {
            Map<String, String> filteredQuestionAndAnswer = new HashMap<>();

            // do algorithm
            data.forEach((question, answer) -> {
                log.info("Processing question: {}", question);
                filteredQuestionAndAnswer.put(question, answer);

                MachineLearning machineLearning = new MachineLearning();

                machineLearning.showWeights();
                machineLearning.testAllInputsAndShowResults();

                machineLearning.trainWithSupervisedLearning();

                machineLearning.showWeights();
                machineLearning.testAllInputsAndShowResults();
            });

            // display result
            filteredQuestionAndAnswer.forEach((q, a) -> log.info("Q: {} | A: {}", q, a));
            return filteredQuestionAndAnswer;
        } catch (Exception e) {
            log.error("Error processing CSV", e);
            throw e;
        }
    }
}
