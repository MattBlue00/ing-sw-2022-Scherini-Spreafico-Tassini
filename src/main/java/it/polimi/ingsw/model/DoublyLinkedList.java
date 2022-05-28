package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IslandNotFoundException;
import it.polimi.ingsw.utils.Constants;

import java.io.Serializable;

/*
    In this list, the last node of the doubly linked list contains the address of the first node and
    the first node contains the address of the last node.
    In a circular doubly linked list, there is a cycle and none of the node pointers are set to null.
 */

public class DoublyLinkedList implements Serializable {
    private Island head = null; // first element of the list
    private int size; // to check the number of islands in the list

    public int getSize() {
        return size;
    }

    /*
        Create the list with the initial number of islands
     */
    public DoublyLinkedList() {
        for(int i = 1; i <= Constants.MAX_NUM_OF_ISLANDS; i++)
            addIsland(new Island(i));
    }

    /*
        The method adds an Island at the bottom the list, if the list is empty
        it just adds the first element and set prev and next to the same Island.
    */
    public void addIsland(Island island){
        if(head == null){
            island.setNext(island);
            island.setPrev(island);
            head = island;
            size++;
            return;
        }
        // find last node in the list if list is not empty
        Island tail = head.getPrev();
        // next of island will point to head since list is circular
        island.setNext(head);
        head.setPrev(island);
        island.setPrev(tail);
        tail.setNext(island);
        //keep track of the increment of the list's size
        size++;
    }

    /*
        MergeIslands gets an island as parameter.
        If the island owner is the same as the near islands owners, the method merges
        the current island with the near ones.
        It removes the near islands and sum the values of the current owner on the current island.
     */
    public void mergeIslands(Island island){
        Island prev = island.getPrev();
        Island next = island.getNext();
        if(island.getOwner() == null) // safety return (should never happen)
            return;
        if(prev.getOwner() != null && prev.getOwner().equals(island.getOwner())){
            island.setNumOfTowers(island.getNumOfTowers() + prev.getNumOfTowers());
            prev.getStudents().forEach(island::addStudent);
            removeIsland(prev);
        }
        if(next.getOwner() != null && next.getOwner().equals(island.getOwner())){
            island.setNumOfTowers(island.getNumOfTowers() + next.getNumOfTowers());
            next.getStudents().forEach(island::addStudent);
            removeIsland(next);
        }
    }

    /*
        The method removes an island, and decrease the size of the data structure.
        if the removed item is the head, it declares as new head the next island.
     */
    public void removeIsland(Island island) {
        if(island != null) {
            if(island.equals(head))
                head = island.getNext();
            island.getPrev().setNext(island.getNext());
            island.getNext().setPrev(island.getPrev());
            size--;
        }
    }

    /*
        Custom getter that take an integer islandID and return the Island with the same IslandID.
        If there isn't an Island with that ID it throws an InvalidIslandException
     */
    public Island getIslandFromID(int islandID) throws IslandNotFoundException {
        Island island = head;
        int islandsToCheck = size;
        while(islandsToCheck > 0){
            if(island.getId()==islandID)
                return island;
            island = island.getNext();
            islandsToCheck--;
        }
        throw new IslandNotFoundException("No Island " + islandID + " was found.");
    }

}