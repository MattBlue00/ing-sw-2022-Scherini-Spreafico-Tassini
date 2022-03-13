package it.polimi.ingsw.model;

public class TowerRoom {
    private final int capacity;
    private int towersLeft;

    public TowerRoom(int capacity) {
        this.capacity = capacity;
    }

    public int getTowersLeft() {
        return towersLeft;
    }
}
