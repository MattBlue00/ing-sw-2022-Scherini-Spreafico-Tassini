package it.polimi.ingsw.view.gui.popupcontrollers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class PopupController {

    private final Stage window;

    public PopupController() {
        window = new Stage();
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
    }

    public abstract Object display();

    public Parent load(String url){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(url));
        Parent rootLayout = null;
        try {
            rootLayout = fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return rootLayout;
    }

    public Stage getWindow() {
        return window;
    }
}
