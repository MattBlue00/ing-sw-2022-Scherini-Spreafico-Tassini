package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.InvalidIslandException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest{

    /*@Test
    public void testMoveStudent() {

        Game game = new Game(2);
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo");
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo");
        game.addPlayer(p1);
        game.addPlayer(p2);
        game.setCurrentPlayer(p1);
        Student s1 = new Student(Color.BLUE);

        p1.getSchool().getHall().addStudent(s1);

        p1.moveStudent(1, Color.BLUE.toString()); //TODO: method need to be implemented first

        // The island with id 1 should have one blue student

        try {
            assertEquals(1, game.getBoard().getIslands().getIslandFromID(1).getStudents().size());
            assertTrue(game.getBoard().getIslands().getIslandFromID(1).getStudents().contains(s1));
        }
        catch(InvalidIslandException e){
            e.printStackTrace();
        }

    }*/
}