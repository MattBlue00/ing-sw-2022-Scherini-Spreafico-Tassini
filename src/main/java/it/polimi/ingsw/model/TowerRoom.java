package it.polimi.ingsw.model;

import java.io.Serializable;

public class TowerRoom implements Serializable {

    private int towersLeft;

    public TowerRoom(int capacity) {
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
