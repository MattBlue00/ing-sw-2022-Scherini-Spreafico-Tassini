package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.view.VirtualView;

import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.network.server.Server.LOGGER;

/**
 * This server-side class controls the game flow of a game in Normal mode towards all of its phases, from its creation
 * to the winner's declaration. Each {@link Game} has its own game controller, which regulates the flow of every event.
 */

public class GameController implements Serializable {
    private Game game;
    private int gameControllerID;
    private boolean playerPlanningPhaseDone;
    private boolean planningPhaseDone;
    private boolean playerActionPhaseDone;
    private int currentPlayerIndex;
    private int movesLeft;
    private boolean motherNatureMoved;
    private GameState gameState;
    protected static final String INVALID_STATE = "Invalid game state.";
    protected static final String END_STATE = "The game has ended, the winner is: ";
    private final List<String> gameQueue;
    private final transient Map<String, VirtualView> virtualViewMap;

    /**
     * Game controller constructor.
     */

    public GameController(){
        this.playerPlanningPhaseDone = false;
        this.planningPhaseDone = false;
        this.playerActionPhaseDone = false;
        this.currentPlayerIndex = 0;
        this.motherNatureMoved = false;
        this.gameQueue = new ArrayList<>();
        this.gameState = GameState.SETUP;
        this.virtualViewMap = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Returns a list of the nicknames of the players in the queue.
     *
     * @return the list of nicknames.
     */

    public List<String> getGameQueue() {
        return gameQueue;
    }

    /**
     * Sets the game controller's ID.
     *
     * @param gameControllerID the {@code int} representing the ID of the game controller.
     */

    public void setGameControllerID(int gameControllerID) {
        this.gameControllerID = gameControllerID;
    }

    /**
     * Sets the game's state.
     *
     * @param gameState the value that represents the new state of the game.
     */

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Gets the game's state.
     *
     * @return the state of the game.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Returns the game controlled by the controller.
     *
     * @return the game controlled by the controller.
     */

    public Game getGame() {
        return game;
    }

    /**
     * Sets the game controlled by the controller.
     *
     * @param game the game to control.
     */

    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Returns the students' moves left during the current player's Action Phase.
     *
     * @return the students' moves left.
     */

    public int getMovesLeft() {
        return movesLeft;
    }

    /**
     * Sets the students' moves left to do during the current player's Action Phase.
     *
     * @param movesLeft the new value of the students' moves left to do.
     */

    public void setMovesLeft(int movesLeft) {
        this.movesLeft = movesLeft;
    }

    /**
     * Checks if Mother Nature has already been moved by the current player (during their Action Phase).
     *
     * @return {@code true} if Mother Nature has been moved, {@code false} otherwise.
     */

    public boolean hasMotherNatureMoved() {
        return motherNatureMoved;
    }

    /**
     * Sets the boolean flag to {@code true} if the current player has moved Mother Nature, {@code false} otherwise.
     *
     * @param motherNatureMoved the new flag.
     */

    public void setMotherNatureMoved(boolean motherNatureMoved) {
        this.motherNatureMoved = motherNatureMoved;
    }

    /**
     * Sets the boolean flag to {@code true} if the current player has correctly played an Assistant Card, {@code false} otherwise.
     *
     * @param playerPlanningPhaseDone the new flag.
     */

    public void setPlayerPlanningPhaseDone(boolean playerPlanningPhaseDone) {
        this.playerPlanningPhaseDone = playerPlanningPhaseDone;
    }

    /**
     * Checks if the current round's Planning Phase has ended.
     *
     * @return {@code true} if the current round's Planning Phase has ended, {@code false} otherwise.
     */

    public boolean getPlanningPhaseDone() {
        return planningPhaseDone;
    }

    /**
     * Sets the boolean flag to {@code true} if the current round's Planning Phase has ended, {@code false} otherwise.
     *
     * @param planningPhaseDone the new flag.
     */

    public void setPlanningPhaseDone(boolean planningPhaseDone) {
        this.planningPhaseDone = planningPhaseDone;
    }

    /**
     * Checks if the current player's Action Phase has ended.
     *
     * @return {@code true} if the current player's Action Phase has ended, {@code false} otherwise.
     */

    public boolean getPlayerActionPhaseDone() {
        return playerActionPhaseDone;
    }

    /**
     * Sets the boolean flag to {@code true} if the current player's Action Phase has ended, {@code false} otherwise.
     *
     * @param playerActionPhaseDone the new flag.
     */

    public void setPlayerActionPhaseDone(boolean playerActionPhaseDone) {
        this.playerActionPhaseDone = playerActionPhaseDone;
    }

    /**
     * Returns the current player's index.
     *
     * @return the {@code int} corresponding to the current player's index in the list of players (starting from 0).
     */

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Sets the current player's index (when a player ends a game phase, its index is overwritten by the next player's index).
     *
     * @param currentPlayerIndex the new index.
     */

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    /**
     * Returns the map that contains the {@link VirtualView} associated to each player/client.
     *
     * @return the {@link Map} that contains a {@link VirtualView} for each nickname associated to a player.
     */

