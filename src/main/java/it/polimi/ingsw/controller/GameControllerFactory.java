package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.message.CreateGameMessage;
import it.polimi.ingsw.network.message.Message;

/**
 * This class acts as a factory method for the game controller construction (it can be {@link GameController} or
 * {@link GameControllerExpertMode}).
 */

public class GameControllerFactory {

    /**
     * Game controller's factory method constructor.
     */

    public GameControllerFactory(){}

    /**
     * Returns the correct instance of the game controller according to the parameter sent by the client (encapsulated
     * in the {@link CreateGameMessage} message).
     *
     * @param receivedMessage the message sent by the client.
     * @return the correct instance of the game controller.
     */

    public GameController getGameController(Message receivedMessage){
        if(((CreateGameMessage) receivedMessage).isExpertMode())
            return new GameControllerExpertMode();
        else
            return new GameController();
    }
}

