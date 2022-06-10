package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameControllerFactory;
import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.exceptions.WrongMessageSentException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.message.CreateGameMessage;
import it.polimi.ingsw.network.message.JoinGameMessage;
import it.polimi.ingsw.network.message.Message;
import it.polimi.ingsw.network.message.MessageType;
import it.polimi.ingsw.utils.ANSIConstants;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.util.*;
import java.util.logging.Logger;

/**
 * Main server class that starts a socket server.
 * It can handle different types of connections.
 */
public class Server{

    private final Map<Integer,GameController> gameControllerMap;
    public static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private final Object lock; //LOCK for synchronization
    private final Map<String, ClientHandler> clientHandlerMap;

    public Server() {
        this.lock = new Object();
        this.clientHandlerMap = Collections.synchronizedMap(new HashMap<>());
        this.gameControllerMap = new HashMap<>();
    }

    /**
     * Method used to implement the correct {@code GameController}.
     * It's a factory method, returns a {@code GameController}.
     *
     * @param receivedMessage message passed to the factory.
     * @return {@code GameController}.
     */
    public GameController initController(Message receivedMessage){
        GameControllerFactory gameControllerFactory = new GameControllerFactory();
        return gameControllerFactory.getGameController(receivedMessage);
    }

    /**
     * Adds a client to be managed by the server instance.
     *
     * @param nickname the nickname associated with the client.
     * @param clientHandler the ClientHandler associated with the client.
     * @throws TryAgainException when the client's nickname has already been chosen.
     */
    public void addClient(String nickname, ClientHandler clientHandler) throws TryAgainException{
        clientHandler.setVirtualView(new VirtualView(clientHandler));
        if(!clientHandlerMap.containsKey(nickname)) {
            clientHandlerMap.put(nickname, clientHandler);
            Server.LOGGER.info("Added " + nickname + " to clientHandlerMap");
            return;
        }
        throw new TryAgainException("Error: nickname already exists");
    }

    /**
     * Removes a client given his nickname.
     *
     * @param nickname the VirtualView to be removed.
     */
    public void removeClient(String nickname){
        clientHandlerMap.remove(nickname);
        LOGGER.info("Removed " + nickname + " from the client list.");
    }

    /**
        This method creates a new GameController associated to the gameNumber.
        Then the method gets from the message the number of players and calls
        {@code GameController.prepareGame(playerNum)} to create a new game with the
        number of players declared in the message.

     @param message message with the number of players.
     @throws WrongMessageSentException If the gameNumber is already in the list.
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
        LOGGER.info(() -> "Game Number has already been chosen.");
        clientHandlerMap.get(nickname).getVirtualView().showGenericMessage("The game ID has already been chosen...");
        clientHandlerMap.get(nickname).getVirtualView().askGameInfo();
    }

    /**
     * This method is divided in three parts:
     *  1)  If the server receives a CreateGameMessage then a new GameController.
     *      is created and the creator automatically join the game.
     *  2)  If the server receives a JoinGameMessage then the client is added to the chosen
     *      existing game.
     *  3) Else the message is passed to the correct gameController.
     *
     * @param message message that will be controlled as said before.
     */

    public void getMessage(Message message){
        try {
            if(message.getMessageType() == MessageType.CREATE_GAME) {
                createNewGameController(message);
            }
            else if(message.getMessageType() == MessageType.JOIN_GAME) {
                try {
                    if(gameControllerMap.get(((JoinGameMessage) message).getGameID()).getGameQueue().size() ==
                            gameControllerMap.get(((JoinGameMessage) message).getGameID()).getGame().getPlayersNumber()){
                        clientHandlerMap.get(message.getNickname()).getVirtualView().
                                showGenericMessage("The game number " + ((JoinGameMessage) message).getGameID() + " is full." +
                                        "\nPlease create a new game or choose another one.");
                        clientHandlerMap.get(message.getNickname()).getVirtualView().askCreateOrJoin();
                    }
                    else{
                        gameControllerMap.get(((JoinGameMessage) message).getGameID()).
                                addPlayerToQueue(message.getNickname(), clientHandlerMap.get(message.getNickname()).getVirtualView());
                        clientHandlerMap.get(message.getNickname()).getVirtualView().askWizardID();
                    }
                }catch (NullPointerException e){
                    clientHandlerMap.get(message.getNickname()).getVirtualView().showGenericMessage("This game ID does not exists...");
                    clientHandlerMap.get(message.getNickname()).getVirtualView().askGameNumber();
                }
            }
            else{
                int gameID  = getGameIDFromNickname(message.getNickname());
                if(gameID != -1) {
                    LOGGER.info(() -> "Message sent to game number: "+gameID);
                    gameControllerMap.get(gameID).getMessage(message);
                }
                else throw new WrongMessageSentException("Error");
            }
        }
        catch(TryAgainException e){
            e.printStackTrace();
        }
    }

