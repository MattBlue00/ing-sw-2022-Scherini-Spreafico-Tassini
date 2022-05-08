package it.polimi.ingsw;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {

        // For network debugging purpose only
        //GameController g = new GameController();
        //g.setGameState(GameState.LOGIN);
        //Server s1 = new Server(g);

        //SocketServer socketServer = new SocketServer(s1, 12345);
        //socketServer.run();

        GameController gc = new GameController();
        PlayerNumberMessage message = new PlayerNumberMessage("Matteo", 2);
        Constants.setConstants(2);

        try {
            gc.prepareGame(message);
        }
        catch(Exception ignored){}
        gc.setGameState(GameState.IN_GAME);
        Player p1 = new Player(Wizard.BLUE_WIZARD, "Matteo", gc.getGame().getPlayersNumber());
        Player p2 = new Player(Wizard.PINK_WIZARD, "Ludo", gc.getGame().getPlayersNumber());
        gc.getGame().addPlayer(p1);
        gc.getGame().addPlayer(p2);
        gc.getGame().setCurrentPlayer(p1);

        try {
            gc.getMessage(new AssistantCardMessage("Matteo", "TURTLE"));
        }
        catch(Exception ignored){}

        try {
            gc.getMessage(new AssistantCardMessage("Ludo", "FOX"));
        }
        catch(Exception ignored){}

        gc.getGame().getBoard().setMotherNaturePos(12);
        List<Student> bag = new ArrayList<>(30);
        for(int i = 0; i < 10; i++)
            bag.add(new Student(Color.PINK));
        gc.getGame().getBoard().setStudentsBag(bag);

        try {
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));

            gc.getMessage(new MoveToTableMessage("Ludo", "PINK"));
            gc.getMessage(new MoveToTableMessage("Ludo", "PINK"));
            gc.getMessage(new MoveToIslandMessage("Ludo", "PINK", 1));

            gc.getMessage(new MotherNatureStepsMessage("Ludo", 1));

            gc.getMessage(new CloudChoiceMessage("Ludo", 0));

            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));
            gc.getGame().getCurrentPlayer().getSchool().getHall().addStudent(new Student(Color.PINK));

            gc.getMessage(new MoveToTableMessage("Matteo", "PINK"));
            gc.getMessage(new MoveToTableMessage("Matteo", "PINK"));
            gc.getMessage(new MoveToIslandMessage("Matteo", "PINK", 3));

            gc.getMessage(new MotherNatureStepsMessage("Matteo", 2));
            gc.getMessage(new CloudChoiceMessage("Matteo", 1));

            gc.getGame().showGameBoard();

        }
        catch(Exception ignored){}

    }
}
