package project.ai.customAi.service.straightForward;

import org.apache.commons.text.similarity.LevenshteinDistance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.service.AiAlgorithm;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("straightForwardUsage")
public class StraightForwardAlg implements AiAlgorithm {

    @Override
    public Map<String, String> handleAlgorithm(String keyword, Map<String, String> data) {
        try {
            Map<String, String> filteredQuestionAndAnswer = new HashMap<>();

            // do algorithm
            data.forEach((question, answer) -> {
                log.info("Processing question: {}", question);

                // question == keyword
                if(question.equals(keyword)) {
                    log.info("Question equals keyword: {}", keyword);
                    filteredQuestionAndAnswer.put(question, answer);
                }

                // question == keyword -> case-insensitive
                if(question.equalsIgnoreCase(keyword)) {
                    log.info("Question equalsIgnoreCase keyword: {}", keyword);
                    filteredQuestionAndAnswer.put(question, answer);
                }

                // question contains keyword -> case-insensitive
                if(question.toLowerCase().contains(keyword.toLowerCase())) {
                    log.info("Question contains keyword: {}", keyword);
                    filteredQuestionAndAnswer.put(question, answer);
                }

                // question contains word like keyword -> case-insensitive, 2 differences allowed
                for (String word : question.split(" ")) {
                    LevenshteinDistance ld = new LevenshteinDistance();
                    int distance = ld.apply(word.toLowerCase(), keyword.toLowerCase());

                    if(distance <= 2) {
                        log.info("Question is close to keyword: {}", keyword);
                        filteredQuestionAndAnswer.put(question, answer);
                        break;
                    }
                }
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
