/**
 * Represents an abstract Hand class for a card game.
 * This class should be extended by specific card game implementations.
 */
abstract class Hand {
    protected final Card[] hand;
    protected int numCards;
    protected int value;

    /**
     * Constructs a Hand object with a specified maximum number of cards.
     *
     * @param maxCards the maximum number of cards allowed in this hand
     */
    public Hand(int maxCards) {
        hand = new Card[maxCards];
        numCards = 0;
        value = 0;
    }

    /**
     * Adds a card to the hand if there is space for it.
     *
     * @param card the card to be added to the hand
     */
    public void addCard(Card card) {
        if (numCards < hand.length) {
            hand[numCards] = card;
            value += card.RANK.toInt();
            numCards++;
        }
    }

    /**
     * Returns the array of cards in the hand.
     *
     * @return the array of cards in the hand
     */
    public Card[] getHand() {
        return hand;
    }

    /**
     * Returns the current number of cards in the hand.
     *
     * @return the current number of cards in the hand
     */
    public int getNumCards() {
        return numCards;
    }

    /**
     * Returns the total value of the hand.
     *
     * @return the total value of the hand
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns a string representation of the hand.
     * The string representation consists of the cards separated by colons.
     *
     * @return a string representation of the hand
     */
    @Override
    public String toString() {
        String hand = "";
        hand += this.hand[0].toString();
        for (int i = 1; i < numCards; i++) {
            Card card = this.hand[i];
            hand += ":" + card.toString();
        }
        return hand;
    }
}
