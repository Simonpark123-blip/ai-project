package project.ai.customAi.ui.elements;

import javafx.scene.text.Text;

public class TextPrefab extends Text {

    public TextPrefab(String content, double positionY, String styleClass){
        super();
        this.setLayoutY(positionY);
        this.setText(content);
        this.getStyleClass().add(styleClass);
    }

}
