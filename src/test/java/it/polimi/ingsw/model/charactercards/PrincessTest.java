package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrincessTest {

    @Test
    public void princessTest(){

        GameExpertMode g1 = new GameExpertMode(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getConstants());

        CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
        cards[0] = new Princess(g1.getBoard().getStudentsBag());
        cards[1] = new Centaur();
        cards[2] = new Flagman();
        g1.addCharacterCards(cards);

        g1.addPlayer(p1);
        g1.addPlayer(p2);
        g1.setCurrentPlayer(p1);
        p1.setCoinsWallet(5);

        try {
            Student studentToMove = ((Princess) cards[0]).getStudents()[0];
            ((Princess) cards[0]).doOnClick(studentToMove.getColor().toString());
            g1.playerPlaysCharacterCard(11);
            assertFalse(cards[0].getIsActive());
            assertEquals(3, g1.getCurrentPlayer().getCoinsWallet());
            assertEquals(3, cards[0].getCost());
            assertEquals(1, g1.getCurrentPlayer().getSchool().getTable(studentToMove.getColor().toString()).getStudents().size());
            assertEquals(studentToMove, g1.getCurrentPlayer().getSchool().getTable(studentToMove.getColor().toString()).getStudents().get(0));
            assertNotEquals(studentToMove, ((Princess) cards[0]).getStudents()[0]);
        } catch (CharacterCardAlreadyPlayedException | NotEnoughCoinsException | CharacterCardNotFoundException |
                 NonExistentColorException ignored){}

    }

}