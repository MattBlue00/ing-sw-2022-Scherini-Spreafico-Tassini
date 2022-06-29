package it.polimi.ingsw.view.gui.scenecontrollers;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * This scene controller handles the scene in which the client chooses whether to create a new game or to join an
 * existing one.
 */

public class LobbySceneController extends ViewObservable implements GenericSceneController{

    private String choice;
    @FXML
    private Button createBtn;
    @FXML
    private Button joinBtn;

    /**
     * Sets the choice made by the client.
     *
     * @param choice a {@link String} representing the choice made by the client.
     */

    public void setChoice(String choice) {
        this.choice = choice;
    }

    /**
     * Properly initializes the scene.
     */

    @FXML
    public void initialize() {
        createBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::createGame);
        joinBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::joinGame);
    }

    /**
     * When the "create game" button is clicked, sets the choice made.
     *
     * @param e the event that triggers the method.
     */

    public void createGame(Event e){
        setChoice("CREATE");
        changeMenuScene(choice);
    }

    /**
     * When the "join game" button is clicked, sets the choice made.
     *
     * @param e the event that triggers the method.
     */

    public void joinGame(Event e){
        setChoice("JOIN");
        changeMenuScene(choice);
    }

    /**
     * Based on the choice made, changes the scene into the desired one.
     *
     * @param choice a {@link String} representing the choice made.
     */

    public void changeMenuScene(String choice){
        notifyObserver(viewObserver -> viewObserver.onUpdateCreateOrJoin(choice));
    }

}
