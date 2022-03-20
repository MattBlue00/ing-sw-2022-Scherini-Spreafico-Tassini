package it.polimi.ingsw.model;

public class School {

    private final int NUM_TABLES = 5;
    private Table tables[];
    private TowerRoom towerRoom;
    private Hall hall;

    public School(int numberOfPlayers){
        this.tables = new Table[5];
        Color colors[] = Color.values();
        for(int i = 0; i < NUM_TABLES; i++){
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

    // getter and setter
    public Hall getHall() {
        return hall;
    }

    // methods
    public void moveStudentToIsland(int islandID, String color){
        this.hall.removeStudent(color).ifPresent(student -> student.moveToIsland(islandID));
    }

    public void moveStudentToTable(Player player, String color){
        this.hall.removeStudent(color).ifPresent(student -> student.moveToTable(color));
    }

}
