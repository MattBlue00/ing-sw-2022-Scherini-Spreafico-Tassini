package it.polimi.ingsw.view.gui.scenecontrollers;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.utils.Constants;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Paint;
import javafx.scene.text.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameBoardSceneController extends ViewObservable implements GenericSceneController{

    @FXML
    private GridPane deck;
    @FXML
    private GridPane characterCards;
    @FXML
    private TextFlow history;
    @FXML
    private TilePane cloud1, cloud2, cloud3;
    @FXML
    private AnchorPane extraCloud;
    private Game game;
    private String nickname;
    private final List<TilePane> clouds;
    private final List<GridPane> islands;
    private final List<Text> costs;


    public GameBoardSceneController(){
        clouds = new ArrayList<>();
        islands = new ArrayList<>();
        costs = new ArrayList<>();
        for(int i = 0; i < Constants.CHARACTERS_NUM; i++)
            costs.add(new Text());
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @FXML
    public void initialize(){

        deck.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickAssistantCard);
        cloud1.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickCloud);
        cloud2.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickCloud);
        if(game.getPlayersNumber()==3)
            cloud3.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickCloud);
        if(game instanceof GameExpertMode)
            characterCards.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickCharacterCard);

    }

    public void startGameBoard() {
        clouds.add(cloud1);
        clouds.add(cloud2);

        if (game.getPlayersNumber() == 3) {
            clouds.add(cloud3);
            extraCloud.setVisible(true);
        } else {
            extraCloud.setVisible(false);
        }

        if(game instanceof GameExpertMode){
            CharacterCard[] cards = ((GameExpertMode) game).getCharacters();
            for (int i = 0; i < Constants.CHARACTERS_NUM; i++) {

                int characterCardId = cards[i].getId();
                String imagePath = "/img/characters/character_front" + (characterCardId) + ".jpg";
                ImageView image = new ImageView(new Image(String.valueOf(getClass().getResource(imagePath))));
                image.setPreserveRatio(true);
                image.setFitWidth(85);
                characterCards.add(image, i, 0);

                String coinPath = "/img/coin.png";
                Pane pane = new Pane();
                ImageView coin = new ImageView(new Image(String.valueOf(getClass().getResource(coinPath))));
                coin.setPreserveRatio(true);
                coin.setFitWidth(40);
                DropShadow coinEffect = new DropShadow();
                coinEffect.setBlurType(BlurType.ONE_PASS_BOX);
                coin.setEffect(coinEffect);
                coin.setDisable(true);
                pane.getChildren().add(coin);
                GridPane.setValignment(pane, VPos.TOP);

                Text cost = costs.get(i);
                cost.setText(String.valueOf(cards[i].getCost()));
                cost.setFill(Paint.valueOf("WHITE"));
                cost.setFont(Font.font(String.valueOf(Font.getDefault()), FontWeight.EXTRA_BOLD, 16.0));
                cost.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
                cost.setTextAlignment(TextAlignment.CENTER);
                cost.setDisable(true);
                cost.setX(cost.getX() + 16.0);
                cost.setY(cost.getY() + 27.0);
                pane.getChildren().add(cost);
                characterCards.add(pane, i, 0);

            }
        }
    }

    // Components

    public void renderGameBoard(){
        showDeck();
        //showIslands();
        if(game instanceof GameExpertMode) showCharacterCards();
        //showPersonalSchool();
        //ShowSchools();
        showClouds();
    }

    private void showDeck(){
        deck.setDisable(true);
        for(int i = 0; i<game.getPlayerFromNickname(nickname).getDeck().size(); i++){
            String imagePath = "/img/assistants/assistant" +(i+1)+".png";
            ImageView image = new ImageView(new Image(String.valueOf(getClass().getResource(imagePath))));
            image.setPreserveRatio(true);
            image.setFitWidth(85);
            deck.add(image, i, 0);
        }
    }

    private void showClouds(){

        for(TilePane cloud : clouds){
            cloud.getChildren().clear();
        }

        for(int i=0; i<clouds.size(); i++){
            clouds.get(i).setDisable(true);
            for(Student student : game.getBoard().getCloud(i).getStudents()){
                String color = student.getColor().toString();
                ImageView studentImage = new ImageView(new Image("img/student_"+color+".png"));
                studentImage.setFitWidth(20);
                studentImage.setPreserveRatio(true);
                clouds.get(i).getChildren().add(studentImage);
            }
        }
    }

    private void showCharacterCards(){
        characterCards.setDisable(true);
        CharacterCard[] cards = ((GameExpertMode) game).getCharacters();
        for(int i = 0; i < Constants.CHARACTERS_NUM; i++){
            costs.get(i).setText(String.valueOf(cards[i].getCost()));
        }
    }

    public void showIslands(){
        for(GridPane island : islands)
            island.setDisable(false);
    }


    public void showUpdate(String updateMessage){
        Text message = new Text(updateMessage+"\n");
        history.getChildren().add(message);
    }


    // Events
    public void onClickAssistantCard(Event e){
        deck.setDisable(true);
        Node node = (Node) e.getTarget();
        int cardId = GridPane.getColumnIndex(node);
        ImageView cardImage = (ImageView) node;
        playAssistantCard(cardId);
        String wizard_path = game.getPlayerFromNickname(getNickname()).getWizardID().toString().toLowerCase();
        cardImage.setImage(new Image(String.valueOf(getClass().getResource("/img/wizards/"+wizard_path+".jpg"))));
        cardImage.getStyleClass().set(0,"");
    }

    public void onClickCloud(Event e){
        Node node = (Node) e.getTarget();
        if(node.equals(cloud1))
            chooseCloud(0);
        else if (node.equals(cloud2))
            chooseCloud(1);
        else chooseCloud(2);
        for(Node cloud : clouds)
            cloud.setDisable(true);
    }

    public void onClickCharacterCard(Event e){
        Node node = (Node) e.getTarget();
        int columnIndex = GridPane.getColumnIndex(node);
        int characterCardID = (((GameExpertMode) game).getCharacters())[columnIndex].getId();
        playCharacterCard(characterCardID);
    }

    // Activations from GUI class (askMessage result)
    public void activateAssistantCardChoice(){
        showUpdate("Select the Assistant Card you want to play.");
        for(Node img : deck.getChildren()){
            img.getStyleClass().set(0,"clickable");
        }
        deck.setDisable(false);
    }

    public void activateCloudChoice(){
        for(Node cloud : clouds){
            cloud.getStyleClass().add("clickable");
            cloud.setDisable(false);
        }
    }

    public void activateCharacterCards(){
        showUpdate("You can also play a Character Card.");
        for(Node img : characterCards.getChildren()){
            img.getStyleClass().add("clickable");
        }
        characterCards.setDisable(false);
    }


    private void playAssistantCard(int cardId) {
        String cardName = game.getCurrentPlayer().getDeck().get(cardId).getName();
        notifyObserver(viewObserver -> viewObserver.onUpdateAssistantCard(cardName));
    }


    private void chooseCloud(int cloudId){
        notifyObserver(viewObserver -> viewObserver.onUpdateCloudChoice(cloudId));
    }


    private void playCharacterCard(int characterCard){
        String cardName = ((GameExpertMode) game).getCharacterCardByID(characterCard).getClass().getSimpleName();
        notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCard(characterCard));
        if(game.getCurrentPlayer().getCharacterCardAlreadyPlayed()) {
            characterCards.setDisable(true);
            showUpdate(game.getCurrentPlayer().getNickname() + " has played the " + cardName + "!");
        }
    }

    public Game getGame() {
        return game;
    }
}
