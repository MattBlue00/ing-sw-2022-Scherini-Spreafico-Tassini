package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameControllerFactory;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.exceptions.WrongMessageSentException;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.view.VirtualView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Server{

    private GameControllerFactory gameControllerFactory;
    private final Map<Integer,GameController> gameControllerMap;
    private Object lock; //LOCK for synchronization
    private final Map<String, ClientHandler> clientHandlerMap;

    public Server() {
        this.lock = new Object();
        this.clientHandlerMap = Collections.synchronizedMap(new HashMap<>());
        this.gameControllerMap = new HashMap<>();
    }

    public GameController initController(Message receivedMessage){
        this.gameControllerFactory = new GameControllerFactory();
        return gameControllerFactory.getGameController(receivedMessage);
    }

    /*
        Add a new client to the server.
        If the client's nickname has already been chosen throw a new Exception.
     */
    public void addClient(String nickname, ClientHandler clientHandler) throws TryAgainException{
        clientHandler.setVirtualView(new VirtualView(clientHandler));
        if(!clientHandlerMap.containsKey(nickname)) {
            clientHandlerMap.put(nickname, clientHandler);
            return;
        }
        throw new TryAgainException("Error: nickname already exists");
    }

    /*
        Remove a client from the server
     */
    public void removeClient(String nickname){
        clientHandlerMap.remove(nickname);
    }

    /*
        This method creates a new GameController associated to the gameNumber.
        If the gameNumber is already in the list throws a new Exception.
        Then the method gets from the message the number of players and calls
        GameController.prepareGame(playerNum), to create a new game with the
        number of players declared in the message.
     */
    public void createNewGameController(Message message) throws WrongMessageSentException {
        int gameNumber = ((CreateGameMessage) message).getGameNumber();
        int playerNum = ((CreateGameMessage) message).getPlayerNum();
        String nickname = message.getNickname();
        if(!gameControllerMap.containsKey(gameNumber)) {
            gameControllerMap.put(gameNumber, initController(message));
            gameControllerMap.get(gameNumber).setGameControllerID(gameNumber);
            gameControllerMap.get(gameNumber).prepareGame(playerNum);
            gameControllerMap.get(gameNumber).addPlayerToQueue(nickname, clientHandlerMap.get(nickname).getVirtualView());
            clientHandlerMap.get(nickname).getVirtualView().askWizardID();
            return;
        }
        System.out.println("Game Number has already been chosen.");
        clientHandlerMap.get(nickname).getVirtualView().showGenericMessage("The game ID has already been chosen...");
        clientHandlerMap.get(nickname).getVirtualView().askGameInfo();
    }

    /*
        This method is divided in three parts:
        1)  If the server receives a CreateGameMessage then a new GameController
            is created and the creator automatically join the game.
        2)  If the server receives a JoinGameMessage then the client is added to the chosen
            existing game.
        3) Else the message is passed to the correct gameController
     */
    public void getMessage(Message message){
        try {
            if(message.getMessageType() == MessageType.CREATE_GAME) {
                createNewGameController(message);
            }
            else if(message.getMessageType() == MessageType.JOIN_GAME) {
                try {
                    gameControllerMap.get(((JoinGameMessage) message).getGameID()).addPlayerToQueue(message.getNickname(), clientHandlerMap.get(message.getNickname()).getVirtualView());
                    clientHandlerMap.get(message.getNickname()).getVirtualView().askWizardID();
                }catch (NullPointerException e){
                    clientHandlerMap.get(message.getNickname()).getVirtualView().showGenericMessage("This game ID does not exists...");
                    clientHandlerMap.get(message.getNickname()).getVirtualView().askGameNumber();
                }
            }
            else{
                int gameID  = getGameIDFromNickname(message.getNickname());
                if(gameID != -1) {
                    System.out.println("Message sent to game number: "+gameID);
                    gameControllerMap.get(gameID).getMessage(message);
                }
                else throw new WrongMessageSentException("Error");
            }
        }
        catch(TryAgainException e){
            e.printStackTrace();
        }
    }

    /*
        returns the gameID associated to a nickname, if it doesn't exists
        returns -1.
    */
    private int getGameIDFromNickname(String nickname){
        return gameControllerMap.entrySet()
                .stream()
                .filter(element -> element.getValue().getGameQueue().contains(nickname))
                .mapToInt(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
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
