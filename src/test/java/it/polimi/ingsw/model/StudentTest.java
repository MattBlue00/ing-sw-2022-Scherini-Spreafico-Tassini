package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NonExistentTableException;
import junit.framework.TestCase;
import org.junit.Test;

public class StudentTest extends TestCase {

    @Test
    public void testMoveToHall() {

        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo");
        Student s1 = new Student(Color.YELLOW);
        Student s2 = new Student(Color.BLUE);

        s1.moveToHall(p1);
        s2.moveToHall(p1);

        assertEquals(true, p1.getSchool().getHall().getStudents().contains(s1));
        assertEquals(true, p1.getSchool().getHall().getStudents().contains(s2));

    }

    //TODO
    //waiting for the method to be implemented
    @Test
    public void testMoveToIsland() {
    }

    @Test
    public void testMoveToTable() throws NonExistentTableException {

        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo");
        Student s1 = new Student(Color.YELLOW);
        Student s2 = new Student(Color.BLUE);

        s1.moveToTable(p1, "YELLOW");
        s2.moveToTable(p1, "BLUE");

        assertEquals(true, p1.getSchool().getTable("YELLOW").getStudents().contains(s1));
        assertEquals(true, p1.getSchool().getTable("BLUE").getStudents().contains(s2));

    }
}