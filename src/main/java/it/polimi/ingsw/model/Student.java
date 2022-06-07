package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.NonExistentColorException;

import java.io.Serializable;

/**
 * The students are the "passive" protagonists of the game. Each player can decide for their lives, and place them
 * in their school or onto the islands as they wish. Doing so, the players gain influence and eventually the craftier
 * wins the game. Each student has a {@link Color}.
 */

public class Student implements Serializable {

    private final Color color;

    /**
     * Student constructor.
     */

    public Student(Color color) {
        this.color = color;
    }

    /**
     * Returns the color of the student.
     *
     * @return the {@link Color} of the student.
     */

    public Color getColor() {
        return color;
    }

    /**
     * Adds the student to the given player's {@link Hall}.
     *
     * @param player the given {@link Player}.
     */

    public void moveToHall(Player player){
        player.getSchool().getHall().addStudent(this);
    }

    /**
     * Adds the student to the given island.
     *
     * @param island the {@link Island} to move the student to.
     */

    public void moveToIsland(Island island){
        island.addStudent(this);
    }

    /**
     * Adds the student to the given player's dining room, at the proper {@link Table}, if possible.
     *
     * @param player the given {@link Player}.
     * @throws NonExistentColorException if a table of a non-existent color is somehow accessed (it should never happen,
     * so it is safely ignorable).
     * @throws FullTableException if the table of the student's color from the given player's dining room is full.
     */

    public void moveToTable(Player player) throws NonExistentColorException, FullTableException {
        player.getSchool().getTable(this.color.toString()).addStudent(this, player);
    }

}
