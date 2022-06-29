package it.polimi.ingsw.view.gui.scenecontrollers;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * This scene controller handles the scene in which the client joins an existing game.
 */

public class JoiningGameController extends ViewObservable implements GenericSceneController{

    @FXML
    private TextField gameID;
    @FXML
    private Button joinGame;

    /**
     * Properly initializes the scene.
     */

    @FXML
    public void initialize(){
        joinGame.addEventHandler(MouseEvent.MOUSE_CLICKED, this::joinGame);
    }

    /**
     * When the "join game" button is clicked, tries to make the client join the chosen game.
     *
     * @param e the event that triggers the method.
     */

    public void joinGame(Event e){
        int gameID;
        try {
            gameID = Integer.parseInt(this.gameID.getText());
            notifyObserver(viewObserver -> viewObserver.onUpdateGameNumber(gameID));
        }catch (NumberFormatException err){
            new Alert(Alert.AlertType.ERROR, "Please enter a valid number").showAndWait();
        }
    }
}
