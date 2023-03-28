/**
 * The BlackjackHand class represents a hand of playing cards in Blackjack.
 * It extends the Hand class and includes additional Blackjack-specific methods.
 */
class BlackjackHand extends Hand {
    private int numAces;
    private int acesConsidered;

    /**
     * Constructs an empty BlackjackHand object.
     */
    public BlackjackHand() {
        super(11); // Maximum 11 cards in a blackjack hand
        numAces = 0;
        acesConsidered = 0;
    }

    /**
     * Adds a card to the hand.
     *
     * @param card The card to add
     */
    @Override
    public void addCard(Card card) {
        super.addCard(card);
        if (card.rank == Rank.ACE) {
            numAces++;
        }
    }

    /**
     * Calculates the total value of the hand, considering the best value for Aces.
     *
     * @return the total value of the hand
     */
    @Override
    public int getValue() {
        // Handle Aces (value of 1 or 11)
        while (value > 21 && acesConsidered < numAces) {
            value -= 10;
            acesConsidered++;
        }
        while (value + 10 <= 21 && acesConsidered > 0) {
            value += 10; // Subtract 10 to account for the Ace being worth 1 instead of 11
            acesConsidered--;
        }

        return value;
    }

    /**
     * Returns whether the hand is a soft hand (contains an ace valued as 11).
     *
     * @return true if the hand is soft, false otherwise
     */
    public boolean isSoft() {
        return numAces > acesConsidered;
    }

    /**
     * Returns whether the hand is splittable (two cards of the same rank).
     *
     * @return true if the hand is splittable, false otherwise
     */
    public boolean isPair() {
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
