package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.message.GameModeMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerFactoryTest {

    @Test
    void getGameController() {
        GameControllerFactory gameControllerFactory = new GameControllerFactory();
        GameModeMessage message = new GameModeMessage("Ludo", false);

        GameController gameController = gameControllerFactory.getGameController(message);
        assertEquals(true, gameController instanceof GameController);
        assertEquals(false, gameController instanceof GameControllerExpertMode);

        GameControllerFactory gameControllerFactory_2 = new GameControllerFactory();
        GameModeMessage message_2 = new GameModeMessage("Ludo", true);

        GameController gameController_2 = gameControllerFactory_2.getGameController(message_2);
        assertEquals(true, gameController_2 instanceof GameControllerExpertMode);
    }
}