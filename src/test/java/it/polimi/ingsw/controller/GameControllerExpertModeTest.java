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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerExpertModeTest {

    // TODO: need to test character cards

    @Test
    public void testGetMessageCaseInGame() throws WrongMessageSentException {
        GameController gc = new GameControllerExpertMode();
        PlayerNumberMessage message = new PlayerNumberMessage("Matteo", 2);
        gc.prepareGame(message.getPlayerNumber());
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", gc.getGame().getPlayersNumber());
        Player p2 = new Player(Wizard.PINK_WIZARD, "Ludo", gc.getGame().getPlayersNumber());
        gc.getGame().addPlayer(p1);
        gc.getGame().addPlayer(p2);
        gc.startGame();
        CharacterCard c1 = new Healer();
        CharacterCard c2 = new Innkeeper();
        CharacterCard c3 = new Flagman();
        GameExpertMode g = (GameExpertMode) gc.getGame();
        g.addCharacterCards(new CharacterCard[]{c1, c2, c3});
        p1.setCoinsWallet(5);

        try {
            gc.getMessage(new AssistantCardMessage(gc.getGame().getCurrentPlayer().getNickname(), "TURTLE"));
        }
        catch(Exception ignored){}
        assertEquals("TURTLE", gc.getGame().getPlayers().get(0).getLastAssistantCardPlayed().get().getName());
        assertThrows(AssistantCardAlreadyPlayedException.class,
                () -> gc.getMessage(new AssistantCardMessage(gc.getGame().getCurrentPlayer().getNickname(), "TURTLE")));
        try {
            gc.getMessage(new AssistantCardMessage(gc.getGame().getCurrentPlayer().getNickname(), "FOX"));
        }
        catch(Exception ignored){}
        assertEquals("FOX", gc.getGame().getPlayers().get(0).getLastAssistantCardPlayed().get().getName());

        assertTrue(gc.getPlanningPhaseDone());
        assertEquals(0, gc.getCurrentPlayerIndex());

        List<Student> bag = new ArrayList<>(30);
        for(int i = 0; i < 10; i++)
            bag.add(new Student(Color.PINK));

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
            assertEquals(gc.getGame().getCurrentPlayer(),
                    gc.getGame().getBoard().getIslands().getIslandFromID(1).getOwner().get());
            assertTrue(gc.getMotherNatureMoved());
            assertThrows(WrongMessageSentException.class,
                    () -> gc.getMessage(new MotherNatureStepsMessage(gc.getGame().getCurrentPlayer().getNickname(), 1)));

            gc.getMessage(new CloudChoiceMessage(gc.getGame().getCurrentPlayer().getNickname(), 0));
            assertEquals(0, gc.getGame().getBoard().getCloud(0).getStudents().size());
            assertEquals(7, gc.getGame().getPlayers().get(0).getSchool().getHall().getStudents().size());

            assertEquals(gc.getGame().getCurrentPlayer().getNickname(), gc.getGame().getCurrentPlayer().getNickname());
            assertEquals(Constants.PLAYER_MOVES, gc.getMovesLeft());
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
            assertTrue(gc.getGame().getCurrentPlayer().getSchool().getTable("PINK").getHasProfessor());

            gc.getMessage(new CharacterCardMessage(gc.getGame().getCurrentPlayer().getNickname(), 5));
            assertEquals(3, p1.getCoinsWallet());
            assertEquals(3, g.getBoard().getNumOfVetos());
            assertEquals(3, c1.getCost());
            assertTrue(g.getBoard().getIslands().getIslandFromID(1).hasVeto());

            gc.getMessage(new MotherNatureStepsMessage(gc.getGame().getCurrentPlayer().getNickname(), 2));
            assertEquals(gc.getGame().getCurrentPlayer(),
                    gc.getGame().getBoard().getIslands().getIslandFromID(3).getOwner().get());
            assertTrue(gc.getMotherNatureMoved());

            assertThrows(EmptyCloudException.class,
                    () -> gc.getMessage(new CloudChoiceMessage(gc.getGame().getCurrentPlayer().getNickname(), 0)));
            assertThrows(WrongMessageSentException.class,
                    () -> gc.getMessage(new PlayerNumberMessage(gc.getGame().getCurrentPlayer().getNickname(), 2)));
            assertThrows(CharacterCardAlreadyPlayedException.class,
                    () -> gc.getMessage(new CharacterCardMessage(gc.getGame().getCurrentPlayer().getNickname(), 8)));
            gc.getMessage(new CloudChoiceMessage(gc.getGame().getCurrentPlayer().getNickname(), 1));
            assertEquals(0, gc.getGame().getBoard().getCloud(1).getStudents().size());
            assertEquals(7, gc.getGame().getPlayers().get(0).getSchool().getHall().getStudents().size());

            assertEquals(0, gc.getCurrentPlayerIndex());
            assertEquals("Ludo", gc.getGame().getCurrentPlayer().getNickname());
            assertEquals(Constants.PLAYER_MOVES, gc.getMovesLeft());
            assertFalse(gc.getPlanningPhaseDone());
            assertFalse(gc.getMotherNatureMoved());
            assertFalse(gc.getPlayerActionPhaseDone());

            assertEquals(1, gc.getGame().getRoundNumber());
        }
        catch(Exception ignored){}

    }

    @Test
    public void testHandleCharacterCardChoice() throws WrongMessageSentException {
        GameController gc = new GameControllerExpertMode();
        PlayerNumberMessage message = new PlayerNumberMessage("Matteo", 2);
        gc.prepareGame(2);
        gc.setGameState(GameState.IN_GAME);
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", gc.getGame().getPlayersNumber());
        Player p2 = new Player(Wizard.PINK_WIZARD, "Ludo", gc.getGame().getPlayersNumber());
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
            ((GameControllerExpertMode) gc).handleCharacterCardChoice(new CharacterCardMessageString("Matteo", 9, "green"));
            assertEquals(gc.getGame().getCurrentPlayer().getCoinsWallet(), 2);
            assertTrue(gc.getGame().getCurrentPlayer().getCharacterCardAlreadyPlayed());

        } catch (CharacterCardAlreadyPlayedException e) {
            e.printStackTrace();
        } catch (NotEnoughCoinsException e) {
            e.printStackTrace();
        } catch (CharacterCardNotFoundException e) {
            e.printStackTrace();
        }
    }

}