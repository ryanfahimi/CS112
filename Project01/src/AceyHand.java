/**
 * The AceyHand class represents a hand of playing cards in Acey.
 */
class AceyHand {
    private final Card[] hand;
    private int difference;
    private int numCards;
    private int value;

    /**
     * Constructs an empty AceyHand object.
     */
    public AceyHand() {
        hand = new Card[3];
        numCards = 0;
        value = 0;
    }

    /**
     * Adds a card to the AceyHand object.
     *
     * @param card The card to add.
     */
    public void addCard(Card card) {
        hand[numCards] = card;
        value += card.rank.toInt();
        numCards++;
        if (numCards == 2) {
            difference = Math.abs(hand[1].rank.toInt() - hand[0].rank.toInt());
        }
    }

    /**
     * Checks if the AceyHand object is a high hand.
     *
     * @return true if the AceyHand object is a high hand, false otherwise.
     */
    public boolean isHigh() {
        int firstRank = getFirstCard().rank.toInt();
        int secondRank = getSecondCard().rank.toInt();
        int thirdRank = getThirdCard().rank.toInt();
        return numCards == 3 && firstRank == secondRank
                && thirdRank > firstRank;
    }

    /**
     * Checks if the AceyHand object is a low hand.
     *
     * @return true if the AceyHand object is a low hand, false otherwise.
     */
    public boolean isLow() {
        int firstRank = getFirstCard().rank.toInt();
        int secondRank = getSecondCard().rank.toInt();
        int thirdRank = getThirdCard().rank.toInt();
        return numCards == 3 && firstRank == secondRank
                && thirdRank < firstRank;
    }

    /**
     * Checks if the AceyHand object is a mid hand.
     *
     * @return true if the AceyHand object is a mid hand, false otherwise.
     */
    public boolean isMid() {
        int firstRank = getFirstCard().rank.toInt();
        int secondRank = getSecondCard().rank.toInt();
        int thirdRank = getThirdCard().rank.toInt();
        return numCards == 3 && (firstRank < thirdRank && thirdRank < secondRank)
                || (secondRank < thirdRank && thirdRank < firstRank);
    }

    // Getters

    public Card getFirstCard() {
        return hand[0];
    }

    public Card getSecondCard() {
        return hand[1];
    }

    public Card getThirdCard() {
        return hand[2];
    }

    public Card[] getHand() {
        return hand;
    }

    public int getDifference() {
        return difference;
    }

    public int getNumCards() {
        return numCards;
    }

    public int getValue() {
        return value;
    }
}
