package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullTableException;
import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.exceptions.StudentNotFoundException;
import it.polimi.ingsw.utils.Constants;

public class School {

    private Table tables[];
    private TowerRoom towerRoom;
    private Hall hall;

    public School(){
        this.tables = new Table[Constants.NUM_COLORS];
        Color colors[] = Color.values();
        for(int i = 0; i < Constants.NUM_TABLES; i++)
            tables[i] = new Table(colors[i]);
        this.hall = new Hall(Constants.MAX_HALL_STUDENTS);
        this.towerRoom = new TowerRoom(Constants.MAX_TOWERS);
    }

    // Getter methods

    public Hall getHall() {
        return hall;
    }

    public Table getTable(String color) throws NonExistentColorException {

        for (Table table : tables) {
            if (table.getColor().toString().equals(color))
                return table;
        }
        throw new NonExistentColorException("No " + color + " table was found.");

    }

    public TowerRoom getTowerRoom() {
        return towerRoom;
    }

    // School methods

    public void moveStudentToIsland(Island island, String color) throws StudentNotFoundException {
        this.hall.removeStudent(color).moveToIsland(island);
    }

    public void moveStudentToTable(Player player, String color) throws
            StudentNotFoundException, NonExistentColorException, FullTableException {

        this.hall.removeStudent(color).moveToTable(player);
    }

    /*public void moveStudentToHall(Player player, String color) throws NonExistentColorException {
        getTable(color).getStudents().get(tables.length-1).moveToHall(player);
    }*/

}
