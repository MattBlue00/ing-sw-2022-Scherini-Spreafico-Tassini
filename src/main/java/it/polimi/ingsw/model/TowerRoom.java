package it.polimi.ingsw.model;

public class TowerRoom {

    private final int capacity;
    private int towersLeft;

    public TowerRoom(int capacity) {
        this.capacity = capacity;
        this.towersLeft = capacity;
    }

    // Getter and setter methods

    public int getTowersLeft() {
        return towersLeft;
    }

    public void setTowersLeft(int towersLeft) {
        this.towersLeft = towersLeft;
    }
}
