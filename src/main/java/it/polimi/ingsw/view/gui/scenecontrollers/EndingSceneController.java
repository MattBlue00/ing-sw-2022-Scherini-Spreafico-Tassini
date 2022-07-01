package it.polimi.ingsw.view.gui.scenecontrollers;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class EndingSceneController extends ViewObservable implements GenericSceneController{

    @FXML
    Button quit;

    @FXML
    public void initialize() {
        quit.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onQuit);
    }

    @FXML
    private void onQuit(Event e) {
        Platform.exit();
        System.exit(0);
    }
}
