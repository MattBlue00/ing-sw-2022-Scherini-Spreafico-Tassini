package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyBagException;
import it.polimi.ingsw.exceptions.EmptyCloudException;
import it.polimi.ingsw.exceptions.IslandNotFoundException;
import it.polimi.ingsw.utils.Constants;

import java.io.Serializable;
import java.util.*;

/**
 * This class contains all the "physical" elements of the game. Every game has one, and it is shared by both Normal
 * and Expert game modes (Expert game exclusive "veto tiles" are present but unused - and not shown - in Normal mode).
 */

public class GameBoard implements Serializable {

    private int motherNaturePos; // represent the current position of motherNature in the island data structure
    private final Cloud[] clouds;
    private final DoublyLinkedList islands;
    private List<Student> studentsBag;
    private int numOfVetos;

    /**
     * Game board constructor.
     *
     * @param playersNumber the number of players that will play with this board.
     * @param constants the set of constants for this game.
     */

    public GameBoard(int playersNumber, Constants constants){

        Random random = new Random();
        this.motherNaturePos = random.nextInt(12) + 1; // random position between 1 and 12

        this.clouds = new Cloud[playersNumber];
        this.studentsBag = new ArrayList<>();
        this.islands = new DoublyLinkedList();
        this.numOfVetos = 4;

        // filling the bag with the right amount of students

        for(int i = 0; i< Constants.STUDENTS_PER_COLOR; i++){
            studentsBag.add(new Student(Color.RED));
        }
        for(int i=0; i<Constants.STUDENTS_PER_COLOR; i++){
            studentsBag.add(new Student(Color.YELLOW));
        }
        for(int i=0; i<Constants.STUDENTS_PER_COLOR; i++){
            studentsBag.add(new Student(Color.BLUE));
        }
        for(int i=0; i<Constants.STUDENTS_PER_COLOR; i++){
            studentsBag.add(new Student(Color.GREEN));
        }
        for(int i=0; i<Constants.STUDENTS_PER_COLOR; i++){
            studentsBag.add(new Student(Color.PINK));
        }

        // preparing the students to put on the islands

        List<Student> islandStudentsList = new ArrayList<>(10);
        for(int i = 0; i < Constants.NUM_COLORS; i++){
            islandStudentsList.add(studentsBag.remove((Constants.STUDENTS_PER_COLOR*i)));
            islandStudentsList.add(studentsBag.remove((Constants.STUDENTS_PER_COLOR*i)+1));
        }
        Collections.shuffle(islandStudentsList);

        // putting the students on the islands (according to rules)

        int noStudentPos1 = motherNaturePos;
        int noStudentPos2;
        if(motherNaturePos > 6)
            noStudentPos2 = motherNaturePos - 6;
        else
            noStudentPos2 = motherNaturePos + 6;
        for(int i = 0; i < Constants.MAX_NUM_OF_ISLANDS; i++){
            try {
                if((i+1) != noStudentPos1 && (i+1) != noStudentPos2) {
                    Island currentIsland = islands.getIslandFromID(i+1);
                    currentIsland.addStudent(islandStudentsList.remove(islandStudentsList.size() - 1));
                }
            } catch (IslandNotFoundException e){
                e.printStackTrace();
            }
        }

        // shuffling the remaining students
        Collections.shuffle(studentsBag);

        // building and filling the clouds with students

        for(int i = 0; i<playersNumber; i++) {
            clouds[i] = new Cloud(constants.MAX_CLOUD_STUDENTS);
            for(int j = 0; j<clouds[i].getCapacity(); j++)
                clouds[i].addStudent(studentsBag.remove(studentsBag.size() - 1));
        }
    }

    /**
     * Returns Mother Nature's current position.
     *
     * @return an {@code int} representing the ID of the island where Mother Nature is.
     */

    public int getMotherNaturePos() {
        return motherNaturePos;
    }

    /**
     * Updates Mother Nature's current position.
     *
     * @param motherNaturePos an {@code int} representing the ID of the island where Mother Nature should be.
     */

    public void setMotherNaturePos(int motherNaturePos) {
        this.motherNaturePos = motherNaturePos;
    }

    /**
     * Returns the data structure containing the game islands.
     *
     * @return a {@link DoublyLinkedList} containing the game islands.
     */

    public DoublyLinkedList getIslands() { return islands; }

    /**
     * Returns the cloud of the given index (if the index is 0, the cloud is the 1st).
     *
     * @param i an {@code int} representing the index of the cloud to return.
     * @return the desired {@link Cloud}.
     */

    public Cloud getCloud(int i) {
        return clouds[i];
    }

    /**
     * Returns the bag of students of the game board.
     *
     * @return a list containing all the students of the bag.
     */

    public List<Student> getStudentsBag() {
        return studentsBag;
    }

    /**
     * Sets the bag of students of the game board.
     *
     * @param bag a list containing the new students of the bag.
     */

    public void setStudentsBag(List<Student> bag){
        this.studentsBag = bag;
    }

    /**
     * Returns the number of veto tiles available on the game board (Expert mode exclusive).
     *
     * @return an {@code int} representing the number of veto tiles available (0-4).
     */

    public int getNumOfVetos() { return numOfVetos; }

    /**
     * Updates the number of veto tiles available on the game board (Expert mode exclusive).
     *
     * @param numOfVetos an {@code int} representing the new number of veto tiles available.
     */

