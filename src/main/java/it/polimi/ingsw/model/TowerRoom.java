package it.polimi.ingsw.model;

public class TowerRoom {
    private final int capacity;
    private int towersLeft;
    private final School school;

    public TowerRoom(int capacity, School school) {
        this.capacity = capacity;
        this.school = school;
    }

    public int getTowersLeft() {
        return towersLeft;
    }
}
