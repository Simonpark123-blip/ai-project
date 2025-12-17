package project.ai.customAi.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.logConstant.PrefixConstant;
import project.ai.customAi.service.neuronalNetwork.NeuronalNetworkAlphanumericAlg;

@Slf4j
@Service
@Profile("nnAlphanumeric")
public class NNAlphanumericJob extends AbstractAiAlgorithmJob {

    public NNAlphanumericJob(NeuronalNetworkAlphanumericAlg alg) {
        super(alg, PrefixConstant.NN_ALPHANUMERIC_ALGORITHM);
    }

}
