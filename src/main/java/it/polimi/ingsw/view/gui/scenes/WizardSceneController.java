package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class WizardSceneController extends ViewObservable implements GenericSceneController{
    @FXML
    ImageView blue_wizard;
    @FXML
    ImageView pink_wizard;
    @FXML
    ImageView green_wizard;
    @FXML
    ImageView yellow_wizard;

    public void initialize(){
        blue_wizard.addEventHandler(MouseEvent.MOUSE_CLICKED, this::blueWizardChoice);
        pink_wizard.addEventHandler(MouseEvent.MOUSE_CLICKED, this::pinkWizardChoice);
        green_wizard.addEventHandler(MouseEvent.MOUSE_CLICKED, this::greenWizardChoice);
        yellow_wizard.addEventHandler(MouseEvent.MOUSE_CLICKED, this::yellowWizardChoice);
    }

    public void blueWizardChoice(Event e){
        notifyObserver(viewObserver -> viewObserver.onUpdateWizardID("BLUE_WIZARD"));
    }
    public void pinkWizardChoice(Event e){
        notifyObserver(viewObserver -> viewObserver.onUpdateWizardID("PINK_WIZARD"));
    }
    public void greenWizardChoice(Event e){
        notifyObserver(viewObserver -> viewObserver.onUpdateWizardID("GREEN_WIZARD"));
    }
    public void yellowWizardChoice(Event e){
        notifyObserver(viewObserver -> viewObserver.onUpdateWizardID("YELLOW_WIZARD"));
    }

}
