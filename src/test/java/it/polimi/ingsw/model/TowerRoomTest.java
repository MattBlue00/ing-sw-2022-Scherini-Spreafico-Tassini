package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TowerRoomTest {

    @Test
    void getTowersLeft() {
        TowerRoom towerRoom = new TowerRoom(9);
        assertEquals(9, towerRoom.getTowersLeft());
    }

    @Test
    void setTowersLeft() {
        TowerRoom towerRoom = new TowerRoom(9);
        assertEquals(9, towerRoom.getTowersLeft());
        towerRoom.setTowersLeft(5);
        assertEquals(5, towerRoom.getTowersLeft());
    }
}