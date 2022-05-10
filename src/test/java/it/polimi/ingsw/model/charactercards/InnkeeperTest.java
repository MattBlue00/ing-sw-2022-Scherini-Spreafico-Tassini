package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InnkeeperTest {

    @Test
    public void innkeeperTest() {

        GameExpertMode g1 = new GameExpertMode(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getConstants());

        CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
        cards[0] = new Innkeeper();
        cards[1] = new Centaur();
        cards[2] = new Flagman();
        g1.addCharacterCards(cards);

        g1.addPlayer(p1);
        g1.addPlayer(p2);
        g1.setCurrentPlayer(p1);
        p1.setCoinsWallet(5);

        try {

            Student s1 = new Student(Color.BLUE);
            Student s2 = new Student(Color.BLUE);
            Student s3 = new Student(Color.BLUE);
            Student s4 = new Student(Color.BLUE);

            // p1 now has 1 blue student
            p1.getSchool().getTable(Color.BLUE.toString()).addStudent(s1, p1);

            g1.setCurrentPlayer(p2);

            // p2 now has 2 blue students
            p2.getSchool().getTable(Color.BLUE.toString()).addStudent(s2, p2);
            p2.getSchool().getTable(Color.BLUE.toString()).addStudent(s3, p2);

            assertEquals(1, p1.getSchool().getTable(Color.BLUE.toString()).getStudents().size());
            assertEquals(2, p2.getSchool().getTable(Color.BLUE.toString()).getStudents().size());

            // p2 should have blue professor
            g1.profCheck();
            assertTrue(p2.getSchool().getTable(Color.BLUE.toString()).getHasProfessor());

            g1.setCurrentPlayer(p1);

            // p1 now has 2 blue student
            p1.getSchool().getTable(Color.BLUE.toString()).addStudent(s4, p1);

            g1.playerPlaysCharacterCard(2);
            assertTrue(cards[0].getIsActive());

            //p1 now should have blue professor
            g1.profCheck();

            assertFalse(cards[0].getIsActive());
            assertEquals(3, p1.getCoinsWallet());
            assertEquals(3, cards[0].getCost());

            assertTrue(p1.getSchool().getTable(Color.BLUE.toString()).getHasProfessor());
            assertFalse(p2.getSchool().getTable(Color.BLUE.toString()).getHasProfessor());

        } catch (NonExistentColorException | FullTableException | CharacterCardNotFoundException
                 | CharacterCardAlreadyPlayedException | NotEnoughCoinsException ignored) {
        }
    }
}