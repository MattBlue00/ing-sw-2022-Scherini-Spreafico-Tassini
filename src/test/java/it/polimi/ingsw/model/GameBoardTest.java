package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.EmptyBagException;
import it.polimi.ingsw.exceptions.EmptyCloudException;
import it.polimi.ingsw.exceptions.IslandNotFoundException;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest{

    @Test
    public void testRefillClouds() {
        GameBoard gameboard = new GameBoard(2, new Constants(2));
        Player player = new Player(Wizard.PINK_WIZARD, "Ludo", new Constants(2));

        try {
            gameboard.takeStudentsFromCloud(0, player);
        } catch (EmptyCloudException e) {
            e.printStackTrace();
        }

        try {
            gameboard.refillClouds();
        } catch (EmptyBagException ignored){}

        assertEquals(3, gameboard.getCloud(0).getStudents().size());
    }

    @Test
    public void testTakeStudentsFromCloud() {
        GameBoard gameboard = new GameBoard(2, new Constants(2));
        Player player = new Player(Wizard.PINK_WIZARD, "Ludo", new Constants(2));

        try {
            gameboard.takeStudentsFromCloud(0, player);
        } catch (EmptyCloudException e) {
            e.printStackTrace();
        }

        assertEquals(0, gameboard.getCloud(0).getStudents().size());
    }

    @Test
    public void testMotherNaturePosRandom(){

        GameController gc = new GameController();
        gc.prepareGame(2);
        gc.getGame().addPlayer(new Player(Wizard.BLUE_WIZARD, "Matteo", new Constants(2)));
        gc.getGame().addPlayer(new Player(Wizard.PINK_WIZARD, "Ludo", new Constants(2)));
        gc.startGame();

        assertTrue(gc.getGame().getBoard().getMotherNaturePos() >= 1 &&
                gc.getGame().getBoard().getMotherNaturePos() <= 12);

    }

    @Test
    public void testIslandStudentsRandom(){

        GameController gc = new GameController();
        gc.prepareGame(2);
        gc.getGame().addPlayer(new Player(Wizard.BLUE_WIZARD, "Matteo", new Constants(2)));
        gc.getGame().addPlayer(new Player(Wizard.PINK_WIZARD, "Ludo", new Constants(2)));
        gc.startGame();

        int noStudentPos1 = gc.getGame().getBoard().getMotherNaturePos();
        int noStudentPos2;
        if(noStudentPos1 > 6)
            noStudentPos2 = noStudentPos1 - 6;
        else
            noStudentPos2 = noStudentPos1 + 6;
        for(int i = 0; i < Constants.MAX_NUM_OF_ISLANDS; i++){
            try {
                Island currentIsland = gc.getGame().getBoard().getIslands().getIslandFromID(i+1);
                if((i+1) != noStudentPos1 && (i+1) != noStudentPos2)
                    assertEquals(1, currentIsland.getStudents().size());
                else
                    assertEquals(0, currentIsland.getStudents().size());
            } catch (IslandNotFoundException ignored){}
        }

    }

}