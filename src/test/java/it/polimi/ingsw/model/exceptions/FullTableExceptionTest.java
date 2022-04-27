package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FullTableExceptionTest {

    @Test
    public void exceptionTest(){

        GameExpertMode g1 = new GameExpertMode(2);
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getPlayersNumber());
        g1.setCurrentPlayer(p1);

        try{
            p1.getSchool().getTable(Color.BLUE.toString()).addStudent(new Student(Color.BLUE), p1);
            p1.getSchool().getTable(Color.BLUE.toString()).addStudent(new Student(Color.BLUE), p1);
            p1.getSchool().getTable(Color.BLUE.toString()).addStudent(new Student(Color.BLUE), p1);
            p1.getSchool().getTable(Color.BLUE.toString()).addStudent(new Student(Color.BLUE), p1);
            p1.getSchool().getTable(Color.BLUE.toString()).addStudent(new Student(Color.BLUE), p1);
            p1.getSchool().getTable(Color.BLUE.toString()).addStudent(new Student(Color.BLUE), p1);
            p1.getSchool().getTable(Color.BLUE.toString()).addStudent(new Student(Color.BLUE), p1);
            p1.getSchool().getTable(Color.BLUE.toString()).addStudent(new Student(Color.BLUE), p1);
            p1.getSchool().getTable(Color.BLUE.toString()).addStudent(new Student(Color.BLUE), p1);
            p1.getSchool().getTable(Color.BLUE.toString()).addStudent(new Student(Color.BLUE), p1);

            assertThrows(FullTableException.class,
                    () -> p1.getSchool().getTable(Color.BLUE.toString()).addStudent(new Student(Color.BLUE), p1));
        }
        catch(NonExistentColorException | FullTableException e){}

    }

}