package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameControllerFactory;
import it.polimi.ingsw.model.exceptions.TryAgainException;
import it.polimi.ingsw.model.exceptions.WrongMessageSentException;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Server{

    private GameControllerFactory gameControllerFactory;
    private GameController gameController;
    private Object lock; //LOCK for synchronization
    private final Map<String, ClientHandler> clientHandlerMap;


    public Server() {
        this.lock = new Object();
        this.clientHandlerMap = Collections.synchronizedMap(new HashMap<>());
    }

    public GameController initController(Message receivedMessage){
        this.gameControllerFactory = new GameControllerFactory();
         return gameControllerFactory.getGameController(receivedMessage);
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
            if(message.getMessageType()== MessageType.GAME_MODE_REPLY)
                initController(message);
            else{
                if(gameController != null)
                    gameController.getMessage(message);
                else throw new WrongMessageSentException("Error");
            }
        }
        catch(TryAgainException e){
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
