/**
 * The BlackjackHand class represents a hand of playing cards in Blackjack.
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
    public void addCard(Card card) {
        super.addCard(card);
        if (card.rank.toString().equals("A")) {
            numAces++;
        }
    }

    /**
     * Returns the numerical value of the hand for Blackjack.
     *
     * @return The hand's value
     */
    public int getValue() {
        // Handle aces as either 1 or 11, depending on the hand value
        while (value > 21 && numAces > 0) {
            value -= 10;
            numAces--;
        }
        return super.getValue();
    }

    public boolean isBust() {
        return getValue() > 21;
    }

}