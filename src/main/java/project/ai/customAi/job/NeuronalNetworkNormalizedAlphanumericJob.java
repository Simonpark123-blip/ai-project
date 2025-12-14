package project.ai.customAi.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.logConstants.PrefixConstant;
import project.ai.customAi.service.neuronalNetwork.NeuronalNetworkAlphanumericAlg;
import project.ai.customAi.service.neuronalNetwork.NeuronalNetworkNormalizedAlphanumericAlg;

@Slf4j
@Service
@Profile("neuronalNetworkNormalizedAlphanumericUsage")
public class NeuronalNetworkNormalizedAlphanumericJob extends AbstractAiAlgorithmJob {

    public NeuronalNetworkNormalizedAlphanumericJob(NeuronalNetworkNormalizedAlphanumericAlg alg) {
        super(alg, PrefixConstant.NEURONAL_NETWORK_ALGORITHM);
    }

}