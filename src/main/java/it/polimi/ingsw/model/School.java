package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NonExistentTableException;

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

    // getter
    public Hall getHall() {
        return hall;
    }

    public Table getTable(String color) throws NonExistentTableException{

        for (Table table : tables) {
            if (table.getColor().toString().equals(color))
                return table;
        }
        throw new NonExistentTableException("No " + color + " table was found.");

    }

    // methods
    public void moveStudentToIsland(int islandID, String color){
        this.hall.removeStudent(color).ifPresent(student -> student.moveToIsland(islandID));
    }

    public void moveStudentToTable(Player player, String color){
        this.hall.removeStudent(color).ifPresent(student -> student.moveToTable(player));
    }

}
