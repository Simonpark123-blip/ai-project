package project.ai.customAi.service.NN;

import lombok.extern.slf4j.Slf4j;
import project.ai.customAi.pojo.NN.ProcessMonitoring;
import project.ai.customAi.pojo.NN.TrainingParameter.TrainingParameter;
import project.ai.customAi.pojo.NN.Neuron;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Slf4j
public class FFN {

    private final ArrayList<Neuron> hiddenLayer = new ArrayList<>();
    private final ArrayList<Neuron> outputLayer = new ArrayList<>();

    // caches (last forward pass)
    private final double[] lastHiddenOutputs;
    private final double[] lastOutputs;

    private final Random rnd = new Random();

    public FFN(int inputCount, int hiddenLayerNeurons, int outputLayerNeurons) {
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
        int trainingCaseCount = TrainingParameter.inputs.length;

        // indices to shuffle
        List<Integer> indices = IntStream.range(0, trainingCaseCount).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        // iterate through epochs
        for (int epoch = 1; epoch <= TrainingParameter.numberOfEpochs; epoch++) {

            Collections.shuffle(indices, rnd);

            // iterate through testCases
            for (int idx : indices) {
                double[] trainingInput = TrainingParameter.inputs[idx];
                double[] expectedOutput = TrainingParameter.targets[idx];

                // forward
                calculateOutput(trainingInput);

                // backpropagation
                backpropagateAndUpdate(trainingInput, expectedOutput);
            }

            // logging (current epoch)
            if (epoch % 10000 == 0) {
                double totalErr = 0.0;
                for (int i = 0; i < trainingCaseCount; i++) {
                    calculateOutput(TrainingParameter.inputs[i]);
                    totalErr += calculateTotalError(TrainingParameter.targets[i]);
                }
                totalErr /= trainingCaseCount;
                log.info("Epoch {} - avg total error: {}", epoch, totalErr);
            }
        }
    }

    // sum of squared errors (SSE) == mean squared error (MSE)
    // calculates SSE to ensure that extreme differences to target
    // can easily be detected (using gradient calculated from derival)
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
        // iterate through output-neurons
        double[] deltaOutput = new double[outputNeuronCount];
        for (int currentNeuronIndex = 0; currentNeuronIndex < outputNeuronCount; currentNeuronIndex++) {
            double output = lastOutputs[currentNeuronIndex];
            double error = targets[currentNeuronIndex] - output;
            double deriv = activationDerivative(output);
            // delta-rule for output-neurons
            deltaOutput[currentNeuronIndex] = error * deriv;
        }

        updateNeuronWeights(outputNeuronCount, deltaOutput, outputLayer, lastHiddenOutputs);

        /// hidden-neurons
        // back-propagate all errors from output-layer to hiddenlayer (feed-forward)
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

        updateNeuronWeights(hiddenNeuronCount, deltaHidden, hiddenLayer, input);
    }

    private void updateNeuronWeights(int neuronCount, double[] delta, ArrayList<Neuron> layer, double[] input) {
        for (int i = 0; i < neuronCount; i++) {
            Neuron currentNeuron = layer.get(i);
            int weightCount = currentNeuron.getWeights().length; // hidden + bias
            // update weights
            for (int currentWeightIndex = 0; currentWeightIndex < weightCount - 1; currentWeightIndex++) {
                double oldWeight = currentNeuron.getWeight(currentWeightIndex);
                double deltaWeight = TrainingParameter.learningRate * delta[i] * input[currentWeightIndex];
                currentNeuron.setWeight(currentWeightIndex, oldWeight + deltaWeight);
            }
            // update bias
            double biasOld = currentNeuron.getWeight(weightCount - 1);
            currentNeuron.setWeight(weightCount - 1, biasOld + TrainingParameter.learningRate * delta[i]);
        }
    }

    public void testAllInputsAndShowResults() {
        double successCases = 0;
        double totalErrorAtAll = 0;

        // iterate through test-data
        for (int i = 0; i < TrainingParameter.inputs.length; i++) {
            calculateOutput(TrainingParameter.inputs[i]);
            totalErrorAtAll += calculateTotalError(TrainingParameter.targets[i]);
            boolean areAllOutputsOK = true;
            for (int j = 0; j < TrainingParameter.targets[0].length; j++) {
                // check if diff between actual and expected result (absolute) is within tolerance
                if (Math.abs(lastOutputs[j] - TrainingParameter.targets[i][j]) > TrainingParameter.faultTolerance) {
                    areAllOutputsOK = false;
                    break;
                }
            }
            if (areAllOutputsOK) successCases++;
        }

        double learnedInPercent = successCases / TrainingParameter.inputs.length * 100;
        totalErrorAtAll /= TrainingParameter.inputs.length;
        log.info("Average of total error at all outputs: {}", totalErrorAtAll);
        log.info("Learned percent at all outputs: {}", learnedInPercent);
    }

    private double applyActivation(double value) {
        return switch (TrainingParameter.activationFunction) {
            case SIGMOID -> 1.0 / (1.0 + Math.exp(-value)); // analog
            case HEAVISIDE -> value >= 0.0 ? 1.0 : 0.0; // digital
            default -> value; // identity: input of activation == output
        };
    }

    // using derivation-func to estimate direction, which is needed
    // to adjust weights (negative gradient)
    // derivation-func of sigmoid: sigma'(z) = sigma(z) * (1 - sigma(z))
    private double activationDerivative(double activatedValue) {
        return switch (TrainingParameter.activationFunction) {
            case SIGMOID -> activatedValue * (1.0 - activatedValue);
            case ONLYSUM -> 1.0;
            default -> throw new IllegalStateException("Not differentiable activation function");
        };
    }
}
