package it.polimi.ingsw.view.gui.popupcontrollers;

import it.polimi.ingsw.model.Color;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.Objects;

public class ColorPopupController extends PopupController{

    @FXML
    private GridPane colorBox;
    private String colorChosen;

    public ColorPopupController() {
        super();
    }

    public void chooseColor(Event e){
        Node node = (Node) e.getTarget();
        int index = GridPane.getColumnIndex(node);
        colorChosen = Color.values()[index].toString();
        getWindow().close();
    }

    public String display(){
        getWindow().setTitle("Choose a color");
        getWindow().setHeight(100);
        getWindow().setWidth(360);

        Parent rootLayout = load("/fxml/color_popup.fxml");
        Scene scene = new Scene(rootLayout, 360, 70);
        scene.getStylesheets().add
                (Objects.requireNonNull(getClass().getResource("/css/popup_style.css")).toExternalForm());
        getWindow().setScene(scene);

        colorBox = (GridPane) rootLayout;
        colorBox.setDisable(false);
        for(Node img : colorBox.getChildren())
            img.getStyleClass().add("popupClickable");
        colorBox.addEventHandler(MouseEvent.MOUSE_CLICKED, this::chooseColor);

        getWindow().showAndWait();

        return colorChosen;
    }

}
