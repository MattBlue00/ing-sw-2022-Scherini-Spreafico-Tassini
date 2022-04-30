package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Wizard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IslandNotFoundExceptionTest {

    @Test
    public void exceptionTest(){

        Game g1 = new Game(2);
        Player p1 = new Player(Wizard.BLUE.toString(), "Matteo", g1.getPlayersNumber());
        g1.addPlayer(p1);
        g1.setCurrentPlayer(p1);

        assertThrows(IslandNotFoundException.class,
                () -> g1.islandConquerCheck(0));

    }

}