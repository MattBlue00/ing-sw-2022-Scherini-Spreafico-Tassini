package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final int playersNumber;
    private boolean isFinished;
    private final Player players[];
    private List<Island> islands;
    private List<Student> students;
    private final Cloud clouds[];
    private int motherNaturePosition;


    public Game(int playersNumber, boolean isFinished) {
        this.playersNumber = playersNumber;
        this.isFinished = isFinished;
        this.players = new Player[playersNumber];
        this.islands = new ArrayList<>(12);
        this.students = new ArrayList<>(130);
        this.clouds = new Cloud[playersNumber];
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
}
