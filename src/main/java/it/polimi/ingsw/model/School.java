package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.utils.Constants;

import java.io.Serializable;

/**
 * The school is one of the "physical" elements of the game. It is divided in three parts: the {@link Hall}, the dining
 * room (with a {@link Table} for each {@link Color}), and the {@link TowerRoom}. Each {@link Player} has one, and it
 * is perhaps the most crucial element of the game.
 */

public class School implements Serializable {

    private final Table[] tables;
    private final TowerRoom towerRoom;
    private final Hall hall;

    /**
     * School constructor.
     *
     * @param constants the set of constants of the game.
     */

    public School(Constants constants){
        // dining room
        this.tables = new Table[Constants.NUM_COLORS];
        Color[] colors = Color.values();
        for(int i = 0; i < Constants.NUM_TABLES; i++)
            tables[i] = new Table(colors[i]);
        // hall
        this.hall = new Hall(constants.MAX_HALL_STUDENTS);
        // tower room
        this.towerRoom = new TowerRoom(constants.MAX_TOWERS);
    }

    /**
     * Returns the school's {@link Hall}.
     *
     * @return the school's {@link Hall}.
     */

    public Hall getHall() {
        return hall;
    }

    /**
     * Returns the table of the given color between the ones in the school, if present.
     *
     * @param color the color of the desired table.
     * @return the table of the desired color from the school.
     * @throws NonExistentColorException if a non-existent color is somehow provided (client-side controls should
     * prevent this, so it should never be thrown).
     */

    public Table getTable(String color) throws NonExistentColorException {
        for (Table table : tables) {
            if (table.getColor().toString().equals(color))
                return table;
        }
        throw new NonExistentColorException("No " + color + " table was found.");

    }

    /**
     * Returns the school's {@link TowerRoom}.
     *
     * @return the school's {@link TowerRoom}.
     */

    public TowerRoom getTowerRoom() {
        return towerRoom;
    }

    /**
     * Actually removes a student of the given color from the {@link Hall} and puts it onto the given {@link Island},
     * if possible.
     *
     * @param island the {@link Island} to move the student to.
     * @param color the color of the student to move.
     * @throws StudentNotFoundException if a student of the given color does not currently exist in the school's
     * {@link Hall}.
     */

    public void moveStudentToIsland(Island island, String color) throws StudentNotFoundException {
        this.hall.removeStudent(color).moveToIsland(island);
    }

    /**
     * Actually removes a student of the given color from the {@link Hall} and places it at the proper school's
     * {@link Table}, if possible.
     *
     * @param player the player whose coins' wallet may be updated.
     * @param color the color of the student to move.
     * @throws StudentNotFoundException if a student of the given color does not currently exist in the school's
     * {@link Hall}.
     * @throws NonExistentColorException if a non-existent color is somehow provided (client-side controls should
     * prevent this, so it should never be thrown).
     * @throws FullTableException if the table of the given color present in the school is currently full.
     */

    public void moveStudentToTable(Player player, String color) throws
            StudentNotFoundException, NonExistentColorException, FullTableException {
        this.hall.removeStudent(color).moveToTable(player);
    }

}
