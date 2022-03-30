package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.EmptyCloudException;
import it.polimi.ingsw.model.exceptions.NonExistentTableException;
import junit.framework.TestCase;
import org.junit.Test;

public class GameTest extends TestCase {

    //testing Game methods (without getter e setter)

    @Test
    public void testRefillCloudsWithTwoPlayers() {

        Game g1 = new Game(2);
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo");
        g1.setCurrentPlayer(p1);

        try {
            g1.takeStudentsFromCloud(0);
        } catch (EmptyCloudException e) {
            e.printStackTrace();
        }

        try {
            g1.refillClouds();
        } catch (EmptyBagException e) {
            e.printStackTrace();
        }

        assertEquals(3, g1.getBoard().getCloud(0).getStudents().size());
    }

    //TODO
    //waiting for the method to be implemented
    public void testPlayersPlayAssistantCard() {
    }

    //TODO
    //waiting for the support methods to be implemented
    @Test
    public void testPlayerMovesStudent() {

        Game g1 = new Game(2);
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo");
        g1.setCurrentPlayer(p1);

        g1.playerMovesStudent();

        //incomplete
    }

    @Test
    public void testProfCheck() throws NonExistentTableException {
        Game g1 = new Game(2);
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo");
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo");

        Student s1 = new Student(Color.YELLOW);
        Student s2 = new Student(Color.YELLOW);
        Student s3 = new Student(Color.GREEN);
        Student s4 = new Student(Color.GREEN);

        //TODO: review test
        //g1.addPlayer(p1);
        //g1.addPlayer(p2);

        //all hasProfessor flags are false (for now)

        //TODO: review test
        //s1.moveToTable(p1, "YELLOW");
        //s2.moveToTable(p1, "YELLOW");
        //s3.moveToTable(p2, "GREEN");
        //s4.moveToTable(p2, "GREEN");

        //p1 has 2 yellow students and should have yellow professor
        //p2 has 2 yellow students and should have green professor

        g1.profCheck();

        assertEquals(true, p1.getSchool().getTable("YELLOW").getHasProfessor());
        assertEquals(false, p1.getSchool().getTable("GREEN").getHasProfessor());
        assertEquals(false, p2.getSchool().getTable("YELLOW").getHasProfessor());
        assertEquals(true, p2.getSchool().getTable("GREEN").getHasProfessor());

        //TODO: review test
        //s3.moveToTable(p1, "GREEN");
        //s4.moveToTable(p1, "GREEN");

        //p1 has 2 yellow students and should have yellow professor
        //p1 has 2 green students and should NOT have green professor
        //p2 has 2 green students and should have green professor

        g1.profCheck();

        assertEquals(true, p1.getSchool().getTable("YELLOW").getHasProfessor());
        assertEquals(false, p1.getSchool().getTable("GREEN").getHasProfessor());
        assertEquals(false, p2.getSchool().getTable("YELLOW").getHasProfessor());
        assertEquals(true, p2.getSchool().getTable("GREEN").getHasProfessor());

        //TODO: review test
        //s3.moveToTable(p1, "GREEN");

        //p1 has 2 yellow students and should have yellow professor
        //p1 has 3 green students and should have green professor
        //p2 has 2 green students and should not have green professor

        g1.profCheck();

        assertEquals(true, p1.getSchool().getTable("YELLOW").getHasProfessor());
        assertEquals(true, p1.getSchool().getTable("GREEN").getHasProfessor());
        assertEquals(false, p2.getSchool().getTable("YELLOW").getHasProfessor());
        assertEquals(false, p2.getSchool().getTable("GREEN").getHasProfessor());

    }

    //TODO
    //waiting for the method to be implemented
    @Test
    public void testPlayerPlaysCharacterCard() {
    }

    //TODO
    //waiting for the method to be implemented
    @Test
    public void testMoveMothernature() {
    }

    //TODO
    //waiting for the method to be implemented
    @Test
    public void testIslandConquerCheck() {
    }

    //TODO
    //waiting for the method to be implemented
    @Test
    public void testMergeCheck() {
    }

    @Test
    public void testTakeStudentsFromCloudWithTwoPlayers() {
        Game g1 = new Game(2);
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo");
        g1.setCurrentPlayer(p1);

        try {
            g1.takeStudentsFromCloud(0);
        } catch (EmptyCloudException e) {
            e.printStackTrace();
        }

        assertEquals(0, g1.getBoard().getCloud(0).getStudents().size());
    }
}