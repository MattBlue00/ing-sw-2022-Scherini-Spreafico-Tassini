package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class NicknameSceneController extends ViewObservable implements GenericSceneController {
    @FXML
    private TextField nicknameField;

    @FXML
    private Button addingClientBtn;

    @FXML
    public void initialize() {
        addingClientBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onEnterLobbyClick);
    }

    private  void onEnterLobbyClick(Event e) {
        String nickname;
        nickname = nicknameField.getText();

        new Thread(() -> notifyObserver(viewObserver -> viewObserver.onUpdateNickname(nickname))).start();
    }
}
