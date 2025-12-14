package project.ai.customAi.pojo.neuronalNetwork;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Data
@Slf4j
public class Neuron {

    private final int numberOfInputSignals;
    private final double[] weights; // weights.length == numberOfInputSignals + 1 (bias)
    private static final Random RAND = new Random();

    public Neuron(int numberOfInputSignals) {
        this.numberOfInputSignals = numberOfInputSignals;
        this.weights = new double[numberOfInputSignals + 1];
        initWeights();
    }

    private void initWeights() {
        // weights (Xavier/Glorot-initialization based on uniform distribution)
        double range = 1.0 / Math.sqrt(Math.max(1, numberOfInputSignals));
        for (int i = 0; i < numberOfInputSignals; i++) {
            // weights between [-range, +range]
            weights[i] = (RAND.nextDouble() * 2.0 - 1.0) * range;
        }
        // bias
        weights[numberOfInputSignals] = 0.0;
    }

    public double weightedSum(double[] input) {
        double sum = 0.0;
        for (int i = 0; i < numberOfInputSignals; i++) {
            sum += input[i] * weights[i];
        }
        sum += weights[numberOfInputSignals]; // bias
        return sum;
    }

    public double getWeight(int index) {
        return weights[index];
    }

    public void setWeight(int index, double value) {
        weights[index] = value;
    }

}
