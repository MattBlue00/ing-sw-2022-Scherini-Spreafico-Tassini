package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.CharacterCardAlreadyPlayedException;
import it.polimi.ingsw.exceptions.CharacterCardNotFoundException;
import it.polimi.ingsw.exceptions.InvalidNumberOfStepsException;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PostmanTest {

    @Test
    public void postmanTest(){

        GameExpertMode g1 = new GameExpertMode(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getConstants());

        CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
        cards[0] = new Postman();
        cards[1] = new Centaur();
        cards[2] = new Flagman();

        AssistantCard card_1 = new AssistantCard(AssistantType.CHEETAH);

        g1.addCharacterCards(cards);

        g1.addPlayer(p1);
        g1.addPlayer(p2);
        g1.setCurrentPlayer(p1);
        p1.setCoinsWallet(5);
        g1.getBoard().setMotherNaturePos(1);

        try{

            p1.setLastAssistantCardPlayed(card_1);
            g1.playerPlaysCharacterCard(4);

            assertTrue(cards[0].getIsActive());

            //max possible steps
            g1.moveMotherNature(3);

            assertEquals(4, p1.getCoinsWallet());
            assertEquals(2, cards[0].getCost());
            assertEquals(4, g1.getBoard().getMotherNaturePos());
            assertFalse(cards[0].getIsActive());

    }
        catch(CharacterCardNotFoundException | CharacterCardAlreadyPlayedException |
              NotEnoughCoinsException | InvalidNumberOfStepsException ignored){}

    }
}
