package project.ai.customAi.ui.elements;

import javafx.stage.Stage;

public class StagePrefab extends Stage {

    public StagePrefab(String title) {
        super();
        this.setTitle(title);
        this.centerOnScreen();
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.toFront();
        this.requestFocus();
    }

}
