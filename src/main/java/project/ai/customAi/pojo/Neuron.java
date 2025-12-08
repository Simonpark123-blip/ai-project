package project.ai.customAi.pojo;

import lombok.Data;
import project.ai.customAi.service.Activation;
import project.ai.customAi.service.ActivationFunction;

import java.util.ArrayList;
import java.util.Random;

@Data
public class Neuron {

    private final int numberOfInputSignals;         // Dendrite
    private double[] weights;                       // Gewichte
    private final ArrayList<Neuron> nextNeurons;    // Axon → nächstes Neuron

    public Neuron(int numberOfInputSignals, ArrayList<Neuron> nextNeurons, double bias) {
        this.numberOfInputSignals = numberOfInputSignals;
        weights = new double[numberOfInputSignals + 1]; // add one more due to bias
        setRandomWeights();
        setBias(bias);
        this.nextNeurons = nextNeurons;
    }

    public Neuron(int numberOfInputSignals, ArrayList<Neuron> nextNeurons) {
        this(numberOfInputSignals, nextNeurons, getRandomValue());
    }

    private void setBias(double bias) {
        weights[weights.length - 1] = bias; // last weight is bias
    }

    private void setRandomWeights() {
        for (int i = 0; i < weights.length - 1; i++)
            weights[i] = getRandomValue();
    }

    private static double getRandomValue() {
        Random random = new Random();
        return 2 * random.nextDouble() - 1; // -1 < weight < 1
    }

    public double getOutput(double[] x, ActivationFunction activationFunction) {
        double output = 0;
        for (int i = 0; i < numberOfInputSignals; i++)
            output += x[i] * weights[i];                    // weights
        output += 1 * weights[numberOfInputSignals];        // bias

        switch (activationFunction) {
            case HEAVISIDE:
                output = Activation.activateWithHeaviside(output);
                break;
            case SIGMOID:
                output = Activation.activateWithSigmoid(output);
                break;
            case ONLYSUM:
            default:
                break;
        }

        return output;
    }

    public double getWeights(int i) {
        return weights[i];
    }

    public void setWeights(int i, double weight) {
        weights[i] = weight;
    }

}
