package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NonExistentTableException;

import java.util.ArrayList;

public class Island {

    private Player owner;
    private final int id;
    private int ownerInfluence;
    private int numOfTowers;
    private ArrayList<Student> students;
    private Island prev; // to iterate the DoublyLinkedList islands
    private Island next; // to iterate the DoublyLinkedList islands


    public Island(int id) {
        this.id = id;
        this.ownerInfluence = 0;
    }

    public int getId() {
        return id;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getOwnerInfluence() {
        return ownerInfluence;
    }

    public void setOwnerInfluence(int ownerInfluence) {
        this.ownerInfluence = ownerInfluence;
    }

    public void addStudent(Student student){
        students.add(student);
    }

    public Island getPrev() {
        return prev;
    }

    public void setPrev(Island prev) {
        this.prev = prev;
    }

    public Island getNext() {
        return next;
    }

    public void setNext(Island next) {
        this.next = next;
    }

    public int getNumOfTowers() {
        return numOfTowers;
    }

    public void setNumOfTowers(int numOfTowers) {
        this.numOfTowers = numOfTowers;
    }

    // TODO: need a lot of testing
    /*
        The method counts the number of towers and the number of students of a
        player (passed as parameter), the only students counted are the students
        with the same color as the professors in the player's school
     */
    public int influenceCalc(Player currentPlayer){
        int validStudents = 0;
        int currentPlayerInfluence = numOfTowers;
        for(Student student : students){
            try {
                // check if the professor (with the same color of the student) is present in the player's school
                if(currentPlayer.getSchool().getTable(student.getColor().toString()).getHasProfessor()){
                    validStudents++;
                }
            } catch (NonExistentTableException e) {
                e.printStackTrace();
            }
        }
        currentPlayerInfluence = currentPlayerInfluence + validStudents;
        return currentPlayerInfluence;
    }

    /*
    This special version of the influenceCalc method excludes one specific color out of the influence calculation.
     */
    public int influenceCalc(Player currentPlayer, Color color){
        int validStudents = 0;
        int currentPlayerInfluence = numOfTowers;
        for(Student student : students){
            try {

                // skips the desired color
                if(student.getColor().equals(color)) continue;

                // check if the professor (with the same color of the student) is present in the player's school
                if(currentPlayer.getSchool().getTable(student.getColor().toString()).getHasProfessor()){
                    validStudents++;
                }
            } catch (NonExistentTableException e) {
                e.printStackTrace();
            }
        }
        currentPlayerInfluence = currentPlayerInfluence + validStudents;
        return currentPlayerInfluence;
    }

}
