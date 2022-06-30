package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.view.gui.GraphicalUserInterface;
import it.polimi.ingsw.view.gui.SceneController;
import it.polimi.ingsw.view.gui.scenecontrollers.ConnectionSceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class contains the main methods that allow the Eriantys application to launch via GUI, as well as basic
 * attributes and useful fundamental methods.
 */

public class ApplicationGUI extends Application {

    private Stage stage;

    /**
     * Launches the application via GUI.
     */

    public static void main(String[] args) {
        launch();
    }

    /**
     * Loads, sets and shows the first scene of the application.
     *
     * @param stage the {@link Stage} in which the application will be used.
     * @throws Exception if something goes wrong.
     */

    @Override
    public void start(Stage stage) throws Exception {
        SceneController sceneController = new SceneController(this);
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
        stage.setHeight(600);
        stage.setWidth(600);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {event.consume(); quit();});
        stage.show();
    }

    /**
     * Safely closes the application.
     */

    @Override
    public void stop() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Changes the scene currently shown on the stage.
     *
     * @param scene the new {@link Scene} to show.
     */

    public void changeScene(Scene scene){
        stage.setScene(scene);
        if(scene.getRoot().getId().equals("pane")){ // if it is the GameBoard
            stage.setWidth(1341);
            stage.setHeight(789.5);
            stage.centerOnScreen();
        }else{
            stage.setWidth(600);
            stage.setHeight(600);
        }
        stage.show();
    }

    /**
     * Alerts the player of something via a popup.
     *
     * @param message the message to notify.
     */

    public void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Allows the player to step back from a rage-quit.
     */

    public void quit(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Quit");
        alert.setHeaderText("You're about to close the app.");
        alert.setContentText("Are you sure?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            stage.close();
            stop();
        }

    }

}
