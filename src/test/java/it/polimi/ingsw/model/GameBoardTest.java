package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyBagException;
import it.polimi.ingsw.exceptions.EmptyCloudException;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest{

    @Test
    public void testRefillClouds() {
        GameBoard gameboard = new GameBoard(2);
        Player player = new Player(Wizard.PINK_WIZARD, "Ludo", 2);

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
        Player player = new Player(Wizard.PINK_WIZARD, "Ludo", 2);

        try {
            gameboard.takeStudentsFromCloud(0, player);
        } catch (EmptyCloudException e) {
            e.printStackTrace();
        }

        assertEquals(0, gameboard.getCloud(0).getStudents().size());
    }

    @Test
    public void testMotherNaturePosRandom(){

        Constants.setConstants(2);
        GameBoard gb = new GameBoard(2);

        assertTrue(gb.getMotherNaturePos() >= 1 && gb.getMotherNaturePos() <= 12);

    }

}