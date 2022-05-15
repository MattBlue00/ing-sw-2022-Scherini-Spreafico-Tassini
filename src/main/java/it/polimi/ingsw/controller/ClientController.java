package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.observers.Observer;
import it.polimi.ingsw.observers.ViewObserver;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientController implements ViewObserver, Observer {

    private final View view;
    private Client client;
    private String nickname;

    private final ExecutorService taskQueue;
    public ClientController(View view) {
        this.view = view;
        this.taskQueue = Executors.newSingleThreadExecutor();
    }

    public static boolean isValidAddress(String address) {
        String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return address.matches(regex);
    }

    public static boolean isValidPort(String portStr){
        try {
            int port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void onUpdateServerData(String address, int port) {
        try {
            client = new SocketClient(address, port);
            client.addObserver(this);
            // For communication between server and
            client.readMessage();
            taskQueue.execute(view::askNickname);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateNickname(String nickname) {
        this.nickname = nickname;
        client.sendMessage(new LoginRequest(this.nickname));
    }

    @Override
    public void onUpdateCreateOrJoin(String choice) {
        if(choice.equalsIgnoreCase("CREATE"))
            taskQueue.execute(view::askGameInfo);
        else
            taskQueue.execute(view::askGameNumber);
    }

    @Override
    public void onUpdateGameInfo(int gameNumber, boolean mode, int numOfPlayers) {
        client.sendMessage(new CreateGameMessage(nickname, gameNumber, numOfPlayers, mode));
    }

    @Override
    public void onUpdateGameNumber(int gameNumber) {
        client.sendMessage(new JoinGameMessage(nickname, gameNumber));
    }

    @Override
    public void onUpdateWizardID(String wizardID) {
        client.sendMessage(new WizardIDMessage(nickname, wizardID));
    }

    @Override
    public void update(Message message) {
        switch(message.getMessageType()){
            case ASK_TYPE:
                switch (((AskMessage) message).getAskType()) {
                    case NICKNAME_NOT_UNIQUE -> taskQueue.execute(view::askNickname);
                    case GAME_ID -> taskQueue.execute(view::askCreateOrJoin);
                    case WIZARD_ID -> taskQueue.execute(view::askWizardID);
                    default -> {//should never be reached
                    }
                }
                break;
            case GENERIC:
                taskQueue.execute(() -> view.showGenericMessage(message.toString()));
            default: //should never be reached
                break;
        }
    }
}
