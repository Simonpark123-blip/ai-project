package project.ai.customAi.service.perceptron;

import lombok.extern.slf4j.Slf4j;
import project.ai.customAi.pojo.Neuron;
import project.ai.customAi.pojo.ProcessMonitoring;
import project.ai.customAi.pojo.TrainingParameter;

import java.util.Random;

@Slf4j
public class MachineLearning {

    private double[] inputs;
    private final Neuron neuron;
    private final TrainingParameter trainingParameter;

    public MachineLearning(TrainingParameter trainingParameter) {
        this.trainingParameter = trainingParameter;
        int numberOfInputSignals = this.trainingParameter.getInputs()[0].length;
        inputs = new double[numberOfInputSignals];
        neuron = new Neuron(numberOfInputSignals, null);
        ProcessMonitoring.lastWeights = neuron.getWeights();
    }

    public void trainWithSupervisedLearning() {
        Random random = new Random();
        for (int epoche = 1; epoche <= this.trainingParameter.getNumberOfEpochs(); epoche++) {
            int sample = random.nextInt(this.trainingParameter.getInputs().length); // choose a random dataset from LogicalAndData
            log.info("Training epoch {} with sample {}", epoche, sample + 1);
            calculateOutput(this.trainingParameter.getInputs()[sample], this.trainingParameter.getActivationFunction());
            if (this.trainingParameter.getTargets()[sample] - ProcessMonitoring.lastOutputWithActivationFunction != 0) {
                calculateNewWeights(this.trainingParameter.getTargets()[sample]);
            }
        }
        log.info("Training epoch {} complete", this.trainingParameter.getNumberOfEpochs());
    }

    public double testAllInputsAndShowResults() {
        double learnedInPercent = 0;
        log.info("Outputs: ");
        for (int i = 0; i < this.trainingParameter.getInputs().length; i++) {
            calculateOutput(this.trainingParameter.getInputs()[i], this.trainingParameter.getActivationFunction());
            log.info("{} -> activation-function: {}", ProcessMonitoring.lastOutputAsSum, ProcessMonitoring.lastOutputWithActivationFunction);
            if (ProcessMonitoring.lastOutputWithActivationFunction == this.trainingParameter.getTargets()[i]) {
                learnedInPercent++;
            }
        }
        learnedInPercent /= this.trainingParameter.getInputs().length * 0.01;
        log.info("Learned input percentage is {}%", learnedInPercent);
        return learnedInPercent;
    }

    public void calculateOutput(double[] inputs, ActivationFunction activationFunction) {
        this.inputs = inputs;
        ProcessMonitoring.lastOutputAsSum = neuron.getOutput(inputs, ActivationFunction.ONLYSUM);
        ProcessMonitoring.lastOutputWithActivationFunction = neuron.getOutput(inputs, activationFunction);
        ProcessMonitoring.lastWeights = neuron.getWeights();
    }

    private void calculateNewWeights(double target) {
        double error = target - ProcessMonitoring.lastOutputWithActivationFunction;
        log.error("Error: {}", error);
        for (int i = 0; i < ProcessMonitoring.lastWeights.length; i++) {
            double newWeight = neuron.getWeights(i) + calculateDeltaW(i, error);
            neuron.setWeights(i, newWeight);
        }
        log.warn("New weights: {}", neuron.getWeights());
    }

    /**
    * delta rule = error * learningRate * input
    * error = difference expected and actual result
    * learningRate = ratio: how seriously should a weight be changed (defined by user)
    * input = ratio: how significant is the neuron for the calculation (not necessary if input is 0, but extremely necessary if input is large)
    *                   bias always counts with significance of 1
    */
    private double calculateDeltaW(int i, double error) {
        double deltaW = error * this.trainingParameter.getLearningRate();
        deltaW *= (i < inputs.length) ? inputs[i] : 1;                      // weight : bias
        return deltaW;
    }

}
