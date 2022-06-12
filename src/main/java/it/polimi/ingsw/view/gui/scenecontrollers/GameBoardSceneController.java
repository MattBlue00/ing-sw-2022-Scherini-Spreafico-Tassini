package it.polimi.ingsw.view.gui.scenecontrollers;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.utils.Constants;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


import java.util.ArrayList;
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
    @FXML
    private Label green_students_island2;
    private Game game;
    private String nickname;
    private final List<TilePane> clouds;
    private final List<GridPane> islands;


    public GameBoardSceneController(){
        clouds = new ArrayList<>();
        islands = new ArrayList<>();
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
        cloud3.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickCloud);
        //characterCards.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickCharacterCard);
    }

    // Components

    public void renderGameBoard(){
        showDeck();
        //showIslands();
        //if(game instanceof GameExpertMode) showCharacterCards();
        //showPersonalSchool();
        //ShowSchools();
        showClouds();
    }

    private void showDeck(){
        deck.setDisable(true);
        history.getChildren().add(new Text("Please play an Assistant Card.\n"));
        for(int i = 0; i<game.getPlayerFromNickname(nickname).getDeck().size(); i++){
            String imagePath = "/img/assistants/assistant" +(i+1)+".png";
            ImageView image = new ImageView(new Image(String.valueOf(getClass().getResource(imagePath))));
            image.setPreserveRatio(true);
            image.setFitWidth(85);
            deck.add(image, i, 0);
        }
    }

    private void showClouds(){
        extraCloud.setVisible(false);
        clouds.add(cloud1);
        clouds.add(cloud2);

        if(game.getPlayersNumber()==3){
            clouds.add(cloud3);
            extraCloud.setVisible(true);
        }

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
        characterCards.setDisable(true);
        Node node = (Node) e.getTarget();
        int characterCardId = GridPane.getColumnIndex(node);

        playCharacterCard(characterCardId);
    }

    // Activations from GUI class (askMessage result)
    public void activateAssistantCardChoice(){
        for(Node img : deck.getChildren()){
            img.getStyleClass().set(0,"clickable");
        }
        deck.setDisable(false);
    }

    public void activateCloudChoice(){
        history.getChildren().add(new Text("Choose a cloud to pick students from...\n"));
        for(Node cloud : clouds){
            cloud.getStyleClass().add("clickable");
            cloud.setDisable(false);
        }
    }

    public void activateCharacterCards(){}


    private void playAssistantCard(int cardId) {
        String cardName = game.getCurrentPlayer().getDeck().get(cardId).getName();
        showUpdate("You played: "+cardName);
        notifyObserver(viewObserver -> viewObserver.onUpdateAssistantCard(cardName));
    }


    private void chooseCloud(int cloudId){
        history.getChildren().add(new Text("cloud chosen: "+(cloudId+1)+"\n"));
        notifyObserver(viewObserver -> viewObserver.onUpdateCloudChoice(cloudId));
    }


    private void playCharacterCard(int characterCard){
        String cardName = ((GameExpertMode) game).getCharacterCardByID(characterCard).toString();
        showUpdate("You played: " + cardName);
        if(characterCard == 2){
            notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCard(characterCard));
        }
    }

}
