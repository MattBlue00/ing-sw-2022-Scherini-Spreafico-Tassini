package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.message.AssistantCardReply;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.PlayerNumberReply;
import it.polimi.ingsw.observers.Observer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameController implements Observer<Message>{

    // TODO: Error handling will be changed for each method when we will implement the network & the view
    // TODO: All methods in the controller need testing

    private Game game;

    private GameState gameState;
    private static final String INVALID_STATE = "Invalid game state";
    private static final String END_STATE = "The game has ended, the winner is: ";


    /*
        Initialize GameController
     */
    public void GameController(){
        setGameState(GameState.LOGIN);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void getMessage(Message receivedMessage){
        switch (gameState){
            case LOGIN:
                loginState(receivedMessage);
                break;
            case INIT:
                //TODO: methods to initialize a new game
                initGame();
                break;
            case IN_GAME:
                //TODO: methods to control the game flow
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
    private void loginState(Message receivedMessage){
        if(receivedMessage.getMessageType() == MessageType.PLAYER_NUMBER_REPLY){
            game.setPlayersNumber(((PlayerNumberReply) receivedMessage).getPlayerNumber());
        }
        else System.out.println("Error");
    }

    // To control when the game is initialized
    private void initGame(){
        setGameState(GameState.INIT);
    }

    // When all players can start playing
    private void startGame(){
        setGameState(GameState.IN_GAME);
    }

    private void planningPhase(){
        // TODO: receive from the view a message with the Assistant card the player wants to play
        //handleAssistantCardChoice();
        setOrder();
    }

    private void actionPhase(){}

    /*
        The method receive a message from a player, if it is an AssistantCard
        the String value (name) is assigned to chosenCard.
        If the chosen card hasn't already been played the player plays the chosenCard.
     */
    private void handleAssistantCardChoice(Message receivedMessage){
        if(receivedMessage.getMessageType() == MessageType.ASSISTANT_CARD_REPLY){
           String chosenCard = ((AssistantCardReply) receivedMessage).getCardName().toUpperCase();
           if(!isCardAlreadyPlayed(chosenCard))game.getCurrentPlayer().playAssistantCard(chosenCard);
           else System.out.println("Card has already been played");
        }
    }

    /*
        If the player's deck has only one card return false.
        Else if another player's lastCardPlayed cardName is equal to the cardName passed
        as parameter by the current player isCardAlreadyPlayed return true.
     */
    private boolean isCardAlreadyPlayed(String cardName){
        List<Player> players = game.getPlayers();
        if(game.getCurrentPlayer().getDeck().size() == 1)
            return false;
        for(Player player : players){
            if(player.getLastAssistantCardPlayed().getName().equals(cardName))
                return true;
        }
        return false;
    }

    /*
        The method order a copy of the players array based on the lastCardPlayed.getWeight attribute.
        In the end it set the new array in game.
        TODO: find a better way to order, the swap and order method may be set in the model and not in the controller.
     */
    private void setOrder(){
        List<Player> players = game.getPlayers();
        for(int i=0; i<game.getPlayersNumber();i++){
            if(players.get(i).getLastAssistantCardPlayed().getWeight() > players.get(i+1).getLastAssistantCardPlayed().getWeight()) {
                Collections.swap(players, i, i+1);
            }
        }
        game.setPlayers(players);
    }

    private void handleStudentChoice(Message receivedMessage){}

    private void handleMovement(Message receivedMessage){}

    private void handleMotherNature(Message receivedMessage){}

    private void handleCloudChoice(Message receivedMessage){}

    /*
        If one of the three game conditions is true the game is finished.
     */
    private boolean winCheck(){
        return noTowersLeftCheck() || isStudentBagEmpty() || lessThanThreeIslandsCheck();
    }

    /*
        The noTowersLeftCheck() method controls if the current player (who is ending the turn)
        has no towers left in the school, if it is true the method declares the winner and
        print the winning player.
     */
    private boolean noTowersLeftCheck(){
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
    private boolean isStudentBagEmpty(){
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
    private boolean lessThanThreeIslandsCheck(){
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
    private Player declareWinningPlayer(){
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

    // TODO: may need to separate the expert mode controller
    private void handleCharacterCardChoice(Message receivedMessage){}

    @Override
    public void update(Message message) {
        // TODO: implement update
    }

}
