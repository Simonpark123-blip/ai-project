package project.ai.customAi.service;

import java.util.Map;

public interface AiAlgorithm {

    Map<String, String> handleAlgorithm(String keyword, Map<String, String> data);

}
