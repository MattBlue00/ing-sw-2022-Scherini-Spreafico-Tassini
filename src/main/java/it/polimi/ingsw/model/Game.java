package it.polimi.ingsw.model;

public class Game {
    private final int playersNumber;
    private final boolean isFinished;
    private final Player players[];


    public Game(int playersNumber, boolean isFinished) {
        this.playersNumber = playersNumber;
        this.isFinished = isFinished;
        this.players = new Player[playersNumber];
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
