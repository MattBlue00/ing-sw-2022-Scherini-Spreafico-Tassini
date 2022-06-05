package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.observers.Observer;
import it.polimi.ingsw.observers.ViewObserver;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.CommandLineInterface;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This client-side class allows the client (by using a CLI or a GUI) to communicate with the server via the network.
 * It wraps messages from the client and sends them to the server via the network, and unwraps messages from the
 * server (via the network) in order to ask something to the client via CLI/GUI.
 */

public class ClientController implements ViewObserver, Observer {

    private final View view;
    private Client client;
    private String nickname;
    private ExecutorService taskQueue;
    private final ScheduledExecutorService pinger;

    /**
     * Client controller constructor.
     *
     * @param view the {@link View} to control.
     */

    public ClientController(View view) {
        this.view = view;
        this.taskQueue = Executors.newSingleThreadExecutor();
        this.pinger = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Establishes a connection with the server via the given address and the given port.
     * Then, asks the client for their nickname.
     *
     * @param address the given address.
     * @param port the given port.
     */

    @Override
    public void onUpdateServerData(String address, int port) {
        try {
            client = new SocketClient(address, port);
            pinger.scheduleAtFixedRate(this::isReachable, 0, 1000, TimeUnit.MILLISECONDS);
            ((SocketClient) client).setReadExecutionQueue(Executors.newSingleThreadExecutor());
            client.addObserver(this);
            client.readMessage();
            taskQueue.execute(view::askNickname);
        }
        catch(SocketException e){
            System.out.println("Either the server or the network is unreachable. Please try again.");
            taskQueue.shutdownNow();
            taskQueue = Executors.newSingleThreadExecutor();
            taskQueue.execute(view::askServerData);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends to the server a login request with the chosen nickname.
     *
     * @param nickname the nickname the client wants to (try to) login with.
     */

    @Override
    public void onUpdateNickname(String nickname) {
        this.nickname = nickname;
        client.sendMessage(new LoginRequest(this.nickname));
    }

    /**
     * Based on the client's choice, asks for the game parameters (if the choice is "CREATE") or asks for an
     * existing game number (if the choice is "JOIN").
     *
     * @param choice the choice made by the client.
     */

    @Override
    public void onUpdateCreateOrJoin(String choice) {
        if(choice.equalsIgnoreCase("CREATE"))
            taskQueue.execute(view::askGameInfo);
        else
            taskQueue.execute(view::askGameNumber);
    }

    /**
     * Sends to the server the game parameters chosen by the client, so that it can build the desired game.
     *
     * @param gameNumber the ID of the game the client wishes to create.
     * @param mode the game mode of the game the client wishes to create ({@code false} -> NORMAL, {@code true} -> EXPERT).
     * @param numOfPlayers the maximum number of players of the game the client wishes to create.
     */

    @Override
    public void onUpdateGameInfo(int gameNumber, boolean mode, int numOfPlayers) {
        client.sendMessage(new CreateGameMessage(nickname, gameNumber, numOfPlayers, mode));
    }

    /**
     * Sends to the server the ID of the game the client wishes to join.
     *
     * @param gameNumber the ID of the game the client wishes to join.
     */

    @Override
    public void onUpdateGameNumber(int gameNumber) {
        client.sendMessage(new JoinGameMessage(nickname, gameNumber));
    }

    /**
     * Sends to the server the Wizard ID the client wishes to embody for that game.
     *
     * @param wizardID the {@code String} representing the WizardID the client wishes to embody.
     */

    @Override
    public void onUpdateWizardID(String wizardID) {
        client.sendMessage(new WizardIDMessage(nickname, wizardID));
    }

    /**
     * Sends to the server the Assistant Card the client wishes to play.
     *
     * @param assistantCard the Assistant Card the client wishes to play.
     */

    @Override
    public void onUpdateAssistantCard(String assistantCard) {
        client.sendMessage(new AssistantCardMessage(nickname, assistantCard));
    }

    /**
     * Sends to the server the color of the student the client wishes to move, as well as the ID of the island
     * the client wishes to move it onto.
     *
     * @param color the color of the student the client wishes to move.
     * @param islandID the ID of the island the client wishes to move the student onto.
     */

    @Override
    public void onUpdateIslandStudentMove(String color, int islandID){
        client.sendMessage(new MoveToIslandMessage(nickname, color, islandID));
    }

    /**
     * Sends to the server the color of the student the client wishes to move to its table.
     *
     * @param color the color of the student the client wishes to move to its table.
     */

    @Override
    public void onUpdateTableStudentMove(String color){
        client.sendMessage(new MoveToTableMessage(nickname, color));
    }

    /**
     * Sends to the server the number of steps the client wishes Mother Nature to move of.
     *
     * @param steps the steps the client wishes Mother Nature to move of.
     */

    @Override
    public void onUpdateMotherNatureSteps(int steps) {
        client.sendMessage(new MotherNatureStepsMessage(nickname, steps));
    }

    /**
     * Sends to the server the ID of the cloud the client wishes to take students from.
     *
     * @param cloudID the ID of the cloud the client wishes to take students from.
     */

    @Override
    public void onUpdateCloudChoice(int cloudID) {
        client.sendMessage(new CloudChoiceMessage(nickname, cloudID));
    }

    /**
     * Sends to the server the ID of the Character Card the client wishes to play.
     * Note that this method is called if no parameters are required.
     *
     * @param characterCardID the ID of the Character Card the client wishes to play.
     */

    @Override
    public void onUpdateCharacterCard(int characterCardID) {
        client.sendMessage(new CharacterCardMessage(nickname, characterCardID));
    }

    /**
     * Sends to the server the ID of the Character Card the client wishes to use, as well as
     * the {@code int} parameter required.
     *
     * @param characterCardID the ID of the Character Card the client wishes to play.
     * @param par the required {@code int} parameter (its meaning changes from card to card).
     */

    @Override
    public void onUpdateCharacterCardInt(int characterCardID, int par) {
        client.sendMessage(new CharacterCardMessageInt(nickname, characterCardID, par));
    }

    /**
     * Sends to the server the ID of the Character Card the client wishes to use, as well as
     * the String parameter required.
     *
     * @param characterCardID the ID of the Character Card the client wishes to play.
     * @param par the required {@code String} parameter (its meaning changes from card to card).
     */

    @Override
    public void onUpdateCharacterCardString(int characterCardID, String par) {
        client.sendMessage(new CharacterCardMessageString(nickname, characterCardID, par));
    }

    /**
     * Sends to the server the ID of the Character Card the client wishes to use, as well as
     * the String and the {@code int} parameters required.
     *
     * @param characterCardID the ID of the Character Card the client wishes to play.
     * @param par1 the required {@code String} parameter.
     * @param par2 the required {@code int} parameter.
     */

    @Override
    public void onUpdateCharacterCardStringInt(int characterCardID, String par1, int par2) {
        client.sendMessage(new CharacterCardMessageStringInt(nickname, characterCardID, par1, par2));
    }

    /**
     * Sends to the server the ID of the Character Card the client wishes to use, as well as
     * the list of {@code String} parameter required.
     *
     * @param characterCardID the ID of the Character Card the client wishes to play.
     * @param list the required list of {@code String} parameters (its meaning changes from card to card).
     */

    @Override
    public void onUpdateCharacterCardArrayListString(int characterCardID, ArrayList<String> list) {
        client.sendMessage(new CharacterCardMessageArrayListString(nickname, characterCardID, list));
    }

    /**
     * Sends to the server a String that contains the client's choice between playing a Character Card or
     * moving a student (this choice is only possible in Expert Mode).
     *
     * @param choice the choice made by the client.
     */

    @Override
    public void onUpdateActionChoice(String choice) {
        client.sendMessage(new ActionChoiceMessage(nickname, choice));
    }

    /**
     * When receiving a message from the client, the server may:
     * - ask for more info (case {@code ASK_TYPE}).
     * - answer with the desired info (other cases).
     * Note that the {@code default} branch should never be reached.
     *
     * @param message the message received from the server.
     */

    @Override
    public void update(Message message) {
        switch(message.getMessageType()){
            case ASK_TYPE:
                switch (((AskMessage) message).getAskType()) {
                    case NICKNAME_NOT_UNIQUE -> taskQueue.execute(view::askNickname);
                    case GAME_ID -> taskQueue.execute(view::askCreateOrJoin);
                    case WIZARD_ID -> taskQueue.execute(view::askWizardID);
                    case ASSISTANT_CARD ->  taskQueue.execute(view::askAssistantCard);
                    case MOVE_STUDENT -> taskQueue.execute(view::askMoveStudent);
                    case MOVE_MOTHER_NATURE -> taskQueue.execute(view::askMotherNatureSteps);
                    case CLOUD_CHOICE -> taskQueue.execute(view::askCloud);
                    case CHARACTER_CARD -> taskQueue.execute(view::askCharacterCard);
                    case ACTION_CHOICE -> taskQueue.execute(view::askAction);
                    default -> {//should never be reached
                    }
                }
                break;
            case EXISTING_GAMES:
                taskQueue.execute(()-> view.showExistingGames(((ShowExistingGamesMessage) message).getExistingGames()));
                break;
            case GAME_STATUS_FIRST_ACTION_PHASE:
                taskQueue.execute(() -> view.showGameStatusFirstActionPhase(((GameStatusFirstActionPhaseMessage) message).getGame()));
                break;
            case GAME_STATUS:
                taskQueue.execute(() -> view.showGameStatus(((GameStatusMessage) message).getGame()));
                break;
            case SHOW_DECK_MESSAGE:
                taskQueue.execute(() -> view.showDeck(((ShowDeckMessage) message).getGame()));
                break;
            case GENERIC:
                taskQueue.execute(() -> view.showGenericMessage(message.toString()));
                break;
            case DISCONNECTION:
                taskQueue.shutdownNow();
                taskQueue = Executors.newSingleThreadExecutor();
                taskQueue.execute(() -> view.showDisconnectionMessage(((DisconnectionMessage) message).getMessageStr()));
                break;
            default: //should never be reached
                break;
        }
    }

    /**
     * Checks if the given string is a valid IPv4 address by using a regex.
     *
     * @param address the String given by the client.
     */

    public static boolean isValidAddress(String address) {
        String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return address.matches(regex);
    }

    /**
     * Checks if the given {@code String} is in the range of allowed ports when parsed to {@code int}.
     *
     * @param portStr the {@code String} given by the client.
     */

    public static boolean isValidPort(String portStr){
        try {
            int port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void isReachable(){
        try{
            boolean reachable;
            reachable = ((SocketClient) client).getSocket().getInetAddress().isReachable(Constants.CONNECTION_TIMEOUT_CLIENT);
            if(!reachable){
                System.out.println("The server is no more reachable. Please restart the app and try again.");
                client.disconnect();
                pinger.shutdownNow();
                taskQueue.shutdownNow();
                Thread.currentThread().interrupt();
                System.exit(1);
            }
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
    }

}
