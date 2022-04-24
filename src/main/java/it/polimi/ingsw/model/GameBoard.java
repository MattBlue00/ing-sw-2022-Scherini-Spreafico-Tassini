package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.EmptyCloudException;
import it.polimi.ingsw.model.exceptions.IslandNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameBoard {

    // GameBoard variables
    private int motherNaturePos; // represent the current position of motherNature in the island array
    private final Cloud clouds[];
    private DoublyLinkedList islands;
    private List<Student> studentsBag;
    private int numOfVetos;


    public GameBoard(int playerNum){
        this.clouds = new Cloud[playerNum];
        this.studentsBag = new ArrayList<>();
        this.islands = new DoublyLinkedList();
        this.numOfVetos = 4;

        // just to fill the bag with something
        for(int i=0; i<15; i++){
            studentsBag.add(new Student(Color.RED));
        }

        for(int i = 0; i<playerNum; i++) {
            Cloud cloud = new Cloud(3); // not scalable, need to find a better solution then if/else
            clouds[i] = cloud;
            for(int j=0; j<cloud.getCapacity(); j++)
                cloud.addStudent(studentsBag.remove(studentsBag.size() - 1));
        }
    }


    // getter and setter

    public int getMotherNaturePos() {
        return motherNaturePos;
    }

    public void setMotherNaturePos(int motherNaturePos) {
        this.motherNaturePos = motherNaturePos;
    }

    public DoublyLinkedList getIslands() { return islands; }

    public int getNumOfVetos() { return numOfVetos; }

    public void setNumOfVetos(int numOfVetos) { this.numOfVetos = numOfVetos; }

    // methods

    /*
        When called by the Game this method removes a student from the bag
        then add the extracted student to the students array of the cloud, one cloud at a time,
        it throws an EmptyBagException if the bag is already empty
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

    // TODO: moveMotherNature will activate islandConquerCheck()?
    /*
        Move motherNature in a new island.
        It receives an integer steps, then set motherNature position to the new position.
        Since islands is a circular array
     */
    public void moveMotherNature(int steps){
        setMotherNaturePos((getMotherNaturePos() + steps)%(islands.getSize()));
    }

    /*
        When called by the Game this method call the remove from the single cloud,
        gets a List of students as a response and then move all the students, one at a time,
        to the current player's Hall
     */
    public void takeStudentsFromCloud(int cloudID, Player curr) throws EmptyCloudException {
        clouds[cloudID].removeStudents().forEach(student -> student.moveToHall(curr));
    }

    /*
        When called it checks if the owner of the island is different from the current player.
        If it is a different Player the method calls influenceCalc()
        and if the influence of the current player is higher than the owner influence
        we change the ownership of the Island.
        In the end the method calls mergeIslands() to see if it is possible to merge the current
        Island with the near islands.
    */

    public void islandConquerCheck(Player currentPlayer, int islandID) throws IslandNotFoundException{

        if(getIslands().getIslandFromID(islandID).hasVeto()) {
            getIslands().getIslandFromID(islandID).setVeto(false);
            numOfVetos++;
            return;
        }
        Island selectedIsland = islands.getIslandFromID(islandID);
        Optional<Player> owner = selectedIsland.getOwner();
        if(owner.isPresent()) {
            if (!owner.get().equals(currentPlayer)) {
                int calcCurrent = selectedIsland.influenceCalc(currentPlayer);
                int calcOwner = selectedIsland.influenceCalc(owner.get());
                islandConquerAlgorithm(currentPlayer, selectedIsland, calcCurrent, calcOwner, islands);
            }
        }
        else{
            int calcCurrent = selectedIsland.influenceCalc(currentPlayer);
            islandConquerAlgorithm(currentPlayer, selectedIsland, calcCurrent, 0, islands);
        }

    }

    public static void islandConquerAlgorithm
            (Player currentPlayer, Island selectedIsland, int calcCurrent, int calcOwner, DoublyLinkedList islands) {

        if(selectedIsland.getOwner().isPresent()) {
            if (calcCurrent > calcOwner && calcCurrent > 0) {

                int towersToMove = selectedIsland.getNumOfTowers();
                TowerRoom towerRoomPreviousOwner = selectedIsland.getOwner().get().getSchool().getTowerRoom();
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

    // For debugging

    public Cloud getCloud(int i) {
        return clouds[i];
    }

    public List<Student> getStudentsBag() {
        return studentsBag;
    }

    public void setStudentsBag(List<Student> bag){
        this.studentsBag = bag;
    }

}
