package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.IslandNotFoundException;
import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.exceptions.StudentNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest{

    @Test
    public void testPlayAssistantCard(){
        Player player = new Player(Wizard.YELLOW_WIZARD, "Samuele", 2);

        Game game = new Game(2);
        game.setCurrentPlayer(player);

        player.playAssistantCard("FOX");
        assertEquals("FOX", game.getCurrentPlayer().getLastAssistantCardPlayed().get().getName());
    }

    @Test
    void moveStudentToIslandTest() throws IslandNotFoundException {
        Game g1 = new Game(2);
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getPlayersNumber());
        Student s1 = new Student(Color.YELLOW);
            Island island = g1.getBoard().getIslands().getIslandFromID(1);

        g1.setCurrentPlayer(p1);
        p1.getSchool().getHall().addStudent(s1);
        try {
            p1.moveStudent(island, "YELLOW");

            assertEquals(s1, island.getStudents().get(0));
        } catch (StudentNotFoundException e){}
    }

    @Test
    void moveStudentToTableTest(){
        Game g1 = new Game(2);
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getPlayersNumber());
        Student s1 = new Student(Color.YELLOW);

        g1.setCurrentPlayer(p1);
        p1.getSchool().getHall().addStudent(s1);
        try {
            p1.moveStudent("YELLOW");
            assertEquals(s1, p1.getSchool().getTable("YELLOW").getStudents().get(0));
        } catch (FullTableException | StudentNotFoundException | NonExistentColorException e){}
    }

    @Test
    void getAssistantCardFromNameTest(){
        Game g1 = new Game(2);
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getPlayersNumber());
        g1.setCurrentPlayer(p1);

        assertEquals("FOX", p1.getAssistantCardFromName("FOX").get().getName());
        p1.playAssistantCard("FOX");
        assertEquals(Optional.empty(), p1.getAssistantCardFromName("FOX"));
    }
}