    public void setNumOfVetos(int numOfVetos) { this.numOfVetos = numOfVetos; }

    /**
     * Removes student from the bag and puts them onto the cloud until all the clouds are full.
     *
     * @throws EmptyBagException if the bag is empty.
     */

    public void refillClouds() throws EmptyBagException{
        for(Cloud c : clouds){
            for(int i=0; i< c.getCapacity(); i++) {
                if(studentsBag.size()==0)
                    throw new EmptyBagException("The student bag is empty!");
                Student tempStudent = studentsBag.remove(studentsBag.size()-1);
                c.addStudent(tempStudent);
            }
        }
    }

    /**
     * Moves Mother Nature of the given number of steps.
     *
     * @param steps a {@code int} representing the number of steps.
     */

    public void moveMotherNature(int steps){
        setMotherNaturePos((getMotherNaturePos() + steps)%(islands.getSize()));
    }

    /**
     * Takes all the students from the chosen cloud and moves them into the current player's hall.
     *
     * @param cloudID the given cloud ID.
     * @param curr the current player.
     * @throws EmptyCloudException if the chosen cloud is empty (has already been chosen).
     */

    public void takeStudentsFromCloud(int cloudID, Player curr) throws EmptyCloudException {
        clouds[cloudID].removeStudents().forEach(student -> student.moveToHall(curr));
    }

    /**
     * Checks:
     * 1) if there's a veto tiles on the selected island (if there is one, it is removed and nothing else is done) -
     * Expert mode exclusive.
     * 2) if the owner of the selected island is different from the current player. In case it is true, calculates the
     * owner's and the current player's influence: if the influence of the current player is higher than the owner's,
     * the current player becomes the selected island's owner.
     * 3) if two or three islands may be merged.
     *
     * @param currentPlayer the current player.
     * @param islandID an {@code int} representing the ID of the island to check.
     * @throws IslandNotFoundException if there is no island with the given ID.
    */

    public void islandConquerCheck(Player currentPlayer, int islandID) throws IslandNotFoundException{

        if(getIslands().getIslandFromID(islandID).hasVetoTile()) {
            getIslands().getIslandFromID(islandID).setHasVetoTile(false);
            numOfVetos++;
            return;
        }
        Island selectedIsland = islands.getIslandFromID(islandID);
        Player owner = selectedIsland.getOwner();
        if(owner != null) {
            if (!owner.equals(currentPlayer)) {
                int calcCurrent = selectedIsland.influenceCalc(currentPlayer);
                int calcOwner = selectedIsland.influenceCalc(owner);
                islandConquerAlgorithm(currentPlayer, selectedIsland, calcCurrent, calcOwner, islands);
                motherNaturePos = selectedIsland.getId();
            }
        }
        else{
            int calcCurrent = selectedIsland.influenceCalc(currentPlayer);
            islandConquerAlgorithm(currentPlayer, selectedIsland, calcCurrent, 0, islands);
            motherNaturePos = selectedIsland.getId();
        }

    }

    /**
     * Changes the islands' status (owner, towers, students), if criteria are met.
     *
     * @param currentPlayer the current player.
     * @param selectedIsland the {@code int} representing the ID of the island to check.
     * @param calcCurrent the {@code int} representing the current player's influence on the island to check.
     * @param calcOwner the {@code int} representing the influence of the owner of the island to check.
     * @param islands the data structure containing the islands of the game board.
     */

    public static void islandConquerAlgorithm
            (Player currentPlayer, Island selectedIsland, int calcCurrent, int calcOwner, DoublyLinkedList islands) {

        if(selectedIsland.getOwner() != null) {
            if (calcCurrent > calcOwner && calcCurrent > 0) {

                int towersToMove = selectedIsland.getNumOfTowers();
                TowerRoom towerRoomPreviousOwner = selectedIsland.getOwner().getSchool().getTowerRoom();
                TowerRoom towerRoomNewOwner = currentPlayer.getSchool().getTowerRoom();
                towerRoomPreviousOwner.setTowersLeft(towerRoomPreviousOwner.getTowersLeft() + towersToMove);
                selectedIsland.setOwner(currentPlayer);
                towerRoomNewOwner.setTowersLeft(towerRoomNewOwner.getTowersLeft() - towersToMove);
            }
        }
        else{
            if(calcCurrent > 0){
                selectedIsland.setOwner(currentPlayer);
                selectedIsland.setNumOfTowers(1);
                int numOfTowers = currentPlayer.getSchool().getTowerRoom().getTowersLeft();
                currentPlayer.getSchool().getTowerRoom().setTowersLeft(numOfTowers - 1);
            }
        }

        islands.mergeIslands(selectedIsland);

    }

    /**
     * Reassigns the IDs of the islands after a merge operation.
     */

    public static void reassignIslandIDs(DoublyLinkedList islands){

        Island head = islands.getHead();
        if(head.getId() != 1)
            head.setId(1);
        Island currentIsland = head;
        do{
            if(!(currentIsland.getId() == currentIsland.getNext().getId() - 1))
                currentIsland.getNext().setId(currentIsland.getId() + 1);
            currentIsland = currentIsland.getNext();
        }while(!currentIsland.getNext().equals(head));

    }

}
