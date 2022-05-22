package it.polimi.ingsw;

import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.network.message.CreateGameMessage;
import it.polimi.ingsw.network.message.JoinGameMessage;
import it.polimi.ingsw.network.message.LoginRequest;
import it.polimi.ingsw.network.message.WizardIDMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
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
            while (!str.equalsIgnoreCase("CREATE") && !str.equalsIgnoreCase("JOIN")){
                System.out.println("The given input is not correct, please retry. \nCREATE or JOIN a game: ");
                str = reader.readLine();
            }
            if(str.equalsIgnoreCase("CREATE")){
                System.out.println("GameNumber : ");
                int gameNumber = scan.nextInt();
                System.out.println("Expert mode (true, false) : ");
                str = reader.readLine();
                while (!str.equalsIgnoreCase("true") && !str.equalsIgnoreCase("false")){
                    System.out.println("The given input is not correct, please retry. \nExpert mode (true, false)  ");
                    str = reader.readLine();
                }
                boolean gameMode = Boolean.parseBoolean(str.toLowerCase(Locale.ROOT));
                System.out.println("Num of players (two or three): ");
                int playerNum = scan.nextInt();
                while (playerNum != 2 && playerNum != 3){
                    System.out.println("The given input is not correct, please retry. \nNum of players (two or three): ");
                    playerNum = scan.nextInt();
                }
                c.sendMessage(new CreateGameMessage(user, gameNumber, playerNum, gameMode));
            }
            else if (str.equalsIgnoreCase("JOIN")){
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
            while (!str.equalsIgnoreCase("BLUE_WIZARD") && !str.equalsIgnoreCase("PINK_WIZARD")
                && !str.equalsIgnoreCase("YELLOW_WIZARD") && !str.equalsIgnoreCase("GREEN_WIZARD")){
                System.out.println("The given input is not correct, please retry. \nGameWizard (BLUE_WIZARD | PINK_WIZARD | YELLOW_WIZARD | GREEN_WIZARD): ");
                str = reader.readLine();
            }
            c.sendMessage(new WizardIDMessage(user, str.toUpperCase(Locale.ROOT)));
            str = reader.readLine();
            System.out.println("-------------END-------------");
            c.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
