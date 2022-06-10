package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.observers.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


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
    @FXML
    private TextFlow history;
    private Game game;

    public void setGame(Game game) {
        this.game = game;
    }

    @FXML
    public void initialize(){
        deck.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickAssistantCard);
    }

    public void showDeck(){
        deck.setDisable(false);
        for(int i = 0; i<10; i++){
            String imagePath = "/img/Assistenti/Assistente"+(i+1)+".png";
            ImageView image = new ImageView(new Image(String.valueOf(getClass().getResource(imagePath))));
            image.setPreserveRatio(true);
            image.setFitWidth(85);
            deck.add(image, i, 0);
            image.getStyleClass().set(0,"clickable");
        }
    }

    public void onClickAssistantCard(Event e){
        deck.setDisable(true);
        Node node = (Node) e.getTarget();
        int cardId = GridPane.getColumnIndex(node);

        ImageView cardImage = (ImageView) node;
        playCard(cardId);
        String wizard_path = game.getCurrentPlayer().getWizardID().toString().toLowerCase();
        cardImage.setImage(new Image(String.valueOf(getClass().getResource("/img/wizards/"+wizard_path+".jpg"))));
        cardImage.getStyleClass().set(0,"");
    }

    private void playCard(int cardId) {
        String cardName = game.getCurrentPlayer().getDeck().get(cardId).getName();
        showUpdate("You played: "+cardName);
        notifyObserver(viewObserver -> viewObserver.onUpdateAssistantCard(cardName));
    }

    public void showUpdate(String updateMessage){
        Text message = new Text(updateMessage+"\n");
        history.getChildren().add(message);
    }
}
