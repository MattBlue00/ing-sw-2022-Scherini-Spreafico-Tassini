package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.message.GameModeReply;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;

public class GameControllerFactory {

    public GameControllerFactory(){}

    public GameController getGameController(Message receivedMessage){
        if(((GameModeReply) receivedMessage).isGameExpertMode())
            return new GameControllerExpertMode();
        else
            return new GameController();
    }
}

