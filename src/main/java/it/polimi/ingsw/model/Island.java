package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.NonExistentTableException;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class Island {

    private Optional<Player> owner;
    private final int id;
    private int numOfTowers;
    private List<Student> students;
    private Island prev; // to iterate the DoublyLinkedList islands
    private Island next; // to iterate the DoublyLinkedList islands
    private boolean veto; // when the flag is true it's not possible to conquer the island
                              // influenceCalc isn't calculated


    public Island(int id) {
        this.id = id;
        this.veto = false;
        this.students = new ArrayList<>();
        this.owner = Optional.empty();
    }

    // Getter and setter methods

    public int getId() {
        return id;
    }

    public Optional<Player> getOwner(){
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = Optional.of(owner);
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

    public boolean hasVeto() { return veto; }

    public void setVeto(boolean veto) { this.veto = veto; }

    public List<Student> getStudents() { return students; }

    // Island methods

    // TODO: needs a lot of testing
    /*
        The method counts the number of towers and the number of students of a
        player (passed as parameter), the only students counted are the students
        with the same color as the professors in the player's school
     */
    public int influenceCalc(Player currentPlayer){

        int influencePoints = 0;

        try {

            influencePoints += influenceCalcStudents(currentPlayer);

            if(owner.isPresent() && owner.get().equals(currentPlayer))
                influencePoints += numOfTowers;

        } catch (NonExistentTableException e) {
            e.printStackTrace();
        }

        return influencePoints;
    }

    /*
        This special version of the influenceCalc method excludes one specific color out of the influence calculation.
     */
    public int influenceCalc(Player currentPlayer, Color color){

        int influencePoints = 0;

        try {

            influencePoints += influenceCalcStudents(currentPlayer, color);

            if(owner.isPresent() && owner.get().equals(currentPlayer))
                influencePoints += numOfTowers;

        } catch (NonExistentTableException e) {
            e.printStackTrace();
        }
        return influencePoints;

    }

    /*
        This special version of the influenceCalc method excludes the towers out of the influence calculation.
     */
    public int influenceCalcWithoutTowers(Player currentPlayer){
        int influencePoints = 0;

        try {

            influencePoints += influenceCalcStudents(currentPlayer);

        } catch (NonExistentTableException e) {
            e.printStackTrace();
        }

        return influencePoints;
    }

    /*
        This method contains the algorithms which returns the correct number of students that assign influence points
        to a player.
     */
    public int influenceCalcStudents(Player currentPlayer) throws NonExistentTableException{

        int validStudents = 0;

        for(Student student : students){
            // check if the professor (with the same color of the student) is present in the player's school
            if(currentPlayer.getSchool().getTable(student.getColor().toString()).getHasProfessor()){
                validStudents++;
            }
        }

        return validStudents;
    }

    /*
        This method contains the algorithms which returns the correct number of students that assign influence points
        to a player, without considering the students of a specific color.
     */
    public int influenceCalcStudents(Player currentPlayer, Color color) throws NonExistentTableException{

        int validStudents = 0;

        for(Student student : students){

            // skips the desired color
            if(student.getColor().equals(color)) continue;

            // check if the professor (with the same color of the student) is present in the player's school
            if(currentPlayer.getSchool().getTable(student.getColor().toString()).getHasProfessor()){
                validStudents++;
            }

        }

        return validStudents;

    }

}
