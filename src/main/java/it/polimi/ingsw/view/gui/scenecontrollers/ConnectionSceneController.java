package it.polimi.ingsw.view.gui.scenecontrollers;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.observers.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ConnectionSceneController extends ViewObservable implements GenericSceneController{
    @FXML
    private TextField ipAddressField;
    @FXML
    private TextField portField;
    @FXML
    private Button connectionBtn;

    @FXML
    public void initialize() {
        connectionBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConnectButtonClick);
    }

    @FXML
    private void onConnectButtonClick(Event e) {
        String port;
        String ipAddress;

        port = portField.getText();
        ipAddress = ipAddressField.getText();

        boolean isValidIpAddress = ClientController.isValidAddress(ipAddress);
        boolean isValidPort = ClientController.isValidPort(port);

        if (isValidIpAddress && isValidPort) {
            new Thread(() -> notifyObserver(viewObserver -> viewObserver.onUpdateServerData(ipAddress, Integer.parseInt(port)))).start();
        }else new Alert(Alert.AlertType.ERROR, "ERROR IN ADDRESS OR PORT CHOICE").showAndWait();
    }
}