package it.polimi.ingsw.view.gui.scenecontrollers;

import it.polimi.ingsw.exceptions.IslandNotFoundException;
import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.charactercards.*;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.view.gui.popupcontrollers.BardPopupController;
import it.polimi.ingsw.view.gui.popupcontrollers.ColorPopupController;
import it.polimi.ingsw.view.gui.popupcontrollers.JesterPopupController;
import it.polimi.ingsw.view.gui.popupcontrollers.StudentsPopupController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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

import java.util.*;

/**
 * This scene controller controls the Game Board scene, updating all of its contents everytime a player does something.
 */

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
    private AnchorPane opposingSchool2Pane;
    @FXML
    private GridPane playerHall, playerDiningRoom, playerProfTable, playerTowerRoom;
    @FXML
    private GridPane opposingHall1, opposingHall2, opposingDiningRoom1, opposingDiningRoom2,
            opposingProfTable1, opposingProfTable2, opposingTowerRoom1, opposingTowerRoom2;
    @FXML
    private Label playerNickname, opposingNickname1, opposingNickname2;
    @FXML
    private GridPane walletGridPane;
    @FXML
    private Label coinsLabel;
    @FXML
    private Label characterCardsLabel;
    private Game game;
    private String nickname;
    private final List<TilePane> clouds;
    private final List<List<GridPane>> opposingSchools;
    private final List<GridPane> opposingSchool1, opposingSchool2;
    private final List<Label> opposingNicknameLabels;
    private final List<GridPane> islands;
    private final List<Text> costs;
    private final Node[] playersWallet;
    private final Text[] coins;
    private final List<Player> playersCoins;
    private final Text[][] studentsOnIsland;
    private final ImageView[] towerOnIsland; // used for the tower image on each island
    private final Text[] towersNumberOnIsland; // used for the towers number on each island
    private int motherNatureOldPosition;
    private CharacterCard lastCharacterCardPlayed;
    private String studentToMoveColor;
    private boolean moveStudentPhase;
    private final Text numOfVetos;
    private final TilePane[] studentsOnTheCards;
    private final List<AssistantCard> assistantCards;
    private int numIslands;

    /**
     * GameBoardSceneController constructor.
     */

    public GameBoardSceneController() {
        assistantCards = new ArrayList<>();
        clouds = new ArrayList<>();
        islands = new ArrayList<>();
        costs = new ArrayList<>();
        opposingSchools = new ArrayList<>();
        opposingSchool1 = new ArrayList<>();
        opposingSchool2 = new ArrayList<>();
        opposingNicknameLabels = new ArrayList<>();
        studentsOnTheCards = new TilePane[3];
        for (int i = 0; i < Constants.CHARACTERS_NUM; i++)
            costs.add(new Text());
        studentsOnIsland = new Text[12][5];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 5; j++) {
                studentsOnIsland[i][j] = new Text("0");
            }
        }
        towerOnIsland = new ImageView[12];
        for (int i = 0; i < 12; i++) {
            towerOnIsland[i] = new ImageView();
        }
        towersNumberOnIsland = new Text[12];
        for (int i = 0; i < 12; i++) {
            towersNumberOnIsland[i] = new Text();
        }
        moveStudentPhase = false;
        studentToMoveColor = null;
        numOfVetos = new Text();
        playersWallet = new Node[3];
        coins = new Text[3];
        playersCoins = new ArrayList<>();
        numIslands = Constants.MAX_NUM_OF_ISLANDS;
    }

    /**
     * Returns the {@link Game} whose data are displayed on the scene.
     *
     * @return the {@link Game} whose data are displayed on the scene.
     */

    public Game getGame() {
        return game;
    }

    /**
     * Sets the {@link Game} whose data are displayed on the scene.
     *
     * @param game the {@link Game} whose data need to be displayed on the scene.
     */

    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Returns the nickname of the user.
     *
     * @return a {@link String} representing the nickname of the user.
     */

    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname of the user.
     *
     * @param nickname a {@link String} representing the nickname of the user.
     */

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Properly initializes the scene by adding the event handlers.
     */

    @FXML
    public void initialize(){
        deck.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickAssistantCard);
        cloud1.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickCloud);
        cloud2.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickCloud);
        playerHall.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickPlayerHall);
        playerDiningRoom.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickPlayerDiningRoom);
        if(game.getPlayersNumber()==3)
            cloud3.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickCloud);
        if(game instanceof GameExpertMode)
            characterCards.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onClickCharacterCard);
    }

    /**
     * Builds the Game Board according to the given game.
     */

    public void startGameBoard() {

        renderDeck();

        clouds.add(cloud1);
        clouds.add(cloud2);

        assistantCards.addAll(game.getPlayerFromNickname(nickname).getDeck());

        opposingSchool1.add(opposingHall1);
        opposingSchool1.add(opposingDiningRoom1);
        opposingSchool1.add(opposingProfTable1);
        opposingSchool1.add(opposingTowerRoom1);
        opposingSchools.add(opposingSchool1);

        opposingNicknameLabels.add(opposingNickname1);

        if (game.getPlayersNumber() == 3) {
            clouds.add(cloud3);
            extraCloud.setVisible(true);
            opposingSchool2.add(opposingHall2);
            opposingSchool2.add(opposingDiningRoom2);
            opposingSchool2.add(opposingProfTable2);
            opposingSchool2.add(opposingTowerRoom2);
            opposingSchools.add(opposingSchool2);
            opposingSchool2Pane.setVisible(true);
            opposingNicknameLabels.add(opposingNickname2);
        } else {
            extraCloud.setVisible(false);
            opposingSchool2Pane.setVisible(false);
            opposingNickname2.setVisible(false);
        }
        initializeIslands();

        renderPlayerSchool();

        if(game instanceof GameExpertMode) {
            initializeCharacterCards();
            initializeCoinsWallet();
            lastCharacterCardPlayed = null;
        }
        else {
            coinsLabel.setVisible(false);
            characterCardsLabel.setVisible(false);
        }
    }

    /**
     * Renders the Game Board with the up-to-date data.
     */

    public void renderGameBoard(){
        renderIslands();
        renderClouds();
        renderPlayerSchool();
        renderOpposingSchools();
        renderNicknameLabels();
        if(game instanceof GameExpertMode) {
            renderCharacterCardCosts();
            renderVetoTiles();
            renderStudentsOnCards();
            renderPlayersWallet();
        }
    }

    /* RENDERING METHODS */

    /**
     * Renders the deck with the currently available Assistant Cards.
     */

    private void renderDeck(){
        deck.getChildren().clear();
        deck.setDisable(true);
        for(AssistantCard assistantCard : game.getPlayerFromNickname(nickname).getDeck()){
            String imagePath = "/img/assistants/assistant"+(assistantCard.getWeight())+".png";
            ImageView image = new ImageView(new Image(String.valueOf(getClass().getResource(imagePath))));
            image.setPreserveRatio(true);
            image.setFitWidth(85);
            deck.add(image, assistantCard.getWeight()-1, 0);
        }
    }

    /**
     * Renders the player's school (the bigger one on the GUI) with the up-to-date information.
     */

    private void renderPlayerSchool(){

        int i = 0;
        int j = 1;
        playerHall.getChildren().clear();
        playerHall.setDisable(true);
        for(Student student : game.getPlayerFromNickname(nickname).getSchool().getHall().getStudents()){
            String color = student.color().toString().toLowerCase();
            ImageView studentImage = new ImageView(new Image("/img/student_"+color+".png"));
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

        playerDiningRoom.getChildren().clear();
        playerDiningRoom.setDisable(true);
        i = 0;
        j = 0;
        for(Color color : Color.values()) {
            try {
                Table table = game.getPlayerFromNickname(nickname).getSchool().getTable(color.toString());
                for(Student ignored : table.getStudents()){
                    ImageView studentImage = new ImageView(new Image("/img/student_"+color.toString().toLowerCase()+".png"));
                    studentImage.setFitWidth(20);
                    studentImage.setPreserveRatio(true);
                    playerDiningRoom.add(studentImage, j, i);
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
                    ImageView studentImage = new ImageView(new Image("/img/prof_"+color.toString().toLowerCase()+".png"));
                    studentImage.setFitWidth(20);
                    studentImage.setPreserveRatio(true);
                    studentImage.setRotate(90.0);
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
        String towerColor = game.getTowersColor().get(game.getPlayerFromNickname(nickname)).toString().toLowerCase();
        for(int towers = 0; towers < game.getPlayerFromNickname(nickname).getSchool().getTowerRoom().getTowersLeft(); towers++){
            ImageView towerImage = new ImageView(new Image("/img/"+ towerColor.toLowerCase()+"_tower.png"));
            towerImage.setFitWidth(30);
            towerImage.setPreserveRatio(true);
            playerTowerRoom.add(towerImage, j, i);
            if(j==1){
                i++;
                j = 0;
            }
            else
                j++;
        }

    }

    /**
     * Renders the opposing players' schools (the smaller ones) with the up-to-date information.
     */

    private void renderOpposingSchools(){

        List<String> players = new ArrayList<>();
        game.getPlayers().forEach(x -> players.add(x.getNickname()));
        players.remove(nickname);
        int playerIndex = 0;

        for(List<GridPane> school : opposingSchools){
            int i = 0;
            int j = 1;
            GridPane hall = school.get(0);
            hall.getChildren().clear();
            hall.setDisable(true);
            for(Student student : game.getPlayerFromNickname(players.get(playerIndex)).getSchool().getHall().getStudents()){
                String color = student.color().toString().toLowerCase();
                ImageView studentImage = new ImageView(new Image("/img/student_"+color+".png"));
                studentImage.setFitWidth(9);
                studentImage.setPreserveRatio(true);
                hall.add(studentImage, j, i);
                if(j==1) {
                    i++;
                    j = 0;
                }
                else
                    j++;
            }

            GridPane diningRoom = school.get(1);
            diningRoom.getChildren().clear();
            diningRoom.setDisable(true);
            i = 0;
            j = 0;
            for(Color color : Color.values()) {
                try {
                    Table table = game.getPlayerFromNickname(players.get(playerIndex)).getSchool().getTable(color.toString());
                    for(Student ignored : table.getStudents()){
                        ImageView studentImage = new ImageView(new Image("/img/student_"+color.toString().toLowerCase()+".png"));
                        studentImage.setFitWidth(9);
                        studentImage.setPreserveRatio(true);
                        diningRoom.add(studentImage, j, i);
                        j++;
                    }
                } catch (NonExistentColorException e) {
                    throw new RuntimeException(e);
                }
                i++;
                j = 0;
            }

            GridPane profTable = school.get(2);
            profTable.getChildren().clear();
            profTable.setDisable(true);
            i = 0;
            for(Color color : Color.values()){
                try {
                    if(game.getPlayerFromNickname(players.get(playerIndex)).getSchool().getTable(color.toString()).getHasProfessor()){
                        ImageView studentImage = new ImageView(new Image("/img/prof_"+color.toString().toLowerCase()+".png"));
                        studentImage.setFitWidth(9);
                        studentImage.setPreserveRatio(true);
                        studentImage.setRotate(90.0);
                        profTable.add(studentImage, 0, i);
                    }
                } catch (NonExistentColorException e) {
                    throw new RuntimeException(e);
                }
                i++;
            }

            GridPane towerRoom = school.get(3);
            towerRoom.getChildren().clear();
            towerRoom.setDisable(true);
            i = 0;
            String towerColor = game.getTowersColor().get(game.getPlayerFromNickname(players.get(playerIndex))).
                    toString().toLowerCase();
            for(int towers = 0; towers < game.getPlayerFromNickname(players.get(playerIndex)).
                    getSchool().getTowerRoom().getTowersLeft(); towers++){
                ImageView studentImage = new ImageView(new Image("/img/"+towerColor.toLowerCase()+"_tower.png"));
                studentImage.setFitWidth(12);
                studentImage.setPreserveRatio(true);
                towerRoom.add(studentImage, j, i);
                if(j==1){
                    i++;
                    j = 0;
                }
                else
                    j++;
            }

            playerIndex++;
        }

    }

    /**
     * Renders the nickname labels over every player's school.
     */

    private void renderNicknameLabels(){

        List<String> players = new ArrayList<>();
        game.getPlayers().forEach(x -> players.add(x.getNickname()));
        players.remove(nickname);
        int playerIndex = 0;

        playerNickname.setText(nickname);

        for(Label label : opposingNicknameLabels){
            label.setText(players.get(playerIndex));
            playerIndex++;
        }

    }

    /**
     * Renders the islands on the Game Board with the up-to-date information.
     */

    private void renderIslands(){

        int i = 1;
        int j = 0;
        for(GridPane island : islands){
            island.setDisable(true);
            island.setOnMouseClicked(this::onClickIsland);
            try {
                Island currentIsland = game.getBoard().getIslands().getIslandFromID(i);
                for (Color color : Color.values()) {
                    setStudentsOnIsland(i, j, currentIsland, color);
                    j++;

                    towerOnIsland[i-1].setImage(null);
                    towersNumberOnIsland[i-1].setText("");
                    if (currentIsland.getNumOfTowers() != 0){
                        Player player = currentIsland.getOwner();
                        String towerColor = game.getTowersColor().get(player).toString().toLowerCase();
                        Image towerImage = new Image("/img/"+towerColor+"_tower.png");
                        towerOnIsland[i-1].setImage(towerImage);
                        if(towerColor.equals(TowerColor.WHITE.toString().toLowerCase())) {
                            towerOnIsland[i-1].setFitWidth(35);
                        }
                        else
                            towerOnIsland[i-1].setFitWidth(40);
                        towerOnIsland[i-1].setPreserveRatio(true);
                        towerOnIsland[i-1].setX(58);
                        towerOnIsland[i-1].setY(38);
                        towerOnIsland[i-1].setStyle("-fx-effect: dropshadow(one-pass-box, rgba(80,80,80,80), 10, 0, 0, 0);");
                        towersNumberOnIsland[i-1].setText("       "+ currentIsland.getNumOfTowers());
                        towersNumberOnIsland[i-1].setFill(Paint.valueOf("WHITE"));
                        towersNumberOnIsland[i-1].setTextAlignment(TextAlignment.RIGHT);
                        towersNumberOnIsland[i-1].setFont(Font.font(String.valueOf(Font.getDefault()), FontWeight.EXTRA_BOLD, 12.0));
                    }

                    if (i == motherNatureOldPosition && i != game.getBoard().getMotherNaturePos())
                        island.getChildren().removeIf( node -> GridPane.getColumnIndex(node) == 0 && GridPane.getRowIndex(node) == 0);

                    if (game.getBoard().getMotherNaturePos() == i){
                        setMotherNature(island);
                    }

                    if (currentIsland.hasVetoTile()){
                        String imagePath = "/img/veto.png";
                        ImageView vetoTile = new ImageView(new Image(String.valueOf(getClass().getResource(imagePath))));
                        vetoTile.setFitWidth(30);
                        vetoTile.setPreserveRatio(true);
                        island.add(vetoTile,0,1);
                    }

                    if (!currentIsland.hasVetoTile()){
                        island.getChildren().removeIf( node -> GridPane.getColumnIndex(node) == 0 && GridPane.getRowIndex(node) == 1 && node instanceof ImageView);
                    }
                }
            } catch (IslandNotFoundException e) {
                if(numIslands > game.getBoard().getIslands().getSize())
                    mergeIslands();
            }
            i++;
            j = 0;
        }
        motherNatureOldPosition = game.getBoard().getMotherNaturePos();
    }

    /**
     * Renders the clouds with the up-to-date students.
     */

    private void renderClouds(){

        for(TilePane cloud : clouds){
            cloud.getChildren().clear();
        }

        for(int i=0; i<clouds.size(); i++){
            clouds.get(i).setDisable(true);
            for(Student student : game.getBoard().getCloud(i).getStudents()){
                String color = student.color().toString().toLowerCase();
                ImageView studentImage = new ImageView(new Image("/img/student_"+color+".png"));
                studentImage.setFitWidth(20);
                studentImage.setPreserveRatio(true);
                clouds.get(i).getChildren().add(studentImage);
            }
        }
    }

    /**
     * Renders the players' wallets with the up-to-date number of coins.
     */

    private void renderPlayersWallet(){
        for (int i = 0; i < game.getPlayersNumber(); i++){
            Player currentPlayer = game.getPlayerFromNickname(playersCoins.get(i).getNickname());
            coins[i].setText(String.valueOf(currentPlayer.getCoinsWallet()));
            coins[i].setDisable(true);
        }
    }

    /**
     * Renders the Character Cards' costs with the up-to-date costs.
     */

    private void renderCharacterCardCosts(){
        characterCards.setDisable(true);
        CharacterCard[] cards = ((GameExpertMode) game).getCharacters();
        for(int i = 0; i < Constants.CHARACTERS_NUM; i++){
            costs.get(i).setText(String.valueOf(cards[i].getCost()));
        }
    }

    /**
     * Renders the students on the Character Cards (if present) with the up-to-date information.
     */

    private void renderStudentsOnCards(){

        for(int i = 0; i < Constants.CHARACTERS_NUM; i++){
            if(studentsOnTheCards[i]!=null) {
                studentsOnTheCards[i].getChildren().clear();
                CharacterCard[] cards = ((GameExpertMode) game).getCharacters();
                for(Student student : ((StudentsCard) cards[i]).getStudentsOnTheCard()){
                    String color = student.color().toString().toLowerCase();
                    ImageView studentImage = new ImageView(new Image("/img/student_"+color+".png"));
                    studentImage.setFitWidth(11.5);
                    studentImage.setStyle("-fx-effect: dropshadow(one-pass-box, rgb(255,255,255), 10, 0, 0, 0);");
                    studentImage.setPreserveRatio(true);
                    studentsOnTheCards[i].getChildren().add(studentImage);
                }
            }
        }

    }

    /**
     * Renders the up-to-date number of veto tiles on the {@link Healer}.
     */

    private void renderVetoTiles(){
        Optional<CharacterCard> vetoCard =
                Arrays.stream(((GameExpertMode) game).getCharacters()).filter(x -> x instanceof Healer).findFirst();
        if(vetoCard.isPresent()){
            numOfVetos.setText(String.valueOf(game.getBoard().getNumOfVetos()));
        }
    }

    /* HISTORY METHODS */

    /**
     * Shows what happened on the Game Board thanks to a history text box.
     *
     * @param updateMessage the message to display.
     */

    public void showUpdate(String updateMessage){
        Text message = new Text(updateMessage+"\n");
        history.getChildren().add(message);
    }

    /* EVENT HANDLER METHODS */

    /**
     * Tries to play an Assistant Card when clicked by the player.
     *
     * @param e the event that triggered the method.
     */

    public void onClickAssistantCard(Event e){
        Node node = (Node) e.getTarget();
        try{
            int cardId = GridPane.getColumnIndex(node);
            deck.setDisable(true);
            playAssistantCard(cardId);
            ImageView cardImage = (ImageView) node;
            String wizard_path = game.getPlayerFromNickname(getNickname()).getWizardID().toString().toLowerCase();
            cardImage.setImage(new Image(String.valueOf(getClass().getResource("/img/wizards/" + wizard_path + ".jpg"))));
            cardImage.getStyleClass().set(0, "");
        }catch(NullPointerException e1){ System.out.println("entered null value");}
    }

    /**
     * Allows the movement of a student when clicked on the player's hall.
     *
     * @param e the event that triggered the method.
     */

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
                activatePlayerDiningRoom();

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

    /**
     * Moves a student previously selected to the proper table when the dining hall is clicked (it doesn't matter if
     * a player clicks on the wrong table: since it's the whole dining room that is clickable and not the single
     * tables, the controller will do the work for the player and place the student correctly).
     *
     * @param e the event that triggered the method.
     */

    public void onClickPlayerDiningRoom(Event e) {
        moveStudentToDiningRoom(studentToMoveColor);
        playerDiningRoom.setDisable(true);
        playerDiningRoom.getStyleClass().remove("clickable");
    }

    /**
     * Picks the students from a cloud when clicked (if possible).
     *
     * @param e the event that triggered the method.
     */

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

    /**
     * According to what happened before, moves the selected student onto the clicked island or selects the clicked
     * island to do the effect of a {@link IntCard} / {@link StringIntCard} there.
     *
     * @param e the event that triggered the method.
     */

    public void onClickIsland(Event e){
        Node node = (Node) e.getTarget();
        if(node.getParent()!=null){
            node = node.getParent();
        }
        int islandChosenId = -1;

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

        numIslands = game.getBoard().getIslands().getSize();

        if (lastCharacterCardPlayed != null) {
            if(lastCharacterCardPlayed instanceof StringIntCard)
                playCharacterCardStringInt(studentToMoveColor, islandChosenId);
            else
                playCharacterCardInt(islandChosenId);
        }
        else if (moveStudentPhase && islandChosenId != -1)
            moveStudentToIsland(studentToMoveColor, islandChosenId);
        else if (!moveStudentPhase && islandChosenId != -1)
            moveMotherNature(islandChosenId);

        islands.forEach(island -> island.getStyleClass().remove("clickable"));
    }

    /**
     * Tries to play a Character Card when clicked. Based on the card chosen, it may cause a popup to appear.
     *
     * @param e the event that triggered the method.
     */

    public void onClickCharacterCard(Event e){
        Node node = (Node) e.getTarget();
        int columnIndex = GridPane.getColumnIndex(node);
        lastCharacterCardPlayed = ((GameExpertMode) game).getCharacters()[columnIndex];
        if(lastCharacterCardPlayed instanceof StudentsCard &&
                !(lastCharacterCardPlayed instanceof Jester)){
            StudentsPopupController spc = new StudentsPopupController();
            spc.setStudents(((StudentsCard) lastCharacterCardPlayed).getStudentsOnTheCard());
            studentToMoveColor = spc.display();
            if(studentToMoveColor != null) {
                if (lastCharacterCardPlayed instanceof StringIntCard)
                    activateIslands();
                else
                    playCharacterCardString(studentToMoveColor);
            }
            else
                lastCharacterCardPlayed = null;
        }
        else if(lastCharacterCardPlayed instanceof IntCard){
            activateIslands();
        }
        else if(lastCharacterCardPlayed instanceof StringCard){
            String colorChosen = new ColorPopupController().display();
            if(colorChosen != null)
                playCharacterCardString(colorChosen);
            else
                lastCharacterCardPlayed = null;
        }
        else if(lastCharacterCardPlayed instanceof ArrayListStringCard){
            ArrayList<String> parameter;
            if(!(lastCharacterCardPlayed instanceof StudentsCard))
                parameter = new BardPopupController().display();
            else
                parameter = new JesterPopupController(((StudentsCard) lastCharacterCardPlayed).getStudentsOnTheCard()).display();
            if(parameter != null)
                playCharacterCardArrayListString(parameter);
            else
                lastCharacterCardPlayed = null;
        }
        else playCharacterCard();
    }

    /* ACTIVATION METHODS */

    /**
     * Allows the player to click one of the cards of their Assistant Cards deck.
     */

    public void activateAssistantCardChoice(){
        renderDeck();
        showUpdate("Select the Assistant Card you want to play.");
        for(Node img : deck.getChildren()){
            img.getStyleClass().set(0,"clickable");
        }
        deck.setDisable(false);
    }

    /**
     * Allows the player to click one of the islands currently present on the Game Board.
     */

    public void activateIslands(){
        for (Node island : islands){
            island.getStyleClass().add("clickable");
            island.setDisable(false);
        }
    }

    /**
     * Allows the player to click the dining room of their school.
     */

    public void activatePlayerDiningRoom(){
        playerDiningRoom.getStyleClass().add("clickable");
        playerDiningRoom.setDisable(false);
    }

    /**
     * Allows the player to click a student into the hall of their school.
     */

    public void activateMoveStudent() {
        playerHall.setDisable(false);
        playerHall.getChildren().forEach( student -> {
            if(student instanceof ImageView && ((ImageView) student).getImage().getUrl().contains("student")){
                student.getStyleClass().add("clickable");
                student.setDisable(false);
            }
        });
    }

    /**
     * Allows the player to click one of the present clouds.
     */

    public void activateCloudChoice(){
        for(Node cloud : clouds){
            cloud.getStyleClass().add("clickable");
            cloud.setDisable(false);
        }
    }

    /**
     * Allows the player to click one of the available Character Cards.
     */

    public void activateCharacterCards(){
        for(Node img : characterCards.getChildren()){
            if(!(img instanceof TilePane) )
                img.getStyleClass().add("clickable");
        }
        characterCards.setDisable(false);
    }

    /* OBSERVER-NOTIFYING METHODS */

    /**
     * Notifies the controller of the chosen Assistant Card.
     *
     * @param cardId an {@code int} representing the weight of the card chosen (each card has a unique weight).
     */

    private void playAssistantCard(int cardId) {
        String cardName = assistantCards.get(cardId).getName();
        notifyObserver(viewObserver -> viewObserver.onUpdateAssistantCard(cardName));
        if(game.getPlayerFromNickname(nickname).getLatestAssistantCardPlayed() != null)
            showUpdate("You have played the " + cardName + " Assistant Card!");
    }

    /**
     * Notifies the controller of the chosen cloud.
     *
     * @param cloudId an {@code int} representing the ID of the chosen cloud.
     */

    private void chooseCloud(int cloudId){
        notifyObserver(viewObserver -> viewObserver.onUpdateCloudChoice(cloudId));
        if(game.getPlayerFromNickname(nickname).getSchool().getHall().getStudents().size() == game.getConstants().MAX_HALL_STUDENTS)
            showUpdate(nickname + " has chosen a cloud!");
    }

    /**
     * Notifies the controller of the color of the hall student the player wishes to move to the dining room.
     *
     * @param color a {@link String} representing the color of the student to move.
     */

    private void moveStudentToDiningRoom(String color){
        notifyObserver(viewObserver -> viewObserver.onUpdateTableStudentMove(color));
        showUpdate(game.getCurrentPlayer().getNickname() + " has moved a " + color + " student to" +
                " the dining room!");
        moveStudentPhase = false;
        studentToMoveColor = null;
    }

    /**
     * Notifies the controller of the color of the hall student the player wishes to move to the chosen island.
     *
     * @param color a {@link String} representing the color of the student to move.
     * @param islandId an {@code int} representing the ID of the chosen island.
     */

    private void moveStudentToIsland(String color, int islandId){
        notifyObserver(viewObserver -> viewObserver.onUpdateIslandStudentMove(color, islandId));
        showUpdate(game.getCurrentPlayer().getNickname() + " has moved a " + color + " student to" +
                " the island number " + islandId + "!");
        moveStudentPhase = false;
        studentToMoveColor = null;
    }

    /**
     * Notifies the controller of the number of steps the player wishes Mother Nature to move of. Since the
     * player chooses the destination island rather than providing directly the amount of steps, the latter is
     * calculated in this method and provided to the controller.
     *
     * @param islandChosenId an {@code int} representing the ID of the chosen island.
     */

    private void moveMotherNature(int islandChosenId){
        int steps = islandChosenId - game.getBoard().getMotherNaturePos();
        if(steps < 0){
            int stepsToDoBeforeFinalIsland = game.getBoard().getIslands().getSize() - game.getBoard().getMotherNaturePos();
            steps = stepsToDoBeforeFinalIsland + islandChosenId;
        }
        int oldPosition = game.getBoard().getMotherNaturePos();
        int finalSteps = steps;
        notifyObserver(viewObserver -> viewObserver.onUpdateMotherNatureSteps(finalSteps));
        if(oldPosition != game.getBoard().getMotherNaturePos())
            showUpdate(game.getCurrentPlayer().getNickname() + " has moved Mother Nature to island number " +
                islandChosenId + "!");
    }

    /**
     * Notifies the controller of the Character Card the player wishes to use. If this method is used, it means that
     * the card has no parameter.
     */

    private void playCharacterCard(){
        String cardName = lastCharacterCardPlayed.getClass().getSimpleName();
        notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCard(lastCharacterCardPlayed.getId()));
        if(game.getCurrentPlayer().getCharacterCardAlreadyPlayed()) {
            characterCards.setDisable(true);
            showUpdate(game.getCurrentPlayer().getNickname() + " has played the " + cardName + "!");
        }
        lastCharacterCardPlayed = null;
    }

    /**
     * Notifies the controller of the Character Card the player wishes to use. If this method is used, it means that
     * the card is a {@link IntCard}.
     *
     * @param islandChosenId an {@code int} representing the ID of the chosen island.
     */

    private void playCharacterCardInt(int islandChosenId){
        String cardName = lastCharacterCardPlayed.getClass().getSimpleName();
        notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardInt(lastCharacterCardPlayed.getId(), islandChosenId));
        if(game.getCurrentPlayer().getCharacterCardAlreadyPlayed()) {
            characterCards.setDisable(true);
            showUpdate(game.getCurrentPlayer().getNickname() + " has played the " + cardName + "!");
        }
        lastCharacterCardPlayed = null;
    }

    /**
     * Notifies the controller of the Character Card the player wishes to use. If this method is used, it means that
     * the card is a {@link StringCard}.
     *
     * @param chosenColor a {@link String} representing the chosen color.
     */

    private void playCharacterCardString(String chosenColor){
        String cardName = lastCharacterCardPlayed.getClass().getSimpleName();
        notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardString(lastCharacterCardPlayed.getId(), chosenColor));
        if(game.getCurrentPlayer().getCharacterCardAlreadyPlayed()) {
            characterCards.setDisable(true);
            showUpdate(game.getCurrentPlayer().getNickname() + " has played the " + cardName + "!");
        }
        lastCharacterCardPlayed = null;
    }

    /**
     * Notifies the controller of the Character Card the player wishes to use. If this method is used, it means that
     * the card is a {@link StringIntCard}.
     *
     * @param chosenColor a {@link String} representing the chosen color.
     * @param islandChosenId an {@code int} representing the ID of the chosen island.
     */

    private void playCharacterCardStringInt(String chosenColor, int islandChosenId){
        String cardName = lastCharacterCardPlayed.getClass().getSimpleName();
        notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardStringInt(lastCharacterCardPlayed.getId(), chosenColor, islandChosenId));
        if(game.getCurrentPlayer().getCharacterCardAlreadyPlayed()) {
            characterCards.setDisable(true);
            showUpdate(game.getCurrentPlayer().getNickname() + " has played the " + cardName + "!");
        }
        lastCharacterCardPlayed = null;
        studentToMoveColor = null;
    }

    /**
     * Notifies the controller of the Character Card the player wishes to use. If this method is used, it means that
     * the card is a {@link ArrayListStringCard}.
     *
     * @param parameter a list representing the chosen colors.
     */

    private void playCharacterCardArrayListString(ArrayList<String> parameter){
        String cardName = lastCharacterCardPlayed.getClass().getSimpleName();
        notifyObserver(viewObserver -> viewObserver.onUpdateCharacterCardArrayListString(lastCharacterCardPlayed.getId(), parameter));
        if(game.getCurrentPlayer().getCharacterCardAlreadyPlayed()) {
            characterCards.setDisable(true);
            showUpdate(game.getCurrentPlayer().getNickname() + " has played the " + cardName + "!");
        }
        lastCharacterCardPlayed = null;
    }

    /* INITIALIZING METHODS */

    /**
     * Properly initializes the islands to show.
     */

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

    /**
     * Properly initializes the Character Cards to display.
     */

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
            Pane coinPane = new Pane();
            ImageView coin = new ImageView(new Image(String.valueOf(getClass().getResource(coinPath))));
            coin.setPreserveRatio(true);
            coin.setFitWidth(40);
            DropShadow coinEffect = new DropShadow();
            coinEffect.setBlurType(BlurType.ONE_PASS_BOX);
            coin.setEffect(coinEffect);
            coin.setDisable(true);
            coinPane.getChildren().add(coin);
            GridPane.setValignment(coinPane, VPos.TOP);

            Text cost = costs.get(i);
            cost.setText(String.valueOf(cards[i].getCost()));
            cost.setFill(Paint.valueOf("WHITE"));
            cost.setFont(Font.font(String.valueOf(Font.getDefault()), FontWeight.EXTRA_BOLD, 16.0));
            cost.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
            cost.setTextAlignment(TextAlignment.CENTER);
            cost.setDisable(true);
            cost.setX(cost.getX() + 16.0);
            cost.setY(cost.getY() + 27.0);
            coinPane.getChildren().add(cost);
            characterCards.add(coinPane, i, 0);

            if(cards[i] instanceof Healer){

                TilePane vetoPane = new TilePane();
                vetoPane.setPrefTileWidth(22);
                vetoPane.setPrefTileHeight(20);
                vetoPane.setAlignment(Pos.TOP_RIGHT);

                numOfVetos.setText(String.valueOf(game.getBoard().getNumOfVetos()));
                numOfVetos.setFill(Paint.valueOf("WHITE"));
                numOfVetos.setFont(Font.font(String.valueOf(Font.getDefault()), FontWeight.EXTRA_BOLD, 16.0));
                numOfVetos.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
                numOfVetos.setTextAlignment(TextAlignment.CENTER);
                numOfVetos.setDisable(true);
                vetoPane.getChildren().add(numOfVetos);
                vetoPane.setDisable(true);

                characterCards.add(vetoPane, i, 0);

            }

            if(cards[i] instanceof StudentsCard){
                studentsOnTheCards[i] = new TilePane();
                studentsOnTheCards[i].setPrefTileHeight(12);
                studentsOnTheCards[i].setPrefTileWidth(12);
                studentsOnTheCards[i].setAlignment(Pos.CENTER);
                studentsOnTheCards[i].setDisable(true);
                characterCards.add(studentsOnTheCards[i], i, 0);
            }
        }
    }

    /**
     * Properly initializes the box where the amount of coins of each player will be shown.
     */

    private void initializeCoinsWallet() {

        playersCoins.add(0, game.getPlayerFromNickname(nickname));
        for (Player player : game.getPlayers()){
            if (!player.getNickname().equals(nickname)){
                playersCoins.add(player);
            }
        }
        for (int i = 0; i < game.getPlayersNumber(); i++) {

            coins[i] = new Text(String.valueOf(playersCoins.get(i).getCoinsWallet()));

            Text playerNickname = new Text("      "+playersCoins.get(i).getNickname());
            playerNickname.setFill(Paint.valueOf("WHITE"));
            playerNickname.setFont(Font.font(String.valueOf(Font.getDefault()), FontWeight.EXTRA_BOLD, 14.0));
            playerNickname.setTextAlignment(TextAlignment.CENTER);
            playerNickname.setDisable(true);

            String coinPath = "/img/coin.png";
            Pane coinPane = new Pane();
            ImageView coin = new ImageView(new Image(String.valueOf(getClass().getResource(coinPath))));
            coin.setPreserveRatio(true);
            coin.setFitWidth(38);
            DropShadow coinEffect = new DropShadow();
            coinEffect.setBlurType(BlurType.ONE_PASS_BOX);
            coin.setEffect(coinEffect);
            coinPane.getChildren().add(coin);
            coin.setDisable(true);

            coins[i].setFill(Paint.valueOf("WHITE"));
            coins[i].setFont(Font.font(String.valueOf(Font.getDefault()), FontWeight.EXTRA_BOLD, 14.0));
            coins[i].setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
            coins[i].setTextAlignment(TextAlignment.CENTER);
            coins[i].setDisable(true);
            coins[i].setX(coin.getX() + 16.0);
            coins[i].setY(coin.getY() + 25.0);
            coinPane.getChildren().add(coins[i]);

            playersWallet[i] = coinPane;
            playersWallet[i].setDisable(true);
            walletGridPane.add(playerNickname,0,i);
            walletGridPane.add(playersWallet[i],1,i);
        }
    }

    /* ISLAND HELPERS */

    /**
     * Sets as not visible and not clickable the islands that have been merged.
     */

    private void mergeIslands(){
        do {
            switch (numIslands) {
                case 12 -> disableIsland(island12);
                case 11 -> disableIsland(island11);
                case 10 -> disableIsland(island10);
                case 9 -> disableIsland(island9);
                case 8 -> disableIsland(island8);
                case 7 -> disableIsland(island7);
                case 6 -> disableIsland(island6);
                case 5 -> disableIsland(island5);
                case 4 -> disableIsland(island4);
                case 3 -> disableIsland(island3);
                case 2 -> disableIsland(island2);
                default -> throw new IllegalStateException("Unexpected value: " + numIslands);
            }
            numIslands--;
        }while(numIslands > game.getBoard().getIslands().getSize());
    }

    /**
     * Sets a specific island as not visible and not clickable by the user.
     *
     * @param island the island to set invisible.
     */

    private void disableIsland(GridPane island) {
        island.setVisible(false);
        island.setDisable(true);
        island.getStyleClass().remove("clickable");
        island.getChildren().forEach(node -> {
            node.setDisable(true);
            node.setVisible(false);
            node.getStyleClass().remove("clickable");
        });
    }

    /**
     * Helper: correctly displays Mother Nature onto the right island.
     *
     * @param island the island to put Mother Nature onto.
     */

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

    /**
     * Helper: correctly displays the number of students onto the given island.
     *
     * @param i an {@code int} representing an iterator.
     * @param j an {code int} representing an iterator.
     * @param currentIsland the given island.
     * @param color the considered color.
     */

    private void setStudentsOnIsland(int i, int j, Island currentIsland, Color color) {
        int num_students = currentIsland.getNumOfStudentsOfColor(color.toString());
        studentsOnIsland[i-1][j].setText(String.valueOf(num_students));
        studentsOnIsland[i-1][j].setFill(Paint.valueOf("WHITE"));
        studentsOnIsland[i-1][j].setFont(Font.font(String.valueOf(Font.getDefault()), FontWeight.EXTRA_BOLD, 12.0));
    }

}