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



        System.out.println( "---------END---------" );
    }
}
