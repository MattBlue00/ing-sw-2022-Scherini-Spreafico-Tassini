package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Cloud {

    private final int capacity;
    private boolean hasBeenChosen;
    private ArrayList<Student> students;

    public Cloud(int capacity) {
        this.capacity = capacity;
    }
}
