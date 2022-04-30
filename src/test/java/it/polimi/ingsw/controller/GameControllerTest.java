package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.network.message.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    @Test
    public void testLoginState() throws WrongMessageSentException {
        String user = "Samuele";
        int playersNumber = 3;
        PlayerNumberReply message = new PlayerNumberReply(user, playersNumber);

        GameController gameController = new GameController();

        gameController.prepareGame(message);
        gameController.loginState(message);
        assertEquals(playersNumber, gameController.getGame().getPlayersNumber());
    }

    @Test
    public void testHandleAssistantCardChoice() throws WrongMessageSentException {
        String user = "Samuele";
        String cardName = "FOX";

        GameController gameController = new GameController();
        PlayerNumberReply message = new PlayerNumberReply(user, 2);

        gameController.prepareGame(message);

        assertEquals(2, gameController.getGame().getPlayersNumber());

        Player player = new Player(Wizard.BLUE.toString(), user, gameController.getGame().getPlayersNumber() );

        gameController.getGame().setCurrentPlayer(player);
        try {
            gameController.handleAssistantCardChoice(new AssistantCardReply(user, cardName));
        } catch (AssistantCardAlreadyPlayedException e) {
            e.printStackTrace();
        }

        assertEquals(cardName, gameController.getGame().getCurrentPlayer().getLastAssistantCardPlayed().get().getName());
        assertEquals(5, gameController.getGame().getCurrentPlayer().getLastAssistantCardPlayed().get().getWeight());
        assertEquals(3, gameController.getGame().getCurrentPlayer().getLastAssistantCardPlayed().get().getMotherNatureSteps());
        assertEquals(9, gameController.getGame().getCurrentPlayer().getDeck().size());
    }

    @Test
    public void testSetOrder() throws WrongMessageSentException {
        GameController gameController = new GameController();
        PlayerNumberReply message = new PlayerNumberReply("Samuele", 2);

        gameController.prepareGame(message);

        List<Player> players = new ArrayList<>();
        players.add(new Player(Wizard.YELLOW.toString(), "Samuele", gameController.
                getGame().getPlayersNumber()));
        players.add(new Player(Wizard.BLUE.toString(), "Matteo", gameController.
                getGame().getPlayersNumber()));
        gameController.getGame().setPlayers(players);

        players.get(0).playAssistantCard("FOX");
        players.get(1).playAssistantCard("CHEETAH");

        gameController.setOrder();

        assertEquals(players.get(0).getNickname(), "Matteo");
        assertEquals(players.get(1).getNickname(), "Samuele");
    }

    @Test
    public void testHandleStudentMovement() throws FullTableException, IslandNotFoundException,
            StudentNotFoundException, NonExistentColorException, WrongMessageSentException {
        GameController gameController = new GameController();
        PlayerNumberReply m = new PlayerNumberReply("Samuele", 2);

        gameController.prepareGame(m);

        String user = "Samuele";
        Player player = new Player(Wizard.GREEN.toString(), user, gameController.getGame().getPlayersNumber());
        Message message = new MoveToIslandReply(player.getNickname(), "BLUE", 1);



        gameController.getGame().setCurrentPlayer(player);
        gameController.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.BLUE));

        gameController.handleStudentMovement(message);

        try {
            assertEquals(1, gameController.getGame().getBoard().getIslands().getIslandFromID(1).getStudents().size());
            assertEquals(0, gameController.getGame().getCurrentPlayer().getSchool().getHall().getStudents().size());
        } catch (IslandNotFoundException e) {
            e.printStackTrace();
        }

        Message message1 = new MoveToTableReply(player.getNickname(), "RED");
        gameController.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.RED));
        gameController.handleStudentMovement(message1);

        try {
            assertEquals(1, gameController.getGame().getCurrentPlayer().getSchool().getTable("RED").getStudents().size());
            assertEquals(0, gameController.getGame().getCurrentPlayer().getSchool().getHall().getStudents().size());
        } catch (NonExistentColorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHandleMotherNature() throws WrongMessageSentException {
        String user = "Samuele";
        int steps = 5;

        GameController gameController = new GameController();
        PlayerNumberReply m = new PlayerNumberReply("Samuele", 2);

        gameController.prepareGame(m);

        Player player = new Player(Wizard.GREEN.toString(), user, gameController.getGame().getPlayersNumber());
        Message message = new MotherNatureStepsReply(user,steps);
        gameController.getGame().setCurrentPlayer(player);

        player.setLastAssistantCardPlayed(new AssistantCard(AssistantType.TURTLE));

        Random rand = new Random();
        int randomPos = rand.nextInt((12)+1);
        gameController.getGame().getBoard().setMotherNaturePos(randomPos);
        try {
            gameController.handleMotherNature(message);
        } catch (InvalidNumberOfStepsException | IslandNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals((randomPos+steps)%12, gameController.getGame().getBoard().getMotherNaturePos());
    }

    @Test
    public void testHandleCloudChoice() throws WrongMessageSentException {
        String user = "Samuele";

        Message message = new CloudChoiceReply(user, 1);

        GameController gameController = new GameController(); // When game is created clouds are full
        PlayerNumberReply m = new PlayerNumberReply("Samuele", 2);

        gameController.prepareGame(m);
        Player player = new Player(Wizard.PINK.toString(), user, gameController.getGame().getPlayersNumber());
        gameController.getGame().setCurrentPlayer(player);

        try {
            gameController.handleCloudChoice(message);
        } catch (EmptyCloudException e) {
            e.printStackTrace();
        }

        assertEquals(0, gameController.getGame().getBoard().getCloud(1).getStudents().size());
    }

    @Test
    public void testDeclareWinningPlayer() throws WrongMessageSentException {
        GameController gameController = new GameController();
        PlayerNumberReply message = new PlayerNumberReply("Samuele", 3);

        gameController.prepareGame(message);

        gameController.getGame().addPlayer(new Player(Wizard.YELLOW.toString(),"Samuele",
                gameController.getGame().getPlayersNumber()));
        gameController.getGame().addPlayer(new Player(Wizard.PINK.toString(),"Ludovica",
                gameController.getGame().getPlayersNumber()));
        gameController.getGame().addPlayer(new Player(Wizard.BLUE.toString(),"Matteo",
                gameController.getGame().getPlayersNumber()));

        gameController.getGame().getPlayers().get(0).getSchool().getTowerRoom().setTowersLeft(2);
        gameController.getGame().getPlayers().get(1).getSchool().getTowerRoom().setTowersLeft(1);
        gameController.getGame().getPlayers().get(2).getSchool().getTowerRoom().setTowersLeft(4);

        Player player = gameController.declareWinningPlayer();

        assertEquals(player, gameController.getGame().getPlayers().get(1));
    }

    @Test
    public void testIsStudentBagEmpty() throws WrongMessageSentException {
        GameController gameController = new GameController();
        PlayerNumberReply message = new PlayerNumberReply("Samuele", 3);

        gameController.prepareGame(message);
        gameController.getGame().getBoard().setStudentsBag(new ArrayList<>());

        gameController.getGame().addPlayer(new Player(Wizard.YELLOW.toString(),"Samuele",
                gameController.getGame().getPlayersNumber()));
        gameController.getGame().addPlayer(new Player(Wizard.PINK.toString(),"Ludovica",
                gameController.getGame().getPlayersNumber()));
        gameController.getGame().addPlayer(new Player(Wizard.BLUE.toString(),"Matteo",
                gameController.getGame().getPlayersNumber()));

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
    public void testNoTowersLeftCheck() throws WrongMessageSentException {
        String user = "Samuele";
        GameController gameController = new GameController();
        PlayerNumberReply message = new PlayerNumberReply("Samuele", 2);

        gameController.prepareGame(message);

        Player player = new Player(Wizard.YELLOW.toString(), user, gameController.getGame().getPlayersNumber());
        gameController.getGame().setCurrentPlayer(player);

        gameController.getGame().getCurrentPlayer().getSchool().getTowerRoom().setTowersLeft(0);
        assertTrue(gameController.noTowersLeftCheck());

        gameController.getGame().getCurrentPlayer().getSchool().getTowerRoom().setTowersLeft(2);
        assertFalse(gameController.noTowersLeftCheck());
    }

    @Test
    public void testGetMessageCaseLogin() throws WrongMessageSentException {
        GameController gc = new GameController();
        PlayerNumberReply message = new PlayerNumberReply("Matteo", 2);

        gc.prepareGame(message);
        gc.setGameState(GameState.LOGIN);
        Player p1 = new Player(Wizard.BLUE.toString(), "Matteo", gc.getGame().getPlayersNumber());
        Player p2 = new Player(Wizard.PINK.toString(), "Ludo", gc.getGame().getPlayersNumber());
        gc.getGame().addPlayer(p1);
        gc.getGame().addPlayer(p2);
        gc.getGame().setCurrentPlayer(p1);
        try {
            gc.getMessage(new PlayerNumberReply("Matteo", 2));
        }
        catch(Exception ignored){}
        assertEquals(2, gc.getGame().getPlayersNumber());
    }

    @Test
    public void testGetMessageCaseInGame() throws WrongMessageSentException {
        GameController gc = new GameController();
        PlayerNumberReply message = new PlayerNumberReply("Matteo", 2);

        gc.prepareGame(message);
        gc.setGameState(GameState.IN_GAME);
        Player p1 = new Player(Wizard.BLUE.toString(), "Matteo", gc.getGame().getPlayersNumber());
        Player p2 = new Player(Wizard.PINK.toString(), "Ludo", gc.getGame().getPlayersNumber());
        gc.getGame().addPlayer(p1);
        gc.getGame().addPlayer(p2);
        gc.getGame().setCurrentPlayer(p1);

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

            gc.getMessage(new MotherNatureStepsReply("Matteo", 2));
            assertEquals(gc.getGame().getCurrentPlayer(),
                    gc.getGame().getBoard().getIslands().getIslandFromID(3).getOwner().get());
            assertTrue(gc.getMotherNatureMoved());

            assertThrows(EmptyCloudException.class,
                    () -> gc.getMessage(new CloudChoiceReply("Matteo", 0)));
            assertThrows(WrongMessageSentException.class,
                    () -> gc.getMessage(new PlayerNumberReply("Matteo", 2)));
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

    @Test
    public void testPrepareGame() throws WrongMessageSentException {
        String user = "Ludovica";
        int playersNumber = 2;
        PlayerNumberReply message = new PlayerNumberReply(user, playersNumber);

        GameController gameController = new GameController();

        gameController.prepareGame(message);
        assertEquals(playersNumber, gameController.getGame().getPlayersNumber());
    }

    @Test
    public void testHandlePlayerLoginWithThreePlayers() throws WrongMessageSentException {
        String user1 = "Ludovica";
        int playersNumber = 3;
        PlayerNumberReply message = new PlayerNumberReply(user1, playersNumber);

        GameController gameController = new GameController();

        gameController.prepareGame(message);
        assertEquals(playersNumber, gameController.getGame().getPlayersNumber());

        String user2 = "Matteo";
        String user3 = "Samuele";

        PlayerLoginRequest m1 = new PlayerLoginRequest(user1, Wizard.BLUE.toString());
        PlayerLoginRequest m2 = new PlayerLoginRequest(user2, Wizard.PINK.toString());
        PlayerLoginRequest m3 = new PlayerLoginRequest(user3, Wizard.GREEN.toString());

        gameController.handlePlayerLogin(m1);
        gameController.handlePlayerLogin(m2);
        gameController.handlePlayerLogin(m3);

        assertEquals(user1, gameController.getGame().getPlayers().get(0).getNickname());
        assertEquals(user2, gameController.getGame().getPlayers().get(1).getNickname());
        assertEquals(user3, gameController.getGame().getPlayers().get(2).getNickname());
    }

    @Test
    public void testHandlePlayerLoginWithTwoPlayers() throws WrongMessageSentException {
        String user1 = "Ludovica";
        int playersNumber = 2;
        PlayerNumberReply message = new PlayerNumberReply(user1, playersNumber);

        GameController gameController = new GameController();

        gameController.prepareGame(message);
        assertEquals(playersNumber, gameController.getGame().getPlayersNumber());

        String user2 = "Matteo";

        PlayerLoginRequest m2 = new PlayerLoginRequest(user2, Wizard.PINK.toString());
        PlayerLoginRequest m1 = new PlayerLoginRequest(user1, Wizard.BLUE.toString());

        gameController.handlePlayerLogin(m1);
        gameController.handlePlayerLogin(m2);

        assertEquals(user1, gameController.getGame().getPlayers().get(0).getNickname());
        assertEquals(user2, gameController.getGame().getPlayers().get(1).getNickname());
    }

    @Test
    public void testGetMessageCaseLoginWithHandlePlayerLogin() throws WrongMessageSentException {
        String user1 = "Ludovica";
        int playersNumber = 2;
        PlayerNumberReply message = new PlayerNumberReply(user1, playersNumber);

        GameController gameController = new GameController();

        gameController.prepareGame(message);
        assertEquals(playersNumber, gameController.getGame().getPlayersNumber());

        String user2 = "Matteo";
        gameController.setGameState(GameState.LOGIN);

        PlayerLoginRequest m2 = new PlayerLoginRequest(user2, Wizard.PINK.toString());
        PlayerLoginRequest m1 = new PlayerLoginRequest(user1, Wizard.GREEN.toString());

        try {
            gameController.getMessage(m1);
            gameController.getMessage(m2);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        assertEquals(user1, gameController.getGame().getPlayers().get(0).getNickname());
        assertEquals("Matteo", gameController.getGame().getPlayers().get(1).getNickname());
        assertEquals(gameController.getGame().getPlayersNumber(),
                gameController.getGame().getPlayers().size());
        assertEquals(GameState.IN_GAME, gameController.getGameState());
    }

    @Test
    public void testGetMessageCaseInit() throws WrongMessageSentException {
        String user1 = "Ludovica";
        int playersNumber = 2;
        PlayerNumberReply message = new PlayerNumberReply(user1, playersNumber);
        GameController gameController = new GameController();

        gameController.prepareGame(message);
        gameController.setGameState(GameState.INIT);

        try {
            gameController.getMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(playersNumber, gameController.getGame().getPlayersNumber());
        assertEquals(GameState.LOGIN, gameController.getGameState());
    }
}