package it.polimi.ingsw.view.gui.popupcontrollers;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.view.gui.PopupController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import it.polimi.ingsw.model.charactercards.Bard;

import java.util.ArrayList;

/**
 * This popup controller is specifically designed for the {@link Bard}, and controls the popup.
 */

public class BardPopupController extends PopupController {

    @FXML
    private AnchorPane popupBox;
    private ArrayList<String> parameter;
    private boolean quit;

    /**
     * BardPopupController constructor.
     */

    public BardPopupController() {
        super();
        parameter = new ArrayList<>();
    }

    /**
     * Sets the choice made by the player via the popup.
     *
     * @param e the event that triggered the setting.
     */

    public void setParameter(Event e){
        for(Node node : popupBox.getChildren()) {
            if (node instanceof ChoiceBox) {
                parameter.add((String) ((ChoiceBox) node).getValue());
            }
        }
        parameter.removeIf(x -> x.equals("---"));
        if(parameter.size()%2!=0)
            parameter = null;
        getWindow().close();
    }

    /**
     * Properly initializes the popup.
     */

    @FXML
    public void initialize(){
        for(Node node : popupBox.getChildren()) {
            if (node instanceof ChoiceBox) {
                if(((ChoiceBox) node).getItems().size() == 0) {
                    for (Color color : Color.values())
                        ((ChoiceBox) node).getItems().add(color.toString());
                    ((ChoiceBox) node).getItems().add("---");
                    ((ChoiceBox) node).setValue("---");
                }
            }
            if(node instanceof Button) {
                node.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::setParameter);
                node.addEventHandler(MouseEvent.MOUSE_CLICKED, this::setParameter);
            }
        }
    }

    /**
     * Displays the popup.
     *
     * @return an {@link ArrayList} of {@link String} representing the choice made by the player.
     */

    @Override
    public ArrayList<String> display() {

        getWindow().setTitle("Switch the students");
        getWindow().setHeight(205);
        getWindow().setWidth(400);

        Parent rootLayout = load("/fxml/bard_popup.fxml");
        Scene scene = new Scene(rootLayout, 400, 175);
        getWindow().setScene(scene);

        popupBox = (AnchorPane) rootLayout;
        initialize();

        getWindow().setOnCloseRequest(event -> {event.consume(); quit();});

        getWindow().showAndWait();

        if(quit)
            return null;
        else
            return parameter;
    }

    /**
     * Handles the closing request of the popup. Since the {@link Bard} may work even with no parameter, this is
     * needed to avoid undesired coin loss.
     */

    private void quit(){
        quit = true;
        getWindow().close();
    }
}
