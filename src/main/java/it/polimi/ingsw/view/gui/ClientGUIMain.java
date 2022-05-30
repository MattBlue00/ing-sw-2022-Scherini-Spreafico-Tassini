package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.view.gui.scenes.ConnectionSceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientGUIMain extends Application {
    private Stage stage;
    private SceneController sceneController;

    @Override
    public void start(Stage stage) throws Exception {
        this.sceneController = new SceneController(this);
        this.stage = stage;
        GraphicalUserInterface gui = new GraphicalUserInterface(sceneController);
        ClientController clientController = new ClientController(gui);
        gui.addObserver(clientController);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/connection_scene.fxml"));

        Parent rootLayout = null;
        try {
            rootLayout = fxmlLoader.load();
        } catch (IOException e) {
            System.exit(1);
        }

        ConnectionSceneController controller = fxmlLoader.getController();
        controller.addObserver(clientController);


        Scene scene = new Scene(rootLayout, 600, 600);
        String url = String.valueOf(getClass().getResource("/img/logo.png"));
        Image icon = new Image(url);

        gui.getSceneController().setCurrentScene(scene);

        stage.getIcons().add(icon);
        stage.setTitle("Eriantys");
        stage.setMinHeight(500);
        stage.setMinWidth(500);
        stage.setHeight(600);
        stage.setWidth(600);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
    }

    public void changeScene(Scene scene){
        stage.setScene(scene);
        stage.setWidth(600);
        stage.setHeight(600);
        stage.setMinHeight(500);
        stage.setMinWidth(500);
        stage.show();
    }

    public void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
