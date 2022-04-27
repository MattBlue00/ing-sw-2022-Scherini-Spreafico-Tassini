package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.FullTableException;
import it.polimi.ingsw.model.exceptions.IslandNotFoundException;
import it.polimi.ingsw.model.exceptions.NonExistentColorException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DoublyLinkedListTest {

    @Test
    public void mergeIslandsTest(){

        Game g1 = new Game(2);
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getPlayersNumber());
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getPlayersNumber());

        try{

            assertEquals(12, g1.getBoard().getIslands().getSize());

            g1.addPlayer(p1);
            g1.addPlayer(p2);
            g1.setCurrentPlayer(p1);
            Student s1 = new Student(Color.PINK);
            Student s2 = new Student(Color.PINK);
            Student s3 = new Student(Color.PINK);
            s1.moveToTable(p1);
            s2.moveToIsland(g1.getBoard().getIslands().getIslandFromID(1));
            s3.moveToIsland(g1.getBoard().getIslands().getIslandFromID(2));
            g1.profCheck();
            g1.islandConquerCheck(1);
            g1.islandConquerCheck(2);

            assertEquals(11, g1.getBoard().getIslands().getSize());
            assertEquals(Optional.of(p1), g1.getBoard().getIslands().getIslandFromID(1).getOwner());

        }
        catch(NonExistentColorException | FullTableException | IslandNotFoundException ignored){}

    }

}