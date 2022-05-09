package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.exceptions.CharacterCardAlreadyPlayedException;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Wizard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardAlreadyPlayedExceptionTest {

    @Test
    public void exceptionTest(){

        GameExpertMode g1 = new GameExpertMode(2);
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getPlayersNumber());
        g1.addPlayer(p1);
        g1.setCurrentPlayer(p1);
        p1.setCharacterCardAlreadyPlayed(true);

        assertThrows(CharacterCardAlreadyPlayedException.class,
                () -> g1.playerPlaysCharacterCard(1));

    }

}