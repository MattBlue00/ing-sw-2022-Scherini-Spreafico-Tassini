package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.observers.ViewObserver;
import it.polimi.ingsw.view.gui.scenes.GenericSceneController;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.List;

public class SceneController extends ViewObservable {

    private Scene currentScene;
    private GenericSceneController currentController;

    public Scene getCurrentScene() {
        return currentScene;
    }

    public GenericSceneController getCurrentController() {
        return currentController;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public <T> void changeRootPane(List<ViewObserver> observers, Scene scene, String fxml){
        T controller = null;

        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));
            Parent root = loader.load();
            controller = loader.getController();
            ((ViewObservable) controller).addAllObservers(observers);

            currentController = (GenericSceneController) controller;
            currentScene = scene;
            currentScene.setRoot(root);
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
    public void changeRootPane(GenericSceneController controller, Scene scene, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));

            // Setting the controller BEFORE the load() method.
            loader.setController(controller);
            currentController = controller;
            Parent root = loader.load();

            currentScene = scene;
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeRootPane(List<ViewObserver> observers, String fxml){
        changeRootPane(observers, currentScene, fxml);
    }

    public void changeRootPane(GenericSceneController controller, Event event, String fxml) {
        Scene scene = ((Node) event.getSource()).getScene();
        changeRootPane(controller, scene, fxml);
    }
}
