package project.ai.customAi.logConstant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PrefixConstant {

    STRAIGHT_FORWARD_ALGORITHM("Straight-Forward"),
    SINGLE_PERCEPTRON_ALGORITHM("Single-Perceptron"),
    NN_BINARY_ALGORITHM("Binary-NN"),
    NN_ALPHANUMERIC_ALGORITHM("Alphanumeric-NN"),
    NN_NORMALIZED_ALPHANUMERIC_ALGORITHM("Normalized-Alphanumeric-NN"),
    NN_FULLWORD_ALGORITHM("Fullword-NN");

    private final String label;

}
