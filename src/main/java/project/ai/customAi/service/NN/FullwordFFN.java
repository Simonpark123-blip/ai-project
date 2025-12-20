package project.ai.customAi.service.NN;

import lombok.extern.slf4j.Slf4j;
import project.ai.customAi.pojo.NN.Neuron;
import project.ai.customAi.pojo.NN.ProcessMonitoring;
import project.ai.customAi.pojo.NN.TrainingParameter.FullwordTrainingParameter;

import java.util.*;
import java.util.stream.IntStream;

@Slf4j
public class FullwordFFN {

    private final ArrayList<Neuron> hiddenLayer = new ArrayList<>();
    private final ArrayList<Neuron> outputLayer = new ArrayList<>();

    // caches (last forward pass)
    private final double[] lastHiddenOutputs;
    private final double[] lastOutputs;

    private final Random rnd = new Random();

    public FullwordFFN(int inputCount, int hiddenLayerNeurons, int outputLayerNeurons) {
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

    // forward pass
    public void calculateOutput(double[] input) {
        // Hidden layer: weighted sum -> activation
        for (int currentNeuronIndex = 0; currentNeuronIndex < hiddenLayer.size(); currentNeuronIndex++) {
            double weightedResult = hiddenLayer.get(currentNeuronIndex).weightedSum(input);
            lastHiddenOutputs[currentNeuronIndex] = applyActivation(weightedResult);
        }

        // Output layer: weighted sum -> activation
        for (int currentNeuronIndex = 0; currentNeuronIndex < outputLayer.size(); currentNeuronIndex++) {
            double weightedResult = outputLayer.get(currentNeuronIndex).weightedSum(lastHiddenOutputs);
            lastOutputs[currentNeuronIndex] = applyActivation(weightedResult);
        }

        ProcessMonitoring.lastOutputs = lastOutputs;
        ProcessMonitoring.lastOutputsFromHiddenLayer = lastHiddenOutputs;
    }

    public void trainWithSupervisedLearning() {
        int trainingCaseCount = FullwordTrainingParameter.inputs.length;

        // indices to shuffle
        List<Integer> indices = IntStream.range(0, trainingCaseCount).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        // iterate through epochs
        for (int epoch = 1; epoch <= FullwordTrainingParameter.numberOfEpochs; epoch++) {

            Collections.shuffle(indices, rnd);

            // iterate through testCases
            for (int idx : indices) {
                double[] trainingInput = FullwordTrainingParameter.inputs[idx];
                double[] expectedOutput = FullwordTrainingParameter.targets[idx];

                // forward
                calculateOutput(trainingInput);

                // backpropagation
                backpropagateAndUpdate(trainingInput, expectedOutput);
            }

            // logging (current epoch)
            if (epoch % 10000 == 0 || epoch == FullwordTrainingParameter.numberOfEpochs) {
                double totalErr = 0.0;
                for (int i = 0; i < trainingCaseCount; i++) {
                    calculateOutput(FullwordTrainingParameter.inputs[i]);
                    totalErr += calculateTotalError(FullwordTrainingParameter.targets[i]);
                }
                totalErr /= trainingCaseCount;
                log.info("Epoch {} - avg total error: {}", epoch, totalErr);
            }
        }
    }

    // sum of squared errors (SSE) per pattern (return sum of 0.5*(o-t)^2 over outputs)
    private double calculateTotalError(double[] targets) {
        double totalError = 0;
        for (int i = 0; i < lastOutputs.length; i++)
            totalError += 0.5 * Math.pow(lastOutputs[i] - targets[i], 2);
        return totalError;
    }

    private void backpropagateAndUpdate(double[] input, double[] targets) {
        int outputNeuronCount = outputLayer.size();
        int hiddenNeuronCount = hiddenLayer.size();

        /// output-neurons
        double[] deltaOutput = new double[outputNeuronCount];
        for (int currentNeuronIndex = 0; currentNeuronIndex < outputNeuronCount; currentNeuronIndex++) {
            double errorWrtOutput = lastOutputs[currentNeuronIndex] - targets[currentNeuronIndex]; // dE/dy
            double deriv = activationDerivative(lastOutputs[currentNeuronIndex]); // dy/dnet
            deltaOutput[currentNeuronIndex] = errorWrtOutput * deriv; // dE/dnet
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
            double hiddenLayerOutput = lastHiddenOutputs[currentNeuronIndex]; // activated output
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
            // update weights (without bias)
            for (int currentWeightIndex = 0; currentWeightIndex < weightCount - 1; currentWeightIndex++) {
                double oldWeight = currentNeuron.getWeight(currentWeightIndex);
                double deltaWeight = FullwordTrainingParameter.learningRate * delta[i] * input[currentWeightIndex];
                currentNeuron.setWeight(currentWeightIndex, oldWeight - deltaWeight);
            }
            // update bias (last weight)
            double biasOld = currentNeuron.getWeight(weightCount - 1);
            currentNeuron.setWeight(weightCount - 1, biasOld - FullwordTrainingParameter.learningRate * delta[i]);
        }
    }

    public void testAllInputsAndShowResults(double[][] inputs, double[][] targets) {
        double successCases = 0;
        double totalErrorAtAll = 0;

        for (int i = 0; i < inputs.length; i++) {
            calculateOutput(inputs[i]);
            totalErrorAtAll += calculateTotalError(targets[i]);

            log.info("Expected input '{}' (length {}) to result in '{}' and was '{}'", Arrays.toString(inputs[i]), inputs[i].length, Arrays.toString(targets[i]), Arrays.toString(lastOutputs));
            log.info("Expected input '{}' to result in '{}' and was '{}'", convertBinaryToChar(inputs[i]), convertBinaryToChar(targets[i]), convertBinaryToChar(lastOutputs));

            boolean areAllOutputsOK = true;
            for (int j = 0; j < targets[i].length; j++) {
                if (Math.abs(lastOutputs[j] - targets[i][j]) > FullwordTrainingParameter.faultTolerance) {
                    areAllOutputsOK = false;
                    break;
                }
            }
            if (areAllOutputsOK) successCases++;
        }

        double learnedInPercent = successCases / inputs.length * 100;
        totalErrorAtAll /= inputs.length;
        log.info("Average of total error at all outputs: {}", totalErrorAtAll);
        log.info("Learned percent at all outputs: {}", learnedInPercent);
    }

    private char[] convertBinaryToChar(double[] encodedChars) {
        char[] result = new char[encodedChars.length];
        for (int i = 0; i < result.length; i++) {
            int val = (int) Math.round((encodedChars[i] + 1.0) / 2.0 * 25);
            val = Math.max(0, Math.min(25, val));
            result[i] = (char) ('A' + val);
        }
        return result;
    }

    private double applyActivation(double value) {
        return switch (FullwordTrainingParameter.activationFunction) {
            case SIGMOID -> 1.0 / (1.0 + Math.exp(-value));
            case HEAVISIDE -> value >= 0.0 ? 1.0 : 0.0;
            case TANH -> Math.tanh(value);
            default -> value;
        };
    }

    private double activationDerivative(double activatedValue) {
        return switch (FullwordTrainingParameter.activationFunction) {
            case SIGMOID -> activatedValue * (1.0 - activatedValue);
            case ONLYSUM -> 1.0;
            case TANH -> 1 - Math.pow(activatedValue, 2);
            default -> throw new IllegalStateException("Not differentiable activation function: " + FullwordTrainingParameter.activationFunction);
        };
    }

}
