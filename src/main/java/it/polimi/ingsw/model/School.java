package it.polimi.ingsw.model;

public class School {
    private final Player owner;
    private final Hall hall;
    private final Table table;
    private final TowerRoom towerRoom;

    public School(Player owner, Hall hall, Table table, TowerRoom towerRoom) {
        this.owner = owner;
        this.hall = hall;
        this.table = table;
        this.towerRoom = towerRoom;
    }

    public void moveToTable(Student student){}

    public void moveToIsland(Island island){}
}
