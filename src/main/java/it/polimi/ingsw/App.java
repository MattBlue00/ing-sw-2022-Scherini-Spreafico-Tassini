package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exceptions.EmptyBagException;
import it.polimi.ingsw.model.exceptions.EmptyCloudException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "---------START---------" );

        Game g1 = new Game(2);
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Samu");
        Player p2 = new Player(Wizard.PINK_WIZARD, "Ludo");
        Player p3 = new Player(Wizard.GREEN_WIZARD, "Matteo");

        g1.setCurrentPlayer(p1);

        System.out.println("numero di stud nel sacchetto : "+g1.getBoard().getStudentsBag().size());
        System.out.println("numero di stud sulla nuvola 0: "+g1.getBoard().getCloud(0).getStudents().size());


        try {
            g1.takeStudentsFromCloud(0);
        } catch (EmptyCloudException e) {
            e.printStackTrace();
        }


        System.out.println("stampo i colori degli studenti presenti nel mio Ingresso:");
        p1.getSchool().getHall().getStudents().forEach(s -> System.out.println(s.getColor().toString()));

        System.out.println("numero di stud sulla nuvola 0: "+g1.getBoard().getCloud(0).getStudents().size());

        try {
            g1.refillClouds();
        } catch (EmptyBagException e) {
            e.printStackTrace();
        }
        System.out.println("numero di stud sulla nuvola 0: "+g1.getBoard().getCloud(0).getStudents().size());
        System.out.println("numero di stud nel sacchetto : "+g1.getBoard().getStudentsBag().size());

        System.out.println( "Print delle isole nella loro DoubleLinkedList: ");

        DoublyLinkedList islands = new DoublyLinkedList();

        Island i1 = new Island(1);
        islands.addIsland(i1);
        Island i2 = new Island(2);
        islands.addIsland(i2);
        Island i3 = new Island(3);
        islands.addIsland(i3);
        Island i4 = new Island(4);
        islands.addIsland(i4);
        Island i5 = new Island(5);
        islands.addIsland(i5);

        i1.setNumOfTowers(1);
        //i1.setOwnerInfluence(3);
        i1.setOwner(p1);
        i2.setNumOfTowers(1);
        //i2.setOwnerInfluence(4);
        i2.setOwner(p1);
        i3.setNumOfTowers(2);
        //i3.setOwnerInfluence(10);
        i3.setOwner(p2);
        i4.setNumOfTowers(1);
        //i4.setOwnerInfluence(3);
        i4.setOwner(p3);
        i5.setNumOfTowers(1);
        //i5.setOwnerInfluence(5);
        i5.setOwner(p1);
        islands.printList();

        islands.mergeIslands(i1);
        islands.printList();

        islands.removeIsland(i1);
        islands.removeIsland(i3);
        islands.removeIsland(i4);
        islands.printList();


        GameBoard gb1 = new GameBoard(2);
        gb1.setMotherNaturePos(11);
        gb1.moveMotherNature(5);
        System.out.println("Pos: "+gb1.getMotherNaturePos());

        System.out.println( "---------END---------" );

    }
}
