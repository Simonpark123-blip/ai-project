package project.ai.customAi.service;

public class Activation {

    public static double activateWithHeaviside(double sum) { // digital
        if (sum > 0)
            return 1;
        return 0;
    }

    public static double activateWithSigmoid(double sum) { // analog
        return (1 / (1 + Math.exp(-sum)));
    }

}
