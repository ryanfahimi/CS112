/**
 * The BlackjackHand class represents a hand of playing cards in Blackjack.
 * It extends the Hand class and includes additional Blackjack-specific methods.
 */
class BlackjackHand extends Hand {
    private int numAces;

    /**
     * Constructs an empty BlackjackHand object.
     */
    public BlackjackHand() {
        super(11); // Maximum 11 cards in a blackjack hand
        numAces = 0;
    }

    /**
     * Adds a card to the hand.
     *
     * @param card The card to add
     */
    @Override
    public void addCard(Card card) {
        super.addCard(card);
        if (card.rank.toInt() == 1) {
            numAces++;
        }
    }

    /**
     * Returns the numerical value of the hand for Blackjack.
     * Aces are treated as 1 or 11 depending on the hand value.
     *
     * @return The hand's value
     */
    @Override
    public int getValue() {
        int numAces = this.numAces;
        // Handle aces as either 1 or 11, depending on the hand value
        while (value + 10 <= 21 && numAces > 0) {
            value += 10;
            numAces--;
        }
        return super.getValue();
    }

    /**
     * Returns whether the hand is a soft hand (contains an ace valued as 11).
     *
     * @return true if the hand is soft, false otherwise
     */
    public boolean isSoft() {
        return numAces > 0 && value + 10 < 21;
    }

    /**
     * Returns whether the hand is splittable (two cards of the same rank).
     *
     * @return true if the hand is splittable, false otherwise
     */
    public boolean isSplittable() {
        return numCards == 2 && hand[0].rank.toInt() == hand[1].rank.toInt();
    }

    /**
     * Returns whether the hand is a bust (value over 21).
     *
     * @return true if the hand is a bust, false otherwise
     */
    public boolean isBust() {
        return getValue() > 21;
    }

    /**
     * Returns whether the hand is a blackjack (value of 21).
     *
     * @return true if the hand is a blackjack, false otherwise
     */
    public boolean isBlackjack() {
        return getValue() == 21;
    }
}
