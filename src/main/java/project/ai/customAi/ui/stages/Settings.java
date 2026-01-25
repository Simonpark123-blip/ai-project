package project.ai.customAi.ui.stages;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import project.ai.customAi.service.NN.*;
import project.ai.customAi.ui.elements.ComboboxPrefab;
import project.ai.customAi.ui.elements.TextPrefab;
import project.ai.customAi.ui.enums.ActionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

@Slf4j
public class Settings {

    private final NNFeaturedFullwordAlg featuredFullwordAlg;
    private final NNFullwordAlg fullwordAlg;
    private final NNNormalizedAlphanumericAlg normalizedAlphanumericAlg;
    private final NNAlphanumericAlg alphanumericAlg;
    private final NNBinaryAlg binaryAlg;

    public Settings() {
        featuredFullwordAlg = new NNFeaturedFullwordAlg();
        fullwordAlg = new NNFullwordAlg();
        normalizedAlphanumericAlg = new NNNormalizedAlphanumericAlg();
        alphanumericAlg = new NNAlphanumericAlg();
        binaryAlg = new NNBinaryAlg();
    }

    public Scene getConfigScene(StackPane parent) {
        VBox contentBox = new VBox(15);
        contentBox.setPrefWidth(400);
        TextPrefab errorText = new TextPrefab(null, 0, "error-text");

        AtomicReference<String> profile = getProfileSelectionElements(contentBox);
        AtomicReference<AtomicInteger> epochsRef = new AtomicReference<>();
        AtomicReference<ActionType> actionType = new AtomicReference<>();
        TextField userInputField = new TextField("Your word...");

        getActionSelection(contentBox, profile, epochsRef, actionType, userInputField, errorText);
        contentBox.getChildren().add(errorText);
        parent.getChildren().add(contentBox);

        Scene configScene = new Scene(parent, 500, 700);
        configScene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styling/configSceneStyle.css")).toExternalForm()
        );
        return configScene;
    }

    private void getActionSelection(
            VBox parent,
            AtomicReference<String> profile,
            AtomicReference<AtomicInteger> epochsRef,
            AtomicReference<ActionType> actionType,
            TextField userInputField,
            TextPrefab errorText
    ) {
        Button testModel = new Button("Test model");
        Button trainModel = new Button("Train model");
        Button trainAndTestModel = new Button("Train and test model");

        // Train
        trainModel.setOnAction(e -> {
            epochsRef.set(getEpochsElements(parent));
            actionType.set(ActionType.TRAINING);
        });

        // Train & Test
        trainAndTestModel.setOnAction(e -> {
            epochsRef.set(getEpochsElements(parent));
            actionType.set(ActionType.TRAINING_AND_TESTING);
            if (!parent.getChildren().contains(userInputField)) {
                parent.getChildren().add(userInputField);
            }
        });

        // Test
        testModel.setOnAction(e -> {
            actionType.set(ActionType.TESTING);
            if (!parent.getChildren().contains(userInputField)) {
                parent.getChildren().add(userInputField);
            }
        });

        // Start Button
        getStartButtonElements(profile, epochsRef, parent, userInputField, errorText, actionType);

        parent.getChildren().addAll(testModel, trainModel, trainAndTestModel);
    }

    private void getStartButtonElements(
            AtomicReference<String> profile,
            AtomicReference<AtomicInteger> epochsRef,
            VBox parent,
            TextField userInputField,
            TextPrefab errorText,
            AtomicReference<ActionType> actionType
    ) {
        Button startModel = new Button("Start Model");
        TextPrefab resultText = new TextPrefab(null, 0, "success-text");

        startModel.setOnAction(e -> {
            List<String> errorList = new ArrayList<>();
            AtomicInteger epochs = epochsRef.get();

            if (ActionType.TESTING.equals(actionType.get())) {
                if (userInputField.getText().isEmpty()) {
                    errorList.add("Please enter a word!");
                    log.warn("No userInput detected!");
                }
            } else if (ActionType.TRAINING.equals(actionType.get())) {
                if (epochs == null || epochs.get() == 0) {
                    errorList.add("Please select epochs for starting a model!");
                    log.warn("No epochs selected!");
                }
            } else if (ActionType.TRAINING_AND_TESTING.equals(actionType.get())) {
                if (userInputField.getText().isEmpty()) {
                    errorList.add("Please enter a word!");
                    log.warn("No userInput detected!");
                }
                if (epochs == null || epochs.get() == 0) {
                    errorList.add("Please select epochs to start a model!");
                    log.warn("No epochs selected!");
                }
                if (epochs != null && epochs.get() != 0 && !userInputField.getText().isEmpty()) {
                    resultText.setText(handleAlgorithmExec(profile, epochs, errorList, userInputField));
                }
            }

            if (!errorList.isEmpty()) {
                errorText.setText(String.join("\n", errorList));
            } else {
                errorText.setText("");
            }
        });

        parent.getChildren().addAll(startModel, resultText);
    }

    private AtomicInteger getEpochsElements(VBox parent) {
        AtomicInteger epochs = new AtomicInteger();
        TextPrefab epochsText = new TextPrefab(null, 0, null);

        String[] epochsValues = IntStream.iterate(1000, i -> i + 1000)
                .limit(500)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);

        ComboboxPrefab epochsDropdown = new ComboboxPrefab("Select number of epochs", epochsValues, 0);
        epochsDropdown.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                epochs.set(Integer.parseInt(newVal));
                epochsText.setText("Chosen epochs: " + newVal);
            }
        });

        parent.getChildren().addAll(epochsDropdown, epochsText);
        return epochs;
    }

    private AtomicReference<String> getProfileSelectionElements(VBox parent) {
        AtomicReference<String> profile = new AtomicReference<>("");
        TextPrefab profileText = new TextPrefab(null, 0, null);
        String[] profiles = {"binary", "alphanumeric", "normalized-alphanumeric", "fullword", "featured-fullword"};

        ComboboxPrefab profileDropdown = new ComboboxPrefab("Select profile", profiles, 0);
        profileDropdown.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                profile.set(newVal);
                profileText.setText("Custom-AI starting with profile: " + newVal);
            }
        });

        parent.getChildren().addAll(profileDropdown, profileText);
        return profile;
    }

    private String handleAlgorithmExec(AtomicReference<String> profile, AtomicInteger epochs, List<String> errorList, TextField userInputField) {
        return String.valueOf(switch (profile.get()) {
            case "featured-fullword" -> featuredFullwordAlg.handleAlgorithm(userInputField.getText(), null, epochs).get("result");
            case "fullword" -> fullwordAlg.handleAlgorithm(null, null, epochs);
            case "normalized-alphanumeric" -> normalizedAlphanumericAlg.handleAlgorithm(null, null, epochs);
            case "alphanumeric" -> alphanumericAlg.handleAlgorithm(null, null, epochs);
            case "binary" -> binaryAlg.handleAlgorithm(null, null, epochs);
            default -> {
                errorList.add("Please select a valid model!");
                yield true;
            }
        });
    }
}