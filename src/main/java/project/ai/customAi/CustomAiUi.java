package project.ai.customAi;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import project.ai.customAi.config.EnvConfig;
//import project.ai.customAi.config.SpringBootConfig;
import project.ai.customAi.service.NN.FullwordFFNV2;
import project.ai.customAi.service.NN.NNFullwordAlgV2;
import project.ai.customAi.service.fullword.FeatureCalculation;
import project.ai.customAi.ui.uiElements.ComboboxPrefab;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class CustomAiUi extends Application {

    //private ConfigurableApplicationContext context;
    //private EnvConfig envConfig;
    private NNFullwordAlgV2 nnFullwordAlgV2;
    private FeatureCalculation featureCalculation;

    @Override
    public void init() {
        //context = SpringApplication.run(SpringBootConfig.class, "Test");
        //envConfig = context.getBean(EnvConfig.class);
        featureCalculation = new FeatureCalculation();
        nnFullwordAlgV2 = new NNFullwordAlgV2();//context.getBean(NNFullwordAlgV2.class);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Custom AI");

        Group root = new Group();
        Button addFieldButton = new Button("Add Input Field");

        addFieldButton.setOnAction(e -> {
            TextField newField = new TextField();
            newField.setPromptText("Enter text here...");
            root.getChildren().add(newField);
        });
        Text profileText = new Text();
        profileText.setY(100.f);

        Text epochsText = new Text();
        epochsText.setY(105.f);

        AtomicReference<String> profile = new AtomicReference<>("");
        String[] profiles = {"nnFullwordV2"};//envConfig.getCustomVar("spring.profiles.active").split(",");
        ComboboxPrefab profileDropdown = new ComboboxPrefab("Select profile", profiles);
        profileDropdown.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                profileText.setText("Custom AI starting with the following active profile " + newVal);
                profile.set(newVal);
            }
        });
        String[] epochs = IntStream.iterate(10000, i -> i + 10000).limit(10000).boxed().map(Object::toString).map(Object::toString).toArray(String[]::new);
        ComboboxPrefab epochsDropdown = new ComboboxPrefab("Select a number of epochs", epochs);
        epochsDropdown.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                epochsText.setText("Chosen epochs: " + newVal);
            }
        });

        Button startModel = new Button("Start Model");
        startModel.setLayoutY(120.f);
        startModel.setOnAction(e -> {
            switch(profile.get()){
                case "nnFullwordV2" -> nnFullwordAlgV2.handleAlgorithm(null, null);
                default -> throw new IllegalStateException("Unexpected value: " + profile);
            }
        });

        root.getChildren().add(addFieldButton);
        root.getChildren().add(profileText);
        root.getChildren().add(epochsText);
        root.getChildren().add(startModel);
        root.getChildren().add(profileDropdown);
        Scene loadingScene = new Scene(root,500,700);
        stage.setScene(loadingScene);
        stage.show();
    }


    @Override
    public void stop() {
        //context.close();
    }
}
