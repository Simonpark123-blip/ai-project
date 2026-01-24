package project.ai.customAi.ui.uiElements;

import javafx.scene.control.ComboBox;

public class ComboboxPrefab extends ComboBox<String> {

    public ComboboxPrefab(String promptText, String[] dropdownValues){
        super();
        this.setLayoutY(100);
        this.setPromptText(promptText);
        this.getItems().addAll(dropdownValues);
    }

}
