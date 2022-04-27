package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Wizard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentNotFoundExceptionTest {

    @Test
    public void exceptionTest(){

        Game g1 = new Game(2);
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getPlayersNumber());
        g1.addPlayer(p1);
        g1.setCurrentPlayer(p1);

        assertThrows(StudentNotFoundException.class,
                () -> p1.getSchool().getTable(Color.BLUE.toString()).removeStudent());

        assertThrows(StudentNotFoundException.class,
                () -> p1.getSchool().getHall().removeStudent(Color.BLUE.toString()));

    }

}