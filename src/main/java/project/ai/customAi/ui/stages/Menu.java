package project.ai.customAi.ui.stages;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import project.ai.customAi.ui.elements.StagePrefab;

import java.util.Objects;

@Slf4j
public class Menu {

    private final StagePrefab stage;

    public Menu(StagePrefab stage) {
        this.stage = stage;
    }

    public Scene getMenuScene(StackPane parent) {
        VBox menuBox = createMenuButtons();
        parent.getChildren().add(menuBox);

        Scene scene = new Scene(parent, 500, 700);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/styling/configSceneStyle.css")).toExternalForm()
        );

        return scene;
    }

    private VBox createMenuButtons() {
        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);

        Button viewModelsButton = new Button("View models");
        Button settingsButton = new Button("Settings");
        Button exitButton = new Button("Exit");

        viewModelsButton.setOnAction(e -> {
                Settings settings = new Settings();

                StackPane settingsRoot = new StackPane();
                Scene settingsScene = settings.getConfigScene(settingsRoot);

                stage.setScene(settingsScene);
            }
        );

        settingsButton.setOnAction(e ->
                System.out.println("Settings clicked")
        );

        exitButton.setOnAction(e ->
                System.exit(0)
        );

        box.getChildren().addAll(
                viewModelsButton,
                settingsButton,
                exitButton
        );

        return box;
    }
}