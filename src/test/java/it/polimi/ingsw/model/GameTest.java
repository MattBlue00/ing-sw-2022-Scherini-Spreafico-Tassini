package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameTest{

    // Testing Game methods (getters and setters are excluded)

    @Test
    public void testRefillCloudsWithTwoPlayers() {

        Game g1 = new Game(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        g1.setCurrentPlayer(p1);

        try {
            g1.takeStudentsFromCloud(0);
        } catch (EmptyCloudException e) {
            e.printStackTrace();
        }

        try {
            g1.refillClouds();
        } catch (EmptyBagException ignored){}

        assertEquals(3, g1.getBoard().getCloud(0).getStudents().size());
    }


    @Test
    public void playerMovesStudentToTable() throws NonExistentColorException {
        // TODO: test playerMovesStudent (for islands and for tables)
        Game g1 = new Game(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Student s1 = new Student(Color.YELLOW);

        g1.setCurrentPlayer(p1);
        p1.getSchool().getHall().addStudent(s1);
        try {
            g1.playerMovesStudent("YELLOW");
        }catch(FullTableException | StudentNotFoundException ignored){}

        assertEquals(s1, p1.getSchool().getTable("YELLOW").getStudents().get(0));

    }

    @Test
    public void playerMovesStudentToIsland() throws IslandNotFoundException {
        Game g1 = new Game(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Student s1 = new Student(Color.YELLOW);

        g1.setCurrentPlayer(p1);
        p1.getSchool().getHall().addStudent(s1);
        try {
            g1.playerMovesStudent("YELLOW", 1);
        }
        catch(StudentNotFoundException ignored){}

        assertEquals(s1, g1.getBoard().getIslands().getIslandFromID(1).getStudents().get(0));

    }

    @Test
    public void testProfCheck() throws NonExistentColorException {

        /*
            This test verifies that:
            - at round 1, whoever gets a student of a particular first, automatically gets the professor
            - later on, if another player ties the number of students of that same color, that other player will
              NOT get the professor
            - later on, if that player eventually surpasses the opponent's number of students of the same color,
              steals the professor
         */

        Game g1 = new Game(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getConstants());

        Student s1 = new Student(Color.YELLOW);
        Student s2 = new Student(Color.YELLOW);
        Student s3 = new Student(Color.GREEN);
        Student s4 = new Student(Color.GREEN);

        g1.addPlayer(p1);
        g1.addPlayer(p2);

        // All hasProfessor flags are false, so far
        try {
            s1.moveToTable(p1);
            s2.moveToTable(p1);
            s3.moveToTable(p2);
            s4.moveToTable(p2);
        }
        catch(NonExistentColorException | FullTableException ignored){}

        // p1 has 2 yellow students and should have yellow professor
        // p2 has 2 yellow students and should have green professor

        g1.profCheck();

        assertTrue(p1.getSchool().getTable("YELLOW").getHasProfessor());
        assertFalse(p1.getSchool().getTable("GREEN").getHasProfessor());
        assertFalse(p2.getSchool().getTable("YELLOW").getHasProfessor());
        assertTrue(p2.getSchool().getTable("GREEN").getHasProfessor());

        Student s5 = new Student(Color.GREEN);
        Student s6 = new Student(Color.GREEN);

        try {
            s5.moveToTable(p1);
            s6.moveToTable(p1);
        }
        catch(NonExistentColorException | FullTableException ignored){}

        // p1 has 2 yellow students and should have yellow professor
        // p1 has 2 green students and should NOT have green professor
        // p2 has 2 green students and should have green professor

        g1.profCheck();

        assertTrue(p1.getSchool().getTable("YELLOW").getHasProfessor());
        assertFalse(p1.getSchool().getTable("GREEN").getHasProfessor());
        assertFalse(p2.getSchool().getTable("YELLOW").getHasProfessor());
        assertTrue(p2.getSchool().getTable("GREEN").getHasProfessor());

        Student s7 = new Student(Color.GREEN);

        try{
            s7.moveToTable(p1);
        }
        catch(NonExistentColorException | FullTableException ignored){}

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

        Game g1 = new Game(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());

        g1.getBoard().setMotherNaturePos(1);
        g1.setCurrentPlayer(p1);
        p1.setLastAssistantCardPlayed(new AssistantCard(AssistantType.OCTOPUS));

        try{
            g1.moveMotherNature(3); // from island 1 to 4
            assertEquals(4, g1.getBoard().getMotherNaturePos());
            p1.setLastAssistantCardPlayed(new AssistantCard(AssistantType.TURTLE));
            g1.moveMotherNature(5); // from island 4 to 9
            assertEquals(9, g1.getBoard().getMotherNaturePos());
            p1.setLastAssistantCardPlayed(new AssistantCard(AssistantType.ELEPHANT));
            g1.moveMotherNature(1); // from island 9 to 10
            assertEquals(10, g1.getBoard().getMotherNaturePos());
            p1.setLastAssistantCardPlayed(new AssistantCard(AssistantType.DOG));
            g1.moveMotherNature(3); // from island 10 to 1
            assertEquals(1, g1.getBoard().getMotherNaturePos());
            p1.setLastAssistantCardPlayed(new AssistantCard(AssistantType.OSTRICH));
            g1.moveMotherNature(1); // from island 1 to 2
            assertEquals(2, g1.getBoard().getMotherNaturePos());
        }
        catch(InvalidNumberOfStepsException | IslandNotFoundException e){
            e.printStackTrace();
        }

    }

    @Test
    public void testIslandConquerCheck() {

        /*
            This test verifies that:
            1. if a player arrives in an island in which has 0 influence points, the island, even if
               unconquered, does not change its status
            2. once a player arrives in an island in which has at least 1 influence point, it becomes his
            3. once a player arrives in an island in which the owner has the same amount of influence as him,
               considering only the students, the owner remains the same
            4. once a player arrives in an island in which he has 1 more influence points than the owner,
               he still doesn't conquer the island because of the tower presence
            5. once a player arrives in an island in which he has at least two more influence points than the owner,
               he conquers the island
         */

        Game g1 = new Game(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getConstants());

        g1.addPlayer(p1);
        g1.addPlayer(p2);
        g1.setCurrentPlayer(p1);

        Student s1 = new Student(Color.BLUE);
        Student s2 = new Student(Color.GREEN);

        try {
            s1.moveToTable(p1);
            g1.profCheck();
        }
        catch(NonExistentColorException | FullTableException ignored){}


        // Test 1

        // p1 has 1 blue student, island 1 has 1 green student

        try {
            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s2);
            g1.islandConquerCheck(1);
            assertEquals(null, g1.getBoard().getIslands().getIslandFromID(1).getOwner());
        }
        catch(IslandNotFoundException ignored){}

        // Test 2

        // island 1 gets 1 blue student

        try {
            Student s3 = new Student(Color.BLUE);
            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s3);
            g1.islandConquerCheck(1);
            assertEquals(p1, g1.getBoard().getIslands().getIslandFromID(1).getOwner());
            assertEquals(7, p1.getSchool().getTowerRoom().getTowersLeft());
        }
        catch(IslandNotFoundException ignored){}

        g1.setCurrentPlayer(p2);

        // Test 3

        // p2 has 1 pink student, island 1 gets 1 pink student

        try {
            Student s4 = new Student(Color.PINK);
            Student s5 = new Student(Color.PINK);
            s4.moveToTable(p2);
            g1.profCheck();
            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s5);
            g1.islandConquerCheck(1);
            assertEquals(p1, g1.getBoard().getIslands().getIslandFromID(1).getOwner());
            assertEquals(7, p1.getSchool().getTowerRoom().getTowersLeft());
        }
        catch(NonExistentColorException | FullTableException | IslandNotFoundException ignored){}

        Student s6 = new Student(Color.PINK);

        // Test 4

        // island 1 gets 1 more pink student

        try {
            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s6);
            g1.islandConquerCheck(1);
            assertEquals(p1, g1.getBoard().getIslands().getIslandFromID(1).getOwner());
            assertEquals(7, p1.getSchool().getTowerRoom().getTowersLeft());
        }
        catch(IslandNotFoundException ignored){}

        Student s7 = new Student(Color.PINK);

        // Test 5

        // island 1 gets 1 more pink student

        try {
            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s7);
            g1.islandConquerCheck(1);
            assertEquals(p2, g1.getBoard().getIslands().getIslandFromID(1).getOwner());
            assertEquals(8, p1.getSchool().getTowerRoom().getTowersLeft());
            assertEquals(7, p2.getSchool().getTowerRoom().getTowersLeft());
        }
        catch(IslandNotFoundException ignored){}

    }

    @Test
    public void testTakeStudentsFromCloudWithTwoPlayers() {

        Game g1 = new Game(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        g1.setCurrentPlayer(p1);

        try {
            g1.takeStudentsFromCloud(0);
        } catch (EmptyCloudException ignored){}

        assertEquals(0, g1.getBoard().getCloud(0).getStudents().size());     // the cloud should be empty
        assertEquals(3, p1.getSchool().getHall().getStudents().size());         // the hall should have 3 more students

    }

    @Test
    void getPlayersNumberTest() {
        Game g1 = new Game(2, new Constants(2));
        assertEquals(2, g1.getPlayersNumber());
    }

    @Test
    void setPlayersNumberTest() {
        Game g1 = new Game(2, new Constants(2));
        g1.setPlayersNumber(3);
        assertEquals(3, g1.getPlayersNumber());
    }

    @Test
    void getCurrentPlayerTest() {
        Game g1 = new Game(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());

        g1.setCurrentPlayer(p1);
        assertEquals(p1, g1.getCurrentPlayer());
    }

    @Test
    void getRoundNumberTest() {
        Game g1 = new Game(2, new Constants(2));
        assertEquals(1, g1.getRoundNumber());
    }

    @Test
    void setRoundNumberTest() {
        Game g1 = new Game(2, new Constants(2));
        g1.setRoundNumber(1);
        assertEquals(1, g1.getRoundNumber());
    }

    @Test
    void setPlayersTest(){
        Game g1 = new Game(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getConstants());
        List<Player> players = new ArrayList<>();

        players.add(p1);
        players.add(p2);
        g1.setPlayers(players);

        assertEquals(players, g1.getPlayers());
    }

    @Test
    public void showGameBoardTest(){

        Game g1 = new Game(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getConstants());
        List<Player> players = new ArrayList<>();

        players.add(p1);
        players.add(p2);
        g1.setPlayers(players);
        g1.setCurrentPlayer(p1);

        assertDoesNotThrow(g1::showGameBoard);

    }
}