package it.polimi.ingsw.model;

import it.polimi.ingsw.model.charactercards.Bard;
import it.polimi.ingsw.model.charactercards.Centaur;
import it.polimi.ingsw.model.charactercards.Flagman;
import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.InvalidNumberOfStepsException;
import it.polimi.ingsw.exceptions.IslandNotFoundException;
import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameExpertModeTest {

    @Test
    public void testProfCheck() throws NonExistentColorException {

        /*
            This test verifies that:
            - at round 1, whoever gets a student of a particular color first, automatically gets the professor
            - later on, if another player ties the number of students of that same color, that other player will
              NOT get the professor
            - later on, if that player eventually surpasses the opponent's number of students of the same color,
              steals the professor
         */

        GameExpertMode g1 = new GameExpertMode(2);
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getPlayersNumber());
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getPlayersNumber());

        CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
        cards[0] = new Bard();
        cards[1] = new Centaur();
        cards[2] = new Flagman();
        g1.addCharacterCards(cards);

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
        catch(NonExistentColorException | FullTableException e){}

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
        catch(NonExistentColorException | FullTableException e){}

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
        catch(NonExistentColorException | FullTableException e){}

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

        GameExpertMode g1 = new GameExpertMode(2);
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getPlayersNumber());

        CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
        cards[0] = new Bard();
        cards[1] = new Centaur();
        cards[2] = new Flagman();
        g1.addCharacterCards(cards);

        g1.getBoard().setMotherNaturePos(1);
        g1.setCurrentPlayer(p1);
        p1.setLastAssistantCardPlayed(new AssistantCard(AssistantType.OCTOPUS));

        try{
            g1.moveMotherNature(3); // from island 1 to 4
        }
        catch(InvalidNumberOfStepsException e){
            e.printStackTrace();
        }

        assertEquals(4, g1.getBoard().getMotherNaturePos());

        p1.setLastAssistantCardPlayed(new AssistantCard(AssistantType.TURTLE));

        try{
            g1.moveMotherNature(5); // from island 4 to 9
        }
        catch(InvalidNumberOfStepsException e){
            e.printStackTrace();
        }

        assertEquals(9, g1.getBoard().getMotherNaturePos());

        p1.setLastAssistantCardPlayed(new AssistantCard(AssistantType.ELEPHANT));

        try{
            g1.moveMotherNature(1); // from island 9 to 10
        }
        catch(InvalidNumberOfStepsException e){
            e.printStackTrace();
        }

        assertEquals(10, g1.getBoard().getMotherNaturePos());

        p1.setLastAssistantCardPlayed(new AssistantCard(AssistantType.DOG));

        try{
            g1.moveMotherNature(3); // from island 10 to 1
        }
        catch(InvalidNumberOfStepsException e){
            e.printStackTrace();
        }

        assertEquals(1, g1.getBoard().getMotherNaturePos());

        p1.setLastAssistantCardPlayed(new AssistantCard(AssistantType.OSTRICH));

        try{
            g1.moveMotherNature(1); // from island 1 to 2
        }
        catch(InvalidNumberOfStepsException e){
            e.printStackTrace();
        }

        assertEquals(2, g1.getBoard().getMotherNaturePos());

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

        GameExpertMode g1 = new GameExpertMode(2);
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getPlayersNumber());
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getPlayersNumber());

        CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
        cards[0] = new Bard();
        cards[1] = new Centaur();
        cards[2] = new Flagman();
        g1.addCharacterCards(cards);

        g1.addPlayer(p1);
        g1.addPlayer(p2);
        g1.setCurrentPlayer(p1);

        Student s1 = new Student(Color.BLUE);
        Student s2 = new Student(Color.GREEN);

        try {
            s1.moveToTable(p1);
            g1.profCheck();
        }
        catch(NonExistentColorException | FullTableException e){}


        // Test 1

        // p1 has 1 blue student, island 1 has 1 green student

        try {
            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s2);
            g1.islandConquerCheck(1);
            assertEquals(Optional.empty(), g1.getBoard().getIslands().getIslandFromID(1).getOwner());
        }
        catch(IslandNotFoundException e){
            e.printStackTrace();
        }

        // Test 2

        // island 1 gets 1 blue student

        try {
            Student s3 = new Student(Color.BLUE);
            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s3);
            g1.islandConquerCheck(1);
            assertEquals(Optional.of(p1), g1.getBoard().getIslands().getIslandFromID(1).getOwner());
        }
        catch(IslandNotFoundException e){
            e.printStackTrace();
        }

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
            assertEquals(Optional.of(p1), g1.getBoard().getIslands().getIslandFromID(1).getOwner());
        }
        catch(NonExistentColorException | FullTableException | IslandNotFoundException e){
            e.printStackTrace();
        }

        Student s6 = new Student(Color.PINK);

        // Test 4

        // island 1 gets 1 more pink student

        try {
            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s6);
            g1.islandConquerCheck(1);
            assertEquals(Optional.of(p1), g1.getBoard().getIslands().getIslandFromID(1).getOwner());
        }
        catch(IslandNotFoundException e){
            e.printStackTrace();
        }

        Student s7 = new Student(Color.PINK);

        // Test 5

        // island 1 gets 1 more pink student

        try {
            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s7);
            g1.islandConquerCheck(1);
            assertEquals(Optional.of(p2), g1.getBoard().getIslands().getIslandFromID(1).getOwner());
        }
        catch(IslandNotFoundException e){
            e.printStackTrace();
        }

    }

}