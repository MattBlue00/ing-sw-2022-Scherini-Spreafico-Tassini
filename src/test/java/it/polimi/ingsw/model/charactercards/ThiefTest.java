package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ThiefTest {

    @Test
    public void ThiefTest() {
            GameExpertMode g1 = new GameExpertMode(2, new Constants(2));
            Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
            Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getConstants());

            CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
            cards[0] = new Thief();
            cards[1] = new Centaur();
            cards[2] = new Flagman();
            g1.addCharacterCards(cards);

            g1.addPlayer(p1);
            g1.addPlayer(p2);
            g1.setCurrentPlayer(p1);
            p1.setCoinsWallet(5);
            p2.setCoinsWallet(5);

            try{

                Student s1 = new Student(Color.BLUE);
                Student s2 = new Student(Color.BLUE);
                Student s3 = new Student(Color.BLUE);
                Student s4 = new Student(Color.BLUE);
                Student s5 = new Student(Color.BLUE);
                Student s6 = new Student(Color.YELLOW);
                Student s7 = new Student(Color.YELLOW);

                //p1 has three blue students and one yellow student
                p1.getSchool().getTable(Color.BLUE.toString()).addStudent(s1, p1);
                p1.getSchool().getTable(Color.BLUE.toString()).addStudent(s2, p1);
                p1.getSchool().getTable(Color.BLUE.toString()).addStudent(s3, p1);
                p1.getSchool().getTable(Color.YELLOW.toString()).addStudent(s6, p1);

                //p2 has two blue students and one yellow student
                p2.getSchool().getTable(Color.BLUE.toString()).addStudent(s4, p2);
                p2.getSchool().getTable(Color.BLUE.toString()).addStudent(s5, p2);
                p2.getSchool().getTable(Color.YELLOW.toString()).addStudent(s7, p2);

                assertEquals(1, p1.getSchool().getTable(Color.YELLOW.toString()).getStudents().size());
                assertEquals(1, p2.getSchool().getTable(Color.YELLOW.toString()).getStudents().size());
                assertEquals(3, p1.getSchool().getTable(Color.BLUE.toString()).getStudents().size());
                assertEquals(2, p2.getSchool().getTable(Color.BLUE.toString()).getStudents().size());

                g1.setCurrentPlayer(p1);

                ((Thief) cards[0]).doOnClick("BLUE");

                g1.playerPlaysCharacterCard(12);

                assertFalse(cards[0].getIsActive());
                assertEquals(3, p1.getCoinsWallet());
                assertEquals(4, cards[0].getCost());

                assertEquals(1, p1.getSchool().getTable(Color.YELLOW.toString()).getStudents().size());
                assertEquals(1, p2.getSchool().getTable(Color.YELLOW.toString()).getStudents().size());
                assertEquals(0, p1.getSchool().getTable(Color.BLUE.toString()).getStudents().size());
                assertEquals(0, p2.getSchool().getTable(Color.BLUE.toString()).getStudents().size());
                assertEquals(119, g1.getBoard().getStudentsBag().size());

                Student s8 = new Student(Color.BLUE);
                p1.getSchool().getTable("BLUE").addStudent(s8, p1);

                g1.setCurrentPlayer(p2);

                ((Thief) cards[0]).doOnClick("BLUE");

                    g1.playerPlaysCharacterCard(12);

                    assertFalse(cards[0].getIsActive());
                    assertEquals(1, p2.getCoinsWallet());
                    assertEquals(5, cards[0].getCost());
                    assertEquals(1, p1.getSchool().getTable(Color.YELLOW.toString()).getStudents().size());
                    assertEquals(1, p2.getSchool().getTable(Color.YELLOW.toString()).getStudents().size());
                    assertEquals(0, p1.getSchool().getTable(Color.BLUE.toString()).getStudents().size());
                    assertEquals(0, p2.getSchool().getTable(Color.BLUE.toString()).getStudents().size());

            } catch (CharacterCardAlreadyPlayedException | NotEnoughCoinsException | CharacterCardNotFoundException |
                     NonExistentColorException | FullTableException ignored) {}
        }
    }
