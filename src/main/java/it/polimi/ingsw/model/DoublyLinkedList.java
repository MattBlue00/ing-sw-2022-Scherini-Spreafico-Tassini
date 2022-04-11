package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.IslandNotFoundException;

/*
    In this list, the last node of the doubly linked list contains the address of the first node and
    the first node contains the address of the last node.
    In a circular doubly linked list, there is a cycle and none of the node pointers are set to null.
 */

public class DoublyLinkedList {
    private Island head = null; // first element of the list
    int size; // to check the number of islands in the list

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /*
        Create the list with the initial number of islands
     */
    public DoublyLinkedList() {
        for(int i=1; i < Constants.MAX_NUM_OF_ISLANDS+1; i++)
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
        if(prev.getOwner().equals(island.getOwner())){
            island.setNumOfTowers(island.getNumOfTowers() + prev.getNumOfTowers());
            prev.getStudents().forEach(student -> island.addStudent(student));
            removeIsland(prev);
        }
        if(next.getOwner().equals(island.getOwner())){
            island.setNumOfTowers(island.getNumOfTowers() + next.getNumOfTowers());
            next.getStudents().forEach(student -> island.addStudent(student));
            removeIsland(next);
        }
        // TODO : WinCheck after merging
        if(getSize() <= 3){
            System.out.println("GAME OVER");
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
        while(!island.getNext().equals(head) && islandsToCheck > 0){
            if(island.getId()==islandID)
                return island;
            islandsToCheck--;
        }
        throw new IslandNotFoundException("No island found.");
    }


    // For debugging: print the list of islands and their info from head to tail and then from tail to head
    public void printList(){
        Island temp = head;
        System.out.println("From head to tail: ");
        while(!temp.getNext().equals(head)){
            System.out.println("L'isola "+temp.getId()+" appartenente a "+temp.getOwner().get().getNickname()+" possiede influenza: "+temp.influenceCalc(temp.getOwner().get()));
            temp = temp.getNext();
        }
        System.out.println("L'isola "+temp.getId()+" appartenente a "+temp.getOwner().get().getNickname()+" possiede influenza: "+temp.influenceCalc(temp.getOwner().get()));

        /*System.out.println("From tail to head: ");
        Island tail = head.getPrev();
        temp = tail;
        while(!temp.getPrev().equals(tail)){
            System.out.printf("%d ",temp.getId());
            temp = temp.getPrev();
        }
        System.out.println(temp.getId());*/
    }
}