package it.polimi.ingsw.view.gui.scenecontrollers;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.observers.ViewObservable;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
    private GridPane characterCards;
    @FXML
    private TextFlow history;
    @FXML
    private GridPane cloud1;
    @FXML
    private GridPane cloud2;
    @FXML
    private GridPane cloud3;
    @FXML
    private Label green_students_island2;
    private Game game;

    public void setGame(Game game) {
        this.game = game;
    }

    @FXML
    public void initialize(){
        deck.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickAssistantCard);
        cloud1.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickCloud);
        cloud2.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickCloud);
        cloud3.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickCloud);
        //characterCards.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickCharacterCard);
    }

    public void showDeck(){
        deck.setDisable(false);
        history.getChildren().add(new Text("Please play an Assistant Card.\n"));
        for(int i = 0; i<10; i++){
            String imagePath = "/img/assistants/assistant" +(i+1)+".png";
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
        playAssistantCard(cardId);
        String wizard_path = game.getCurrentPlayer().getWizardID().toString().toLowerCase();
        cardImage.setImage(new Image(String.valueOf(getClass().getResource("/img/wizards/"+wizard_path+".jpg"))));
        cardImage.getStyleClass().set(0,"");
    }

    private void playAssistantCard(int cardId) {
        String cardName = game.getCurrentPlayer().getDeck().get(cardId).getName();
        showUpdate("You played: "+cardName);
        notifyObserver(viewObserver -> viewObserver.onUpdateAssistantCard(cardName));
    }

    public void showUpdate(String updateMessage){
        Text message = new Text(updateMessage+"\n");
        history.getChildren().add(message);
    }

    //TODO: method needs to be improved
    public void showStudents(String number){
        green_students_island2.setText(number);
    }

    public void onClickCloud(Event e){
        cloud1.setDisable(true);
        cloud2.setDisable(true);
        cloud3.setDisable(true);
        Node node = (Node) e.getTarget();
        if(node.equals(cloud1))
            chooseCloud(0);
        else if (node.equals(cloud2))
            chooseCloud(1);
        else chooseCloud(2);
    }

    private void chooseCloud(int cloudId){
        showUpdate("Cloud chosen: " + cloudId);
        notifyObserver(viewObserver -> viewObserver.onUpdateCloudChoice(cloudId));
    }

    public void showClouds(){
        cloud1.setDisable(false);
        cloud2.setDisable(false);
        if(game.getConstants().NUM_CLOUDS == 3)
            cloud3.setDisable(false);
        else cloud3.setDisable(true);
    }

    public void onClickCharacterCard(Event e){
        characterCards.setDisable(true);
        Node node = (Node) e.getTarget();
        int characterCardId = GridPane.getColumnIndex(node);

        playCharacterCard(characterCardId);
    }

    private void playCharacterCard(int characterCard){
        String cardName = ((GameExpertMode) game).getCharacterCardByID(characterCard).toString();
        showUpdate("You played: " + cardName);
        if(characterCard == 2){
            notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCard(characterCard));
        }
    }

    public void showCharacterCards(){
        characterCards.setDisable(false);
        CharacterCard[] charactersCard = ((GameExpertMode) game).getCharacters();
        for(int i = 0; i<3; i++){
            int characterCardId = charactersCard[i].getId();
            String imagePath = "/img/characters/character_front" +(characterCardId)+".png";
            ImageView image = new ImageView(new Image(String.valueOf(getClass().getResource(imagePath))));
            image.setPreserveRatio(true);
            image.setFitWidth(85); //TODO: check if it's a correct width
            characterCards.add(image, i, 0);
        }
    }

    public void showIslands(){
        island1.setDisable(false);
        island2.setDisable(false);
        green_students_island2.setDisable(true);
        island3.setDisable(false);
        island4.setDisable(false);
        island5.setDisable(false);
        island6.setDisable(false);
        island7.setDisable(false);
        island8.setDisable(false);
        island9.setDisable(false);
        island10.setDisable(false);
        island11.setDisable(false);
        island12.setDisable(false);
    }

}