    /**
     * Returns the actual {@code gameControllerMap}.
     *
     * @return the actual {@code gameControllerMap}.
     */
    public Map<Integer, GameController> getGameControllerMap() {
        return gameControllerMap;
    }
    /**
     * Returns the actual {@code clientHandlerMap}.
     *
     * @return the actual {@code clientHandlerMap}.
     */
    public Map<String, ClientHandler> getClientHandlerMap() { return clientHandlerMap; }

    /**
     *  Returns the gameID associated to a nickname, if it doesn't
     *  exist returns -1.
     *
     * @param nickname the nickname of the {@link Player} present in the {@link GameController} to get.
     * @return the {@code GameController} associated to a {@code nickname}.
     */

    private int getGameIDFromNickname(String nickname){
        return gameControllerMap.entrySet()
                .stream()
                .filter(element -> element.getValue().getGameQueue().contains(nickname))
                .mapToInt(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
    }


    /**
     * Returns the nickname associated to a {@code clientHandler}.
     *
     * @param clientHandler the clientHandler of the {@code Player} to get.
     * @return the {@code nickname} associated to the {@code clientHandler}.
     */
    private String getNicknameFromClientHandler(ClientHandler clientHandler) {
        return clientHandlerMap.entrySet()
                .stream()
                .filter(entry -> clientHandler.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    /**
     * This method handles what happens when a client disconnect from the server.
     * The method must eliminate the {@link GameController} from the {@code gameControllerMap}.
     * then removes the {@code nickname} and the {@code clientHandler}.
     *
     * @param clientHandler {@code clientHandler} that has disconnected.
     */
    public void onDisconnect(ClientHandler clientHandler){
        synchronized (lock){
            String nick = getNicknameFromClientHandler(clientHandler);
            if(nick != null){
                int gameID = getGameIDFromNickname(nick);
                if(gameID != -1 && gameControllerMap.get(gameID).getGameState().equals(GameState.IN_GAME)) {
                    gameControllerMap.get(gameID).getVirtualViewMap().remove(nick);
                    gameControllerMap.get(gameID).broadcastDisconnectionMessage("Player " + nick +
                            " disconnected from the game.\n"
                            + ANSIConstants.ANSI_BOLD + "----- GAME FINISHED -----" + ANSIConstants.ANSI_RESET);
                    List<View> viewsToNotify = new ArrayList<>();
                    for (Player player : gameControllerMap.get(gameID).getGame().getPlayers()){
                        if(!player.getNickname().equals(nick)){
                            String name = player.getNickname();
                            View virtualView = gameControllerMap.get(gameID).getVirtualViewMap().get(name);
                            viewsToNotify.add(virtualView);
                            gameControllerMap.get(gameID).getVirtualViewMap().remove(name);
                        }
                    }
                    gameControllerMap.remove(gameID);
                    for(View view : viewsToNotify)
                        view.showExistingGames(gameControllerMap);
                    Server.LOGGER.severe("GameController " + gameID + " removed from gameControllerMap." +
                            "\n--- Game finished ---");
                } else if (gameID != -1 && gameControllerMap.get(gameID).getGameState().equals(GameState.SETUP)) {
                    Game game = gameControllerMap.get(gameID).getGame();
                    gameControllerMap.get(gameID).removePlayerFromQueue(nick);
                    Player playerToRemove = game.getPlayerFromNickname(nick);
                    if(playerToRemove != null)
                        gameControllerMap.get(gameID).getGame().getPlayers().remove(playerToRemove);
                }
                removeClient(nick);
            }
        }
    }

    public void onQuit(ClientHandler clientHandler){
        synchronized (lock){
            String nick = getNicknameFromClientHandler(clientHandler);
            if(nick != null){
                int gameID = getGameIDFromNickname(nick);
                if(gameControllerMap.get(gameID).getGameQueue().size() == 0)
                    gameControllerMap.remove(gameID);
                removeClient(nick);
            }
        }
    }

}
