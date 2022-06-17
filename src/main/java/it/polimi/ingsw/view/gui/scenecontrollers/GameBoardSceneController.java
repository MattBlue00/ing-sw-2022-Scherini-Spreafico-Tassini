package it.polimi.ingsw.view.gui.scenecontrollers;

import it.polimi.ingsw.exceptions.IslandNotFoundException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.utils.Constants;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.Node;
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
import java.util.List;

public class GameBoardSceneController extends ViewObservable implements GenericSceneController{

    @FXML
    private GridPane deck;
    @FXML
    private GridPane characterCards;
    @FXML
    private GridPane island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11, island12;
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
    private final Text[][] studentsOnIsland;
    private final ImageView[] towerOnIsland; // used for the tower image on each island
    private final Text[] towersNumberOnIsland; // used for the towers number on each island


    public GameBoardSceneController(){
        clouds = new ArrayList<>();
        islands = new ArrayList<>();
        costs = new ArrayList<>();
        for(int i = 0; i < Constants.CHARACTERS_NUM; i++)
            costs.add(new Text());
        studentsOnIsland = new Text[12][5];
        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 5; j++){
                studentsOnIsland[i][j] = new Text("0");
            }
        }
        towerOnIsland = new ImageView[12];
        for (int i = 0; i < 12; i++){
            towerOnIsland[i] = new ImageView();
        }
        towersNumberOnIsland = new Text[12];
        for (int i = 0; i < 12; i++){
            towersNumberOnIsland[i] = new Text();
        }
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
        for (GridPane island : islands)
            island.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickIsland);
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

        initializeIslands();

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
        showIslands();
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

        int i = 1;
        int j = 0;
        for(GridPane island : islands){
            island.setDisable(false);
            try {
                Island currentIsland = game.getBoard().getIslands().getIslandFromID(i);
                for (Color color : Color.values()) {
                    int num_students = currentIsland.getNumOfStudentsOfColor(color.toString());
                    studentsOnIsland[i-1][j].setText(String.valueOf(num_students));
                    studentsOnIsland[i-1][j].setFill(Paint.valueOf("WHITE"));
                    studentsOnIsland[i-1][j].setFont(Font.font(String.valueOf(Font.getDefault()), FontWeight.EXTRA_BOLD, 12.0));
                    j++;

                 if (currentIsland.getNumOfTowers() != 0){
                        Player player = currentIsland.getOwner();
                        TowerColor towerColor = game.getTowersColor().get(player);
                        ImageView towerImage = new ImageView(new Image("img/"+towerColor.toString().toLowerCase()+"_tower.png"));
                        towerImage.setFitWidth(20);
                        towerImage.setPreserveRatio(true);
                        towerOnIsland[i-1] = towerImage;
                        towersNumberOnIsland[i-1].setText(String.valueOf(currentIsland.getNumOfTowers()));
                        towersNumberOnIsland[i-1].setFill(Paint.valueOf("WHITE"));
                        towersNumberOnIsland[i-1].setFont(Font.font(String.valueOf(Font.getDefault()), FontWeight.EXTRA_BOLD, 12.0));
                    }
                }
            } catch (IslandNotFoundException e) {
                throw new RuntimeException(e);
            }
            i++;
            j = 0;
        }
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

    //TODO: implement method
    public void onClickIsland(Event e){
        Node node = (Node) e.getTarget();
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

    public void activateIslands(){
        for (GridPane island : islands){
            island.getStyleClass().add("clickable");
            island.setDisable(false);
        }
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

    private void initializeIslands(){

        islands.add(island1);
        islands.add(island2);
        islands.add(island3);
        islands.add(island4);
        islands.add(island5);
        islands.add(island6);
        islands.add(island7);
        islands.add(island8);
        islands.add(island9);
        islands.add(island10);
        islands.add(island11);
        islands.add(island12);

        int i = 1;
        int j = 0;
        for(GridPane island : islands){
            island.setDisable(false);
            try {
                Island currentIsland = game.getBoard().getIslands().getIslandFromID(i);
                for (Color color : Color.values()) {
                    int num_students = currentIsland.getNumOfStudentsOfColor(color.toString());
                    studentsOnIsland[i-1][j].setText(String.valueOf(num_students));
                    studentsOnIsland[i-1][j].setFill(Paint.valueOf("WHITE"));
                    studentsOnIsland[i-1][j].setFont(Font.font(String.valueOf(Font.getDefault()), FontWeight.EXTRA_BOLD, 12.0));
                    Text text = studentsOnIsland[i-1][j];
                    island.add(text, 2, j);
                    j++;
                }
            } catch (IslandNotFoundException e) {
                throw new RuntimeException(e);
            }
            island.add(towerOnIsland[i-1],0,1);
            island.add(towersNumberOnIsland[i-1],0,2);
            i++;
            j = 0;
        }
    }

    public Game getGame() {
        return game;
    }
}