    public Map<String, VirtualView> getVirtualViewMap() {
        return virtualViewMap;
    }

    // GameController methods

    /* ---------------------------------------------------------------
    ! (network -> Server handles following stuff)
    ! Fist thing login in the server, gameController not exists yet.
    ! Second thing, the client can choose to join or create a game.
    ! -> If the game is new we create a new gameController based on
    !    the CreateGameMessage params
    ! -> If the game already exists JoinGameMessage with gameID.
    ! (controller -> Controller handles game phases from now on)
    ! From now on the GameController exists and handle match phases.
    --------------------------------------------------------------*/

    /**
     * Receives a message from a client and executes different actions according to the message's type and
     * according to which state is the game in: adding players to the queue (state {@code SETUP}) or modifying
     * parts of the model (state {@code IN_GAME}).
     *
     * @param receivedMessage the message sent by the client.
     * @throws TryAgainException if an exception cannot be caught by the controller, it is caught by the {@link Server} class.
     */

    public void getMessage(Message receivedMessage) throws TryAgainException {
        switch (gameState) {
            case SETUP:
                if (receivedMessage.getMessageType() != MessageType.WIZARD_ID) {
                    throw new WrongMessageSentException("You have to choose a wizardID before starting the game!");
                }
                addPlayerToGame(receivedMessage);
                if(game.getPlayers().size() == game.getPlayersNumber())
                    startGame();
                else
                    if(!virtualViewMap.isEmpty()) {
                        int playersToWait = game.getPlayersNumber() - gameQueue.size();
                        if(playersToWait > 0)
                            broadcastGenericMessage("Please wait for " + playersToWait + " more player(s) to join.");
                        else
                            broadcastGenericMessage("Please wait for the other players to choose their WizardIDs.");
                    }
                break;
            case IN_GAME:
                if (!receivedMessage.getNickname().equals(game.getCurrentPlayer().getNickname()))
                    throw new WrongTurnException("Another player is playing! Please wait.");
                else {
                    if (!planningPhaseDone) {
                        planningPhase(receivedMessage);
                        if (playerPlanningPhaseDone) {
                            currentPlayerIndex++;
                            if (currentPlayerIndex == game.getPlayersNumber())
                                endPlanningPhase();
                            else
                                nextPlayerPlanningPhase();
                        }
                        else{
                            if(!virtualViewMap.isEmpty()) {
                                LOGGER.info(game.getCurrentPlayer().getNickname() + " is choosing another Assistant Card to play.");
                                virtualViewMap.get(game.getCurrentPlayer().getNickname()).askAssistantCard();
                            }
                        }
                    } else {
                        actionPhase(receivedMessage);
                        if (playerActionPhaseDone) {
                            currentPlayerIndex++;
                            if (currentPlayerIndex == game.getPlayersNumber())
                                nextRound();
                            else {
                                nextPlayerActionPhase();
                            }
                        }
                    }
                }
                break;
            default:
                LOGGER.severe(INVALID_STATE);
                break;
        }
    }

    /**
     * Sets the game's state to {@code SETUP}.
     */

    public void goToSetupPhase(){
        setGameState(GameState.SETUP);
    }

