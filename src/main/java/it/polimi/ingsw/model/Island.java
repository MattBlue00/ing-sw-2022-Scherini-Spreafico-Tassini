package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NonExistentColorException;
import it.polimi.ingsw.model.charactercards.Villager;
import it.polimi.ingsw.model.charactercards.Centaur;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * The Island is one of the "physical" elements of the game. Two or more islands form an archipelagos, which has a
 * peculiar circular structure, as specified in the data structure that holds the whole set of islands
 * ({@link DoublyLinkedList}). The islands are a key element of the game, as they are involved in the most common win
 * conditions of the game. In order to be such key factors, each one has an owner, as well as a set of Students and
 * a number of towers that depends on the number of islands that have been merged to the considered one.
 */

public class Island implements Serializable {

    private Player owner;
    private final int id;
    private int numOfTowers;
    private final List<Student> students;
    private Island prev; // to iterate the DoublyLinkedList islands
    private Island next; // to iterate the DoublyLinkedList islands
    private boolean hasVetoTile; // when the flag is true, it's not possible to calculate the influences

    /**
     * Island constructor.
     *
     * @param id an {@code int} that represents the island's ID.
     */

    public Island(int id) {
        this.id = id;
        this.hasVetoTile = false;
        this.students = new ArrayList<>();
        this.owner = null;
        this.numOfTowers = 0;
    }

    /**
     * Returns the ID of the island.
     *
     * @return an {@code int} representing the ID of the island.
     */

    public int getId() {
        return id;
    }

    /**
     * Returns the owner of the island.
     *
     * @return the {@link Player} who currently owns the island.
     */

    public Player getOwner(){
        return owner;
    }

    /**
     * Updates the owner of the island.
     *
     * @param owner the {@link Player} who conquered the island.
     */

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Returns the island that is placed right before this one in the {@link DoublyLinkedList}.
     *
     * @return the {@link Island} that is placed right before this one in the {@link DoublyLinkedList}.
     */

    public Island getPrev() {
        return prev;
    }

    /**
     * Updates the island that is placed right before this one in the {@link DoublyLinkedList}.
     *
     * @param prev the {@link Island} that is now placed right after this one in the {@link DoublyLinkedList}.
     */

    public void setPrev(Island prev) {
        this.prev = prev;
    }

    /**
     * Returns the island that is placed right after this one in the {@link DoublyLinkedList}.
     *
     * @return the {@link Island} that is placed right after this one in the {@link DoublyLinkedList}.
     */

    public Island getNext() {
        return next;
    }

    /**
     * Updates the island that is placed right after this one in the {@link DoublyLinkedList}.
     *
     * @param next the {@link Island} that is now placed right after this one in the {@link DoublyLinkedList}.
     */

    public void setNext(Island next) {
        this.next = next;
    }

    /**
     * Returns the number of towers currently present on the island.
     *
     * @return an {@code int} representing the number of towers currently present on the island.
     */

    public int getNumOfTowers() {
        return numOfTowers;
    }

    /**
     * Updates the number of towers currently present on the island.
     *
     * @param numOfTowers an {@code int} representing the new number of towers currently present on the island.
     */

    public void setNumOfTowers(int numOfTowers) {
        this.numOfTowers = numOfTowers;
    }

    /**
     * Checks if the island has a veto tile on itself.
     *
     * @return {@code true} if the island has a veto tile on itself, {@code false} otherwise.
     */

    public boolean hasVetoTile() { return hasVetoTile; }

    /**
     * Updates the veto tile status of the island.
     *
     * @param hasVetoTile {@code true} if the island should now have a veto tile on it, {@code false} if it shouldn't.
     */

    public void setHasVetoTile(boolean hasVetoTile) { this.hasVetoTile = hasVetoTile; }

    /**
     * Returns a list of the students currently present on the island.
     *
     * @return a list of the students currently present on the island.
     */

    public List<Student> getStudents() { return students; }

