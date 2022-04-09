package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exceptions.*;

import java.util.List;

public class GameExpertMode extends Game {

    private final CharacterCard[] characters;

    public GameExpertMode(int playersNumber) {
        super(playersNumber);
        this.characters = new CharacterCard[Constants.CHARACTERS_NUM];
    }

    // TODO: at the end of player's turn, need to set CharacterCardAlreadyPlayed to false

    /*
        This method lets a player play a chosen character card. If it involves a change in an already existent
        method, its effect will not be visible right after use; otherwise, the card's doEffect method will be
        instantly triggered.
     */

    public void playerPlaysCharacterCard(int id) {

        try {
            if (getCurrentPlayer().getCharacterCardAlreadyPlayed())
                throw new CharacterCardAlreadyPlayedException("You have already played a character card this round!");

            boolean found = false;
            for (CharacterCard card : characters) {
                if (card.getId() == id) {
                    if(!(getCurrentPlayer().getCoinsWallet() >= card.getCost()))
                        throw new NotEnoughCoinsException("You need " +
                                (card.getCost() - getCurrentPlayer().getCoinsWallet())
                                + " more money to use this card!");
                    card.setIsActive(true);
                    found = true;
                    getCurrentPlayer().setCoinsWallet(getCurrentPlayer().getCoinsWallet() - card.getCost());
                    if(card.getId() == 5 || card.getId() == 10 || card.getId() == 3){
                        card.doEffect(this);
                        card.setUpCard();
                    }
                }
            }

            if (!found)
                throw new NonExistentCharacterCardException("No character card with id " + id + " exists.");
            else
                getCurrentPlayer().setCharacterCardAlreadyPlayed(true);

        } catch (CharacterCardAlreadyPlayedException | NonExistentCharacterCardException | NotEnoughCoinsException e) {
            e.printStackTrace();
        }

    }

    /*
        This overridden method controls whether the card which modifies Mother Nature's movement algorithm has
        been chosen by the current player.
     */

    @Override
    public void moveMotherNature(int steps) throws InvalidNumberOfStepsException {

        setMaxSteps(getCurrentPlayer().getLastCardPlayed().getMotherNatureSteps());
        for (CharacterCard card : characters) {
            if (card.getId() == 4 && card.getIsActive()) {
                card.doEffect(this);
                card.setUpCard();
            }
        }
        if (steps > getMaxSteps() || steps < Constants.MIN_NUM_OF_STEPS)
            throw new InvalidNumberOfStepsException("The number of steps selected is not valid.");
        getBoard().moveMotherNature(steps);

    }

    /*
        This overridden method controls whether the card which modifies the profCheck algorithm has been chosen
        by the current player.
     */

    @Override
    public void profCheck() {

        boolean done = false;
        for (CharacterCard card : characters) {
            if (card.getId() == 2 && card.getIsActive()) {
                card.doEffect(this);
                card.setUpCard();
                done = true;
            }
        }

        if(!done)
            Game.profCheckAlgorithm(getPlayersNumber(), getPlayers());

    }

    /*
        This overridden method controls whether one of the cards which modify the islandConquerCheck algorithm
        has been chosen by the current player.
     */

    @Override
    public void islandConquerCheck(int islandID) {
      
        boolean done = false;
        for (CharacterCard card : characters) {
            if ((card.getId() == 6 || card.getId() == 8 || card.getId() == 9)
                    && card.getIsActive()) {
                card.doEffect(this);
                card.setUpCard();
                done = true;
            }
        }
        if (!done)
            getBoard().islandConquerCheck(getCurrentPlayer(), islandID);
      
    }

}
