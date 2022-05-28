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
    public void onUpdateAssistantCard(String assistantCard) {
        client.sendMessage(new AssistantCardMessage(nickname, assistantCard));
    }

    @Override
    public void onUpdateIslandStudentMove(String color, int islandID){
        client.sendMessage(new MoveToIslandMessage(nickname, color, islandID));
    }

    @Override
    public void onUpdateTableStudentMove(String color){
        client.sendMessage(new MoveToTableMessage(nickname, color));
    }

    @Override
    public void onUpdateMotherNatureSteps(int steps) {
        client.sendMessage(new MotherNatureStepsMessage(nickname, steps));
    }

    @Override
    public void onUpdateCloudChoice(int cloudID) {
        client.sendMessage(new CloudChoiceMessage(nickname, cloudID));
    }

    @Override
    public void onUpdateCharacterCard(int characterCardID) {
        client.sendMessage(new CharacterCardMessage(nickname, characterCardID));
    }

    @Override
    public void onUpdateCharacterCardInt(int characterCardID, int par) {
        client.sendMessage(new CharacterCardMessageInt(nickname, characterCardID, par));
    }

    @Override
    public void onUpdateCharacterCardString(int characterCardID, String par) {
        client.sendMessage(new CharacterCardMessageString(nickname, characterCardID, par));
    }

    @Override
    public void onUpdateCharacterCardStringInt(int characterCardID, String par1, int par2) {
        client.sendMessage(new CharacterCardMessageStringInt(nickname, characterCardID, par1, par2));
    }

    @Override
    public void onUpdateActionChoice(String choice) {
        client.sendMessage(new ActionChoiceMessage(nickname, choice));
    }

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
            default: //should never be reached
                break;
        }
    }
}
