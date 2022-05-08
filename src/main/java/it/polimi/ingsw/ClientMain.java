package it.polimi.ingsw;

import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.network.message.CreateGameMessage;
import it.polimi.ingsw.network.message.JoinGameMessage;
import it.polimi.ingsw.network.message.LoginRequest;
import it.polimi.ingsw.network.message.WizardIDMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner scan = new Scanner(System.in);
        try {
            SocketClient c = new SocketClient("127.0.0.1", 12345);
            String str;
            System.out.println("Enter a nickname: ");
            str = reader.readLine();
            c.sendMessage(new LoginRequest(str));
            String user = str;
            System.out.println("CREATE or JOIN a game: ");
            str = reader.readLine();
            if(str.toUpperCase().equals("CREATE")){
                System.out.println("GameNumber : ");
                int gameNumber = scan.nextInt();
                System.out.println("Expert mode (true, false) : ");
                boolean gameMode = Boolean.parseBoolean(reader.readLine());
                System.out.println("Num of players : ");
                int playerNum = scan.nextInt();
                c.sendMessage(new CreateGameMessage(user, gameNumber, playerNum, gameMode));
            }
            else if (str.toUpperCase().equals("JOIN")){
                System.out.println("GameNumber : ");
                int gameNumber = scan.nextInt();
                c.sendMessage(new JoinGameMessage(user, gameNumber));
            }
            else{
                System.out.println("Error in choice");
                c.disconnect();
            }
            System.out.println("GameWizard (BLUE_WIZARD | PINK_WIZARD | YELLOW_WIZARD | GREEN_WIZARD): ");
            str = reader.readLine();
            c.sendMessage(new WizardIDMessage(user, str));
            str = reader.readLine();
            System.out.println("-------------END-------------");
            c.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