    /**
     * Adds a student onto the island.
     *
     * @param student the student to add.
     */

    public void addStudent(Student student){
        students.add(student);
    }

    /**
     * Calculates, given a player, its influence. The influence is the sum of:
     * 1) the number of towers present on the island, if the island's owner equals the given player.
     * 2) the number of students of the colors whose professor the given player has in their school.
     *
     * @param currentPlayer the {@link Player} whose influence on the island needs to be calculated.
     * @return an {@code int} representing the influence the given player has on the island (must be equal or greater
     * than 0).
     */

    public int influenceCalc(Player currentPlayer){

        int influencePoints = 0;

        try {

            influencePoints += influenceCalcStudents(currentPlayer);

            if(owner != null && owner.equals(currentPlayer))
                influencePoints += numOfTowers;

        } catch (NonExistentColorException e) {
            e.printStackTrace();
        }

        return influencePoints;
    }

    /**
     * Calculates, given a player, its influence. The influence is the sum of:
     * 1) the number of towers present on the island, if the island's owner equals the given player.
     * 2) the number of students of the colors whose professor the given player has in their school.
     * This special version takes a given color out of the calculation (this method is callable only by the
     * {@link Villager}'s doEffect, so it is an Expert mode exclusive).
     *
     * @param currentPlayer the {@link Player} whose influence on the island needs to be calculated.
     * @return an {@code int} representing the influence the given player has on the island (must be equal or greater
     * than 0).
     */

    public int influenceCalc(Player currentPlayer, Color color){

        int influencePoints = 0;

        try {

            influencePoints += influenceCalcStudents(currentPlayer, color);

            if(owner != null && owner.equals(currentPlayer))
                influencePoints += numOfTowers;

        } catch (NonExistentColorException e) {
            e.printStackTrace();
        }
        return influencePoints;

    }

    /**
     * Calculates, given a player, its influence. The influence is the sum of:
     * 1) the number of towers present on the island, if the island's owner equals the given player.
     * 2) the number of students of the colors whose professor the given player has in their school.
     * This special version takes the number of towers out of the calculation (this method is callable only by the
     * {@link Centaur}'s doEffect, so it is an Expert mode exclusive).
     *
     * @param currentPlayer the {@link Player} whose influence on the island needs to be calculated.
     * @return an {@code int} representing the influence the given player has on the island (must be equal or greater
     * than 0).
     */

    public int influenceCalcWithoutTowers(Player currentPlayer){
        int influencePoints = 0;

        try {

            influencePoints += influenceCalcStudents(currentPlayer);

        } catch (NonExistentColorException e) {
            e.printStackTrace();
        }

        return influencePoints;
    }

    /**
     * Returns the correct number of students that assign influence points to the given player.
     *
     * @param currentPlayer the {@link Player} whose influence needs to be calculated.
     * @return an {@code int} representing the given player's total amount of students eligible for influence points
     * on the island.
     * @throws NonExistentColorException if a table of a non-existent color is somehow accessed (it should never
     * happen, so it is safely ignorable).
     */

    public int influenceCalcStudents(Player currentPlayer) throws NonExistentColorException {

        int validStudents = 0;

        for(Student student : students){
            // check if the professor (with the same color of the student) is present in the player's school
            if(currentPlayer.getSchool().getTable(student.getColor().toString()).getHasProfessor()){
                validStudents++;
            }
        }

        return validStudents;
    }

    /**
     * Returns the correct number of students that assign influence points to the given player, without taking into
     * account a given color.
     *
     * @param currentPlayer the {@link Player} whose influence needs to be calculated.
     * @param color the {@link Color} to exclude from the calculation.
     * @return an {@code int} representing the given player's total amount of students eligible for influence points
     * on the island.
     * @throws NonExistentColorException if a table of a non-existent color is somehow accessed (it should never
     * happen, so it is safely ignorable).
     */

    public int influenceCalcStudents(Player currentPlayer, Color color) throws NonExistentColorException {

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
