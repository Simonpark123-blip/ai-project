package project.ai.customAi.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.logConstants.PrefixConstant;
import project.ai.customAi.service.AbstractAiAlgorithmJob;
import project.ai.customAi.service.PerceptronAlg;

@Slf4j
@Service
@Profile("perceptronUsage")
public class PerceptronJob extends AbstractAiAlgorithmJob {

    public PerceptronJob(PerceptronAlg alg) {
        super(alg, PrefixConstant.PERCEPTRON_ALGORITHM);
    }

}
