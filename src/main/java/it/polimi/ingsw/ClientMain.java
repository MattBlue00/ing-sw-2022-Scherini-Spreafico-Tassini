package it.polimi.ingsw;

import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.network.message.LoginRequest;
import it.polimi.ingsw.network.message.PlayerNumberReply;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args){
        try {
            SocketClient c = new SocketClient("127.0.0.1", 12345);
            c.sendMessage(new LoginRequest("Giovanni"));
            c.sendMessage(new PlayerNumberReply("Samuele", 2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
