package project.ai.customAi.service.neuronalNetwork;

import lombok.extern.slf4j.Slf4j;
import project.ai.customAi.pojo.neuronalNetwork.Neuron;
import project.ai.customAi.pojo.neuronalNetwork.ProcessMonitoring;
import project.ai.customAi.pojo.neuronalNetwork.TrainingParameter.AlphanumericTrainingParameter;

import java.util.*;
import java.util.stream.IntStream;

@Slf4j
public class AlphanumericFFN {

    private final ArrayList<Neuron> hiddenLayer = new ArrayList<>();
    private final ArrayList<Neuron> outputLayer = new ArrayList<>();

    // caches (last forward pass)
    private final double[] lastHiddenOutputs;
    private final double[] lastOutputs;

    private final Random rnd = new Random();

    public AlphanumericFFN(int inputCount, int hiddenLayerNeurons, int outputLayerNeurons) {
        buildLayer(inputCount, hiddenLayerNeurons, hiddenLayer);
        buildLayer(hiddenLayerNeurons, outputLayerNeurons, outputLayer);

        lastHiddenOutputs = new double[hiddenLayerNeurons];
        lastOutputs = new double[outputLayerNeurons];

        ProcessMonitoring.lastOutputs = lastOutputs;
        ProcessMonitoring.lastOutputsFromHiddenLayer = lastHiddenOutputs;
    }

    private void buildLayer(int numberOfInputsPerNeuron, int numberOfNeurons, ArrayList<Neuron> layer) {
        for (int i = 0; i < numberOfNeurons; i++) {
            layer.add(new Neuron(numberOfInputsPerNeuron));
        }
    }

    public double[][] getWeightsOfHiddenLayer() {
        double[][] weights = new double[hiddenLayer.size()][];
        for (int i = 0; i < hiddenLayer.size(); i++)
            weights[i] = hiddenLayer.get(i).getWeights();
        return weights;
    }

    public double[][] getWeightsOfOutputLayer() {
        double[][] weights = new double[outputLayer.size()][];
        for (int i = 0; i < outputLayer.size(); i++)
            weights[i] = outputLayer.get(i).getWeights();
        return weights;
    }

    // forward pass (pure feed-forward)
    public void calculateOutput(double[] input) {
        // Hidden layer: weighted sum -> activation
        for (int currentNeuronIndex = 0; currentNeuronIndex < hiddenLayer.size(); currentNeuronIndex++) {
            double weightedResult = hiddenLayer.get(currentNeuronIndex).weightedSum(input);
            lastHiddenOutputs[currentNeuronIndex] = applyActivation(weightedResult);
        }

        // Output layer: weighted sum (logits)
        for (int currentNeuronIndex = 0; currentNeuronIndex < outputLayer.size(); currentNeuronIndex++) {
            double weightedResult = outputLayer.get(currentNeuronIndex).weightedSum(lastHiddenOutputs);
            lastOutputs[currentNeuronIndex] = weightedResult;
        }

        // softmax per 26-block
        applyBlockSoftmax();

        ProcessMonitoring.lastOutputs = lastOutputs;
        ProcessMonitoring.lastOutputsFromHiddenLayer = lastHiddenOutputs;
    }

    public void trainWithSupervisedLearning() {
        int trainingCaseCount = AlphanumericTrainingParameter.inputs.length;

        // indices to shuffle
        List<Integer> indices = IntStream.range(0, trainingCaseCount).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        // iterate through epochs
        for (int epoch = 1; epoch <= AlphanumericTrainingParameter.numberOfEpochs; epoch++) {

            Collections.shuffle(indices, rnd);

            // iterate through testCases
            for (int idx : indices) {
                double[] trainingInput = AlphanumericTrainingParameter.inputs[idx];
                double[] expectedOutput = AlphanumericTrainingParameter.targets[idx];

                // forward
                calculateOutput(trainingInput);

                // backpropagation
                backpropagateAndUpdate(trainingInput, expectedOutput);
            }

            // logging (current epoch)
            if (epoch % 10000 == 0) {
                double totalErr = 0.0;
                for (int i = 0; i < trainingCaseCount; i++) {
                    calculateOutput(AlphanumericTrainingParameter.inputs[i]);
                    totalErr += calculateTotalError(AlphanumericTrainingParameter.targets[i]);
                }
                totalErr /= trainingCaseCount;
                log.info("Epoch {} - avg total error: {}", epoch, totalErr);
            }
        }
    }

