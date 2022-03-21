package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.EmptyCloudException;
import junit.framework.TestCase;
import org.junit.Test;

public class GameBoardTest extends TestCase {

    @Test
    public void testRefillClouds() {
        GameBoard gameboard = new GameBoard(2);
        Player player = new Player(Wizard.PINK_WIZARD, "Ludo");

        try {
            gameboard.takeStudentsFromCloud(0, player);
        } catch (EmptyCloudException e) {
            e.printStackTrace();
        }

        try {
            gameboard.refillClouds();
        } catch (EmptyBagException e) {
            e.printStackTrace();
        }
        assertEquals(3, gameboard.getCloud(0).getStudents().size());
    }

    @Test
    public void testTakeStudentsFromCloud() {
        GameBoard gameboard = new GameBoard(2);
        Player player = new Player(Wizard.PINK_WIZARD, "Ludo");

        try {
            gameboard.takeStudentsFromCloud(0, player);
        } catch (EmptyCloudException e) {
            e.printStackTrace();
        }

        assertEquals(0, gameboard.getCloud(0).getStudents().size());
    }
}