package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.FullTableException;
import it.polimi.ingsw.model.exceptions.NonExistentColorException;
import it.polimi.ingsw.model.exceptions.StudentNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TableTest{

    @Test
    public void testAddStudent() throws NonExistentColorException {

        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", 2);

        try {
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
            p1.getSchool().getTable("GREEN").addStudent(new Student(Color.GREEN), p1);
            p1.getSchool().getTable("PINK").addStudent(new Student(Color.PINK), p1);
        }
        catch(FullTableException ignored){}

        assertEquals(1, p1.getCoinsWallet());

        try {
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
        }
        catch(FullTableException ignored){}

        assertEquals(2, p1.getCoinsWallet());

    }

    @Test
    // Throws FullTableException
    public void addStudentToAFullTableTest(){

        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", 2);
        try {
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
        } catch(FullTableException | NonExistentColorException ignored){};

        assertThrows(FullTableException.class, ()->{p1.getSchool().getTable("YELLOW").
                addStudent(new Student(Color.YELLOW), p1);} );
    }

    @Test
    public void testCoinCheck() throws NonExistentColorException {

        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", 2);

        //we should start with one coin
        assertEquals(1, p1.getCoinsWallet());

        try {
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
        }
        catch(FullTableException ignored){}

        //one coin should be added
        assertEquals(2, p1.getCoinsWallet());

        try {
            p1.getSchool().getTable("PINK").addStudent(new Student(Color.PINK), p1);
        }
        catch(FullTableException ignored){}

        //no coin should be added
        assertEquals(2, p1.getCoinsWallet());

        try {
            p1.getSchool().getTable("YELLOW").removeStudent();
            p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
        }
        catch(FullTableException | StudentNotFoundException ignored){}

        //no coin should be added
        assertEquals(2, p1.getCoinsWallet());

    }
}