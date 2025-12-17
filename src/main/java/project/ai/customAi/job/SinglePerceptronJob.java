package project.ai.customAi.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.logConstant.PrefixConstant;
import project.ai.customAi.service.perceptron.PerceptronAlg;

@Slf4j
@Service
@Profile("singlePerceptron")
public class SinglePerceptronJob extends AbstractAiAlgorithmJob {

    public SinglePerceptronJob(PerceptronAlg alg) {
        super(alg, PrefixConstant.SINGLE_PERCEPTRON_ALGORITHM);
    }

}