    /**
     * Creates the game instance according to the number of players and to the specific (sub)class of the controller.
     *
     * @param playerNum the given number of players.
     */

    public void prepareGame(int playerNum) {
        if (this instanceof GameControllerExpertMode)
            this.game = new GameExpertMode(playerNum, new Constants(playerNum));
        else
            this.game = new Game(playerNum, new Constants(playerNum));
        goToSetupPhase();
    }

    /**
     * Adds the nickname chosen by a client in a queue. The queue contains the nickname of the clients
     * before they communicate their WizardID.
     *
     * @param nickname the nickname of the client.
     * @param virtualView the {@link VirtualView} associated to the client.
     */

    public void addPlayerToQueue(String nickname, VirtualView virtualView){
        if(gameQueue.size() < getGame().getPlayersNumber()) {
            this.gameQueue.add(nickname);
            this.virtualViewMap.put(nickname, virtualView);
            LOGGER.info("---------- Players in the queue, game: "+gameControllerID+" ----------");
            for (String player : gameQueue){
                LOGGER.info(player);
            }
            LOGGER.info("--------------- QUEUE END ----------------");
        }
        else {
            LOGGER.info("The game " + gameControllerID + " is full!");
        }
    }

    /**
     * Removes a player from the queue (given the nickname).
     *
     * @param nickname the nickname of the player to remove.
     */
    public void removePlayerFromQueue(String nickname){
        this.gameQueue.remove(nickname);
        this.virtualViewMap.remove(nickname);
        LOGGER.severe(nickname + " removed from game " + gameControllerID + " queue");
    }


    /**
     * Adds a player to the game once they have chosen a unique WizardID and sent it via a message. The WizardID will be
     * asked until a unique one is chosen.
     *
     * @param receivedMessage the message sent by the client.
     */
    public void addPlayerToGame(Message receivedMessage){

        boolean wizardIdAlreadyUsed = false;

        String nickname = gameQueue.stream().filter(nick -> nick.equals(receivedMessage.getNickname())).findFirst().get();
        Wizard wizardID = Wizard.valueOf(((WizardIDMessage) receivedMessage).getWizardID());
        for(int i=0; i<game.getPlayers().size(); i++) {
            if (wizardID.equals(game.getPlayers().get(i).getWizardID())) {
                wizardIdAlreadyUsed = true;
                virtualViewMap.get(receivedMessage.getNickname()).showGenericMessage("Wizard ID has already been chosen...");
                virtualViewMap.get(receivedMessage.getNickname()).askWizardID();
                break;
            }
        }
        if(!wizardIdAlreadyUsed) {
            getGame().addPlayer(new Player(wizardID, nickname, game.getConstants()));
            LOGGER.info("NEW PLAYER "+nickname+", wizard "+wizardID+ ", ADDED TO GAMECONTROLLER NUM. "+gameControllerID);
        }
    }

    /**
     * Starts the game by setting the game state to {@code IN_GAME}, arranging the {@link Game} model for the first
     * round according to Eriantys' rules and broadcasting a proper message to all the participant players.
     */

    public void startGame(){
        game.startGame();
        LOGGER.info("Game " + game + " has been initialized!");
        broadcastGameBoard();
        broadcastPhaseUpdate(false);
        setGameState(GameState.IN_GAME);
        // For old tests
        if(!virtualViewMap.isEmpty()) {
            showDeck(virtualViewMap.get(game.getCurrentPlayer().getNickname()));
            virtualViewMap.get(game.getCurrentPlayer().getNickname()).askAssistantCard();
            broadcastWaitingMessage();
        }
    }

    /**
     * Allows the current player to play his Planning Phase properly.
     *
     * @param message the message sent by the client.
     */

    public void planningPhase(Message message) throws WrongMessageSentException {
        if (message.getMessageType() == MessageType.ASSISTANT_CARD_REPLY) {
            handleAssistantCardChoice(message);
        }
        else
            throw new WrongMessageSentException("Wrong message sent.");
    }

    /**
     * Allows the next player to play his Planning Phase properly by resetting some controller's variables.
     */

