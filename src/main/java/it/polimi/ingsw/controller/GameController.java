package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.PlayerNumberReply;
import it.polimi.ingsw.observers.Observer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GameController implements Observer<Message>{

    //TODO: Error handling will be changed for each method when we will implement the network & the view

    private Game game;

    private GameState gameState;
    private static final String INVALID_STATE = "Invalid game state";

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

    private void planningPhase(){}

    private void actionPhase(){}

    private void handleAssistantCardChoice(Message receivedMessage){}

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

    private void winCheck(){}

    // TODO: may need to separate the expert mode controller
    private void handleCharacterCardChoice(Message receivedMessage){}

    @Override
    public void update(Message message) {
        // TODO: implement update
    }

}
