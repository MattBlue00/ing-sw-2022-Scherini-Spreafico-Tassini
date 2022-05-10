package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.model.GameExpertMode;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.charactercards.*;
import it.polimi.ingsw.network.message.*;

import java.util.Collections;
import java.util.Random;

public class GameControllerExpertMode extends GameController{

    public GameControllerExpertMode() {
        super();
    }

    public void startGame(){

        for(int i = 0; i < getGame().constants.MAX_HALL_STUDENTS; i++) {
            for (Player player : getGame().getPlayers()) {
                player.getSchool().getHall().addStudent(getGame().getBoard().getStudentsBag().remove
                        (getGame().getBoard().getStudentsBag().size() - 1));
            }
        }

        CharacterCard[] cards = new CharacterCard[Constants.CHARACTERS_NUM];
        Random random = new Random();
        for(int i = 0; i < Constants.CHARACTERS_NUM; i++){
            // TODO: when all the CharacterCards are ready, the while loop has to be removed
            int num = 0;
            while(num == 0 || num == 1 || num == 7 || num == 11 || num == 12)
                num = random.nextInt(12) + 1;
            switch(num){
                case 1:
                    // cards[i] = new Monk();
                    break;
                case 2:
                    cards[i] = new Innkeeper();
                    break;
                case 3:
                    cards[i] = new Flagman();
                    break;
                case 4:
                    cards[i] = new Postman();
                    break;
                case 5:
                    cards[i] = new Healer();
                    break;
                case 6:
                    cards[i] = new Centaur();
                    break;
                case 7:
                    // cards[i] = new Jester();
                    break;
                case 8:
                    cards[i] = new Knight();
                    break;
                case 9:
                    cards[i] = new Villager();
                    break;
                case 10:
                    cards[i] = new Bard();
                    break;
                case 11:
                    // cards[i] = new Princess();
                    break;
                case 12:
                    // cards[i] = new Thief();
                    break;
                default: // should never enter here
                    break;
            }
        }
        ((GameExpertMode) getGame()).addCharacterCards(cards);

        Collections.shuffle(getGame().getPlayers());
        getGame().setCurrentPlayer(getGame().getPlayers().get(0));
        setGameState(GameState.IN_GAME);
    }

    @Override
    public void actionPhase(Message message) throws TryAgainException {

        switch(message.getMessageType()){
            case MOVE_TO_TABLE_REPLY:
            case MOVE_TO_ISLAND_REPLY:
                if(getMovesLeft() == 0){
                    throw new WrongMessageSentException("No moves left!");
                }
                else{
                    handleStudentMovement(message);
                    setMovesLeft(getMovesLeft() - 1);
                }
                break;
            case MOTHER_NATURE_STEPS_REPLY:
                if(getMovesLeft() == 0 && !getMotherNatureMoved()){
                    handleMotherNature(message);
                    setMotherNatureMoved(true);
                    getGame().islandConquerCheck(getGame().getBoard().getMotherNaturePos());
                }
                else
                    throw new WrongMessageSentException("You need to move other " + getMovesLeft() +
                            " students before moving Mother Nature!");
                break;
            case CLOUD_CHOICE_REPLY:
                if(getMotherNatureMoved()) {
                    handleCloudChoice(message);
                    setPlayerActionPhaseDone(true);
                }
                else
                    throw new WrongMessageSentException("You need to move mother nature first!");
                break;
            case CHARACTER_CARD_REPLY:
                if(!getMotherNatureMoved())
                    handleCharacterCardChoice(message);
                else
                    throw new WrongMessageSentException("You are no longer able to play a character card!");
                break;
            default:
                throw new WrongMessageSentException("Wrong message sent.");
        }

    }

    /*
        This method lets the current player play a character card, if possible.
     */

    public void handleCharacterCardChoice(Message receivedMessage) throws
            CharacterCardAlreadyPlayedException, NotEnoughCoinsException, CharacterCardNotFoundException {

        int chosenCardID = ((CharacterCardMessage) receivedMessage).getCardID();

        if(getGame() instanceof GameExpertMode) {

            if(receivedMessage instanceof CharacterCardMessageInt) {
                ((IntCard) ((GameExpertMode) getGame()).getCharacterCardByID(chosenCardID)).doOnClick(((CharacterCardMessageInt) receivedMessage).getPar());
            }
            if(receivedMessage instanceof CharacterCardMessageString) {
                ((StringCard) ((GameExpertMode) getGame()).getCharacterCardByID(chosenCardID)).doOnClick(((CharacterCardMessageString) receivedMessage).getPar());
            }
            if(receivedMessage instanceof CharacterCardMessageArraylistString){
                ((ArraylistStringCard) ((GameExpertMode) getGame()).getCharacterCardByID(chosenCardID)).doOnClick(((CharacterCardMessageArraylistString) receivedMessage).getPar());
            }

            ((GameExpertMode) getGame()).playerPlaysCharacterCard(chosenCardID);
        }
    }

    @Override
    public void nextPlayerActionPhase(){
        winCheck();
        getGame().getCurrentPlayer().setCharacterCardAlreadyPlayed(false);
        getGame().setCurrentPlayer(getGame().getPlayers().get(getCurrentPlayerIndex()));
        setMovesLeft(3);
        setPlayerActionPhaseDone(false);
        setMotherNatureMoved(false);
    }

    @Override
    public void nextRound(){
        try{
            getGame().refillClouds();
        }
        catch(EmptyBagException e){
            System.out.println(e.getMessage());
        }
        getGame().getCurrentPlayer().setCharacterCardAlreadyPlayed(false);
        setCurrentPlayerIndex(0);
        getGame().setCurrentPlayer(getGame().getPlayers().get(getCurrentPlayerIndex()));
        setMovesLeft(3);
        setPlanningPhaseDone(false);
        setPlayerActionPhaseDone(false);
        setMotherNatureMoved(false);
        getGame().setRoundNumber(getGame().getRoundNumber() + 1);
    }
}
