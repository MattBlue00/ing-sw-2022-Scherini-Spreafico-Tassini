package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.IslandNotFoundException;
import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest{

    @Test
    public void testPlayAssistantCard(){
        Player player = new Player(Wizard.YELLOW_WIZARD, "Samuele", new Constants(2));

        Game game = new Game(2, new Constants(2));
        game.addPlayer(player);
        game.addPlayer(new Player(Wizard.BLUE_WIZARD, "Matteo", new Constants(2)));
        game.setCurrentPlayer(player);

        player.playAssistantCard("FOX");

        if(game.getCurrentPlayer().getLatestAssistantCardPlayed() == null)
            System.out.println("ERROR in: " + this.getClass());

        assertEquals("FOX", game.getCurrentPlayer().getLatestAssistantCardPlayed().getName());
    }

    @Test
    void moveStudentToIslandTest() throws IslandNotFoundException {
        Game g1 = new Game(2, new Constants(2));
        for(int i = 0; i < Constants.MAX_NUM_OF_ISLANDS; i++){
            try {
                Island currentIsland = g1.getBoard().getIslands().getIslandFromID(i+1);
                if(currentIsland.getStudents().size()>0)
                    currentIsland.getStudents().clear();
            } catch (IslandNotFoundException ignored){}
        }
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Student s1 = new Student(Color.YELLOW);
        Island island = g1.getBoard().getIslands().getIslandFromID(1);

        g1.setCurrentPlayer(p1);
        p1.getSchool().getHall().addStudent(s1);
        try {
            p1.moveStudent(island, "YELLOW");

            assertEquals(s1, island.getStudents().get(0));
        } catch (StudentNotFoundException ignored){}
    }

    @Test
    void moveStudentToTableTest(){
        Game g1 = new Game(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Student s1 = new Student(Color.YELLOW);

        g1.setCurrentPlayer(p1);
        p1.getSchool().getHall().addStudent(s1);
        try {
            p1.moveStudent("YELLOW");
            assertEquals(s1, p1.getSchool().getTable("YELLOW").getStudents().get(0));
        } catch (FullTableException | StudentNotFoundException | NonExistentColorException ignored){}
    }

    @Test
    void getAssistantCardFromNameTest(){
        Game g1 = new Game(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        g1.setCurrentPlayer(p1);

        if(p1.getAssistantCardFromName("FOX") == null)
            System.out.println("ERROR in: " + this.getClass());

        assertEquals("FOX", p1.getAssistantCardFromName("FOX").getName());
        p1.playAssistantCard("FOX");
        assertNull(p1.getAssistantCardFromName("FOX"));
    }

    @Test
    void showSchoolTest(){
        Game g1 = new Game(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getConstants());
        g1.setCurrentPlayer(p1);

        Student s1 = new Student(Color.YELLOW);
        Student s2 = new Student(Color.YELLOW);
        Student s3 = new Student(Color.BLUE);
        Student s4 = new Student(Color.RED);
        Student s5 = new Student(Color.RED);
        Student s6 = new Student(Color.GREEN);
        Student s7 = new Student(Color.PINK);

        p1.getSchool().getHall().addStudent(s3);
        p1.getSchool().getHall().addStudent(s6);

        p2.getSchool().getHall().addStudent(s7);

        try {
            p1.getSchool().getTable("YELLOW").addStudent(s1, p1);
            p1.getSchool().getTable("YELLOW").addStudent(s2, p1);

            p2.getSchool().getTable("RED").addStudent(s4, p2);
            p2.getSchool().getTable("RED").addStudent(s5, p2);

        } catch (NonExistentColorException | FullTableException ignored){}

        try {
            g1.profCheck();
        } catch (NonExistentColorException ignored){}

        assertDoesNotThrow(p1::showSchool);
        assertDoesNotThrow(p2::showSchool);
    }
}