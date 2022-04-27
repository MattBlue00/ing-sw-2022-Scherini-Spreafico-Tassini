package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.charactercards.Flagman;
import it.polimi.ingsw.model.charactercards.Healer;
import it.polimi.ingsw.model.charactercards.Innkeeper;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.network.message.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerExpertModeTest {

    // TODO: need to test character cards

    @Test
    public void testGetMessageCaseInGame() throws WrongMessageSentException {
        GameController gc = new GameControllerExpertMode();
        PlayerNumberReply message = new PlayerNumberReply("Matteo", 2);
        gc.prepareGame(message);
        gc.setGameState(GameState.IN_GAME);
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", gc.getGame().getPlayersNumber());
        Player p2 = new Player(Wizard.PINK_WIZARD, "Ludo", gc.getGame().getPlayersNumber());
        gc.getGame().addPlayer(p1);
        gc.getGame().addPlayer(p2);
        gc.getGame().setCurrentPlayer(p1);
        CharacterCard c1 = new Healer();
        CharacterCard c2 = new Innkeeper();
        CharacterCard c3 = new Flagman();
        GameExpertMode g = (GameExpertMode) gc.getGame();
        g.addCharacterCards(new CharacterCard[]{c1, c2, c3});
        p1.setCoinsWallet(5);

        try {
            gc.getMessage(new AssistantCardReply("Matteo", "TURTLE"));
        }
        catch(Exception ignored){}
        assertEquals("TURTLE", gc.getGame().getPlayers().get(0).getLastAssistantCardPlayed().get().getName());
        assertThrows(AssistantCardAlreadyPlayedException.class,
                () -> gc.getMessage(new AssistantCardReply("Ludo", "TURTLE")));
        assertThrows(WrongTurnException.class,
                () -> gc.getMessage(new AssistantCardReply("Matteo", "FOX")));
        try {
            gc.getMessage(new AssistantCardReply("Ludo", "FOX"));
        }
        catch(Exception ignored){}
        assertEquals("FOX", gc.getGame().getPlayers().get(0).getLastAssistantCardPlayed().get().getName());

        assertTrue(gc.getPlanningPhaseDone());
        assertEquals(0, gc.getCurrentPlayerIndex());
        assertEquals("Ludo", gc.getGame().getPlayers().get(0).getNickname());
        assertEquals("Matteo", gc.getGame().getPlayers().get(1).getNickname());

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

            gc.getMessage(new MoveToTableReply("Ludo", "PINK"));
            gc.getMessage(new MoveToTableReply("Ludo", "PINK"));
            gc.getMessage(new MoveToIslandReply("Ludo", "PINK", 1));
            assertEquals(4, gc.getGame().getCurrentPlayer().getSchool().getHall().getStudents().size());
            assertTrue(gc.getGame().getCurrentPlayer().getSchool().getTable("PINK").getHasProfessor());
            assertThrows(WrongMessageSentException.class,
                    () -> gc.getMessage(new MoveToIslandReply("Ludo", "PINK", 1)));

            gc.getMessage(new MotherNatureStepsReply("Ludo", 1));
            assertEquals(gc.getGame().getCurrentPlayer(),
                    gc.getGame().getBoard().getIslands().getIslandFromID(1).getOwner().get());
            assertTrue(gc.getMotherNatureMoved());
            assertThrows(WrongMessageSentException.class,
                    () -> gc.getMessage(new MotherNatureStepsReply("Ludo", 1)));

            gc.getMessage(new CloudChoiceReply("Ludo", 0));
            assertEquals(0, gc.getGame().getBoard().getCloud(0).getStudents().size());
            assertEquals(7, gc.getGame().getPlayers().get(0).getSchool().getHall().getStudents().size());

            assertEquals("Matteo", gc.getGame().getCurrentPlayer().getNickname());
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

            gc.getMessage(new MoveToTableReply("Matteo", "PINK"));
            gc.getMessage(new MoveToTableReply("Matteo", "PINK"));
            gc.getMessage(new MoveToIslandReply("Matteo", "PINK", 3));
            assertEquals(4, gc.getGame().getCurrentPlayer().getSchool().getHall().getStudents().size());
            assertTrue(gc.getGame().getCurrentPlayer().getSchool().getTable("PINK").getHasProfessor());

            gc.getMessage(new CharacterCardReply("Matteo", 5));
            assertEquals(3, p1.getCoinsWallet());
            assertEquals(3, g.getBoard().getNumOfVetos());
            assertEquals(3, c1.getCost());
            assertTrue(g.getBoard().getIslands().getIslandFromID(1).hasVeto());

            gc.getMessage(new MotherNatureStepsReply("Matteo", 2));
            assertEquals(gc.getGame().getCurrentPlayer(),
                    gc.getGame().getBoard().getIslands().getIslandFromID(3).getOwner().get());
            assertTrue(gc.getMotherNatureMoved());

            assertThrows(EmptyCloudException.class,
                    () -> gc.getMessage(new CloudChoiceReply("Matteo", 0)));
            assertThrows(WrongMessageSentException.class,
                    () -> gc.getMessage(new PlayerNumberReply("Matteo", 2)));
            assertThrows(CharacterCardAlreadyPlayedException.class,
                    () -> gc.getMessage(new CharacterCardReply("Matteo", 8)));
            gc.getMessage(new CloudChoiceReply("Matteo", 1));
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

}