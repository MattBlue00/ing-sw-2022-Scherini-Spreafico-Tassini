package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.network.message.Message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Server{

    private final GameController gameController;
    private Object lock; //LOCK for synchronization
    private final Map<String, ClientHandler> clientHandlerMap;


    public Server(GameController gameController) {
        this.gameController = gameController;
        this.lock = new Object();
        this.clientHandlerMap = Collections.synchronizedMap(new HashMap<>());
    }

    /*
        The method used for getting messages from the client
     */

    public void addClient(String nickname, ClientHandler clientHandler){
        // virtual view will be associated to the added client
        clientHandlerMap.put(nickname, clientHandler); // This can be done if the game is not started yet.
    }

    public void removeClient(String nickname){
        clientHandlerMap.remove(nickname);
    }

    /*
        Calls GameController.getMessage()
     */
    public void getMessage(Message message){
        try {
            gameController.getMessage(message);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
        return the nickname associated to a clientHandler
     */
    private String getNicknameFromClientHandler(ClientHandler clientHandler) {
        return clientHandlerMap.entrySet()
                .stream()
                .filter(entry -> clientHandler.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public void onDisconnect(ClientHandler clientHandler){
        synchronized (lock){
            String nick = getNicknameFromClientHandler(clientHandler);

            if(nick != null){
                removeClient(nick);
            }
        }
    }
}
