package it.polimi.ingsw;

import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.network.message.AssistantCardMessage;
import it.polimi.ingsw.network.message.LoginRequest;
import it.polimi.ingsw.network.message.PlayerNumberMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientMain {
    public static void main(String[] args){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            SocketClient c = new SocketClient("127.0.0.1", 12345);
            c.sendMessage(new LoginRequest("Samuele"));
            c.sendMessage(new PlayerNumberMessage("Samuele", 2));
            String str = "";
            while (!str.equals("quit")){
                str = reader.readLine();
                c.sendMessage(new AssistantCardMessage("Samuele", str));
            }
            c.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
