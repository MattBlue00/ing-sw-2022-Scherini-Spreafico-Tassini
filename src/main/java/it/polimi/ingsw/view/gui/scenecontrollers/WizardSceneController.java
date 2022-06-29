package it.polimi.ingsw.view.gui.scenecontrollers;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * This scene controller handles the scene in which the client chooses a wizard before starting a game.
 */

public class WizardSceneController extends ViewObservable implements GenericSceneController{

    @FXML
    private ImageView blue_wizard;
    @FXML
    private ImageView pink_wizard;
    @FXML
    private ImageView green_wizard;
    @FXML
    private ImageView yellow_wizard;

    /**
     * Properly initializes the scene.
     */

    public void initialize(){
        blue_wizard.addEventHandler(MouseEvent.MOUSE_CLICKED, this::blueWizardChoice);
        pink_wizard.addEventHandler(MouseEvent.MOUSE_CLICKED, this::pinkWizardChoice);
        green_wizard.addEventHandler(MouseEvent.MOUSE_CLICKED, this::greenWizardChoice);
        yellow_wizard.addEventHandler(MouseEvent.MOUSE_CLICKED, this::yellowWizardChoice);
    }

    /**
     * When the blue wizard is clicked, tries to enter the game with the chosen wizard.
     *
     * @param e the event that triggers the method.
     */

    public void blueWizardChoice(Event e){
        notifyObserver(viewObserver -> viewObserver.onUpdateWizardID("BLUE_WIZARD"));
    }

    /**
     * When the pink wizard is clicked, tries to enter the game with the chosen wizard.
     *
     * @param e the event that triggers the method.
     */

    public void pinkWizardChoice(Event e){
        notifyObserver(viewObserver -> viewObserver.onUpdateWizardID("PINK_WIZARD"));
    }

    /**
     * When the green wizard is clicked, tries to enter the game with the chosen wizard.
     *
     * @param e the event that triggers the method.
     */

    public void greenWizardChoice(Event e){
        notifyObserver(viewObserver -> viewObserver.onUpdateWizardID("GREEN_WIZARD"));
    }

    /**
     * When the yellow wizard is clicked, tries to enter the game with the chosen wizard.
     *
     * @param e the event that triggers the method.
     */

    public void yellowWizardChoice(Event e){
        notifyObserver(viewObserver -> viewObserver.onUpdateWizardID("YELLOW_WIZARD"));
    }

}
