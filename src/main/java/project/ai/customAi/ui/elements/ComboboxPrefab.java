package project.ai.customAi.ui.elements;

import javafx.scene.control.ComboBox;

public class ComboboxPrefab extends ComboBox<String> {

    public ComboboxPrefab(String promptText, String[] dropdownValues, double positionY){
        super();
        this.setLayoutY(positionY);
        this.setPromptText(promptText);
        this.getItems().addAll(dropdownValues);
    }

}