    private void applyBlockSoftmax() {
        int blocks = lastOutputs.length / 26;
        for (int b = 0; b < blocks; b++) {
            int offset = b * 26;
            double max = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < 26; i++) max = Math.max(max, lastOutputs[offset + i]);

            double sum = 0.0;
            for (int i = 0; i < 26; i++) {
                lastOutputs[offset + i] = Math.exp(lastOutputs[offset + i] - max);
                sum += lastOutputs[offset + i];
            }
            for (int i = 0; i < 26; i++) {
                lastOutputs[offset + i] /= sum;
            }
        }
    }

    private double calculateTotalError(double[] targets) {
        double loss = 0.0;

        for (int block = 0; block < targets.length; block += 26) {
            for (int i = 0; i < 26; i++) {
                loss -= targets[block + i] * Math.log(lastOutputs[block + i] + 1e-9);
            }
        }
        return loss;
    }

    private void backpropagateAndUpdate(double[] input, double[] targets) {
        int outputNeuronCount = outputLayer.size();
        int hiddenNeuronCount = hiddenLayer.size();

        /// output-neurons
        double[] deltaOutput = new double[outputNeuronCount];
        for (int i = 0; i < outputNeuronCount; i++) {
            // grad for softmax + cross-entropy
            deltaOutput[i] = lastOutputs[i] - targets[i];
        }

        // update output weights: input to output are lastHiddenOutputs
        updateNeuronWeights(outputNeuronCount, deltaOutput, outputLayer, lastHiddenOutputs);

        /// hidden-neurons
        double[] deltaHidden = new double[hiddenNeuronCount];
        for (int currentNeuronIndex = 0; currentNeuronIndex < hiddenNeuronCount; currentNeuronIndex++) {
            double sum = 0.0;
            for (int outputNeuronIndex = 0; outputNeuronIndex < outputNeuronCount; outputNeuronIndex++) {
                sum += outputLayer.get(outputNeuronIndex).getWeight(currentNeuronIndex) * deltaOutput[outputNeuronIndex];
            }
            double hiddenLayerOutput = lastHiddenOutputs[currentNeuronIndex];
            double derivHidden = activationDerivative(hiddenLayerOutput);
            deltaHidden[currentNeuronIndex] = derivHidden * sum;
        }

        // update hidden weights: input to hidden are original input
        updateNeuronWeights(hiddenNeuronCount, deltaHidden, hiddenLayer, input);
    }

    private void updateNeuronWeights(int neuronCount, double[] delta, ArrayList<Neuron> layer, double[] input) {
        for (int i = 0; i < neuronCount; i++) {
            Neuron currentNeuron = layer.get(i);
            int weightCount = currentNeuron.getWeights().length; // inputs + bias
            // update weights
            for (int currentWeightIndex = 0; currentWeightIndex < weightCount - 1; currentWeightIndex++) {
                double oldWeight = currentNeuron.getWeight(currentWeightIndex);
                double deltaWeight = AlphanumericTrainingParameter.learningRate * delta[i] * input[currentWeightIndex];
                currentNeuron.setWeight(currentWeightIndex, oldWeight - deltaWeight);
            }
            // update bias
            double biasOld = currentNeuron.getWeight(weightCount - 1);
            currentNeuron.setWeight(weightCount - 1, biasOld - AlphanumericTrainingParameter.learningRate * delta[i]);
        }
    }

    public void testAllInputsAndShowResults(double[][] inputs, double[][] targets) {
        double successCases = 0;
        double totalErrorAtAll = 0;

        for (int i = 0; i < inputs.length; i++) {
            calculateOutput(inputs[i]);
            totalErrorAtAll += calculateTotalError(targets[i]);

            log.info("Expected input of '{}' (length {}) to result in '{}' and was '{}' (length {})", inputs[i], inputs[i].length, targets[i], lastOutputs, lastOutputs.length);
            log.info("Expected input of '{}' to result in '{}' and was '{}'", convertBinaryToChar(inputs[i]), convertBinaryToChar(targets[i]), convertBinaryToChar(lastOutputs));

            if (outputsMatchTarget(lastOutputs, targets[i])) successCases++;
        }

        double learnedInPercent = successCases / inputs.length * 100;
        totalErrorAtAll /= inputs.length;
        log.info("Average of total error at all outputs: {}", totalErrorAtAll);
        log.info("Learned percent at all outputs: {}", learnedInPercent);
    }

    private boolean outputsMatchTarget(double[] outputs, double[] targets) {
        int blocks = outputs.length / 26;
        for (int b = 0; b < blocks; b++) {
            int offset = b * 26;
            int pred = 0, want = 0;
            for (int i = 0; i < 26; i++) {
                if (outputs[offset + i] > outputs[offset + pred]) pred = i;
                if (targets[offset + i] == 1.0) want = i;
            }
            if (pred != want) return false;
        }
        return true;
    }

    private char[] convertBinaryToChar(double[] probs) {
        char[] result = new char[probs.length / 26];
        for (int block = 0; block < result.length; block++) {
            int offset = block * 26;
            int maxIndex = offset;
            for (int i = offset; i < offset + 26; i++) {
                if (probs[i] > probs[maxIndex]) {
                    maxIndex = i;
                }
            }
            result[block] = (char) ('A' + (maxIndex % 26));
        }
        return result;
    }

    private double applyActivation(double value) {
        return switch (AlphanumericTrainingParameter.activationFunction) {
            case SIGMOID -> 1.0 / (1.0 + Math.exp(-value));
            case HEAVISIDE -> value >= 0.0 ? 1.0 : 0.0;
            default -> value;
        };
    }

    private double activationDerivative(double activatedValue) {
        return switch (AlphanumericTrainingParameter.activationFunction) {
            case SIGMOID -> activatedValue * (1.0 - activatedValue);
            case ONLYSUM -> 1.0;
            default -> throw new IllegalStateException("Not differentiable activation function");
        };
    }

}