package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NonExistentTableException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TableTest{

    @Test
    public void testAddStudent() throws NonExistentTableException {

        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo");

        p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
        p1.getSchool().getTable("GREEN").addStudent(new Student(Color.GREEN), p1);
        p1.getSchool().getTable("PINK").addStudent(new Student(Color.PINK), p1);

        assertEquals(1, p1.getCoinsWallet());

        p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);
        p1.getSchool().getTable("YELLOW").addStudent(new Student(Color.YELLOW), p1);

        assertEquals(2, p1.getCoinsWallet());

    }

    @Test
    public void testCoinCheck() throws NonExistentTableException {

        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo");

        //we should have zero coins
        assertEquals(0, p1.getSchool().getTable("YELLOW").coinCheck());

        p1.getSchool().getTable("YELLOW").addStudentForDebug(new Student(Color.YELLOW));
        p1.getSchool().getTable("YELLOW").addStudentForDebug(new Student(Color.YELLOW));
        p1.getSchool().getTable("YELLOW").addStudentForDebug(new Student(Color.YELLOW));

        //we should have one coin
        assertEquals(1, p1.getSchool().getTable("YELLOW").coinCheck());

        p1.getSchool().getTable("PINK").addStudentForDebug(new Student(Color.PINK));

        //we should have 0 coins
        assertEquals(0, p1.getSchool().getTable("PINK").coinCheck());
    }
}