package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @Test
    public void testLoginState(){
        int playersNumber = 3;

        GameController gameController = new GameController();

        gameController.prepareGame(playersNumber);
        assertEquals(playersNumber, gameController.getGame().getPlayersNumber());
    }

    @Test
    public void testHandleAssistantCardChoice(){
        String user = "Samuele";
        String cardName = "FOX";

        GameController gameController = new GameController();
        PlayerNumberMessage message = new PlayerNumberMessage(user, 2);

        gameController.prepareGame(message.getPlayerNumber());

        assertEquals(2, gameController.getGame().getPlayersNumber());

        Player player1 = new Player(Wizard.BLUE_WIZARD, user, gameController.getGame().getConstants());
        Player player2 = new Player(Wizard.PINK_WIZARD, "Ludo", gameController.getGame().getConstants());
        gameController.getGame().addPlayer(player1);
        gameController.getGame().addPlayer(player2);
        gameController.getGame().setCurrentPlayer(player1);

        gameController.handleAssistantCardChoice(new AssistantCardMessage(user, cardName));

        if(gameController.getGame().getCurrentPlayer().getLatestAssistantCardPlayed() == null)
            System.out.println("ERROR");

        assertEquals(cardName, gameController.getGame().getCurrentPlayer().getLatestAssistantCardPlayed().getName());
        assertEquals(5, gameController.getGame().getCurrentPlayer().getLatestAssistantCardPlayed().getWeight());
        assertEquals(3, gameController.getGame().getCurrentPlayer().getLatestAssistantCardPlayed().getMotherNatureSteps());
        assertEquals(9, gameController.getGame().getCurrentPlayer().getDeck().size());
    }

    @Test
    public void testSetOrderWithTwoPlayers(){
        GameController gameController = new GameController();
        PlayerNumberMessage message = new PlayerNumberMessage("Samuele", 2);

        gameController.prepareGame(message.getPlayerNumber());

        List<Player> players = new ArrayList<>();
        players.add(new Player(Wizard.YELLOW_WIZARD, "Samuele", gameController.
                getGame().getConstants()));
        players.add(new Player(Wizard.BLUE_WIZARD, "Matteo", gameController.
                getGame().getConstants()));
        gameController.getGame().setPlayers(players);

        players.get(0).playAssistantCard("FOX");
        players.get(1).playAssistantCard("CHEETAH");

        gameController.setOrder();

        assertEquals(players.get(0).getNickname(), "Matteo");
        assertEquals(players.get(1).getNickname(), "Samuele");
    }

    @Test
    public void testSetOrderWithThreePlayers(){
        GameController gameController = new GameController();
        PlayerNumberMessage message = new PlayerNumberMessage("Samuele", 3);

        gameController.prepareGame(message.getPlayerNumber());

        List<Player> players = new ArrayList<>();
        players.add(new Player(Wizard.YELLOW_WIZARD, "Samuele", gameController.
                getGame().getConstants()));
        players.add(new Player(Wizard.BLUE_WIZARD, "Matteo", gameController.
                getGame().getConstants()));
        players.add(new Player(Wizard.PINK_WIZARD, "Ludo", gameController.
                getGame().getConstants()));

        gameController.getGame().setPlayers(players);

        assertEquals(players.get(0).getNickname(), "Samuele");
        assertEquals(players.get(1).getNickname(), "Matteo");
        assertEquals(players.get(2).getNickname(), "Ludo");

        players.get(0).playAssistantCard("TURTLE"); // weight = 10
        players.get(1).playAssistantCard("DOG"); // weight = 8
        players.get(2).playAssistantCard("CAT"); // weight = 3

        gameController.setOrder();

        assertEquals(players.get(0).getNickname(), "Ludo");
        assertEquals(players.get(1).getNickname(), "Matteo");
        assertEquals(players.get(2).getNickname(), "Samuele");
    }

    @Test
    public void testHandleStudentMovement() throws FullTableException, IslandNotFoundException,
            StudentNotFoundException, NonExistentColorException{
        GameController gameController = new GameController();
        PlayerNumberMessage m = new PlayerNumberMessage("Samuele", 2);

        gameController.prepareGame(m.getPlayerNumber());

        for(int i = 0; i < Constants.MAX_NUM_OF_ISLANDS; i++){
            try {
                Island currentIsland = gameController.getGame().getBoard().getIslands().getIslandFromID(i+1);
                if(currentIsland.getStudents().size()>0)
                    currentIsland.getStudents().clear();
            } catch (IslandNotFoundException ignored){}
        }

        String user = "Samuele";
        Player player = new Player(Wizard.GREEN_WIZARD, user, gameController.getGame().getConstants());
        Message message = new MoveToIslandMessage(player.getNickname(), "BLUE", 1);

        gameController.getGame().setCurrentPlayer(player);
        gameController.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.BLUE));

        gameController.handleStudentMovement(message);

        try {
            assertEquals(1, gameController.getGame().getBoard().getIslands().getIslandFromID(1).getStudents().size());
            assertEquals(0, gameController.getGame().getCurrentPlayer().getSchool().getHall().getStudents().size());
        } catch (IslandNotFoundException ignored){}

        Message message1 = new MoveToTableMessage(player.getNickname(), "RED");
        gameController.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.RED));
        gameController.handleStudentMovement(message1);

        try {
            assertEquals(1, gameController.getGame().getCurrentPlayer().getSchool().getTable("RED").getStudents().size());
            assertEquals(0, gameController.getGame().getCurrentPlayer().getSchool().getHall().getStudents().size());
        } catch (NonExistentColorException ignored){}
    }

    @Test
    public void testHandleMotherNature(){
        String user = "Samuele";
        int steps = 5;

        GameController gameController = new GameController();
        PlayerNumberMessage m = new PlayerNumberMessage("Samuele", 2);

        gameController.prepareGame(m.getPlayerNumber());

        Player player = new Player(Wizard.GREEN_WIZARD, user, gameController.getGame().getConstants());
        Message message = new MotherNatureStepsMessage(user,steps);
        gameController.getGame().setCurrentPlayer(player);

        player.setLatestAssistantCardPlayed(new AssistantCard(AssistantType.TURTLE));

        Random rand = new Random();
        int randomPos = rand.nextInt((12))+1;
        gameController.getGame().getBoard().setMotherNaturePos(randomPos);
        try {
            gameController.handleMotherNature(message);
        } catch (InvalidNumberOfStepsException e) {
            throw new RuntimeException(e);
        } catch (IslandNotFoundException e) {
            e.printStackTrace();
        }
        int pos = (randomPos + steps)%Constants.MAX_NUM_OF_ISLANDS;
        if(pos%Constants.MAX_NUM_OF_ISLANDS == 0)
            pos = 12;

        assertEquals(pos, gameController.getGame().getBoard().getMotherNaturePos());
    }

    @Test
    public void testHandleCloudChoice(){
        String user = "Samuele";

        Message message = new CloudChoiceMessage(user, 1);

        GameController gameController = new GameController(); // When game is created clouds are full
        PlayerNumberMessage m = new PlayerNumberMessage("Samuele", 2);

        gameController.prepareGame(m.getPlayerNumber());
        Player player = new Player(Wizard.PINK_WIZARD, user, gameController.getGame().getConstants());
        gameController.getGame().setCurrentPlayer(player);

        try {
            gameController.handleCloudChoice(message);
        } catch (EmptyCloudException e) {
            e.printStackTrace();
        }

        assertEquals(0, gameController.getGame().getBoard().getCloud(1).getStudents().size());
    }

    @Test
    public void testDeclareWinningPlayer(){
        GameController gameController = new GameController();
        PlayerNumberMessage message = new PlayerNumberMessage("Samuele", 3);

        gameController.prepareGame(message.getPlayerNumber());

        gameController.getGame().addPlayer(new Player(Wizard.YELLOW_WIZARD,"Samuele",
                gameController.getGame().getConstants()));
        gameController.getGame().addPlayer(new Player(Wizard.PINK_WIZARD,"Ludovica",
                gameController.getGame().getConstants()));
        gameController.getGame().addPlayer(new Player(Wizard.BLUE_WIZARD,"Matteo",
                gameController.getGame().getConstants()));

        gameController.getGame().getPlayers().get(0).getSchool().getTowerRoom().setTowersLeft(2);
        gameController.getGame().getPlayers().get(1).getSchool().getTowerRoom().setTowersLeft(0);
        gameController.getGame().getPlayers().get(2).getSchool().getTowerRoom().setTowersLeft(4);

        assertDoesNotThrow(gameController::declareWinningPlayer);
    }

    @Test
    public void testTie(){
        GameController gameController = new GameController();
        PlayerNumberMessage message = new PlayerNumberMessage("Samuele", 3);

        gameController.prepareGame(message.getPlayerNumber());

        gameController.getGame().addPlayer(new Player(Wizard.YELLOW_WIZARD,"Samuele",
                gameController.getGame().getConstants()));
        gameController.getGame().addPlayer(new Player(Wizard.PINK_WIZARD,"Ludovica",
                gameController.getGame().getConstants()));
        gameController.getGame().addPlayer(new Player(Wizard.BLUE_WIZARD,"Matteo",
                gameController.getGame().getConstants()));

        assertThrows(TieException.class, gameController::declareWinningPlayer);
    }

    @Test
    public void test10RoundsCompletedAndTie(){
        GameController gameController = new GameController();
        PlayerNumberMessage message = new PlayerNumberMessage("Samuele", 3);

        gameController.prepareGame(message.getPlayerNumber());

        gameController.getGame().addPlayer(new Player(Wizard.YELLOW_WIZARD,"Samuele",
                gameController.getGame().getConstants()));
        gameController.getGame().addPlayer(new Player(Wizard.PINK_WIZARD,"Ludovica",
                gameController.getGame().getConstants()));
        gameController.getGame().addPlayer(new Player(Wizard.BLUE_WIZARD,"Matteo",
                gameController.getGame().getConstants()));

        gameController.getGame().setRoundNumber(11);
        gameController.getGame().setCurrentPlayer(gameController.getGame().getPlayers().get(0));

        assertDoesNotThrow(gameController::winCheck);
    }

    @Test
    public void testIsStudentBagEmpty(){
        GameController gameController = new GameController();
        PlayerNumberMessage message = new PlayerNumberMessage("Samuele", 3);

        gameController.prepareGame(message.getPlayerNumber());
        gameController.getGame().getBoard().setStudentsBag(new ArrayList<>());

        gameController.getGame().addPlayer(new Player(Wizard.YELLOW_WIZARD,"Samuele",
                gameController.getGame().getConstants()));
        gameController.getGame().addPlayer(new Player(Wizard.PINK_WIZARD,"Ludovica",
                gameController.getGame().getConstants()));
        gameController.getGame().addPlayer(new Player(Wizard.BLUE_WIZARD,"Matteo",
                gameController.getGame().getConstants()));

        gameController.getGame().getPlayers().get(0).getSchool().getTowerRoom().setTowersLeft(2);
        gameController.getGame().getPlayers().get(1).getSchool().getTowerRoom().setTowersLeft(1);
        gameController.getGame().getPlayers().get(2).getSchool().getTowerRoom().setTowersLeft(4);

        assertTrue(gameController.isStudentBagEmpty());

        List<Student> students = new ArrayList<>();
        students.add(new Student(Color.RED));
        gameController.getGame().getBoard().setStudentsBag(students);
        assertFalse(gameController.isStudentBagEmpty());
    }

    @Test
    public void testNoTowersLeftCheck(){
        String user = "Samuele";
        GameController gameController = new GameController();
        PlayerNumberMessage message = new PlayerNumberMessage("Samuele", 2);

        gameController.prepareGame(message.getPlayerNumber());

        Player player = new Player(Wizard.YELLOW_WIZARD, user, gameController.getGame().getConstants());
        gameController.getGame().addPlayer(player);
        gameController.getGame().setCurrentPlayer(player);

        gameController.getGame().getCurrentPlayer().getSchool().getTowerRoom().setTowersLeft(0);
        assertTrue(gameController.noTowersLeftCheck());

        gameController.getGame().getCurrentPlayer().getSchool().getTowerRoom().setTowersLeft(2);
        assertFalse(gameController.noTowersLeftCheck());
    }

    @Test
    public void testGetMessageCaseLogin(){
        GameController gc = new GameController();
        PlayerNumberMessage message = new PlayerNumberMessage("Matteo", 2);

        gc.prepareGame(message.getPlayerNumber());
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", gc.getGame().getConstants());
        Player p2 = new Player(Wizard.PINK_WIZARD, "Ludo", gc.getGame().getConstants());
        gc.getGame().addPlayer(p1);
        gc.getGame().addPlayer(p2);
        gc.getGame().setCurrentPlayer(p1);
        try {
            gc.getMessage(new PlayerNumberMessage("Matteo", 2));
        }
        catch(Exception ignored){}
        assertEquals(2, gc.getGame().getPlayersNumber());
    }

    @Test
    public void testGetMessageCaseInGame(){
        GameController gc = new GameController();
        PlayerNumberMessage message = new PlayerNumberMessage("Matteo", 2);

        gc.prepareGame(message.getPlayerNumber());
        gc.setGameState(GameState.IN_GAME);
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", gc.getGame().getConstants());
        Player p2 = new Player(Wizard.PINK_WIZARD, "Ludo", gc.getGame().getConstants());
        gc.getGame().addPlayer(p1);
        gc.getGame().addPlayer(p2);
        gc.getGame().setCurrentPlayer(p1);
        gc.getGame().getBoard().setMotherNaturePos(12);

        try {
            gc.getMessage(new AssistantCardMessage("Matteo", "TURTLE"));
        }
        catch(Exception ignored){}

        if(gc.getGame().getPlayers().get(0).getLatestAssistantCardPlayed() == null)
            System.out.println("ASSISTANT ERROR");

        assertEquals("TURTLE", gc.getGame().getPlayers().get(0).getLatestAssistantCardPlayed().getName());
        assertThrows(WrongTurnException.class,
                () -> gc.getMessage(new AssistantCardMessage("Matteo", "FOX")));

        try {
            gc.getMessage(new AssistantCardMessage("Ludo", "FOX"));
        }
        catch(Exception ignored){}
        assertEquals("FOX", gc.getGame().getPlayers().get(0).getLatestAssistantCardPlayed().getName());

        assertTrue(gc.getPlanningPhaseDone());
        assertEquals(0, gc.getCurrentPlayerIndex());
        assertEquals("Ludo", gc.getGame().getPlayers().get(0).getNickname());
        assertEquals("Matteo", gc.getGame().getPlayers().get(1).getNickname());

        List<Student> bag = new ArrayList<>(30);
        for(int i = 0; i < 10; i++)
            bag.add(new Student(Color.PINK));
        gc.getGame().getBoard().setStudentsBag(bag);

        try {
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));

            gc.getMessage(new MoveToTableMessage("Ludo", "PINK"));
            gc.getMessage(new MoveToTableMessage("Ludo", "PINK"));
            gc.getMessage(new MoveToIslandMessage("Ludo", "PINK", 1));
            assertEquals(4, gc.getGame().getCurrentPlayer().getSchool().getHall().getStudents().size());
            assertTrue(gc.getGame().getCurrentPlayer().getSchool().getTable("PINK").getHasProfessor());
            assertThrows(WrongMessageSentException.class,
                    () -> gc.getMessage(new MoveToIslandMessage("Ludo", "PINK", 1)));

            gc.getMessage(new MotherNatureStepsMessage("Ludo", 1));

            if(gc.getGame().getBoard().getIslands().getIslandFromID(1).getOwner() == null)
                System.out.println("LUDO ERROR");

            assertEquals(gc.getGame().getCurrentPlayer(),
                    gc.getGame().getBoard().getIslands().getIslandFromID(1).getOwner());
            assertTrue(gc.hasMotherNatureMoved());
            assertThrows(WrongMessageSentException.class,
                    () -> gc.getMessage(new MotherNatureStepsMessage("Ludo", 1)));

            gc.getMessage(new CloudChoiceMessage("Ludo", 0));
            assertEquals(0, gc.getGame().getBoard().getCloud(0).getStudents().size());
            assertEquals(7, gc.getGame().getPlayers().get(0).getSchool().getHall().getStudents().size());

            assertEquals("Matteo", gc.getGame().getCurrentPlayer().getNickname());
            assertEquals(gc.getGame().getConstants().PLAYER_MOVES, gc.getMovesLeft());
            assertFalse(gc.hasMotherNatureMoved());
            assertFalse(gc.getPlayerActionPhaseDone());
            assertEquals(1, gc.getCurrentPlayerIndex());

            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));

            gc.getMessage(new MoveToTableMessage("Matteo", "PINK"));
            gc.getMessage(new MoveToTableMessage("Matteo", "PINK"));
            gc.getMessage(new MoveToIslandMessage("Matteo", "PINK", 3));
            assertEquals(4, gc.getGame().getCurrentPlayer().getSchool().getHall().getStudents().size());
            assertFalse(gc.getGame().getCurrentPlayer().getSchool().getTable("PINK").getHasProfessor());

            gc.getMessage(new MotherNatureStepsMessage("Matteo", 2));

            if(gc.getGame().getBoard().getIslands().getIslandFromID(3).getOwner() != null)
                System.out.println("MATTEO ERROR");

            assertNull(gc.getGame().getBoard().getIslands().getIslandFromID(3).getOwner());
            assertTrue(gc.hasMotherNatureMoved());

            assertThrows(WrongMessageSentException.class,
                    () -> gc.getMessage(new PlayerNumberMessage("Matteo", 2)));
            gc.getMessage(new CloudChoiceMessage("Matteo", 1));
            assertEquals(3, gc.getGame().getBoard().getCloud(1).getStudents().size());
            assertEquals(7, gc.getGame().getPlayers().get(0).getSchool().getHall().getStudents().size());

            assertEquals(0, gc.getCurrentPlayerIndex());
            assertEquals("Ludo", gc.getGame().getCurrentPlayer().getNickname());
            assertFalse(gc.getPlanningPhaseDone());
            assertFalse(gc.hasMotherNatureMoved());
            assertFalse(gc.getPlayerActionPhaseDone());

            assertEquals(2, gc.getGame().getRoundNumber());

        }
        catch(Exception ignored){}

    }

    @Test
    public void testPrepareGame(){
        String user = "Ludovica";
        int playersNumber = 2;
        PlayerNumberMessage message = new PlayerNumberMessage(user, playersNumber);

        GameController gameController = new GameController();

        gameController.prepareGame(message.getPlayerNumber());
        assertEquals(playersNumber, gameController.getGame().getPlayersNumber());
    }

    @Test
    public void testStartGame(){
        GameController gc = new GameController();
        gc.prepareGame(2);
        gc.getGame().addPlayer(new Player(Wizard.PINK_WIZARD, "Ludo", gc.getGame().getConstants()));
        gc.getGame().addPlayer(new Player(Wizard.BLUE_WIZARD, "Matteo", gc.getGame().getConstants()));
        gc.getGame().setCurrentPlayer(gc.getGame().getPlayers().get(0));
        gc.startGame();

        for(Player player : gc.getGame().getPlayers())
            assertEquals(7, player.getSchool().getHall().getStudents().size());

        assertEquals(gc.getGame().getCurrentPlayer(), gc.getGame().getPlayers().get(0));

    }

    /*
        This test verifies that players never occur in errors when playing Assistant Card, especially when facing
        borderline cases (detailed in the test method).
     */

    @Test
    public void isAssistantCardPlayableTest(){

        GameController gc = new GameController();
        gc.prepareGame(2);
        gc.getGame().addPlayer(new Player(Wizard.PINK_WIZARD, "Ludo", gc.getGame().getConstants()));
        gc.getGame().addPlayer(new Player(Wizard.BLUE_WIZARD, "Matteo", gc.getGame().getConstants()));
        gc.startGame();
        Player p1 = gc.getGame().getPlayers().get(0);
        Player p2 = gc.getGame().getPlayers().get(1);

        // Case 1: Player 1 plays CHEETAH, player 2 CANNOT play CHEETAH

        p1.playAssistantCard("CHEETAH");
        gc.getGame().setCurrentPlayer(p2);
        assertEquals("CHEETAH", p1.getLatestAssistantCardPlayed().getName());
        assertFalse(gc.isAssistantCardPlayable("CHEETAH"));
        p1.resetLatestAssistantCardPlayed();
        gc.getGame().setCurrentPlayer(p1);

        // Case 2: Player 1 plays FOX, player 2 has no other choice than playing FOX, so he plays it

        p1.playAssistantCard("FOX");
        assertEquals("FOX", p1.getLatestAssistantCardPlayed().getName());
        gc.getGame().setCurrentPlayer(p2);
        List<AssistantCard> deck = new ArrayList<>(1);
        deck.add(new AssistantCard(AssistantType.FOX));
        p2.setDeck(deck);
        assertEquals(1, p2.getDeck().size());
        assertTrue(gc.isAssistantCardPlayable("FOX"));

    }

    @Test
    public void testCatchActionPhase(){
        GameController gameController = new GameController();
        PlayerNumberMessage message = new PlayerNumberMessage("Samuele", 3);

        gameController.prepareGame(message.getPlayerNumber());

        gameController.getGame().addPlayer(new Player(Wizard.YELLOW_WIZARD,"Samuele",
                gameController.getGame().getConstants()));
        gameController.getGame().addPlayer(new Player(Wizard.PINK_WIZARD,"Ludovica",
                gameController.getGame().getConstants()));
        gameController.getGame().addPlayer(new Player(Wizard.BLUE_WIZARD,"Matteo",
                gameController.getGame().getConstants()));

        gameController.setGameState(GameState.IN_GAME);
        gameController.getGame().setCurrentPlayer(gameController.getGame().getPlayerFromNickname("Samuele"));
        try {
            gameController.getMessage(new AssistantCardMessage("Samuele", "CHEETAH"));
            gameController.getMessage(new AssistantCardMessage("Ludovica", "CAT"));
            gameController.getMessage(new AssistantCardMessage("Matteo", "DOG"));
        } catch (TryAgainException e) {
            throw new RuntimeException(e);
        }

        Player currentPlayer = gameController.getGame().getCurrentPlayer();
        try {
            for(int i = 0; i < Constants.TABLE_LENGTH; i++)
                currentPlayer.getSchool().getTable(Color.BLUE.toString()).addStudent(new Student(Color.BLUE),currentPlayer);
            currentPlayer.getSchool().getHall().addStudent(new Student(Color.BLUE));
        } catch (FullTableException | NonExistentColorException e) {
            throw new RuntimeException(e);
        }

        assertDoesNotThrow(
                () -> gameController.getMessage(new MoveToTableMessage(currentPlayer.getNickname(), Color.BLUE.toString())));
        gameController.setMovesLeft(0);
        assertDoesNotThrow(
                () -> gameController.getMessage(new MotherNatureStepsMessage(currentPlayer.getNickname(), 8)));
        gameController.setMotherNatureMoved(true);
        assertDoesNotThrow(
                (() -> gameController.getMessage(new CloudChoiceMessage(currentPlayer.getNickname(), 4))));
        try {
            gameController.getGame().takeStudentsFromCloud(1);
        } catch (EmptyCloudException e) {
            throw new RuntimeException(e);
        }
        assertDoesNotThrow(
                () -> gameController.getMessage(new CloudChoiceMessage(currentPlayer.getNickname(), 1)));

    }

    @Test
    public void testSetGame(){

        GameController gc = new GameController();
        Game g = new Game(2, new Constants(2));
        gc.setGame(g);
        assertEquals(gc.getGame(), g);

    }

}