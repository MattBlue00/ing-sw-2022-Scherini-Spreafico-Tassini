package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NonExistentTableException;

public class Student {

    private final Color color;

    public Student(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    // methods

    public void moveToHall(Player curr){
        curr.getSchool().getHall().addStudent(this);
    }

    public void moveToIsland(int islandID){
        // TODO: we need to implement islands first
    }

    public void moveToTable(Player player, String color){

        try{
            player.getSchool().getTable(color).addStudent(this, player);
        }
        catch(NonExistentTableException e){
            e.printStackTrace();
        }
    }
}
