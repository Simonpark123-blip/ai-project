package project.ai.customAi.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.logConstant.PrefixConstant;
import project.ai.customAi.service.neuronalNetwork.NeuronalNetworkNormalizedAlphanumericAlg;

@Slf4j
@Service
@Profile("nnNormalizedAlphanumeric")
public class NNNormalizedAlphanumericJob extends AbstractAiAlgorithmJob {

    public NNNormalizedAlphanumericJob(NeuronalNetworkNormalizedAlphanumericAlg alg) {
        super(alg, PrefixConstant.NN_NORMALIZED_ALPHANUMERIC_ALGORITHM);
    }

}