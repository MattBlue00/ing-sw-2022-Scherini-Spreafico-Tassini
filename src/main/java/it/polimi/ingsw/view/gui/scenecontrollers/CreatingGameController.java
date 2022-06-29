package it.polimi.ingsw.view.gui.scenecontrollers;

import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.observers.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * This scene controller handles the scene in which the client creates a game.
 */

public class CreatingGameController extends ViewObservable implements GenericSceneController{
    @FXML
    private TextField gameID;
    @FXML
    private TextField playerNumber;
    @FXML
    private CheckBox gameMode;
    @FXML
    private Button createGame;

    /**
     * Properly initializes the scene.
     */

    @FXML
    public void initialize(){
        createGame.addEventHandler(MouseEvent.MOUSE_CLICKED, this::createGame);
    }

    /**
     * When the "create game" button is clicked, tries to create a game with the given parameters.
     *
     * @param e the event that triggers the method.
     */

    public void createGame(Event e){
        int gameID;
        int playerNumber;
        boolean gameMode;
        try {
            gameID = Integer.parseInt(this.gameID.getText());
            playerNumber = Integer.parseInt(this.playerNumber.getText());
            if(playerNumber!=2 && playerNumber !=3) throw new TryAgainException("Error in player number");
            gameMode = this.gameMode.isSelected();
            notifyObserver(viewObserver -> viewObserver.onUpdateGameInfo(gameID, gameMode, playerNumber));
        }catch (NumberFormatException | TryAgainException err){
            new Alert(Alert.AlertType.ERROR, "Please enter valid parameters").showAndWait();
        }
    }

}
