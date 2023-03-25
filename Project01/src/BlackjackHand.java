/**
 * The BlackjackHand class represents a hand of playing cards in Blackjack.
 */
class BlackjackHand {
    private Card[] hand;
    private int numCards;

    /**
     * Constructs an empty BlackjackHand object.
     */
    public BlackjackHand() {
        hand = new Card[11]; // Maximum 11 cards in a blackjack hand
        numCards = 0;
    }

    /**
     * Adds a card to the hand.
     *
     * @param card The card to add
     */
    public void addCard(Card card) {
        hand[numCards] = card;
        numCards++;
    }

    /**
     * Returns the numerical value of the hand for Blackjack.
     *
     * @return The hand's value
     */
    public int getValue() {
        int value = 0;
        int numAces = 0;

        for (int i = 0; i < numCards; i++) {
            Card card = hand[i];
            int cardValue = card.rank.toInt();
            if (card.rank.toString().equals("A")) {
                numAces++;
            }
            value += cardValue;
        }

        // Handle aces as either 1 or 11, depending on the hand value
        while (value > 21 && numAces > 0) {
            value -= 10;
            numAces--;
        }

        return value;
    }

    public boolean isBust() {
        return getValue() > 21;
    }

    // Getters

    public int getNumCards() {
        return numCards;
    }

    public Card[] getHand() {
        return hand;
    }

    @Override
    public String toString() {
        String handString = "";
        for (int i = 0; i < numCards; i++) {
            Card card = this.hand[i];
            handString += card.toString() + ":";
        }
        return handString;
    }
}