    public void nextPlayerPlanningPhase(){
        playerPlanningPhaseDone = false;
        game.setCurrentPlayer(game.getPlayers().get(currentPlayerIndex));
        if(!virtualViewMap.isEmpty()) {
            showDeck(virtualViewMap.get(game.getCurrentPlayer().getNickname()));
            broadcastWaitingMessage();
            virtualViewMap.get(game.getCurrentPlayer().getNickname()).askAssistantCard();
        }
    }

    /**
     * Sets the controller's variables and the players' order so that the first player of the current round's
     * Action Phase can play properly.
    */

    public void endPlanningPhase(){
        planningPhaseDone = true;
        setOrder();
        game.setCurrentPlayer(game.getPlayers().get(0));
        currentPlayerIndex = 0;
        movesLeft = game.getConstants().PLAYER_MOVES;
        if(!virtualViewMap.isEmpty()) {
            LOGGER.info("Action Phase is about to start.");
            broadcastPhaseUpdate(true);
            broadcastGameStatusFirstActionPhase();
            broadcastWaitingMessage();
            virtualViewMap.get(game.getCurrentPlayer().getNickname()).askMoveStudent();
        }
    }

    /**
     * Establishes the right flow of the current player's Action Phase by using the controller's variables.
     *
     * @param message the message sent by the client.
     * @throws TryAgainException if an exception cannot be caught by the controller, it is caught by the {@link Server} class.
     */

    public void actionPhase(Message message) throws TryAgainException {

        switch(message.getMessageType()){
            case MOVE_TO_TABLE_REPLY:
            case MOVE_TO_ISLAND_REPLY:
                try {
                    if (movesLeft == 0) {
                        throw new WrongMessageSentException("No moves left!");
                    } else {
                        handleStudentMovement(message);
                        movesLeft--;
                        if (!virtualViewMap.isEmpty() && movesLeft > 0) {
                            LOGGER.info(game.getCurrentPlayer().getNickname() + " has moved a student.");
                            broadcastGameBoard();
                            broadcastWaitingMessage();
                            virtualViewMap.get(game.getCurrentPlayer().getNickname()).askMoveStudent();
                        }
                        if (!virtualViewMap.isEmpty() && movesLeft == 0) {
                            LOGGER.info(game.getCurrentPlayer().getNickname() + " has run out of students' moves.");
                            broadcastGameBoard();
                            broadcastWaitingMessage();
                            virtualViewMap.get(game.getCurrentPlayer().getNickname()).askMotherNatureSteps();
                        }
                    }
                }
                catch(FullTableException | StudentNotFoundException | IslandNotFoundException | NonExistentColorException e){
                    if(!virtualViewMap.isEmpty()) {
                        LOGGER.info(game.getCurrentPlayer().getNickname() + " has occurred in "
                                + e.getClass().getSimpleName() + ": " + e.getMessage());
                        virtualViewMap.get(game.getCurrentPlayer().getNickname()).
                                showGenericMessage(e.getMessage());
                        virtualViewMap.get(game.getCurrentPlayer().getNickname()).askMoveStudent();
                    }
                }
                break;
            case MOTHER_NATURE_STEPS_REPLY:
                try {
                    if (movesLeft == 0 && !motherNatureMoved) {
                        handleMotherNature(message);
                        motherNatureMoved = true;
                        game.islandConquerCheck(game.getBoard().getMotherNaturePos());
                        if (!virtualViewMap.isEmpty()) {
                            LOGGER.info(game.getCurrentPlayer().getNickname() + " has moved Mother Nature.");
                            broadcastGameBoard();
                            broadcastWaitingMessage();
                            virtualViewMap.get(game.getCurrentPlayer().getNickname()).askCloud();
                        }
                    }
                    else
                        throw new WrongMessageSentException("You need to move other " + movesLeft +
                                " students before moving Mother Nature!");
                }
                catch(InvalidNumberOfStepsException | IslandNotFoundException e){
                    if(!virtualViewMap.isEmpty()) {
                        LOGGER.info(game.getCurrentPlayer().getNickname() + " has occurred in "
                                + e.getClass().getSimpleName() + ": " + e.getMessage());
                        virtualViewMap.get(game.getCurrentPlayer().getNickname()).
                                showGenericMessage(e.getMessage());
                        virtualViewMap.get(game.getCurrentPlayer().getNickname()).askMotherNatureSteps();
                    }
                }
                break;
            case CLOUD_CHOICE_REPLY:
                try {
                    if (motherNatureMoved) {
                        handleCloudChoice(message);
                        LOGGER.info(game.getCurrentPlayer().getNickname() + " has chosen a cloud.");
                    }
                    else
                        throw new WrongMessageSentException("You need to move mother nature first!");
                }
                catch(IndexOutOfBoundsException e){
                    if(!virtualViewMap.isEmpty()) {
                        LOGGER.info(game.getCurrentPlayer().getNickname() + " has occurred in "
                                + e.getClass().getSimpleName() + ": " + e.getMessage());
                        virtualViewMap.get(game.getCurrentPlayer().getNickname()).
                                showGenericMessage("There's no cloud with such id, please try again.");
                        virtualViewMap.get(game.getCurrentPlayer().getNickname()).askCloud();
                    }
                }
                catch(EmptyCloudException e){
                    if(!virtualViewMap.isEmpty()) {
                        LOGGER.info(game.getCurrentPlayer().getNickname() + " has occurred in "
                                + e.getClass().getSimpleName() + ": " + e.getMessage());
                        virtualViewMap.get(game.getCurrentPlayer().getNickname()).
                                showGenericMessage(e.getMessage());
                        virtualViewMap.get(game.getCurrentPlayer().getNickname()).askCloud();
                    }
                }
                break;
            default:
                throw new WrongMessageSentException("Wrong message sent.");
        }

    }

