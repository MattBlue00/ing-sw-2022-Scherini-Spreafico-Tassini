package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.IslandNotFoundException;
import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SchoolTest{

    @Test
    public void testMoveStudentToIsland() {

        Game g1 = new Game(3, new Constants(3));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Student s1 = new Student(Color.YELLOW);
        Student s2 = new Student(Color.BLUE);
        g1.addPlayer(p1);
        g1.setCurrentPlayer(p1);

        for(int i = 0; i < Constants.MAX_NUM_OF_ISLANDS; i++){
            try {
                Island currentIsland = g1.getBoard().getIslands().getIslandFromID(i+1);
                if(currentIsland.getStudents().size()>0)
                    currentIsland.getStudents().clear();
            } catch (IslandNotFoundException ignored){}
        }

        s1.moveToHall(p1);
        s2.moveToHall(p1);

        try{

            assertEquals(2, p1.getSchool().getHall().getStudents().size());

            p1.getSchool().moveStudentToIsland(g1.getBoard().getIslands().getIslandFromID(1), Color.YELLOW.toString());
            p1.getSchool().moveStudentToIsland(g1.getBoard().getIslands().getIslandFromID(1), Color.BLUE.toString());

            assertEquals(0, p1.getSchool().getHall().getStudents().size());
            assertEquals(2, g1.getBoard().getIslands().getIslandFromID(1).getStudents().size());

        }
        catch(StudentNotFoundException | IslandNotFoundException ignored){}

    }

    @Test
    public void testMoveStudentToTable() throws NonExistentColorException {

        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", new Constants(2));
        Student s1 = new Student(Color.YELLOW);
        Student s2 = new Student(Color.BLUE);

        s1.moveToHall(p1);
        s2.moveToHall(p1);

        try {
            p1.getSchool().moveStudentToTable(p1, "YELLOW");
            p1.getSchool().moveStudentToTable(p1, "BLUE");
        }
        catch(StudentNotFoundException | NonExistentColorException | FullTableException ignored){}

        assertTrue(p1.getSchool().getTable("YELLOW").getStudents().contains(s1));
        assertTrue(p1.getSchool().getTable("BLUE").getStudents().contains(s2));
    }
}