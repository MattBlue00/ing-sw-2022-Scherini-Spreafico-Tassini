package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    // game variables

    private final int playersNumber;
    private boolean isFinished;
    private final Player players[];
    private List<Island> islands;
    private List<Student> students;
    private final Cloud clouds[];
    private int motherNaturePosition;

    // variables specific to a round

    private int roundNum;
    private boolean isActionPhase; // useful?
    private Player currentPlayer;
    private Player firstPlayer;


    public Game(int playersNumber) {
        this.playersNumber = playersNumber;
        this.isFinished = false;
        this.players = new Player[playersNumber];
        this.islands = new ArrayList<>(12);
        this.students = new ArrayList<>(130);
        this.clouds = new Cloud[playersNumber];
        for(int i = 0; i < playersNumber; i++){
            if(playersNumber == 3)
                clouds[i] = new Cloud(4);
            else
                clouds[i] = new Cloud(3);
        }
        this.roundNum = 1;
        this.isActionPhase = false;

        // TODO: random first position of Mother Nature

        // TODO: first player and current player initialization

        /*
        *   Now we init the game with the required number of students, tower ecc. based
        *   on the number of players.
        */
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
    }

    public boolean isActionPhase() {
        return isActionPhase;
    }

    public void setActionPhase(boolean actionPhase) {
        isActionPhase = actionPhase;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public void choosePlayerOrder() {}

    public void useAssistantCard() {}

    public void initClouds() {}

    public void winCheck() {}

    public void moveMotherNature() {}

    public void modifySchool() {}

    public void professorCheck() {}

    public void chooseStudents() {}

}
