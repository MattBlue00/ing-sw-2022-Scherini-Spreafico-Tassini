package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.message.GameModeMessage;
import it.polimi.ingsw.network.message.Message;

public class GameControllerFactory {

    public GameControllerFactory(){}

    public GameController getGameController(Message receivedMessage){
        if(((GameModeMessage) receivedMessage).isGameExpertMode())
            return new GameControllerExpertMode();
        else
            return new GameController();
    }
}

