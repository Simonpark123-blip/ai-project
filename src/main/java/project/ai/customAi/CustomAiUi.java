package project.ai.customAi;

import javafx.application.Application;
import javafx.stage.Stage;
import project.ai.customAi.service.NN.NNFeaturedFullwordAlg;
import project.ai.customAi.ui.stages.Settings;

public class CustomAiUi extends Application {

    private NNFeaturedFullwordAlg nnFeaturedFullwordAlg;

    @Override
    public void init() {
        nnFeaturedFullwordAlg = new NNFeaturedFullwordAlg();
    }

    @Override
    public void start(Stage stage) {
        Stage settingsStage = new Stage();
        Settings settings = new Settings();
        settings.metaConfig(settingsStage);

        settingsStage.show();
    }


    @Override
    public void stop() {
        //context.close();
    }
}
