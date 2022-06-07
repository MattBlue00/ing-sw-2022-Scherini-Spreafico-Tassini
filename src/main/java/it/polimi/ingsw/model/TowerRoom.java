package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * The tower room is a third of a {@link School}. Each school has one, and it holds the player's towers. A tower, if
 * placed on an island, indicates that the island has been conquered. On an island, there are as many towers as the
 * number of islands that have been merged to create the examined one. If a tower room is empty, it means that the
 * owner of the school's empty tower room has won the game.
 */

public class TowerRoom implements Serializable {

    private int towersLeft;

    /**
     * Tower room constructor.
     *
     * @param capacity the maximum (and initial) number of towers present in the tower room.
     */

    public TowerRoom(int capacity) {
        this.towersLeft = capacity;
    }

    /**
     * Returns the number of towers left in the tower room.
     *
     * @return an {@code int} representing the number of towers left in the tower room.
     */

    public int getTowersLeft() {
        return towersLeft;
    }

    /**
     * Updates the number of towers left in the tower room.
     *
     * @param towersLeft an {@code int} representing the new number of towers left in the tower room.
     */

    public void setTowersLeft(int towersLeft) {
        this.towersLeft = towersLeft;
    }

}
