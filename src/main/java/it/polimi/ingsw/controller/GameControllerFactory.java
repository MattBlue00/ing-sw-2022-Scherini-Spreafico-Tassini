package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.message.CreateGameMessage;
import it.polimi.ingsw.network.message.Message;

public class GameControllerFactory {

    public GameControllerFactory(){}

    public GameController getGameController(Message receivedMessage){
        if(((CreateGameMessage) receivedMessage).isExpertMode())
            return new GameControllerExpertMode();
        else
            return new GameController();
    }
}

