package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.ApplicationGUI;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.observers.ViewObserver;
import it.polimi.ingsw.view.gui.scenecontrollers.GenericSceneController;
import it.polimi.ingsw.view.gui.scenecontrollers.GameBoardSceneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Application;

import java.io.IOException;
import java.util.List;

/**
 * This controller allows all the scenes, and the associated controllers, to change properly.
 */

public class SceneController extends ViewObservable {

    private final ApplicationGUI main;
    private Scene currentScene;
    private GenericSceneController currentController;

    /**
     * SceneController constructor.
     *
     * @param main the {@link Application} to control.
     */

    public SceneController(ApplicationGUI main) {
        this.main = main;
    }

    /**
     * Returns the current scene controller.
     *
     * @return the current scene controller.
     */

    public GenericSceneController getCurrentController() {
        return currentController;
    }

    /**
     * Sets the current scene.
     *
     * @param currentScene the current scene.
     */

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    /**
     * Loads a new scene on the stage. This method is used for all the scenes before the Game Board one.
     *
     * @param observers a list of observers.
     * @param fxml the FXML file of the new scene to load.
     */

    public void changeRootPane(List<ViewObserver> observers, String fxml){
        GenericSceneController controller;
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));
            Parent root = loader.load();

            controller = loader.getController();
            ((ViewObservable) controller).addAllObservers(observers);
            currentController = controller;
            root.setId("");
            currentScene = new Scene(root);

            currentScene.setRoot(root);
        } catch (IOException e) {
          e.printStackTrace();
        }
        main.changeScene(currentScene);
    }

    /**
     * Loads the Game Board onto the stage.
     *
     * @param controller the {@link GameBoardSceneController} to associate to the scene.
     * @param fxml the FXML file of the new scene to load.
     */

    public void changeRootPane(GenericSceneController controller, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));
            // Setting the controller BEFORE the load() method.
            loader.setController(controller);
            currentController = controller;
            Parent root = loader.load();
            root.setId("pane");
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        main.changeScene(currentScene);
    }

    /**
     * Shows an alert message.
     *
     * @param message the message to show.
     */

    public void showAlert(String message){
        main.showAlert(message);
    }
}
