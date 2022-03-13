package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Cloud {
    private final int capacity;
    private boolean hasBeenChosen;
    private final Game game;
    private ArrayList<Student> studentsOnTheCloud;

    public Cloud(int capacity, Game game) {
        this.capacity = capacity;
        this.game = game;
    }
}
