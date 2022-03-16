package it.polimi.ingsw.model;

import java.util.List;
import java.util.ArrayList;

public class Table {

    private final Color color;
    private boolean hasProfessor;
    private List<Student> students;

    public Table(Color color) {
        this.color = color;
        this.students = new ArrayList<>(10);
        this.hasProfessor = false;
    }

    public boolean isHasProfessor() {
        return hasProfessor;
    }

    public void setHasProfessor(boolean hasProfessor) {
        this.hasProfessor = hasProfessor;
    }
}
