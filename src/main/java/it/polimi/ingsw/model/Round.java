package it.polimi.ingsw.model;

public class Round {

    private final int id;
    private boolean isActionPhase;
    private Player currentPlayer;
    private final Player firstPlayer;

    public Round(int id, Player firstPlayer) {
        this.id = id;
        this.isActionPhase = false;
        this.currentPlayer = firstPlayer;
        this.firstPlayer = firstPlayer;
    }

    public int getId() {
        return id;
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

    public void playerCheck() {} // decides who has to play next

}
