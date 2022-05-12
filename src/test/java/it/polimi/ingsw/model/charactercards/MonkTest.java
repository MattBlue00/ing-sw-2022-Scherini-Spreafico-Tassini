package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.CharacterCardAlreadyPlayedException;
import it.polimi.ingsw.exceptions.CharacterCardNotFoundException;
import it.polimi.ingsw.exceptions.IslandNotFoundException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonkTest {

    @Test
    public void monkTest(){

        GameExpertMode g1 = new GameExpertMode(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getConstants());

        CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
        cards[0] = new Monk(g1.getBoard().getStudentsBag());
        cards[1] = new Centaur();
        cards[2] = new Flagman();
        g1.addCharacterCards(cards);

        g1.addPlayer(p1);
        g1.addPlayer(p2);
        g1.setCurrentPlayer(p1);
        p1.setCoinsWallet(5);

        try {
            Student studentToMove = ((Monk) cards[0]).getStudents()[0];
            ((Monk) cards[0]).doOnClick(studentToMove.getColor().toString(), 1);
            g1.playerPlaysCharacterCard(1);
            assertFalse(cards[0].getIsActive());
            assertEquals(4, g1.getCurrentPlayer().getCoinsWallet());
            assertEquals(2, cards[0].getCost());
            assertEquals(g1.getBoard().getIslands().getIslandFromID(1).getStudents().get(0).getColor().toString(),
                    studentToMove.getColor().toString());
            assertNotEquals(studentToMove, ((Monk) cards[0]).getStudents()[0]);
        } catch (CharacterCardAlreadyPlayedException | NotEnoughCoinsException | CharacterCardNotFoundException |
                IslandNotFoundException ignored){}

    }

}