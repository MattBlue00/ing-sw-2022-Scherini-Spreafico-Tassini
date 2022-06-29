package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JesterTest {

    @Test
    public void jesterTest(){

        GameExpertMode g1 = new GameExpertMode(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getConstants());

        List<Student> studentsOnTheCard = new ArrayList<>();
        Student s1 = new Student(Color.BLUE);
        Student s2 = new Student(Color.GREEN);
        Student s3 = new Student(Color.RED);
        Student s4 = new Student(Color.PINK);
        Student s5 = new Student(Color.YELLOW);
        Student s6 = new Student(Color.YELLOW);

        studentsOnTheCard.add(s1);
        studentsOnTheCard.add(s2);
        studentsOnTheCard.add(s3);
        studentsOnTheCard.add(s4);
        studentsOnTheCard.add(s5);
        studentsOnTheCard.add(s6);

        CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
        cards[0] = new Jester(studentsOnTheCard);
        cards[1] = new Centaur();
        cards[2] = new Flagman();
        g1.addCharacterCards(cards);

        Student s7 = new Student(Color.YELLOW);
        Student s8 = new Student(Color.YELLOW);

        p1.getSchool().getHall().addStudent(s7);
        p1.getSchool().getHall().addStudent(s8);

        g1.addPlayer(p1);
        g1.addPlayer(p2);
        g1.setCurrentPlayer(p1);
        p1.setCoinsWallet(5);

        try {
            // the first color is for the student to pick from the hall,
            // the second color is for the student to pick from the card
            List<String> studentsToMove = new ArrayList<>();
            studentsToMove.add("YELLOW");
            studentsToMove.add("BLUE");
            studentsToMove.add("YELLOW");
            studentsToMove.add("RED");

            ((Jester) cards[0]).doOnClick(studentsToMove);
            g1.playerPlaysCharacterCard(7);

            assertFalse(cards[0].getIsActive());
            assertEquals(4, g1.getCurrentPlayer().getCoinsWallet());
            assertEquals(2, cards[0].getCost());
            assertEquals(s1, g1.getCurrentPlayer().getSchool().getHall().getStudents()
                            .stream().filter(x -> x.color().toString().equals("BLUE")).findFirst().get());
            assertEquals(s3, g1.getCurrentPlayer().getSchool().getHall().getStudents()
                    .stream().filter(x -> x.color().toString().equals("RED")).findFirst().get());
            assertTrue(((Jester) cards[0]).getStudentsOnTheCard().contains(s7));
            assertTrue(((Jester) cards[0]).getStudentsOnTheCard().contains(s8));

        } catch (TryAgainException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void showStudentsOnTheCardTest() {
        GameExpertMode g1 = new GameExpertMode(2, new Constants(2));

        CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
        cards[0] = new Jester(g1.getBoard().getStudentsBag());
        cards[1] = new Centaur();
        cards[2] = new Flagman();
        g1.addCharacterCards(cards);

        ((Jester) cards[0]).showStudentsOnTheCard();
        assertEquals(6, ((Jester) cards[0]).getStudentsOnTheCard().size());
    }
}
