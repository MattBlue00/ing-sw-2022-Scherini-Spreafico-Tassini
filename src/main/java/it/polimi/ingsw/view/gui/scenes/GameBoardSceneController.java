package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;


import java.util.List;

public class GameBoardSceneController extends ViewObservable implements GenericSceneController{

    @FXML
    private GridPane island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11, island12;
    @FXML
    private Image cCard1, cCard2, cCard3;
    @FXML
    private GridPane deck;
    @FXML
    private List<GridPane> islands;
    @FXML
    private List<Image> characterCards;
    private Game game;

    public void setGame(Game game) {
        this.game = game;
    }

    @FXML
    public void initialize(){
        deck.setOnMouseClicked(e -> {
            Node node = (Node) e.getTarget();
            int cardId = GridPane.getColumnIndex(node);

            ImageView cardImage = (ImageView) node;
            playCard(cardId);
            cardImage.setImage(new Image(String.valueOf(getClass().getResource("/img/wizards/yellow_wizard.jpg"))));

        });
    }

    public void showDeck(){
        for(int i=0; i<10; i++){
            String imagePath = "/img/Assistenti/Assistente"+(i+1)+".png";
            ImageView image = new ImageView(new Image(String.valueOf(getClass().getResource(imagePath))));
            image.setPreserveRatio(true);
            image.setFitWidth(80);
            deck.add(image, i, 0);
        }
    }

    private void playCard(int cardId) {
        String cardName = game.getCurrentPlayer().getDeck().get(cardId).getName();
        System.out.println(cardName);
        notifyObserver(viewObserver -> viewObserver.onUpdateAssistantCard(cardName));
    }

}
