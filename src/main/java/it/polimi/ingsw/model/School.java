package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.FullTableException;
import it.polimi.ingsw.model.exceptions.NonExistentColorException;
import it.polimi.ingsw.model.exceptions.StudentNotFoundException;

public class School {

    private Table tables[];
    private TowerRoom towerRoom;
    private Hall hall;

    public School(int numberOfPlayers){
        this.tables = new Table[Constants.NUM_COLORS];
        Color colors[] = Color.values();
        for(int i = 0; i < Constants.NUM_TABLES; i++){
            tables[i] = new Table(colors[i]);
        }
        // not a scalable solution, need to find a better way
        if(numberOfPlayers == 3){
            this.towerRoom = new TowerRoom(6);
            this.hall = new Hall(9);
        }
        else {
            this.towerRoom = new TowerRoom(8);
            this.hall = new Hall(7);
        }
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

}
