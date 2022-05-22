package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.message.*;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.view.VirtualView;

import java.util.*;

public class GameController{
    private Game game;
    private int gameControllerID;
    private boolean planningPhaseDone;
    private boolean playerActionPhaseDone;
    private int currentPlayerIndex;
    private int movesLeft;
    private boolean motherNatureMoved;
    private GameState gameState;
    protected static final String INVALID_STATE = "Invalid game state";
    protected static final String END_STATE = "The game has ended, the winner is: ";
    private final List<String> gameQueue;
    private transient Map<String, VirtualView> virtualViewMap;

    /*
        Initialize GameController
     */
    public GameController(){
        this.planningPhaseDone = false;
        this.playerActionPhaseDone = false;
        this.currentPlayerIndex = 0;
        this.movesLeft = Constants.PLAYER_MOVES;
        this.motherNatureMoved = false;
        this.gameQueue = new ArrayList<>();
        this.gameState = GameState.SETUP;
        this.virtualViewMap = Collections.synchronizedMap(new HashMap<>());
    }

    // Getter and Setter methods

    public List<String> getGameQueue() {
        return gameQueue;
    }

    public void setGameControllerID(int gameControllerID) {
        this.gameControllerID = gameControllerID;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    public void setMovesLeft(int movesLeft) {
        this.movesLeft = movesLeft;
    }

    public boolean getMotherNatureMoved() {
        return motherNatureMoved;
    }

    public void setMotherNatureMoved(boolean motherNatureMoved) {
        this.motherNatureMoved = motherNatureMoved;
    }

    public boolean getPlanningPhaseDone() {
        return planningPhaseDone;
    }

    public void setPlanningPhaseDone(boolean planningPhaseDone) {
        this.planningPhaseDone = planningPhaseDone;
    }

    public boolean getPlayerActionPhaseDone() {
        return playerActionPhaseDone;
    }

    public void setPlayerActionPhaseDone(boolean playerActionPhaseDone) {
        this.playerActionPhaseDone = playerActionPhaseDone;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

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

    public void getMessage(Message receivedMessage) throws TryAgainException {
        switch (gameState) {
            case SETUP:
                if (receivedMessage.getMessageType() != MessageType.WIZARD_ID) {
                    throw new WrongMessageSentException("Must communicate wizardID before starting the game");
                }
                addPlayerToGame(receivedMessage);
                if(game.getPlayers().size() == game.getPlayersNumber())
                    startGame();
                break;
            case IN_GAME:
                if(!receivedMessage.getNickname().equals(game.getCurrentPlayer().getNickname()))
                    throw new WrongTurnException("Another player is playing! Please wait.");
                else {
                    if (!planningPhaseDone) {
                        planningPhase(receivedMessage);
                        currentPlayerIndex++;
                        if (currentPlayerIndex == game.getPlayersNumber()) {
                            endPlanningPhase();
                            if(!virtualViewMap.isEmpty()) {
                                System.out.println(game.getCurrentPlayer().getNickname());
                                broadcastGameBoard();
                                virtualViewMap.get(game.getCurrentPlayer().getNickname()).askMoveStudent();
                            }
                        } else {
                            game.setCurrentPlayer(game.getPlayers().get(currentPlayerIndex));
                            if(!virtualViewMap.isEmpty()) {
                                System.out.println(game.getCurrentPlayer().getNickname());
                                showDeck(virtualViewMap.get(game.getCurrentPlayer().getNickname()));
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
                System.out.println(INVALID_STATE);
                break;
        }
    }

    // To enter the phase when players enter the game
    public void goToSetupPhase(){
        setGameState(GameState.SETUP);
    }

    /*
        This method creates the game based on the playerNum param
        and based on the instance (For expert mode or normal mode)
     */
    public void prepareGame(int playerNum) {
        //TODO: exception for numbers > 3
        if (this instanceof GameControllerExpertMode)
            this.game = new GameExpertMode(playerNum, new Constants(playerNum));
        else
            this.game = new Game(playerNum, new Constants(playerNum));
        goToSetupPhase();
    }

    /*
        This method adds the nickname of the client in a queue.
        The queue contains the nickname of the clients before they
        communicate their WizardID.
     */
    public void addPlayerToQueue(String nickname, VirtualView virtualView){
        if(gameQueue.size() < getGame().getPlayersNumber()) {
            this.gameQueue.add(nickname);
            this.virtualViewMap.put(nickname, virtualView);
            System.out.println("----------Players in the queue, game: "+gameControllerID+"----------");
            gameQueue.forEach(System.out::println);
            System.out.println("---------------QUEUE END----------------");
        }
        else
            System.out.println("Full game");
    }


    /*
        This method add the player (based on the receivedMessage's nickname) to the game.
        If their WizardID has already been chosen it will throw an Exception
     */
    //TODO: Exception
    public void addPlayerToGame(Message receivedMessage){

        boolean wizardIdAlreadyUsed = false;

        String nickname = String.valueOf(gameQueue.stream().filter(nick -> nick.equals(receivedMessage.getNickname())).findFirst().get());
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
            System.out.println("NEW PLAYER ADDED: "+nickname+" wizard: "+wizardID);
        }
    }

    public void startGame(){
        game.startGame();
        System.out.println("game: " + game + " has been initialized");
        broadcastGenericMessage("GAME CAN NOW START");
        setGameState(GameState.IN_GAME);
        // For old tests
        if(!virtualViewMap.isEmpty()) {
            System.out.println(game.getCurrentPlayer().getNickname());
            showDeck(virtualViewMap.get(game.getCurrentPlayer().getNickname()));
            virtualViewMap.get(game.getCurrentPlayer().getNickname()).askAssistantCard();
        }
    }

    /*
        This method allows the current player to play his planning phase properly.
     */

    public void planningPhase(Message message) throws AssistantCardAlreadyPlayedException{
        if (message.getMessageType() == MessageType.ASSISTANT_CARD_REPLY) {
            handleAssistantCardChoice(message);
        }
        else
            virtualViewMap.get(message.getNickname()).showGenericMessage("Wrong message sent.");
    }

    /*
        This method establishes the right flow of a player's action phase.
     */

    public void actionPhase(Message message) throws TryAgainException {

        switch(message.getMessageType()){
            case MOVE_TO_TABLE_REPLY:
            case MOVE_TO_ISLAND_REPLY:
                if(movesLeft == 0){
                    throw new WrongMessageSentException("No moves left!");
                }
                else{
                    handleStudentMovement(message);
                    movesLeft--;
                    if(!virtualViewMap.isEmpty() && movesLeft > 0) {
                        System.out.println(game.getCurrentPlayer().getNickname());
                        broadcastGameBoard();
                        virtualViewMap.get(game.getCurrentPlayer().getNickname()).askMoveStudent();
                    }
                    if(!virtualViewMap.isEmpty() && movesLeft == 0) {
                        System.out.println(game.getCurrentPlayer().getNickname());
                        broadcastGameBoard();
                        virtualViewMap.get(game.getCurrentPlayer().getNickname()).askMotherNatureSteps();
                    }
                }
                break;
            case MOTHER_NATURE_STEPS_REPLY:
                if(movesLeft == 0 && !motherNatureMoved){
                    handleMotherNature(message);
                    motherNatureMoved = true;
                    game.islandConquerCheck(game.getBoard().getMotherNaturePos());
                    if(!virtualViewMap.isEmpty()) {
                        System.out.println(game.getCurrentPlayer().getNickname());
                        broadcastGameBoard();
                        virtualViewMap.get(game.getCurrentPlayer().getNickname()).askCloud();
                    }
                }
                else
                    throw new WrongMessageSentException("You need to move other " + movesLeft +
                            " students before moving Mother Nature!");
                break;
            case CLOUD_CHOICE_REPLY:
                if(motherNatureMoved) {
                    handleCloudChoice(message);
                    playerActionPhaseDone = true;
                }
                else
                    throw new WrongMessageSentException("You need to move mother nature first!");
                break;
            default:
                throw new WrongMessageSentException("Wrong message sent.");
        }

    }

    /*
       This method sets the class's attributes and the players' order so that the first player of the action phase
       can play properly.
    */

    public void endPlanningPhase(){
        planningPhaseDone = true;
        setOrder();
        game.setCurrentPlayer(game.getPlayers().get(0));
        currentPlayerIndex = 0;
    }

    /*
        This method sets the class's attributes in order to let the next player play his action phase properly.
     */

    public void nextPlayerActionPhase(){
        winCheck();
        game.setCurrentPlayer(game.getPlayers().get(currentPlayerIndex));
        movesLeft = Constants.PLAYER_MOVES;
        playerActionPhaseDone = false;
        motherNatureMoved = false;
        if(!virtualViewMap.isEmpty()) {
            System.out.println(game.getCurrentPlayer().getNickname());
            broadcastGameBoard();
            virtualViewMap.get(game.getCurrentPlayer().getNickname()).askMoveStudent();
        }
    }

    /*
        This method sets the class's attributes in order to play the next round properly.
     */

    public void nextRound(){
        try{
            game.refillClouds();
        }
        catch(EmptyBagException e){
            System.out.println(e.getMessage());
        }
        currentPlayerIndex = 0;
        game.setCurrentPlayer(game.getPlayers().get(currentPlayerIndex));
        movesLeft = Constants.PLAYER_MOVES;
        planningPhaseDone = false;
        playerActionPhaseDone = false;
        motherNatureMoved = false;
        game.setRoundNumber(game.getRoundNumber() + 1);
        for(Player player : game.getPlayers())
            player.resetLastAssistantCardPlayed();
        if(!virtualViewMap.isEmpty()) {
            System.out.println(game.getCurrentPlayer().getNickname());
            broadcastGameBoard();
            showDeck(virtualViewMap.get(game.getCurrentPlayer().getNickname()));
            virtualViewMap.get(game.getCurrentPlayer().getNickname()).askAssistantCard();
        }
    }

    /*
        The method receive a message from a player, if it is an AssistantCard
        the String value (name) is assigned to chosenCard.
        If the chosen card hasn't already been played the player plays the chosenCard.
     */

    public void handleAssistantCardChoice(Message receivedMessage) throws AssistantCardAlreadyPlayedException{
       String chosenCard = ((AssistantCardMessage) receivedMessage).getCardName().toUpperCase();
       System.out.println(chosenCard);
       if(isAssistantCardPlayable(chosenCard))
           game.getCurrentPlayer().playAssistantCard(chosenCard);
       else {
           if(!virtualViewMap.isEmpty()) {
               virtualViewMap.get(receivedMessage.getNickname()).showGenericMessage("You can't play this assistant card!");
               virtualViewMap.get(receivedMessage.getNickname()).askAssistantCard();
           }
           else throw new AssistantCardAlreadyPlayedException("You can't play this assistant card!");
       }
    }

    /*
        This method decides whether a card is playable by a player.
     */

    public boolean isAssistantCardPlayable(String cardName){
        boolean found = false;
        for(AssistantCard card : game.getCurrentPlayer().getDeck()){
            if(card.getName().equals(cardName))
                found = true;
        }
        if(!found)
            return false;
        List<Player> players = game.getPlayers();
        List<AssistantCard> cardsPlayed = new ArrayList<>(players.size());
        for(Player player : players){
            if(player.getLastAssistantCardPlayed() != null)
                cardsPlayed.add(player.getLastAssistantCardPlayed());
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
        return true;
    }

    /*
        The method order a copy of the players array based on the lastCardPlayed.getWeight attribute.
        In the end it set the new array in game.
        TODO: find a better way to order, the swap and order method may be set in the model and not in the controller.
     */

    public void setOrder(){
        List<Player> players = game.getPlayers();
        for(int i=0; i<game.getPlayersNumber()-1;i++){
            if(players.get(i).getLastAssistantCardPlayed().getWeight() >
                    players.get(i+1).getLastAssistantCardPlayed().getWeight()) {
                Collections.swap(players, i, i+1);
            }
        }
        game.setPlayers(players);
    }

    /*
        The method receive a message with the color of the student we want to move from
        the player's Hall to an Island or a Table
     */

    public void handleStudentMovement(Message receivedMessage) throws FullTableException, StudentNotFoundException,
            NonExistentColorException, IslandNotFoundException {
        if(receivedMessage.getMessageType() == MessageType.MOVE_TO_TABLE_REPLY){
            String color = ((MoveToTableMessage) receivedMessage).getColor();
            game.playerMovesStudent(color);
        }
        if(receivedMessage.getMessageType() == MessageType.MOVE_TO_ISLAND_REPLY){
            String color = ((MoveToIslandMessage) receivedMessage).getColor();
            int islandID = ((MoveToIslandMessage) receivedMessage).getIslandID();
            game.playerMovesStudent(color, islandID);
        }
    }

    /*
        The method receive a message with the number of steps chosen by the player.
        Mother nature will move.
     */

    public void handleMotherNature(Message receivedMessage) throws
            InvalidNumberOfStepsException, IslandNotFoundException{
        game.moveMotherNature(((MotherNatureStepsMessage) receivedMessage).getSteps());
    }

    /*
        The method handles the cloud choice at the end of the action phase.
     */

    public void handleCloudChoice(Message receivedMessage) throws EmptyCloudException {
        game.takeStudentsFromCloud(((CloudChoiceMessage) receivedMessage).getCloudID());
    }

    /*
        If one of the three game conditions is true, the game ends.
     */

    // TODO: need to discuss where to check, how to declare the winner and how to stop the game
    public void winCheck(){
        if(noTowersLeftCheck() || isStudentBagEmpty() || lessThanThreeIslandsCheck())
            declareWinningPlayer();
    }

    /*
        The noTowersLeftCheck() method controls if the current player (who is ending the turn)
        has no towers left in the school, if it is true the method declares the winner and
        print the winning player.
     */

    public boolean noTowersLeftCheck(){
        if(game.getCurrentPlayer().getSchool().getTowerRoom().getTowersLeft() == 0) {
            System.out.println(END_STATE+game.getCurrentPlayer());
            return true;
        }
        return false;
    }

    /*
        The isStudentBagEmpty() method controls if the students bag in game is empty.
        If it's true it calls declareWinningPlayer() and prints the winning player's name.
     */

    public boolean isStudentBagEmpty(){
        if(game.getBoard().getStudentsBag().size() == 0){
            Player winningPlayer = declareWinningPlayer();
            System.out.println(END_STATE+winningPlayer);
            return true;
        }
        return false;
    }

    /*
         The lessThanThreeIslandsCheck() method controls if the DoublyLinkedList of Islands
         in game contains three or fewer islands.
         If it is true it calls declareWinningPlayer() and prints the winning player's name.
    */

    public boolean lessThanThreeIslandsCheck(){
        if(game.getBoard().getIslands().getSize() <= 3){
            Player winningPlayer = declareWinningPlayer();
            System.out.println(END_STATE+winningPlayer);
            return true;
        }
        return false;
    }

    /*
        This method checks which player has the min number of towers in their
        TowerRoom.
        The method returns the winningPlayer.
     */

    public Player declareWinningPlayer(){
        List<Player> players = game.getPlayers();
        Player winningPlayer = players.get(0);
        int minTowers = players.get(0).getSchool().getTowerRoom().getTowersLeft();
        for(int i=1; i<players.size(); i++){
            if(players.get(i).getSchool().getTowerRoom().getTowersLeft() < minTowers) {
                winningPlayer = players.get(i);
                minTowers = winningPlayer.getSchool().getTowerRoom().getTowersLeft();
            }
        }
        return winningPlayer;
    }

    public void broadcastGenericMessage(String message) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showGenericMessage(message);
        }
    }

    public void broadcastGameBoard(){
        for(VirtualView vv : virtualViewMap.values()){
            vv.showGameStatus(this.game);
        }
    }

    public void showDeck(VirtualView virtualView){
        virtualView.showDeck(this.game);
    }
}
