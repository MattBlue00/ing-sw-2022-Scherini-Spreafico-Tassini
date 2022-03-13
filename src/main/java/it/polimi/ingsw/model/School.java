package it.polimi.ingsw.model;

public class School {
    private final Player owner;
    private final Game game;

    public School(Player owner, Game game) {
        this.owner = owner;
        this.game = game;
    }

    public void moveToTable(Student student){}

    public void moveToIsland(Island island){}
}
