package it.polimi.ingsw.model.charactercards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exceptions.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CentaurTest {

    @Test
    public void centaurTest(){

        GameExpertMode g1 = new GameExpertMode(2);
        Player p1 = new Player(Wizard.PINK.toString(), "Ludo", g1.getPlayersNumber());
        Player p2 = new Player(Wizard.BLUE.toString(), "Matteo", g1.getPlayersNumber());

        CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
        cards[0] = new Bard();
        cards[1] = new Centaur();
        cards[2] = new Flagman();
        g1.addCharacterCards(cards);

        g1.addPlayer(p1);
        g1.addPlayer(p2);
        g1.setCurrentPlayer(p1);
        p1.setCoinsWallet(5);
        g1.getBoard().setMotherNaturePos(1);

        try{
            g1.playerPlaysCharacterCard(6);
        }
        catch(CharacterCardAlreadyPlayedException | NotEnoughCoinsException | CharacterCardNotFoundException e){}

        assertTrue(cards[1].getIsActive());
        assertEquals(2, p1.getCoinsWallet());

        Student s1 = new Student(Color.BLUE);
        Student s2 = new Student(Color.GREEN);

        try {
            s1.moveToTable(p1);
            g1.profCheck();
        }
        catch(NonExistentColorException | FullTableException e){}


        // Test 1

        // p1 has 1 blue student, island 1 has 1 green student

        try {
            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s2);
            g1.islandConquerCheck(1);
            assertEquals(Optional.empty(), g1.getBoard().getIslands().getIslandFromID(1).getOwner());
        }
        catch(IslandNotFoundException e){
            e.printStackTrace();
        }

        // Test 2

        // island 1 gets 1 blue student

        try {
            Student s3 = new Student(Color.BLUE);
            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s3);
            g1.islandConquerCheck(1);
            assertEquals(Optional.of(p1), g1.getBoard().getIslands().getIslandFromID(1).getOwner());
        }
        catch(IslandNotFoundException e){
            e.printStackTrace();
        }

        g1.setCurrentPlayer(p2);

        // Test 3

        // p2 has 1 pink student, island 1 gets 1 pink student

        try {
            Student s4 = new Student(Color.PINK);
            Student s5 = new Student(Color.PINK);
            s4.moveToTable(p2);
            g1.profCheck();
            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s5);
            g1.islandConquerCheck(1);
            assertEquals(Optional.of(p1), g1.getBoard().getIslands().getIslandFromID(1).getOwner());
        }
        catch(NonExistentColorException | FullTableException | IslandNotFoundException e){
            e.printStackTrace();
        }

        Student s6 = new Student(Color.PINK);

        // Test 4

        // island 1 gets 1 more pink student

        try {

            g1.getBoard().getIslands().getIslandFromID(1).addStudent(s6);
            g1.playerPlaysCharacterCard(6);

            assertEquals(Optional.of(p2), g1.getBoard().getIslands().getIslandFromID(1).getOwner());
            assertFalse(cards[0].getIsActive());
            assertEquals(2, p1.getCoinsWallet());
            assertEquals(4, cards[1].getCost());

        }
        catch(IslandNotFoundException | CharacterCardNotFoundException | CharacterCardAlreadyPlayedException |
                NotEnoughCoinsException ignored){}

    }

}