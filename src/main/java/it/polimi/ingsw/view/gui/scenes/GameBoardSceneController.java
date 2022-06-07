package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.util.List;

public class GameBoardSceneController extends ViewObservable implements GenericSceneController{

    @FXML
    private GridPane island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11, island12;
    @FXML
    private Image aCard1, aCard2, aCard3, aCard4, aCard5, aCard6, aCard7, aCard8, aCard9, aCard10;
    @FXML
    private Image cCard1, cCard2, cCard3;

    private Game game;
    private List<GridPane> islands;
    private List<Image> deck;
    private List<Image> characterCards;

    public GameBoardSceneController(){
        //TODO: init the controller when created
    }



}
