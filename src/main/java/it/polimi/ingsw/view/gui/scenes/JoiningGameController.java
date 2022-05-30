package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class JoiningGameController extends ViewObservable implements GenericSceneController{
    @FXML
    TextField gameID;
    @FXML
    Button joinGame;

    @FXML
    public void initialize(){
        joinGame.addEventHandler(MouseEvent.MOUSE_CLICKED, this::joinGame);
    }

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
