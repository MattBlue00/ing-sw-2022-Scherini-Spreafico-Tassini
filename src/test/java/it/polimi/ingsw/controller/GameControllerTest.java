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
    public void testLoginState(){
        String user = "Samuele";
        int playersNumber = 2;
        PlayerNumberReply message = new PlayerNumberReply(user, playersNumber);

        GameController gameController = new GameController();

        gameController.loginState(message);
        assertEquals(playersNumber, gameController.getGame().getPlayersNumber());
    }

    @Test
    public void testHandleAssistantCardChoice(){
        String user = "Samuele";
        String cardName = "FOX";

        Player player = new Player(Wizard.BLUE_WIZARD, user);
        GameController gameController = new GameController();

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
    public void testSetOrder(){
        GameController gameController = new GameController();
        List<Player> players = new ArrayList<>();
        players.add(new Player(Wizard.YELLOW_WIZARD, "Samuele"));
        players.add(new Player(Wizard.BLUE_WIZARD, "Matteo"));
        gameController.getGame().setPlayers(players);

        players.get(0).playAssistantCard("FOX");
        players.get(1).playAssistantCard("CHEETAH");

        gameController.setOrder();

        assertEquals(players.get(0).getNickname(), "Matteo");
        assertEquals(players.get(1).getNickname(), "Samuele");
    }

    @Test
    public void testHandleStudentMovement(){
        GameController gameController = new GameController();
        String user = "Samuele";
        Player player = new Player(Wizard.GREEN_WIZARD, user);
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
    public void testHandleMotherNature(){
        String user = "Samuele";
        Player player = new Player(Wizard.GREEN_WIZARD, user);
        int steps = 5;
        Message message = new MotherNatureStepsReply(user,steps);

        GameController gameController = new GameController();
        gameController.getGame().setCurrentPlayer(player);

        player.setLastAssistantCardPlayed(new AssistantCard(AssistantType.TURTLE));

        Random rand = new Random();
        int randomPos = rand.nextInt((12)+1);
        gameController.getGame().getBoard().setMotherNaturePos(randomPos);
        try {
            gameController.handleMotherNature(message);
        } catch (InvalidNumberOfStepsException e) {
            e.printStackTrace();
        }
        System.out.println(randomPos+" "+(randomPos+steps)%12);
        assertEquals((randomPos+steps)%12, gameController.getGame().getBoard().getMotherNaturePos());
    }

    @Test
    public void testHandleCloudChoice(){
        String user = "Samuele";
        Player player = new Player(Wizard.PINK_WIZARD, user);

        Message message = new CloudChoiceReply(user, 1);

        GameController gameController = new GameController(); // When game is created clouds are full
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

        gameController.getGame().addPlayer(new Player(Wizard.YELLOW_WIZARD,"Samuele"));
        gameController.getGame().addPlayer(new Player(Wizard.PINK_WIZARD,"Ludovica"));
        gameController.getGame().addPlayer(new Player(Wizard.BLUE_WIZARD,"Matteo"));

        gameController.getGame().getPlayers().get(0).getSchool().getTowerRoom().setTowersLeft(2);
        gameController.getGame().getPlayers().get(1).getSchool().getTowerRoom().setTowersLeft(1);
        gameController.getGame().getPlayers().get(2).getSchool().getTowerRoom().setTowersLeft(4);

        Player player = gameController.declareWinningPlayer();

        assertEquals(player, gameController.getGame().getPlayers().get(1));
    }

    @Test
    public void testIsStudentBagEmpty(){
        GameController gameController = new GameController();
        gameController.getGame().getBoard().setStudentsBag(new ArrayList<>());

        gameController.getGame().addPlayer(new Player(Wizard.YELLOW_WIZARD,"Samuele"));
        gameController.getGame().addPlayer(new Player(Wizard.PINK_WIZARD,"Ludovica"));
        gameController.getGame().addPlayer(new Player(Wizard.BLUE_WIZARD,"Matteo"));

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
        Player player =new Player(Wizard.YELLOW_WIZARD,user);
        gameController.getGame().setCurrentPlayer(player);

        gameController.getGame().getCurrentPlayer().getSchool().getTowerRoom().setTowersLeft(0);
        assertTrue(gameController.noTowersLeftCheck());

        gameController.getGame().getCurrentPlayer().getSchool().getTowerRoom().setTowersLeft(2);
        assertFalse(gameController.noTowersLeftCheck());
    }

}