package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.TryAgainException;
import it.polimi.ingsw.model.charactercards.Bard;

import java.io.Serializable;

/**
 * The Character Cards are the notable addition to the Expert mode of a game. Players can play one card per round,
 * if they have enough coins for it. The various cards offer a wide variety of effects that can significantly affect
 * the gameplay. Once used, the card's cost increases by one. The initial cost for each card vary from 1 to 3,
 * according to the different types.
 * This abstract class acts as a base for all the "real" cards, which inherit from this class and override some of
 * its methods.
 */

public abstract class CharacterCard implements Serializable {

    private final int id;
    private int cost;
    private boolean isActive;

    /**
     * Character Card constructor.
     *
     * @param id the card id.
     * @param initialCost the initial cost of the card.
     */

    public CharacterCard(int id, int initialCost) {
        this.id = id;
        this.cost = initialCost;
        this.isActive = false;
    }

    /**
     * Returns the ID of the Character Card.
     *
     * @return the {@code int} representing the ID of the card.
     */

    public int getId() {
        return id;
    }

    /**
     * Returns the current cost of the Character Card.
     *
     * @return the {@code int} representing the current cost of the card.
     */

    public int getCost() {
        return cost;
    }

    /**
     * Sets the cost of the Character Card.
     *
     * @param cost the new cost.
     */

    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * Checks if the Character Card is active (which means, its price has been paid but its effect hasn't been
     * performed yet).
     *
     * @return {@code true} if the card is active, {@code false} otherwise.
     */

    public boolean getIsActive(){
        return isActive;
    }

    /**
     * Sets the status (active / not active) of the Character Card.
     *
     * @param isActive the {@code boolean} representing whether the card is active ({@code true}) or not ({@code false}).
     */

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Allows the Character Card to do his effect.
     *
     * @param game the game (Expert mode) in which the card will do his effect.
     * @throws TryAgainException if something goes wrong (see the specific cards for details).
     */

    public abstract void doEffect(GameExpertMode game) throws TryAgainException;

    /**
     * Allows the view to properly show the students on the card.
     */

    public void showStudentsOnTheCard(){}

    /**
     * Updates the card's cost and its status once its effect has ended.
     */

    public void setUpCard(){
        this.setCost(getCost() + 1);
        this.setIsActive(false);
    }

}