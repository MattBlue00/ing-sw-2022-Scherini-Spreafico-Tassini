package it.polimi.ingsw.model;

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

    public void moveToTable(String color){
        // TODO: need to be implemented
    }
}
