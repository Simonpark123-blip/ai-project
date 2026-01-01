package project.ai.customAi.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.logConstant.PrefixConstant;
import project.ai.customAi.service.NN.NNFullwordAlgV2;

@Slf4j
@Service
@Profile("nnFullwordV2")
public class NNFullwordJobV2 extends AbstractAiAlgorithmJob {

    public NNFullwordJobV2(NNFullwordAlgV2 alg) {
        super(alg, PrefixConstant.NN_FULLWORD_ALGORITHM);
    }

}