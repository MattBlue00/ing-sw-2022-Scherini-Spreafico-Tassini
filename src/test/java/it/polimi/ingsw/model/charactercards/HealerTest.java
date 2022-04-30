package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exceptions.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class HealerTest {

    @Test
    public void healerTest(){

        GameExpertMode g1 = new GameExpertMode(2);
        Player p1 = new Player(Wizard.PINK.toString(), "Ludo", g1.getPlayersNumber());
        Player p2 = new Player(Wizard.BLUE.toString(), "Matteo", g1.getPlayersNumber());

        CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
        cards[0] = new Healer();
        cards[1] = new Centaur();
        cards[2] = new Flagman();
        g1.addCharacterCards(cards);

        g1.addPlayer(p1);
        g1.addPlayer(p2);
        g1.setCurrentPlayer(p1);
        p1.setCoinsWallet(5);

        g1.getBoard().setMotherNaturePos(1);

        try{

            Student s1 = new Student(Color.BLUE);
            Student s2 = new Student(Color.BLUE);
            Student s3 = new Student(Color.BLUE);
            Student s4 = new Student(Color.YELLOW);

            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s1);
            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s2);

            p1.getSchool().getTable(Color.BLUE.toString()).addStudent(s3, p2);
            p2.getSchool().getTable(Color.YELLOW.toString()).addStudent(s4, p2);

            g1.profCheck();
            assertTrue(p2.getSchool().getTable(Color.YELLOW.toString()).getHasProfessor());
            assertTrue(p1.getSchool().getTable(Color.BLUE.toString()).getHasProfessor());

            ((Healer) cards[0]).setIslandID(1);
            g1.playerPlaysCharacterCard(5);

            assertFalse(cards[0].getIsActive());
            assertEquals(3, p1.getCoinsWallet());
            assertEquals(3, cards[0].getCost());
            assertTrue(g1.getBoard().getIslands().getIslandFromID(1).hasVeto());

            g1.islandConquerCheck(1);
            assertFalse(cards[0].getIsActive());
            assertEquals(Optional.empty(), g1.getBoard().getIslands().getIslandFromID(1).getOwner());
            assertFalse(g1.getBoard().getIslands().getIslandFromID(1).hasVeto());

        }
        catch(NonExistentColorException | CharacterCardNotFoundException | CharacterCardAlreadyPlayedException |
                NotEnoughCoinsException | IslandNotFoundException | FullTableException ignored){}

    }
}

