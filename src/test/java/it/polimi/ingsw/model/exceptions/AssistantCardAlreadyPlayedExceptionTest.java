package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Wizard;
import it.polimi.ingsw.network.message.PlayerNumberReply;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssistantCardAlreadyPlayedExceptionTest {

    @Test
    public void exceptionTest() throws WrongMessageSentException {

        GameController gc = new GameController();
        PlayerNumberReply message = new PlayerNumberReply("Matteo", 2);
        gc.prepareGame(message);

        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", gc.getGame().getPlayersNumber());
        Player p2 = new Player(Wizard.PINK_WIZARD, "Ludo", gc.getGame().getPlayersNumber());
        gc.getGame().addPlayer(p1);
        gc.getGame().addPlayer(p2);
        gc.getGame().setCurrentPlayer(p2);

        // TODO: the method to test is not ready

    }

}