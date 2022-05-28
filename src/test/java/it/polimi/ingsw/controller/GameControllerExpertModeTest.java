package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.charactercards.Flagman;
import it.polimi.ingsw.model.charactercards.Healer;
import it.polimi.ingsw.model.charactercards.Innkeeper;
import it.polimi.ingsw.model.charactercards.Villager;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerExpertModeTest {

    // TODO: need to test character cards

    @Test
    public void testGetMessageCaseInGame(){
        GameController gc = new GameControllerExpertMode();
        PlayerNumberMessage message = new PlayerNumberMessage("Matteo", 2);
        gc.prepareGame(message.getPlayerNumber());
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", gc.getGame().getConstants());
        Player p2 = new Player(Wizard.PINK_WIZARD, "Ludo", gc.getGame().getConstants());
        gc.getGame().addPlayer(p1);
        gc.getGame().addPlayer(p2);
        gc.getGame().setCurrentPlayer(p1);
        Healer c1 = new Healer();
        Innkeeper c2 = new Innkeeper();
        Flagman c3 = new Flagman();
        GameExpertMode g = (GameExpertMode) gc.getGame();
        g.addCharacterCards(new CharacterCard[]{c1, c2, c3});
        p1.setCoinsWallet(5);
        g.getBoard().setMotherNaturePos(12);
        gc.setGameState(GameState.IN_GAME);

        try {
            gc.getMessage(new AssistantCardMessage(gc.getGame().getCurrentPlayer().getNickname(), "TURTLE"));
        }
        catch(Exception ignored){}

        if(gc.getGame().getPlayers().get(0).getLastAssistantCardPlayed() == null)
            System.out.println("ERROR in: " + this.getClass());

        assertEquals("TURTLE", gc.getGame().getPlayers().get(0).getLastAssistantCardPlayed().getName());
        try {
            gc.getMessage(new AssistantCardMessage(gc.getGame().getCurrentPlayer().getNickname(), "FOX"));
        }
        catch(Exception ignored){}
        assertEquals("FOX", gc.getGame().getPlayers().get(0).getLastAssistantCardPlayed().getName());

        assertTrue(gc.getPlanningPhaseDone());
        assertEquals(0, gc.getCurrentPlayerIndex());

        try {
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));

            gc.getMessage(new MoveToTableMessage(gc.getGame().getCurrentPlayer().getNickname(), "PINK"));
            gc.getMessage(new MoveToTableMessage(gc.getGame().getCurrentPlayer().getNickname(), "PINK"));
            gc.getMessage(new MoveToIslandMessage(gc.getGame().getCurrentPlayer().getNickname(), "PINK", 1));
            assertTrue(gc.getGame().getCurrentPlayer().getSchool().getTable("PINK").getHasProfessor());
            assertThrows(WrongMessageSentException.class,
                    () -> gc.getMessage(new MoveToIslandMessage(gc.getGame().getCurrentPlayer().getNickname(), "PINK", 1)));

            gc.getMessage(new MotherNatureStepsMessage(gc.getGame().getCurrentPlayer().getNickname(), 1));

            if(gc.getGame().getBoard().getIslands().getIslandFromID(1).getOwner() == null)
                System.out.println("ERROR in: " + this.getClass());

            assertEquals(gc.getGame().getCurrentPlayer(),
                    gc.getGame().getBoard().getIslands().getIslandFromID(1).getOwner());
            assertTrue(gc.getMotherNatureMoved());
            assertThrows(WrongMessageSentException.class,
                    () -> gc.getMessage(new MotherNatureStepsMessage(gc.getGame().getCurrentPlayer().getNickname(), 1)));

            gc.getMessage(new CloudChoiceMessage(gc.getGame().getCurrentPlayer().getNickname(), 0));
            assertEquals(0, gc.getGame().getBoard().getCloud(0).getStudents().size());

            assertEquals(gc.getGame().getCurrentPlayer().getNickname(), gc.getGame().getCurrentPlayer().getNickname());
            assertEquals(gc.getGame().getConstants().PLAYER_MOVES, gc.getMovesLeft());
            assertFalse(gc.getMotherNatureMoved());
            assertFalse(gc.getPlayerActionPhaseDone());
            assertEquals(1, gc.getCurrentPlayerIndex());

            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));

            gc.getMessage(new MoveToTableMessage(gc.getGame().getCurrentPlayer().getNickname(), "PINK"));
            gc.getMessage(new MoveToTableMessage(gc.getGame().getCurrentPlayer().getNickname(), "PINK"));
            gc.getMessage(new MoveToIslandMessage(gc.getGame().getCurrentPlayer().getNickname(), "PINK", 3));
            assertEquals(4, gc.getGame().getCurrentPlayer().getSchool().getHall().getStudents().size());
            assertFalse(gc.getGame().getCurrentPlayer().getSchool().getTable("PINK").getHasProfessor());

            c1.doOnClick(5);
            gc.getMessage(new CharacterCardMessage(gc.getGame().getCurrentPlayer().getNickname(), 5));
            assertEquals(3, p1.getCoinsWallet());
            assertEquals(3, g.getBoard().getNumOfVetos());
            assertEquals(3, c1.getCost());
            assertTrue(g.getBoard().getIslands().getIslandFromID(5).hasVeto());

            gc.getMessage(new MotherNatureStepsMessage(gc.getGame().getCurrentPlayer().getNickname(), 2));

            if(gc.getGame().getBoard().getIslands().getIslandFromID(3).getOwner() != null
            )
                System.out.println("ERROR in: " + this.getClass());

            assertNull(gc.getGame().getBoard().getIslands().getIslandFromID(3).getOwner());
            assertTrue(gc.getMotherNatureMoved());

            assertThrows(WrongMessageSentException.class,
                    () -> gc.getMessage(new PlayerNumberMessage(gc.getGame().getCurrentPlayer().getNickname(), 2)));
            assertThrows(WrongMessageSentException.class,
                    () -> gc.getMessage(new CharacterCardMessage(gc.getGame().getCurrentPlayer().getNickname(), 8)));
            gc.getMessage(new CloudChoiceMessage(gc.getGame().getCurrentPlayer().getNickname(), 1));

            // cloud are now refilled
            assertEquals(3, gc.getGame().getBoard().getCloud(0).getStudents().size());
            assertEquals(3, gc.getGame().getBoard().getCloud(1).getStudents().size());

            assertEquals(0, gc.getCurrentPlayerIndex());
            assertEquals("Ludo", gc.getGame().getCurrentPlayer().getNickname());
            assertEquals(gc.getGame().getConstants().PLAYER_MOVES, gc.getMovesLeft());
            assertFalse(gc.getPlanningPhaseDone());
            assertFalse(gc.getMotherNatureMoved());
            assertFalse(gc.getPlayerActionPhaseDone());

            assertEquals(2, gc.getGame().getRoundNumber());
        }
        catch(Exception ignored){}

    }

    @Test
    public void testHandleCharacterCardChoice(){
        GameControllerExpertMode gc = new GameControllerExpertMode();
        gc.prepareGame(2);
        gc.setGameState(GameState.IN_GAME);
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", gc.getGame().getConstants());
        Player p2 = new Player(Wizard.PINK_WIZARD, "Ludo", gc.getGame().getConstants());
        gc.getGame().addPlayer(p1);
        gc.getGame().addPlayer(p2);
        gc.getGame().setCurrentPlayer(p1);
        CharacterCard c1 = new Healer();
        CharacterCard c2 = new Villager();
        CharacterCard c3 = new Flagman();
        GameExpertMode g = (GameExpertMode) gc.getGame();
        g.addCharacterCards(new CharacterCard[]{c1, c2, c3});
        p1.setCoinsWallet(5);

        try {
            gc.handleCharacterCardChoice(new CharacterCardMessageString("Matteo", 9, "green"));
        } catch (NotEnoughCoinsException | CharacterCardAlreadyPlayedException | CharacterCardNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(gc.getGame().getCurrentPlayer().getCoinsWallet(), 2);
        assertTrue(gc.getGame().getCurrentPlayer().getCharacterCardAlreadyPlayed());
    }

    @Test
    public void testStartGame(){
        GameControllerExpertMode gc = new GameControllerExpertMode();
        gc.prepareGame(2);
        gc.getGame().addPlayer(new Player(Wizard.PINK_WIZARD, "Ludo", gc.getGame().getConstants()));
        gc.getGame().addPlayer(new Player(Wizard.BLUE_WIZARD, "Matteo", gc.getGame().getConstants()));
        gc.getGame().setCurrentPlayer(gc.getGame().getPlayers().get(0));
        gc.startGame();

        for(Player player : gc.getGame().getPlayers())
            assertEquals(7, player.getSchool().getHall().getStudents().size());

        assertEquals(gc.getGame().getCurrentPlayer(), gc.getGame().getPlayers().get(0));
        GameExpertMode g = (GameExpertMode) gc.getGame();
        assertEquals(3, g.getCharacters().length);

    }

}