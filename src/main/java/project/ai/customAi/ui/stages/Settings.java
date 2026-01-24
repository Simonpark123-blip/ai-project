package project.ai.customAi.ui.stages;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import project.ai.customAi.service.NN.*;
import project.ai.customAi.ui.elements.ComboboxPrefab;

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

    public Settings(){
        featuredFullwordAlg = new NNFeaturedFullwordAlg();
        fullwordAlg = new NNFullwordAlg();
        normalizedAlphanumericAlg = new NNNormalizedAlphanumericAlg();
        alphanumericAlg = new NNAlphanumericAlg();
        binaryAlg = new NNBinaryAlg();
    }

    public void metaConfig(Stage stage){
        stage.setTitle("Custom AI - Configuration");
        stage.setScene(getConfigScene());
    }

    private Scene getConfigScene() {
        Group root = getConfigSceneGroup();
        Scene configScene = new Scene(root,500,700);

        configScene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styling/configSceneStyle.css")).toExternalForm()
        );

        return configScene;
    }

    private Group getConfigSceneGroup() {
        Group root = new Group();

        // init error-handling
        List<String> errorList = new ArrayList<>();
        Text errorText = new Text();
        errorText.getStyleClass().add("error-text");
        errorText.setLayoutY(340);

        // get all other elements
        AtomicReference<String> profile = getProfileSelectionElements(root);
        AtomicInteger epochs = getEpochsElements(root);
        getStartButtonElements(profile, epochs, root, errorList, errorText);

        root.getChildren().add(errorText);
        return root;
    }

    private void getStartButtonElements(AtomicReference<String> profile, AtomicInteger epochs, Group root, List<String> errorList, Text errorText) {
        Button startModel = new Button("Start Model");

        startModel.setLayoutY(300.f);
        startModel.setOnAction(e -> {
            errorList.clear();

            if(epochs.get() == 0){
                log.warn("No epochs selected!");
                errorList.add("Please select epochs for starting a model! ");
            }

            switch(profile.get()){
                case "featured-fullword" -> featuredFullwordAlg.handleAlgorithm(null, null, epochs);
                case "fullword" -> fullwordAlg.handleAlgorithm(null, null, epochs);
                case "normalized-alphanumeric" -> normalizedAlphanumericAlg.handleAlgorithm(null, null, epochs);
                case "alphanumeric" -> alphanumericAlg.handleAlgorithm(null, null, epochs);
                case "binary" -> binaryAlg.handleAlgorithm(null, null, epochs);
                default -> errorList.add("Please select a valid model!");
            }

            // set error-messages
            if (!errorList.isEmpty()) {
                errorText.setText(String.join("\n", errorList));
                errorText.toFront();
            } else {
                errorText.setText("");
            }
        });

        root.getChildren().add(startModel);

    }

    private AtomicInteger getEpochsElements(Group root) {
        Text epochsText = new Text();
        epochsText.setLayoutY(200.f);

        AtomicInteger epochs = new AtomicInteger();
        String[] epochsValues = IntStream.iterate(10000, i -> i + 10000).limit(100).boxed().map(Object::toString).map(Object::toString).toArray(String[]::new);

        ComboboxPrefab epochsDropdown = new ComboboxPrefab("Select a number of epochs", epochsValues);
        epochsDropdown.setLayoutY(160.f);
        epochsDropdown.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                epochsText.setText("Chosen epochs: " + newVal);
                epochs.set(Integer.parseInt(newVal));
            }
        });
        root.getChildren().add(epochsDropdown);
        root.getChildren().add(epochsText);

        return epochs;
    }

    private AtomicReference<String> getProfileSelectionElements(Group root) {
        AtomicReference<String> profile = new AtomicReference<>("");
        String[] profiles = {"binary", "alphanumeric", "normalized-alphanumeric", "fullword", "featured-fullword"};
        Text profileText = new Text();
        profileText.setLayoutY(150.f);

        ComboboxPrefab profileDropdown = new ComboboxPrefab("Select profile", profiles);
        profileDropdown.setLayoutY(110.f);
        profileDropdown.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                profileText.setText("Custom-AI starting with the following chosen profile: " + newVal);
                profile.set(newVal);
            }
        });

        root.getChildren().add(profileDropdown);
        root.getChildren().add(profileText);
        return profile;
    }

}

//        Button addFieldButton = new Button("Add Input Field");
//
//        addFieldButton.setOnAction(e -> {
//            TextField newField = new TextField();
//            newField.setPromptText("Enter text here...");
//            root.getChildren().add(newField);
//        });
//        root.getChildren().add(addFieldButton);