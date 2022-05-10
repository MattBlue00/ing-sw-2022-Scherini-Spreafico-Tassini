package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FullTableExceptionTest {

    @Test
    public void exceptionTest(){

        GameExpertMode g1 = new GameExpertMode(2, new Constants(2));
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getConstants());
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
        catch(NonExistentColorException | FullTableException ignored){}

    }

}