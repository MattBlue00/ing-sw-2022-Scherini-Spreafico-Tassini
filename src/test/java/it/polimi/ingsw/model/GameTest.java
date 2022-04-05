package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.*;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Optional;

public class GameTest extends TestCase {

    // Testing Game methods (getters and setters are excluded)

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

        /*
            This test verifies that:
            - at round 1, whoever gets a student of a particular first, automatically gets the professor
            - later on, if another player ties the number of students of that same color, that other player will
              NOT get the professor
            - later on, if that player eventually surpasses the opponent's number of students of the same color,
              steals the professor
         */

        Game g1 = new Game(2);
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo");
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo");

        Student s1 = new Student(Color.YELLOW);
        Student s2 = new Student(Color.YELLOW);
        Student s3 = new Student(Color.GREEN);
        Student s4 = new Student(Color.GREEN);

        g1.addPlayer(p1);
        g1.addPlayer(p2);

        // All hasProfessor flags are false, so far

        s1.moveToTable(p1);
        s2.moveToTable(p1);
        s3.moveToTable(p2);
        s4.moveToTable(p2);

        // p1 has 2 yellow students and should have yellow professor
        // p2 has 2 yellow students and should have green professor

        g1.profCheck();

        assertTrue(p1.getSchool().getTable("YELLOW").getHasProfessor());
        assertFalse(p1.getSchool().getTable("GREEN").getHasProfessor());
        assertFalse(p2.getSchool().getTable("YELLOW").getHasProfessor());
        assertTrue(p2.getSchool().getTable("GREEN").getHasProfessor());

        Student s5 = new Student(Color.GREEN);
        Student s6 = new Student(Color.GREEN);

        s5.moveToTable(p1);
        s6.moveToTable(p1);

        // p1 has 2 yellow students and should have yellow professor
        // p1 has 2 green students and should NOT have green professor
        // p2 has 2 green students and should have green professor

        g1.profCheck();

        assertTrue(p1.getSchool().getTable("YELLOW").getHasProfessor());
        assertFalse(p1.getSchool().getTable("GREEN").getHasProfessor());
        assertFalse(p2.getSchool().getTable("YELLOW").getHasProfessor());
        assertTrue(p2.getSchool().getTable("GREEN").getHasProfessor());

        Student s7 = new Student(Color.GREEN);

        s7.moveToTable(p1);

        // p1 has 2 yellow students and should have yellow professor
        // p1 has 3 green students and should have green professor
        // p2 has 2 green students and should NOT have green professor

        g1.profCheck();

        assertTrue(p1.getSchool().getTable("YELLOW").getHasProfessor());
        assertTrue(p1.getSchool().getTable("GREEN").getHasProfessor());
        assertFalse(p2.getSchool().getTable("YELLOW").getHasProfessor());
        assertFalse(p2.getSchool().getTable("GREEN").getHasProfessor());

    }

    @Test
    public void testMoveMotherNature() {

        /*
            This test verifies that Mother Nature moves correctly from a position to another.
         */

        Game g1 = new Game(2);
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo");

        g1.getBoard().setMotherNaturePos(1);
        g1.setCurrentPlayer(p1);
        p1.setLastAssistantCardPlayed(new AssistantCard(7, 7, 4));

        try{
            g1.moveMotherNature(3); // from island 1 to 4
        }
        catch(InvalidNumberOfStepsException e){
            e.printStackTrace();
        }

        assertEquals(4, g1.getBoard().getMotherNaturePos());

        p1.setLastAssistantCardPlayed(new AssistantCard(10, 10, 5));

        try{
            g1.moveMotherNature(5); // from island 4 to 9
        }
        catch(InvalidNumberOfStepsException e){
            e.printStackTrace();
        }

        assertEquals(9, g1.getBoard().getMotherNaturePos());

        p1.setLastAssistantCardPlayed(new AssistantCard(9, 9, 5));

        try{
            g1.moveMotherNature(1); // from island 9 to 10
        }
        catch(InvalidNumberOfStepsException e){
            e.printStackTrace();
        }

        assertEquals(10, g1.getBoard().getMotherNaturePos());

        p1.setLastAssistantCardPlayed(new AssistantCard(8, 8, 4));

        try{
            g1.moveMotherNature(3); // from island 10 to 1
        }
        catch(InvalidNumberOfStepsException e){
            e.printStackTrace();
        }

        assertEquals(1, g1.getBoard().getMotherNaturePos());

        p1.setLastAssistantCardPlayed(new AssistantCard(2, 2, 1));

        try{
            g1.moveMotherNature(1); // from island 1 to 2
        }
        catch(InvalidNumberOfStepsException e){
            e.printStackTrace();
        }

        assertEquals(2, g1.getBoard().getMotherNaturePos());

    }

    //TODO
    //waiting for the method to be implemented
    @Test
    public void testIslandConquerCheck() {

        /*
            This test verifies that:
            1. if a player arrives in an island in which has 0 influence points, the island, even if
              unconquered, does not change its status
            2. once a player arrives in an island in which has at least 1 influence point, it becomes his
         */

        Game g1 = new Game(2);
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo");
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo");

        g1.addPlayer(p1);
        g1.addPlayer(p2);
        g1.setCurrentPlayer(p1);

        Student s1 = new Student(Color.BLUE);
        Student s2 = new Student(Color.GREEN);

        s1.moveToTable(p1);
        g1.profCheck();

        // Test 1

        // TODO: this test fails, fixes are needed

        try {
            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s2);
            g1.islandConquerCheck(1);
            assertEquals(Optional.empty(), g1.getBoard().getIslands().getIslandFromID(1).getOwner());
        }
        catch(InvalidIslandException e){
            e.printStackTrace();
        }

        Student s3 = new Student(Color.BLUE);

        g1.profCheck();

        // Test 2

        /*try {
            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s3);
            g1.getBoard().getIslands().getIslandFromID(1).influenceCalc(p1);
            assertEquals(Optional.of(p1), g1.getBoard().getIslands().getIslandFromID(1).getOwner());
        }
        catch(InvalidIslandException e){
            e.printStackTrace();
        }*/

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

        assertEquals(0, g1.getBoard().getCloud(0).getStudents().size());     // the cloud should be empty
        assertEquals(3, p1.getSchool().getHall().getStudents().size());         // the hall should have 3 more students

    }
}