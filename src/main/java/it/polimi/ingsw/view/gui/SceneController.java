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

    private final ClientGUIMain main;

    private Scene currentScene;
    private GenericSceneController currentController;

    public Scene getCurrentScene() {
        return currentScene;
    }

    public SceneController(ClientGUIMain main) {
        this.main = main;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public void changeRootPane(List<ViewObserver> observers, String fxml){
        GenericSceneController controller;
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));
            Parent root = loader.load();

            controller = loader.getController();
            ((ViewObservable) controller).addAllObservers(observers);
            currentController = controller;

            currentScene = new Scene(root);

            currentScene.setRoot(root);
        } catch (IOException e) {
          e.printStackTrace();
        }
        main.changeScene(currentScene);
    }
    public void changeRootPane(GenericSceneController controller, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneController.class.getResource("/fxml/" + fxml));

            // Setting the controller BEFORE the load() method.
            loader.setController(controller);
            currentController = controller;
            Parent root = loader.load();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        main.changeScene(currentScene);
    }

    public void showAlert(String message){
        main.showAlert(message);
    }
}