    /**
     * Resets the controller's variables in order to let the next player play his Action Phase properly.
     */

    public void nextPlayerActionPhase(){
        game.setCurrentPlayer(game.getPlayers().get(currentPlayerIndex));
        movesLeft = game.getConstants().PLAYER_MOVES;
        playerActionPhaseDone = false;
        motherNatureMoved = false;
        if(!virtualViewMap.isEmpty()) {
            broadcastGameBoard();
            broadcastWaitingMessage();
            virtualViewMap.get(game.getCurrentPlayer().getNickname()).askMoveStudent();
        }
    }

    /**
     * Sets the controller's variables in order to let the players play the next round properly. If the game board's
     * students' bag is empty, instantly ends the game.
     */

    public void nextRound(){
        try{
            if(isStudentBagEmpty())
                declareWinningPlayer();
            game.refillClouds();
        }
        catch(TieException e){
            quit();
        }
        catch(EmptyBagException ex){
            LOGGER.info(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        currentPlayerIndex = 0;
        game.setCurrentPlayer(game.getPlayers().get(currentPlayerIndex));
        playerPlanningPhaseDone = false;
        planningPhaseDone = false;
        playerActionPhaseDone = false;
        motherNatureMoved = false;
        game.setRoundNumber(game.getRoundNumber() + 1);
        for(Player player : game.getPlayers())
            player.resetLatestAssistantCardPlayed();
        if(!virtualViewMap.isEmpty()) {
            LOGGER.info("Planning Phase is about to start.");
            broadcastGameBoard();
            broadcastPhaseUpdate(false);
            showDeck(virtualViewMap.get(game.getCurrentPlayer().getNickname()));
            broadcastWaitingMessage();
            virtualViewMap.get(game.getCurrentPlayer().getNickname()).askAssistantCard();
        }
    }

    /**
     * Allows the current player to play the chosen Assistant Card, if it is playable: in the case it is, all the other
     * players will be notified; otherwise, the current player will be asked to choose another one.
     *
     * @param receivedMessage the message sent by the client.
     */

    public void handleAssistantCardChoice(Message receivedMessage){
       String chosenCard = ((AssistantCardMessage) receivedMessage).getCardName().toUpperCase();
       LOGGER.info(chosenCard);
       if(isAssistantCardPlayable(chosenCard)) {
           game.getCurrentPlayer().playAssistantCard(chosenCard);
           playerPlanningPhaseDone = true;
           if(!virtualViewMap.isEmpty())
               broadcastUpdateMessage(game.getCurrentPlayer().getNickname() + " has played the " + chosenCard + " Assistant Card!");
       }
       else {
           if(!virtualViewMap.isEmpty())
               virtualViewMap.get(receivedMessage.getNickname()).showGenericMessage("You can't play this assistant card!");
       }
    }

    /**
     * Checks if an Assistant Card is playable by a specific player.
     *
     * @param cardName the name of the chosen card.
     * @return {@code true} if the chosen card is playable, {@code false} otherwise.
     */

    public boolean isAssistantCardPlayable(String cardName){

        // Checks if the chosen card is present in the current player's deck.
        boolean found = false;
        for(AssistantCard card : game.getCurrentPlayer().getDeck()){
            if(card.getName().equals(cardName))
                found = true;
        }
        if(!found) // If it is not, the card is not playable.
            return false;

        // If the current player is the first player of the current round's Planning Phase, the card is surely playable
        if(game.getCurrentPlayer().equals(game.getPlayers().get(0)))
            return true;

        // Checks if the card has not been played by other players before the current.
        // In the case it was, checks if the current players has no other possible choices.
        List<Player> players = game.getPlayers();
        List<AssistantCard> cardsPlayed = new ArrayList<>(players.size());
        for(Player player : players){
            if(player.getLatestAssistantCardPlayed() != null)
                cardsPlayed.add(player.getLatestAssistantCardPlayed());
        }
        for(AssistantCard card : cardsPlayed){
            if(card.getName().equals(cardName)){
                List<AssistantCard> playerCards = new ArrayList<>(game.getCurrentPlayer().getDeck());
                playerCards.remove(card);
                for(AssistantCard playerCard : playerCards) {
                    for (AssistantCard cardToCheck : cardsPlayed) {
                        if (!playerCard.getName().equals(cardToCheck.getName()))
                            return false;
                    }
                }
            }
        }

        //If every previous check doesn't return false
        return true;
    }

    /**
     * Establishes the new players' order according to the Assistant Cards played, then sets the model accordingly.
     */

    public void setOrder(){
        List<Player> players = game.getPlayers();
        for(int i = 0; i < game.getPlayersNumber(); i++){
            for (int j = i+1; j < game.getPlayersNumber(); j++){
                if(players.get(i).getLatestAssistantCardPlayed().getWeight() >
                        players.get(j).getLatestAssistantCardPlayed().getWeight()) {
                    Collections.swap(players, i, j);
                }
            }
        }
        game.setPlayers(players);
    }

    /**
     * Handles the movement of a student, as desired by the current player. According to the choice made, it may be
     * moved to its table or to a specified island. The color of the student to move is specified in the message
     * received from the client. If no exception is thrown, every player (except the current one) will be notified.
     *
     * @param receivedMessage the message sent by the client.
     * @throws FullTableException if the table of the specified color is full.
     * @throws StudentNotFoundException if the current player has no student of the specified color in the hall.
     * @throws NonExistentColorException if a non-existent color is somehow encapsulated in the message.
     * @throws IslandNotFoundException if the specified island ID is not associated to any of the currently existing
     *                                 islands.
     */

    public void handleStudentMovement(Message receivedMessage)
            throws FullTableException, StudentNotFoundException, NonExistentColorException, IslandNotFoundException {
        if (receivedMessage.getMessageType() == MessageType.MOVE_TO_TABLE_REPLY) {
            String color = ((MoveToTableMessage) receivedMessage).getColor();
            game.playerMovesStudent(color);
            if (!virtualViewMap.isEmpty())
                broadcastUpdateMessage(game.getCurrentPlayer().getNickname() + " has moved a " + color + " student to its table!");

        }
        if (receivedMessage.getMessageType() == MessageType.MOVE_TO_ISLAND_REPLY) {
            String color = ((MoveToIslandMessage) receivedMessage).getColor();
            int islandID = ((MoveToIslandMessage) receivedMessage).getIslandID();
            game.playerMovesStudent(color, islandID);
            if (!virtualViewMap.isEmpty())
                broadcastUpdateMessage(game.getCurrentPlayer().getNickname() + " has moved a " + color + " student to Island " + islandID + "!");
        }
    }

    /**
     * Handles Mother Nature's movement, as desired by the current player. If no exception is thrown, every player
     * (except the current one) will be notified.
     *
     * @param receivedMessage the message sent by the client.
     * @throws InvalidNumberOfStepsException if the current player asks Mother Nature to move of too many steps
     *                                       (or of less than one).
     * @throws IslandNotFoundException if a non-existent island is somehow (trying to be) reached.
     */

    public void handleMotherNature(Message receivedMessage)
            throws InvalidNumberOfStepsException, IslandNotFoundException {
        game.moveMotherNature(((MotherNatureStepsMessage) receivedMessage).getSteps());
        winCheck();
        if (!virtualViewMap.isEmpty())
            broadcastUpdateMessage(game.getCurrentPlayer().getNickname() + " has moved Mother Nature to Island "
                    + game.getBoard().getMotherNaturePos() + "!");
    }

    /**
     * Handles the current player's cloud choice at the end of their Action Phase, allowing them to empty one.
     * If no exception is thrown, every player (except the current one) will be notified.
     *
     * @param receivedMessage the message sent by the client.
     * @throws EmptyCloudException if the chosen cloud has already been emptied.
     * @throws IndexOutOfBoundsException if the ID specified in the message does not exist.
     */

    public void handleCloudChoice(Message receivedMessage) throws EmptyCloudException, IndexOutOfBoundsException {
        game.takeStudentsFromCloud(((CloudChoiceMessage) receivedMessage).getCloudID());
        playerActionPhaseDone = true;
        if(!virtualViewMap.isEmpty())
            broadcastUpdateMessage(game.getCurrentPlayer().getNickname() + " has emptied the Cloud " +
                    (((CloudChoiceMessage) receivedMessage).getCloudID() + 1) + "!");
    }

    /**
     * Instantly ends the game if there are less than four islands on the game board, if a player has emptied its
     * tower room or if the round number 10 has ended.
     */

    public void winCheck(){
        try {
            if(noTowersLeftCheck() || lessThanFourIslandsCheck() ||
                    (game.getRoundNumber() == 10 && currentPlayerIndex == game.getPlayersNumber()-1)) {
                LOGGER.info(END_STATE + game.getCurrentPlayer().getNickname());
                declareWinningPlayer();
                quit();
            }
        }
        catch(TieException e){
            broadcastGenericMessage(e.getMessage());
        }
    }

    /**
     * Checks if the current player has no towers left in their school.
     *
     * @return {@code true} if the current player's tower hall has no tower, {@code false} otherwise.
     */

    public boolean noTowersLeftCheck(){
        boolean flag = false;
        for(Player player : game.getPlayers()){
            if (player.getSchool().getTowerRoom().getTowersLeft() <= 0) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * Checks if the game's students' bag is empty.
     *
     * @return {@code true} if the game's students' bag is empty, {@code false} otherwise.
     */

    public boolean isStudentBagEmpty(){
        return game.getBoard().getStudentsBag().size() == 0;
    }

    /**
     * Checks if the game board's archipelagos has less than four islands.
     *
     * @return {@code true} if the game board's archipelagos has less than four islands, {@code false} otherwise.
     */

    public boolean lessThanFourIslandsCheck(){
        return game.getBoard().getIslands().getSize() <= 3;
    }

    /**
     * Honors the player that has won the game. The player who wins the game is the player who has the fewer amount of
     * towers left in their tower room. In case of tie, the winning player is the one with the biggest amount of
     * professors. In the very rare case of another tie, there's no winning player, and an exception is thrown.
     */

    public void declareWinningPlayer() throws TieException{
        List<Player> players = game.getPlayers();
        Player winningPlayer = players.get(0);
        int minTowers = players.get(0).getSchool().getTowerRoom().getTowersLeft();
        for(int i=1; i<players.size(); i++){
            if(players.get(i).getSchool().getTowerRoom().getTowersLeft() < minTowers) {
                winningPlayer = players.get(i);
                minTowers = winningPlayer.getSchool().getTowerRoom().getTowersLeft();
            }
            else {
                // If two players have the same amount of towers left, the winning player is the one with
                // the biggest number of professors
                if (players.get(i).getSchool().getTowerRoom().getTowersLeft() == minTowers) {
                    Color[] colors = Color.values();
                    int profCurrentlyWinningPlayer = 0;
                    int profChallengingPlayer = 0;
                    try {
                        for (Color color : colors) {
                            if (winningPlayer.getSchool().getTable(color.toString()).getHasProfessor())
                                profCurrentlyWinningPlayer++;
                            if (players.get(i).getSchool().getTable(color.toString()).getHasProfessor())
                                profChallengingPlayer++;
                        }
                    }
                    // it is impossible to iterate over tables of non-existing colors
                    catch (NonExistentColorException ignored) {
                    }
                    if (profCurrentlyWinningPlayer < profChallengingPlayer)
                        winningPlayer = players.get(i);
                    if (profCurrentlyWinningPlayer == profChallengingPlayer)
                        throw new TieException("There's no winning player!");
                }
            }
        }
        broadcastGenericMessage(winningPlayer.getNickname() + " has won the game! Congratulations!");
    }

    /**
     * Broadcasts a generic message to all the players connected to the game.
     *
     * @param message the message to broadcast to the players.
     */

    public void broadcastGenericMessage(String message) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showGenericMessage(message);
        }
    }

    /**
     * Broadcasts a disconnection message to all the players still connected to the game. Please remember that the
     * disconnection of a player causes the instant end of the game with no player declared winner, but all the players
     * who are still connected to the server may choose whether to create a new game or to join an existing one.
     *
     * @param message the message to broadcast to the connected players.
     */

    public void broadcastDisconnectionMessage(String message){
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showDisconnectionMessage(message);
        }
    }

    /**
     * Broadcasts a waiting message to all the players who are not playing at the moment.
     */

    public void broadcastWaitingMessage(){
        for (VirtualView vv : virtualViewMap.values()) {
            if(!vv.equals(virtualViewMap.get(game.getCurrentPlayer().getNickname())))
                vv.showUpdateMessage("It's " + game.getCurrentPlayer().getNickname() + "'s turn. Please wait.");
        }
    }

    /**
     * Notifies all the players (except for the current player) of the current players' latest action.
     *
     * @param message the message to broadcast to the players.
     */

    public void broadcastUpdateMessage(String message){
        for (VirtualView vv : virtualViewMap.values()) {
            if(!vv.equals(virtualViewMap.get(game.getCurrentPlayer().getNickname())))
                vv.showUpdateMessage("UPDATE: " + message);
        }
    }

    /**
     * Notifies all the players of a phase change.
     *
     * @param isActionPhase {@code true} if the new current phase is the Action Phase, {@code false} otherwise.
     */

    public void broadcastPhaseUpdate(boolean isActionPhase){
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showPhaseUpdate(isActionPhase);
        }
    }

    /**
     * Broadcasts the game board plus useful information about the last Planning Phase (and the consequent players'
     * order) to all the players.
     */

    public void broadcastGameStatusFirstActionPhase(){
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showGameStatusFirstActionPhase(this.game);
        }
    }

    /**
     * Broadcasts the game board to all the players.
     */

    public void broadcastGameBoard(){
        for(VirtualView vv : virtualViewMap.values()){
            vv.showGameStatus(this.game);
        }
    }

    /**
     * Shows to the specified {@link VirtualView} the associated player's Assistant Card deck.
     *
     * @param virtualView the {@link VirtualView} to send the deck to.
     */

    public void showDeck(VirtualView virtualView){
        virtualView.showDeck(this.game);
    }

    /**
     * Ends the game by closing the client apps and removing the game controller from the Server.
     */

    public void quit(){
        LOGGER.info("The game number " + gameControllerID + " has ended.");
        for(VirtualView vv : virtualViewMap.values()){
            vv.showGameStatus(this.game);
            gameQueue.remove(gameQueue.get(0)); // useful to trigger the game controller removal in the Server
            vv.quit();
        }
    }

}
