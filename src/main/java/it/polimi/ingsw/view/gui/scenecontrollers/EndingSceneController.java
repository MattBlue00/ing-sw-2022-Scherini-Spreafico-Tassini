package it.polimi.ingsw.view.gui.scenecontrollers;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * This scene controller handles the quit event after game end.
 */
public class EndingSceneController extends ViewObservable implements GenericSceneController{

    @FXML
    Button quit;

    /**
     * Properly initializes the scene.
     */
    @FXML
    public void initialize() {
        quit.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onQuit);
    }

    /**
     * When the quit button is clicked, the GUI is closed.
     *
     * @param e the event that triggers the method.
     */
    @FXML
    private void onQuit(Event e) {
        Platform.exit();
        System.exit(0);
    }
}
