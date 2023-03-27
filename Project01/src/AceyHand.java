/**
 * The AceyHand class represents a hand of playing cards in Acey.
 */
class AceyHand extends Hand {
    private int difference;

    /**
     * Constructs an empty AceyHand object.
     */
    public AceyHand() {
        super(3);
    }

    /**
     * Adds a card to the AceyHand object.
     *
     * @param card The card to add.
     */
    public void addCard(Card card) {
        super.addCard(card);
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

    public boolean hasMatchingCard() {
        int firstRank = getFirstCard().rank.toInt();
        int secondRank = getSecondCard().rank.toInt();
        int thirdRank = getThirdCard().rank.toInt();
        return firstRank == thirdRank || secondRank == thirdRank;
    }

    public boolean hasAllMatchingCards() {
        int firstRank = getFirstCard().rank.toInt();
        int secondRank = getSecondCard().rank.toInt();
        int thirdRank = getThirdCard().rank.toInt();
        return firstRank == thirdRank && secondRank == thirdRank;
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

    public int getDifference() {
        return difference;
    }
}
