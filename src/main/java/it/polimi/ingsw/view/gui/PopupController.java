package it.polimi.ingsw.view.gui;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This abstract class defines everything that a popup controller should allow.
 */

public abstract class PopupController {

    private final Stage window;

    /**
     * PopupController constructor.
     */

    public PopupController() {
        window = new Stage();
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
    }

    /**
     * Displays the popup.
     *
     * @return an {@link Object} representing the choice made by the player.
     */

    public abstract Object display();

    /**
     * Sets the choice made by the player via the popup.
     *
     * @param e the event that triggered the setting.
     */

    public abstract void setParameter(Event e);

    /**
     * Loads into the popup the corresponding FXML file.
     *
     * @param url the path to the FXML file.
     * @return the root of the FXML file.
     */

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

    /**
     * Returns the stage of the popup.
     *
     * @return the {@link Stage} of the popup.
     */

    public Stage getWindow() {
        return window;
    }
}
