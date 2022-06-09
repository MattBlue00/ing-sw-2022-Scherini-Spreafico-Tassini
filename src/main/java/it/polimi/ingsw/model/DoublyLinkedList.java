package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IslandNotFoundException;
import it.polimi.ingsw.utils.Constants;

import java.io.Serializable;

/**
 * This is the islands' data structure. It is a particular list in which the last node contains the address of the
 * first node and the first node contains the address of the last node (so, it is "circular" and none of the pointers
 * are set to {@code null}).
 */

public class DoublyLinkedList implements Serializable {

    private Island head = null; // first element of the list
    private int size; // to check the number of islands in the list

    /**
     * Doubly Linked List constructor (since it has been specifically written for the game, the list contains 12 nodes
     * from the beginning, each one holding an island).
     */

    public DoublyLinkedList() {
        for(int i = 1; i <= Constants.MAX_NUM_OF_ISLANDS; i++)
            addIsland(new Island(i));
    }

    /**
     * Returns the number of nodes (islands) of the list.
     *
     * @return an {@code int} representing the number of nodes (islands) in the list.
     */

    public int getSize() {
        return size;
    }

    /**
     * Returns the {@link Island} in the head of the list.
     *
     * @return the {@link Island} in the head of the list.
     */

    public Island getHead() {
        return head;
    }

    /**
     * Adds an island at the bottom of the list. If the list is empty, adds the first element and set the "prev" and
     * "next" pointers towards the same island.
     *
     * @param island the island to add.
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

    /**
     * Merges the island passed as a parameter with one or both the adjacent islands, if requirements are met. If
     * merging is performed, removes the adjacent islands and updates the current island with the correct numbers of
     * towers and students.
     *
     * @param island the island whose merging capability is going to be checked.
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
            GameBoard.reassignIslandIDs(this);
        }
        if(next.getOwner() != null && next.getOwner().equals(island.getOwner())){
            island.setNumOfTowers(island.getNumOfTowers() + next.getNumOfTowers());
            next.getStudents().forEach(island::addStudent);
            removeIsland(next);
            GameBoard.reassignIslandIDs(this);
        }
    }

    /**
     * Removes an island and decreases the size of the data structure. If the removed node is the head node, the next
     * island is declared the new head.
     *
     * @param island the island to remove.
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

    /**
     * Returns the island with the specified ID, if present.
     *
     * @param islandID the ID of the island to return.
     * @return the desired island.
     * @throws IslandNotFoundException if the provided island ID does not refer to any of the existing islands.
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