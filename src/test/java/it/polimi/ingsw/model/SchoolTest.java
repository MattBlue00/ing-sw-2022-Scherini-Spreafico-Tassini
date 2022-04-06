package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NonExistentTableException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SchoolTest{

    //TODO: support methods need to be implemented
    @Test
    public void testMoveStudentToIsland() {
    }

    @Test
    public void testMoveStudentToTable() throws NonExistentTableException {

        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo");
        Student s1 = new Student(Color.YELLOW);
        Student s2 = new Student(Color.BLUE);

        s1.moveToHall(p1);
        s2.moveToHall(p1);

        p1.getSchool().moveStudentToTable(p1,"YELLOW");
        p1.getSchool().moveStudentToTable(p1, "BLUE");

        assertEquals(true, p1.getSchool().getTable("YELLOW").getStudents().contains(s1));
        assertEquals(true, p1.getSchool().getTable("BLUE").getStudents().contains(s2));
    }
}