package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.EmptyCloudException;
import junit.framework.TestCase;
import org.junit.Test;

public class GameTest extends TestCase {

    //testing Game methods (without getter e setter)

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

    //TODO
    //waiting for the method to be implemented
    @Test
    public void testCoinCheck() {
    }

    //TODO
    //waiting for the method to be implemented
    @Test
    public void testProfCheck() {
    }

    //TODO
    //waiting for the method to be implemented
    @Test
    public void testPlayerPlaysCharacterCard() {
    }

    //TODO
    //waiting for the method to be implemented
    @Test
    public void testMoveMothernature() {
    }

    //TODO
    //waiting for the method to be implemented
    @Test
    public void testIslandConquerCheck() {
    }

    //TODO
    //waiting for the method to be implemented
    @Test
    public void testMergeCheck() {
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

        assertEquals(0, g1.getBoard().getCloud(0).getStudents().size());
    }
}