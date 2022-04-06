package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.network.message.PlayerNumberReply;
import it.polimi.ingsw.observers.Observer;

public class GameController implements Observer<Message>{
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
        else System.out.println("Error"); //TODO: change when view is implemented
    }

    // To control when the game is initialized
    private void initGame(){
        setGameState(GameState.INIT);
    }

    // When all players can start playing
    private void startGame(){
        setGameState(GameState.IN_GAME);
    }

    @Override
    public void update(Message message) {
        // TODO: implement update
    }

}
