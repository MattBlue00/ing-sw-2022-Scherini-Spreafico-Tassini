package it.polimi.ingsw.view.gui.scenecontrollers;

import it.polimi.ingsw.exceptions.IslandNotFoundException;
import it.polimi.ingsw.exceptions.NonExistentColorException;
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
    @FXML
    private GridPane playerHall, playerDiningHall, playerProfTable, playerTowerRoom;
    private Game game;
    private String nickname;
    private final List<TilePane> clouds;
    private final List<GridPane> islands;
    private final List<Text> costs;
    private final Text[][] studentsOnIsland;
    private final ImageView[] towerOnIsland; // used for the tower image on each island
    private final Text[] towersNumberOnIsland; // used for the towers number on each island
    private int motherNatureOldPosition;
    private String studentToMoveColor;
    private boolean moveStudentPhase;


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
        moveStudentPhase = false;
        studentToMoveColor = null;
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
        playerHall.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickPlayerHall);
        playerDiningHall.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickPlayerDiningHall);
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
        updatePlayerSchool();

        if(game instanceof GameExpertMode)
            initializeCharacterCards();

    }

    // Components

    public void renderGameBoard(){
        showDeck();
        showIslands();
        if(game instanceof GameExpertMode) updateCharacterCards();
        //showPersonalSchool();
        //ShowSchools();
        showClouds();
        updatePlayerSchool();
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

    private void updatePlayerSchool(){

        int i = 0;
        int j = 1;
        playerHall.getChildren().clear();
        playerHall.setDisable(true);
        for(Student student : game.getPlayerFromNickname(nickname).getSchool().getHall().getStudents()){
            String color = student.getColor().toString();
            ImageView studentImage = new ImageView(new Image("img/student_"+color+".png"));
            studentImage.setFitWidth(20);
            studentImage.setPreserveRatio(true);
            playerHall.add(studentImage, j, i);
            if(j==1) {
                i++;
                j = 0;
            }
            else
                j++;
        }

        playerDiningHall.getChildren().clear();
        playerDiningHall.setDisable(true);
        i = 0;
        j = 0;
        for(Color color : Color.values()) {
            try {
                Table table = game.getPlayerFromNickname(nickname).getSchool().getTable(color.toString());
                for(Student ignored : table.getStudents()){
                    ImageView studentImage = new ImageView(new Image("img/student_"+color+".png"));
                    studentImage.setFitWidth(20);
                    studentImage.setPreserveRatio(true);
                    playerDiningHall.add(studentImage, j, i);
                    j++;
                }
            } catch (NonExistentColorException e) {
                throw new RuntimeException(e);
            }
            i++;
            j = 0;
        }

        playerProfTable.getChildren().clear();
        playerProfTable.setDisable(true);
        i = 0;
        for(Color color : Color.values()){
            try {
                if(game.getPlayerFromNickname(nickname).getSchool().getTable(color.toString()).getHasProfessor()){
                    ImageView studentImage = new ImageView(new Image("img/prof_"+color+".png"));
                    studentImage.setFitWidth(25);
                    studentImage.setPreserveRatio(true);
                    playerProfTable.add(studentImage, 0, i);
                }
            } catch (NonExistentColorException e) {
                throw new RuntimeException(e);
            }
            i++;
        }

        playerTowerRoom.getChildren().clear();
        playerTowerRoom.setDisable(true);
        i = 0;
        j = 0;
        String towerColor = game.getTowersColor().get(game.getPlayerFromNickname(nickname)).toString().toLowerCase();
        for(int towers = 0; towers < game.getPlayerFromNickname(nickname).getSchool().getTowerRoom().getTowersLeft(); towers++){
            ImageView studentImage = new ImageView(new Image("img/"+towerColor+"_tower.png"));
            studentImage.setFitWidth(30);
            studentImage.setPreserveRatio(true);
            playerTowerRoom.add(studentImage, j, i);
            if(j==1){
                i++;
                j = 0;
            }
            else
                j++;
        }

    }

    private void updateCharacterCards(){
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
            island.setDisable(true);
            try {
                Island currentIsland = game.getBoard().getIslands().getIslandFromID(i);
                for (Color color : Color.values()) {
                    setStudentsOnIsland(i, j, currentIsland, color);
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

                 if (i == motherNatureOldPosition && i != game.getBoard().getMotherNaturePos())
                        island.getChildren().removeIf( node -> GridPane.getColumnIndex(node) == 0 && GridPane.getRowIndex(node) == 0);

                 if (game.getBoard().getMotherNaturePos() == i){
                     setMotherNature(island);
                 }

                 if (currentIsland.hasVetoTile()){
                     String imagePath = "/img/deny_island_icon.png";
                     ImageView vetoTile = new ImageView(new Image(String.valueOf(getClass().getResource(imagePath))));
                     vetoTile.setFitWidth(20);
                     vetoTile.setPreserveRatio(true);
                     island.add(vetoTile,0,1);
                 }

                 if (!currentIsland.hasVetoTile()){
                     island.getChildren().removeIf( node -> GridPane.getColumnIndex(node) == 0 && GridPane.getRowIndex(node) == 1 && node instanceof ImageView);
                 }

                }
            } catch (IslandNotFoundException e) {
                throw new RuntimeException(e);
            }
            i++;
            j = 0;
        }
        motherNatureOldPosition = game.getBoard().getMotherNaturePos();
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

    public void onClickPlayerHall(Event e){
        Node node = (Node) e.getTarget();
        if(node instanceof ImageView) {

            String tempUrl = ((ImageView) node).getImage().getUrl().toLowerCase();

            if (tempUrl.contains("student")) {

                if (tempUrl.contains("student_green")) {
                    studentToMoveColor = "GREEN";
                } else if (tempUrl.contains("student_yellow")) {
                    studentToMoveColor = "YELLOW";
                } else if (tempUrl.contains("student_red")) {
                    studentToMoveColor = "RED";
                } else if (tempUrl.contains("student_pink")) {
                    studentToMoveColor = "PINK";
                } else if (tempUrl.contains("student_blue")) {
                    studentToMoveColor = "BLUE";
                }

                moveStudentPhase = true;
                activateIslands();
                activatePlayerDiningHall();

                playerHall.getChildren().forEach( student -> {
                    if(student instanceof ImageView && ((ImageView) student).getImage().getUrl().equalsIgnoreCase("student")){
                        student.getStyleClass().remove("clickable");
                        student.setDisable(true);
                    }
                });
                playerHall.setDisable(true);
            }
            else{
                studentToMoveColor = null;
            }
        }
    }


    public void onClickPlayerDiningHall(Event e) {
        moveStudentToDiningHall(studentToMoveColor);
        playerDiningHall.setDisable(true);
        playerDiningHall.getStyleClass().remove("clickable");
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
        int islandChosenId = -1;

        // used for debugging
        System.out.println("Island chosen id: "+islandChosenId+ " prima prova");

        if (node.equals(island1)){
            islandChosenId = 1;
        }
        else if (node.equals(island2)) {
            islandChosenId = 2;
        }
        else if (node.equals(island3)) {
            islandChosenId = 3;
        }
        else if (node.equals(island4)) {
            islandChosenId = 4;
        }
        else if (node.equals(island5)) {
            islandChosenId = 5;
        }
        else if (node.equals(island6)) {
            islandChosenId = 6;
        }
        else if (node.equals(island7)) {
            islandChosenId = 7;
        }
        else if (node.equals(island8)) {
            islandChosenId = 8;
        }
        else if (node.equals(island9)) {
            islandChosenId = 9;
        }
        else if (node.equals(island10)) {
            islandChosenId = 10;
        }
        else if (node.equals(island11)) {
            islandChosenId = 11;
        }
        else if (node.equals(island12)) {
            islandChosenId = 12;
        }

        //used for debugging
        System.out.println("Island chosen id: "+islandChosenId+ " seconda prova");

        if (moveStudentPhase && islandChosenId != -1)
            moveStudentToIsland(studentToMoveColor, islandChosenId);
        else if (!moveStudentPhase && islandChosenId != -1)
            moveMotherNature(islandChosenId);

        islands.forEach(island -> island.getStyleClass().remove("clickable"));
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
        //prima c'era GridPane
        for (Node island : islands){
            island.getStyleClass().add("clickable");
          /*  island.getChildren().forEach( image -> {
                if(image instanceof ImageView && ((ImageView) image).getImage().getUrl().toLowerCase().contains("island")){
                    image.getStyleClass().add("clickable");
                }
            }); */
            island.setDisable(false);
        }
    }

    public void activatePlayerDiningHall(){
        playerDiningHall.getStyleClass().add("clickable");
        playerDiningHall.setDisable(false);
    }

    public void activateMoveStudent() {
        playerHall.setDisable(false);
        playerHall.getChildren().forEach( student -> {
            if(student instanceof ImageView && ((ImageView) student).getImage().getUrl().contains("student")){
                student.getStyleClass().add("clickable");
                student.setDisable(false);
            }
        });
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

    private void moveStudentToDiningHall(String color){
        notifyObserver(viewObserver -> viewObserver.onUpdateTableStudentMove(color));
        moveStudentPhase = false;
        studentToMoveColor = null;
    }

    private void moveStudentToIsland(String color, int islandId){
        notifyObserver(viewObserver -> viewObserver.onUpdateIslandStudentMove(color, islandId));
        moveStudentPhase = false;
        studentToMoveColor = null;
    }

    private void moveMotherNature(int islandChosenId){
        int steps = islandChosenId - game.getBoard().getMotherNaturePos();
        notifyObserver(viewObserver -> viewObserver.onUpdateMotherNatureSteps(steps));
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
            island.setDisable(true);
            try {
                Island currentIsland = game.getBoard().getIslands().getIslandFromID(i);
                for (Color color : Color.values()) {
                    setStudentsOnIsland(i, j, currentIsland, color);
                    Text text = studentsOnIsland[i-1][j];
                    island.add(text, 2, j);

                    if (game.getBoard().getMotherNaturePos() == i){
                        motherNatureOldPosition = i;
                        setMotherNature(island);
                    }
                    j++;
                }
            } catch (IslandNotFoundException e) {
                throw new RuntimeException(e);
            }
            island.add(towerOnIsland[i-1],0,2);
            island.add(towersNumberOnIsland[i-1],0,3);
            i++;
            j = 0;
        }
    }

    private void setMotherNature(GridPane island) {

        String imagePath = "/img/mother_nature.png";
        ImageView motherNature = new ImageView(new Image(String.valueOf(getClass().getResource(imagePath))));
        motherNature.setFitWidth(42);
        motherNature.setPreserveRatio(true);
        motherNature.setStyle("-fx-effect: dropshadow(one-pass-box, rgba(70,70,70,70), 10, 0, 0, 0);");
        motherNature.setY(-40);
        motherNature.setY(+15);
        motherNature.getStyleClass().add("clickable");
        island.add(motherNature,0,0);
    }

    private void setStudentsOnIsland(int i, int j, Island currentIsland, Color color) {
        int num_students = currentIsland.getNumOfStudentsOfColor(color.toString());
        studentsOnIsland[i-1][j].setText(String.valueOf(num_students));
        studentsOnIsland[i-1][j].setFill(Paint.valueOf("WHITE"));
        studentsOnIsland[i-1][j].setFont(Font.font(String.valueOf(Font.getDefault()), FontWeight.EXTRA_BOLD, 12.0));
    }

    public Game getGame() {
        return game;
    }

    private void initializeCharacterCards(){
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
