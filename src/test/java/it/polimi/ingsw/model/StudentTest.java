package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest{

    @Test
    public void testMoveToHall() {

        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", new Constants(2));
        Student s1 = new Student(Color.YELLOW);
        Student s2 = new Student(Color.BLUE);

        s1.moveToHall(p1);
        s2.moveToHall(p1);

        assertTrue(p1.getSchool().getHall().getStudents().contains(s1));
        assertTrue(p1.getSchool().getHall().getStudents().contains(s2));

    }

    @Test
    public void testMoveToIsland() {

        Student s1 = new Student(Color.YELLOW);
        Student s2 = new Student(Color.BLUE);
        Island i1 = new Island(1);

        s1.moveToIsland(i1);
        s2.moveToIsland(i1);

        assertTrue(i1.getStudents().contains(s1));
        assertTrue(i1.getStudents().contains(s2));

    }

    @Test
    public void testMoveToTable() throws NonExistentColorException {

        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", new Constants(2));
        Student s1 = new Student(Color.YELLOW);
        Student s2 = new Student(Color.BLUE);

        try {
            s1.moveToTable(p1);
            s2.moveToTable(p1);
        }
        catch(NonExistentColorException | FullTableException ignored){}

        assertTrue(p1.getSchool().getTable("YELLOW").getStudents().contains(s1));
        assertTrue(p1.getSchool().getTable("BLUE").getStudents().contains(s2));

    }
}