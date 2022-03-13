package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Hall {
    private final int capacity;
    private final School school;
    private ArrayList<Student> studentsInTheHall;

    public Hall(int capacity, School school) {
        this.capacity = capacity;
        this.school = school;
    }
}
