package it.polimi.ingsw.view.gui.scenecontrollers;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class LobbySceneController extends ViewObservable implements GenericSceneController{
    String choice;
    @FXML
    Button createBtn;
    @FXML
    Button joinBtn;

    public void setChoice(String choice) {
        this.choice = choice;
    }

    @FXML
    public void initialize() {
        createBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::createGame);
        joinBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::joinGame);
    }

    public void createGame(Event e){
        setChoice("CREATE");
        changeMenuScene(choice);
    }

    public void joinGame(Event e){
        setChoice("JOIN");
        changeMenuScene(choice);
    }

    public void changeMenuScene(String choice){
        notifyObserver(viewObserver -> viewObserver.onUpdateCreateOrJoin(choice));
    }

}
