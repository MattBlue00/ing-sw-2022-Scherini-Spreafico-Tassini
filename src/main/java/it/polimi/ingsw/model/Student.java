package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.FullTableException;
import it.polimi.ingsw.model.exceptions.NonExistentColorException;

public class Student {

    private final Color color;

    public Student(Color color) {
        this.color = color;
    }

    // Getter method

    public Color getColor() {
        return color;
    }

    // Student methods

    public void moveToHall(Player curr){
        curr.getSchool().getHall().addStudent(this);
    }

    public void moveToIsland(Island island){
        island.addStudent(this);
    }

    public void moveToTable(Player player) throws NonExistentColorException, FullTableException {
        player.getSchool().getTable(this.color.toString()).addStudent(this, player);
    }

}
