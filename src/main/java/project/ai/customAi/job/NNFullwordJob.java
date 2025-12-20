package project.ai.customAi.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.logConstant.PrefixConstant;
import project.ai.customAi.service.NN.NNFullwordAlg;

@Slf4j
@Service
@Profile("nnFullword")
public class NNFullwordJob extends AbstractAiAlgorithmJob {

    public NNFullwordJob(NNFullwordAlg alg) {
        super(alg, PrefixConstant.NN_FULLWORD_ALGORITHM);
    }

}