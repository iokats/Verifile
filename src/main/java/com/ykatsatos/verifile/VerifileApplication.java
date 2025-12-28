package com.ykatsatos.verifile;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class VerifileApplication extends Application {

    private static final String TITLE = "Veriflie";
    private static final String VIEW_FXML = "verifile-view.fxml";

    private VerifileController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VerifileApplication.class.getResource(VIEW_FXML));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        controller = fxmlLoader.getController();

        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {

        if (controller != null) {

            controller.dispose();
        }
    }
}
