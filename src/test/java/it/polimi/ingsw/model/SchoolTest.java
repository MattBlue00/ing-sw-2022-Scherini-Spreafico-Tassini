package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.FullTableException;
import it.polimi.ingsw.model.exceptions.NonExistentColorException;
import it.polimi.ingsw.model.exceptions.StudentNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SchoolTest{

    //TODO: support methods need to be implemented
    @Test
    public void testMoveStudentToIsland() {
    }

    @Test
    public void testMoveStudentToTable() throws NonExistentColorException {

        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo");
        Student s1 = new Student(Color.YELLOW);
        Student s2 = new Student(Color.BLUE);

        s1.moveToHall(p1);
        s2.moveToHall(p1);

        try {
            p1.getSchool().moveStudentToTable(p1, "YELLOW");
            p1.getSchool().moveStudentToTable(p1, "BLUE");
        }
        catch(StudentNotFoundException | NonExistentColorException | FullTableException e){}

        assertTrue(p1.getSchool().getTable("YELLOW").getStudents().contains(s1));
        assertTrue(p1.getSchool().getTable("BLUE").getStudents().contains(s2));
    }
}