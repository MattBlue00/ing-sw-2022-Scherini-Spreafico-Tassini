package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Table {
    private final Color color;
    private boolean hasProfessor;
    private final School school;
    private ArrayList<Student> studentsInTheTable;

    public Table(Color color, School school) {
        this.color = color;
        this.school = school;
    }
}
