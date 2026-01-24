package project.ai.customAi.service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface AiAlgorithm {

    Map<String, String> handleAlgorithm(String keyword, Map<String, String> data, AtomicInteger epochs);

}
