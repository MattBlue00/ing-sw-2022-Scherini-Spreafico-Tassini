package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.exceptions.EmptyCloudException;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Wizard;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmptyCloudExceptionTest {

    @Test
    public void exceptionTest(){

        GameExpertMode g1 = new GameExpertMode(2, new Constants(2));
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getConstants());
        g1.setCurrentPlayer(p1);

        try{
            g1.takeStudentsFromCloud(0);
        }
        catch(EmptyCloudException ignored){}

        assertThrows(EmptyCloudException.class,
                () -> g1.takeStudentsFromCloud(0));

    }

}