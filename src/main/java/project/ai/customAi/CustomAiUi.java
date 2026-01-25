package project.ai.customAi;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import project.ai.customAi.ui.elements.ProgressBarPrefab;
import project.ai.customAi.ui.elements.StagePrefab;
import project.ai.customAi.ui.stages.Menu;

@Slf4j
public class CustomAiUi extends Application {

    @Override
    public void start(Stage primaryStage) {
        showPreloaderStage(() -> {
            StackPane root = new StackPane();

            StagePrefab menuStage = new StagePrefab("Custom-AI Menu");
            Menu menu = new Menu(menuStage);

            menuStage.setScene(menu.getMenuScene(root));
            menuStage.show();
        });
    }

    private void showPreloaderStage(Runnable onFinished) {
        StackPane root = new StackPane();
        Scene preloaderScene = new Scene(root, 400, 100);
        StagePrefab preloaderStage = new StagePrefab("Loading Custom-AI...");
        preloaderStage.setScene(preloaderScene);

        ProgressBarPrefab progressBarPrefab = new ProgressBarPrefab();
        root.getChildren().add(progressBarPrefab);
        preloaderStage.show();

        Thread.startVirtualThread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    int progress = i;
                    Platform.runLater(() -> progressBarPrefab.setProgress(progress / 100.0));
                    Thread.sleep(30);
                }
                Platform.runLater(() -> {
                    preloaderStage.close();
                    onFinished.run();
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}