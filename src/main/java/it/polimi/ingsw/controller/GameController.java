package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exceptions.AssistantCardAlreadyPlayedException;
import it.polimi.ingsw.model.exceptions.EmptyCloudException;
import it.polimi.ingsw.model.exceptions.InvalidNumberOfStepsException;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.observers.Observer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController implements Observer<Message>{

    // TODO: Error handling will be changed for each method when we will implement the network & the view
    // TODO: All methods in the controller need testing

    private Game game;
    private boolean planningPhaseDone;
    private boolean playerActionPhaseDone;
    private int currentPlayerIndex;
    private int movesLeft;
    private boolean motherNatureMoved;

    private GameState gameState;
    private static final String INVALID_STATE = "Invalid game state";
    private static final String END_STATE = "The game has ended, the winner is: ";

    /*
        Initialize GameController
     */
    public GameController(){
        if(this instanceof GameControllerExpertMode)
            this.game = new GameExpertMode(2); // PLACEHOLDER
        else
            this.game = new Game(2); // PLACEHOLDER
        this.planningPhaseDone = false;
        this.playerActionPhaseDone = false;
        this.currentPlayerIndex = 0;
        this.movesLeft = 3;
        this.motherNatureMoved = false;
    }

    // Getter and Setter methods

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    public void setMovesLeft(int movesLeft) {
        this.movesLeft = movesLeft;
    }

    public boolean getMotherNatureMoved() {
        return motherNatureMoved;
    }

    public void setMotherNatureMoved(boolean motherNatureMoved) {
        this.motherNatureMoved = motherNatureMoved;
    }

    public boolean getPlanningPhaseDone() {
        return planningPhaseDone;
    }

    public void setPlanningPhaseDone(boolean planningPhaseDone) {
        this.planningPhaseDone = planningPhaseDone;
    }

    public boolean getPlayerActionPhaseDone() {
        return playerActionPhaseDone;
    }

    public void setPlayerActionPhaseDone(boolean playerActionPhaseDone) {
        this.playerActionPhaseDone = playerActionPhaseDone;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    // GameController methods

    public void getMessage(Message receivedMessage){
        // TODO: receive a message from the view
        switch (gameState){
            case LOGIN:
                loginState(receivedMessage);
                break;
            case INIT:
                //TODO: methods to initialize a new game
                initGame();
                startGame();
                break;
            case IN_GAME:
                if(!planningPhaseDone) {
                    planningPhase(receivedMessage);
                    currentPlayerIndex++;
                    if(currentPlayerIndex == game.getPlayersNumber()) {
                        endPlanningPhase();
                    }
                    else
                        game.setCurrentPlayer(game.getPlayers().get(currentPlayerIndex));
                }
                else {
                    actionPhase(receivedMessage);
                    if(playerActionPhaseDone){
                        currentPlayerIndex++;
                        if(currentPlayerIndex == game.getPlayersNumber())
                            nextRound();
                        else {
                            nextPlayerActionPhase();
                        }
                    }
                }
                break;
            default:
                System.out.println(INVALID_STATE);
                break;
        }
    }

    /*
        The received message contains the number of players.
        Then set the playersNumber in the Game class.
     */

    public void loginState(Message receivedMessage){
        if(receivedMessage.getMessageType() == MessageType.PLAYER_NUMBER_REPLY){
            game.setPlayersNumber(((PlayerNumberReply) receivedMessage).getPlayerNumber());
        }
        else System.out.println("Error");
    }

    // To control when the game is initialized
    public void initGame(){
        setGameState(GameState.INIT);
    }

    // When all players can start playing
    public void startGame(){
        setGameState(GameState.IN_GAME);
    }

    /*
        This method allows the current player to play his planning phase properly.
     */

    public void planningPhase(Message message){

        try{
            if (message.getMessageType() == MessageType.ASSISTANT_CARD_REPLY)
                handleAssistantCardChoice(message);
            else
                System.out.println("Wrong message sent.");
        }
        catch(AssistantCardAlreadyPlayedException e){
            e.printStackTrace();
        }
    }

    /*
        This method establishes the right flow of a player's action phase.
     */

    public void actionPhase(Message message){

        try{
            switch(message.getMessageType()){
                case MOVE_TO_TABLE_REPLY:
                case MOVE_TO_ISLAND_REPLY:
                    if(movesLeft == 0){
                        System.out.println("No moves left!");
                    }
                    else{
                        handleStudentMovement(message);
                        movesLeft--;
                    }
                    break;
                case MOTHER_NATURE_STEPS_REPLY:
                    if(movesLeft == 0 && !motherNatureMoved){
                        handleMotherNature(message);
                        motherNatureMoved = true;
                    }
                    else
                        System.out.println("You need to move other " + movesLeft +
                                " students before moving Mother Nature!");
                    break;
                case CLOUD_CHOICE_REPLY:
                    if(motherNatureMoved) {
                        handleCloudChoice(message);
                        playerActionPhaseDone = true;
                    }
                    else
                        System.out.println("You need to move mother nature first!");
                    break;
                default:
                    System.out.println("Wrong message sent.");
                    break;
            }
        }
        catch(InvalidNumberOfStepsException | EmptyCloudException e){
            e.printStackTrace();
        }

    }

    /*
       This method sets the class's attributes and the players' order so that the first player of the action phase
       can play properly.
    */

    public void endPlanningPhase(){
        planningPhaseDone = true;
        setOrder();
        currentPlayerIndex = 0;
    }

    /*
        This method sets the class's attributes in order to let the next player play his action phase properly.
     */

    public void nextPlayerActionPhase(){
        game.setCurrentPlayer(game.getPlayers().get(currentPlayerIndex));
        movesLeft = 3;
        playerActionPhaseDone = false;
        motherNatureMoved = false;
    }

    /*
        This method sets the class's attributes in order to play the next round properly.
     */

    public void nextRound(){
        currentPlayerIndex = 0;
        game.setCurrentPlayer(game.getPlayers().get(currentPlayerIndex));
        movesLeft = 3;
        planningPhaseDone = false;
        playerActionPhaseDone = false;
        motherNatureMoved = false;
        game.setRoundNumber(game.getRoundNumber() + 1);
        for(Player player : game.getPlayers())
            player.resetLastAssistantCardPlayed();
    }

    /*
        The method receive a message from a player, if it is an AssistantCard
        the String value (name) is assigned to chosenCard.
        If the chosen card hasn't already been played the player plays the chosenCard.
     */

    public void handleAssistantCardChoice(Message receivedMessage) throws AssistantCardAlreadyPlayedException{
       String chosenCard = ((AssistantCardReply) receivedMessage).getCardName().toUpperCase();
       if(isAssistantCardPlayable(chosenCard))
           game.getCurrentPlayer().playAssistantCard(chosenCard);
       else
           throw new AssistantCardAlreadyPlayedException("You have already played this assistant card!");
    }

    /*
        This method decides whether a card is playable by a player.
     */

    public boolean isAssistantCardPlayable(String cardName){
        List<Player> players = game.getPlayers();
        List<AssistantCard> cardsPlayed = new ArrayList<>(players.size());
        for(Player player : players){
            if(player.getLastAssistantCardPlayed().isPresent())
                cardsPlayed.add(player.getLastAssistantCardPlayed().get());
        }
        for(AssistantCard card : cardsPlayed){
            if(card.getName().equals(cardName)){
                List<AssistantCard> playerCards = game.getCurrentPlayer().getDeck();
                playerCards.remove(card);
                for(AssistantCard playerCard : playerCards) {
                    for (AssistantCard cardToCheck : cardsPlayed) {
                        if (!playerCard.getName().equals(cardToCheck.getName()))
                            return false;
                    }
                }
            }
        }
        return true;
    }

    /*
        The method order a copy of the players array based on the lastCardPlayed.getWeight attribute.
        In the end it set the new array in game.
        TODO: find a better way to order, the swap and order method may be set in the model and not in the controller.
     */

    public void setOrder(){
        List<Player> players = game.getPlayers();
        for(int i=0; i<game.getPlayersNumber()-1;i++){
            if(players.get(i).getLastAssistantCardPlayed().get().getWeight() >
                    players.get(i+1).getLastAssistantCardPlayed().get().getWeight()) {
                Collections.swap(players, i, i+1);
            }
        }
        game.setPlayers(players);
    }

    /*
        The method receive a message with the color of the student we want to move from
        the player's Hall to an Island or a Table
     */

    public void handleStudentMovement(Message receivedMessage){
        if(receivedMessage.getMessageType() == MessageType.MOVE_TO_TABLE_REPLY){
            String color = ((MoveToTableReply) receivedMessage).getColor();
            game.playerMovesStudent(color);
        }
        if(receivedMessage.getMessageType() == MessageType.MOVE_TO_ISLAND_REPLY){
            String color = ((MoveToIslandReply) receivedMessage).getColor();
            int islandID = ((MoveToIslandReply) receivedMessage).getIslandID();
            game.playerMovesStudent(color, islandID);
        }
    }

    /*
        The method receive a message with the number of steps chosen by the player.
        Mother nature will move.
     */

    public void handleMotherNature(Message receivedMessage) throws InvalidNumberOfStepsException{
        game.moveMotherNature(((MotherNatureStepsReply) receivedMessage).getSteps());
    }

    /*
        The method handles the cloud choice at the end of the action phase.
     */

    public void handleCloudChoice(Message receivedMessage) throws EmptyCloudException{
        game.takeStudentsFromCloud(((CloudChoiceReply) receivedMessage).getCloudID());
    }

    /*
        If one of the three game conditions is true, the game ends.
     */

    // TODO: need to discuss where to check, how to declare the winner and how to stop the game
    public void winCheck(){
        if(noTowersLeftCheck() || isStudentBagEmpty() || lessThanThreeIslandsCheck())
            declareWinningPlayer();
    }

    /*
        The noTowersLeftCheck() method controls if the current player (who is ending the turn)
        has no towers left in the school, if it is true the method declares the winner and
        print the winning player.
     */

    public boolean noTowersLeftCheck(){
        if(game.getCurrentPlayer().getSchool().getTowerRoom().getTowersLeft() == 0) {
            System.out.println(END_STATE+game.getCurrentPlayer());
            return true;
        }
        return false;
    }

    /*
        The isStudentBagEmpty() method controls if the students bag in game is empty.
        If it's true it calls declareWinningPlayer() and prints the winning player's name.
     */

    public boolean isStudentBagEmpty(){
        if(game.getBoard().getStudentsBag().size() == 0){
            Player winningPlayer = declareWinningPlayer();
            System.out.println(END_STATE+winningPlayer);
            return true;
        }
        return false;
    }

    /*
         The lessThanThreeIslandsCheck() method controls if the DoublyLinkedList of Islands
         in game contains three or fewer islands.
         If it is true it calls declareWinningPlayer() and prints the winning player's name.
    */

    public boolean lessThanThreeIslandsCheck(){
        if(game.getBoard().getIslands().getSize() <= 3){
            Player winningPlayer = declareWinningPlayer();
            System.out.println(END_STATE+winningPlayer);
            return true;
        }
        return false;
    }

    /*
        This method checks which player has the min number of towers in their
        TowerRoom.
        The method returns the winningPlayer.
     */

    public Player declareWinningPlayer(){
        List<Player> players = game.getPlayers();
        Player winningPlayer = players.get(0);
        int minTowers = players.get(0).getSchool().getTowerRoom().getTowersLeft();
        for(int i=1; i<players.size(); i++){
            if(players.get(i).getSchool().getTowerRoom().getTowersLeft() < minTowers) {
                winningPlayer = players.get(i);
                minTowers = winningPlayer.getSchool().getTowerRoom().getTowersLeft();
            }
        }
        return winningPlayer;
    }

    @Override
    public void update(Message message) {
        // TODO: implement update
    }

}
