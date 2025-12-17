package project.ai.customAi.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.logConstant.PrefixConstant;
import project.ai.customAi.service.straightForward.StraightForwardAlg;

@Slf4j
@Service
@Profile("straightForward")
public class StraightForwardJob extends AbstractAiAlgorithmJob {

    public StraightForwardJob(StraightForwardAlg alg) {
        super(alg, PrefixConstant.STRAIGHT_FORWARD_ALGORITHM);
    }

}
