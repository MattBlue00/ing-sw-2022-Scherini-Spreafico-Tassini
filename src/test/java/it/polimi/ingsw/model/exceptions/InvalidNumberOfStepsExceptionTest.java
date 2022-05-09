package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.exceptions.InvalidNumberOfStepsException;
import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidNumberOfStepsExceptionTest {

    @Test
    public void exceptionTest(){

        Game g1 = new Game(2);
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getPlayersNumber());
        g1.addPlayer(p1);
        g1.setCurrentPlayer(p1);
        p1.setLastAssistantCardPlayed(new AssistantCard(AssistantType.CAT));

        assertThrows(InvalidNumberOfStepsException.class,
                () -> g1.moveMotherNature(3));

    }

}