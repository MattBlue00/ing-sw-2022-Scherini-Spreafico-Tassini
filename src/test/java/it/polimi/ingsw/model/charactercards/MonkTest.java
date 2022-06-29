package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.*;
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

        for(int i = 0; i < Constants.MAX_NUM_OF_ISLANDS; i++){
            try {
                Island currentIsland = g1.getBoard().getIslands().getIslandFromID(i+1);
                if(currentIsland.getStudents().size()>0)
                    currentIsland.getStudents().clear();
            } catch (IslandNotFoundException ignored){}
        }

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
            ((Monk) cards[0]).doOnClick(studentToMove.color().toString(), 1);
            g1.playerPlaysCharacterCard(1);
            assertFalse(cards[0].getIsActive());
            assertEquals(4, g1.getCurrentPlayer().getCoinsWallet());
            assertEquals(2, cards[0].getCost());
            assertEquals(g1.getBoard().getIslands().getIslandFromID(1).getStudents().get(0).color().toString(),
                    studentToMove.color().toString());
            assertEquals(4, ((Monk) cards[0]).getStudents().length);
        } catch (TryAgainException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void showStudentsOnTheCard(){
        GameExpertMode g1 = new GameExpertMode(2, new Constants(2));

        CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
        cards[0] = new Monk(g1.getBoard().getStudentsBag());
        cards[1] = new Centaur();
        cards[2] = new Flagman();
        g1.addCharacterCards(cards);

        ((Monk) cards[0]).showStudentsOnTheCard();
        assertEquals(4, ((Monk) cards[0]).getStudentsOnTheCard().size());
    }

}