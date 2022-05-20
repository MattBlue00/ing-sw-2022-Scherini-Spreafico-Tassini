package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BardTest {

    @Test
    public void bardTest(){

        GameExpertMode g1 = new GameExpertMode(2, new Constants(2));
        Player p1 = new Player(Wizard.PINK_WIZARD, "Ludo", g1.getConstants());
        Player p2 = new Player(Wizard.BLUE_WIZARD, "Matteo", g1.getConstants());

        CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
        cards[0] = new Bard();
        cards[1] = new Centaur();
        cards[2] = new Flagman();
        g1.addCharacterCards(cards);

        g1.addPlayer(p1);
        g1.addPlayer(p2);
        g1.setCurrentPlayer(p1);
        p1.setCoinsWallet(5);

        try{

            Student s1 = new Student(Color.BLUE);
            Student s2 = new Student(Color.BLUE);
            Student s3 = new Student(Color.YELLOW);
            Student s4 = new Student(Color.YELLOW);

            p1.getSchool().getHall().addStudent(s1);
            p1.getSchool().getHall().addStudent(s2);
            p1.getSchool().getTable(Color.YELLOW.toString()).addStudent(s3, p1);
            p1.getSchool().getTable(Color.YELLOW.toString()).addStudent(s4, p1);

            assertEquals(2, p1.getSchool().getHall().getStudents().size());
            assertEquals(2, p1.getSchool().getTable(Color.YELLOW.toString()).getStudents().size());
            assertEquals(0, p1.getSchool().getTable(Color.BLUE.toString()).getStudents().size());

            List<String> students = new ArrayList<>();
            students.add("BLUE");
            students.add("YELLOW");
            students.add("BLUE");
            students.add("YELLOW");

            ((Bard) cards[0]).doOnClick(students);
            g1.playerPlaysCharacterCard(10);

            assertFalse(cards[0].getIsActive());
            assertEquals(4, p1.getCoinsWallet());
            assertEquals(2, cards[0].getCost());

            assertEquals(2, p1.getSchool().getHall().getStudents().size());
            assertEquals(0, p1.getSchool().getTable(Color.YELLOW.toString()).getStudents().size());
            assertEquals(2, p1.getSchool().getTable(Color.BLUE.toString()).getStudents().size());

        }
        catch(NonExistentColorException | FullTableException | CharacterCardNotFoundException
              | CharacterCardAlreadyPlayedException | NotEnoughCoinsException ignored){}

    }

}