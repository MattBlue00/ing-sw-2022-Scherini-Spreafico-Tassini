package it.polimi.ingsw.view.gui.scenecontrollers;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * This scene controller handles the scene in which the client chooses their nickname.
 */

public class NicknameSceneController extends ViewObservable implements GenericSceneController {

    @FXML
    private TextField nicknameField;
    @FXML
    private Button addingClientBtn;

    /**
     * Properly initializes the scene.
     */

    @FXML
    public void initialize() {
        addingClientBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onEnterLobbyClick);
    }

    /**
     * When the "enter the lobby" button is clicked, tries to make the client join the Server's lobby with the
     * given nickname.
     *
     * @param e the event that triggers the method.
     */

    private  void onEnterLobbyClick(Event e) {
        String nickname;
        nickname = nicknameField.getText();
        new Thread(() -> notifyObserver(viewObserver -> viewObserver.onUpdateNickname(nickname))).start();
    }
}
