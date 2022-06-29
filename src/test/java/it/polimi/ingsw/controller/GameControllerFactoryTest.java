package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.message.CreateGameMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerFactoryTest {

    @Test
    void getGameController() {
        GameControllerFactory gameControllerFactory = new GameControllerFactory();
        CreateGameMessage message = new CreateGameMessage("Ludo", 1, 2, false);

        GameController gameController = gameControllerFactory.getGameController(message);
        assertNotNull(gameController);
        assertFalse(gameController instanceof GameControllerExpertMode);

        GameControllerFactory gameControllerFactory_2 = new GameControllerFactory();
        CreateGameMessage message_2 = new CreateGameMessage("Ludo", 1, 3, true);

        GameController gameController_2 = gameControllerFactory_2.getGameController(message_2);
        assertTrue(gameController_2 instanceof GameControllerExpertMode);
    }

}