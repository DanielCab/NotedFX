package org.group;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appLayout.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 960, 768);
        scene.getStylesheets().add(getClass().getResource("stylesFX.css").toExternalForm());

        AppController controller = loader.getController();
        controller.setStage(stage);
        controller.setupEventHandlers();

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        stage.setTitle("Noted");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        stage.setMaxHeight(screenBounds.getHeight());
        stage.setMaxWidth(screenBounds.getWidth());
        stage.setResizable(true);
        stage.sizeToScene();
    }
}
