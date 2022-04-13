package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Wizard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssistantCardAlreadyPlayedExceptionTest {

    @Test
    public void exceptionTest(){

        GameController gc = new GameController();
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo");
        Player p2 = new Player(Wizard.PINK_WIZARD, "Ludo");
        gc.getGame().addPlayer(p1);
        gc.getGame().addPlayer(p2);
        gc.getGame().setCurrentPlayer(p2);

        // TODO: the method to test is not ready

    }

}