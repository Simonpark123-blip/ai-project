package project.ai.customAi.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import project.ai.customAi.logConstant.PrefixConstant;
import project.ai.customAi.service.neuronalNetwork.NeuronalNetworkBinaryAlg;

@Slf4j
@Service
@Profile("nnBinary")
public class NNBinaryJob extends AbstractAiAlgorithmJob {

    public NNBinaryJob(NeuronalNetworkBinaryAlg alg) {
        super(alg, PrefixConstant.NN_BINARY_ALGORITHM);
    }

}
