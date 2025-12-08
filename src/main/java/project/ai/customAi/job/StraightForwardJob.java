package project.ai.customAi.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.logConstants.PrefixConstant;
import project.ai.customAi.service.AbstractAiAlgorithmJob;
import project.ai.customAi.service.StraightForwardAlg;

@Slf4j
@Service
@Profile("straightForwardUsage")
public class StraightForwardJob extends AbstractAiAlgorithmJob {

    public StraightForwardJob(StraightForwardAlg alg) {
        super(alg, PrefixConstant.STRAIGHT_FORWARD_ALGORITHM);
    }

}
