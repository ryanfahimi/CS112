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
            difference = Math.abs(hand[1].RANK.toInt() - hand[0].RANK.toInt());
        }
    }

    public int getFavorableCards(String decision, int dealtCardsInt, Card[] dealtCardsArray) {
        int favorableCards = 0;

        int firstRank = getFirstCard().RANK.toInt();
        int secondRank = getSecondCard().RANK.toInt();
        int lower = Math.min(firstRank, secondRank);
        int higher = Math.max(firstRank, secondRank);

        int lowRange, highRange;
        if (decision.equals("mid")) {
            lowRange = lower + 1;
            highRange = higher - 1;
        } else if (decision.equals("high")) {
            lowRange = 2;
            highRange = lower - 1;
        } else { // decision == "low"
            lowRange = higher + 1;
            highRange = 14;
        }

        for (int i = 2; i <= 14; i++) {
            if (i == firstRank || i == secondRank) {
                continue;
            }

            if (i >= lowRange && i <= highRange) {
                favorableCards += 28;
                for (int j = 0; j < dealtCardsInt; j++) {
                    if (i == dealtCardsArray[j].RANK.toInt()) {
                        favorableCards--;
                    }
                }
            }
        }

        return favorableCards;
    }

    /**
     * Checks if the AceyHand object is a high hand.
     *
     * @return true if the AceyHand object is a high hand, false otherwise.
     */
    public boolean isHigh() {
        int firstRank = getFirstCard().RANK.toInt();
        int secondRank = getSecondCard().RANK.toInt();
        int thirdRank = getThirdCard().RANK.toInt();
        return numCards == 3 && firstRank == secondRank
                && thirdRank > firstRank;
    }

    /**
     * Checks if the AceyHand object is a low hand.
     *
     * @return true if the AceyHand object is a low hand, false otherwise.
     */
    public boolean isLow() {
        int firstRank = getFirstCard().RANK.toInt();
        int secondRank = getSecondCard().RANK.toInt();
        int thirdRank = getThirdCard().RANK.toInt();
        return numCards == 3 && firstRank == secondRank
                && thirdRank < firstRank;
    }

    /**
     * Checks if the AceyHand object is a mid hand.
     *
     * @return true if the AceyHand object is a mid hand, false otherwise.
     */
    public boolean isMid() {
        int firstRank = getFirstCard().RANK.toInt();
        int secondRank = getSecondCard().RANK.toInt();
        int thirdRank = getThirdCard().RANK.toInt();
        return numCards == 3 && (firstRank < thirdRank && thirdRank < secondRank)
                || (secondRank < thirdRank && thirdRank < firstRank);
    }

    public boolean thirdCardMatchesCard() {
        int firstRank = getFirstCard().RANK.toInt();
        int secondRank = getSecondCard().RANK.toInt();
        int thirdRank = getThirdCard().RANK.toInt();
        return firstRank == thirdRank || secondRank == thirdRank;
    }

    /**
     * Checks if the third card in the AceyHand object matches the other two
     * cards.
     *
     * @return true if the third card in the AceyHand object matches the other
     *         two cards, false otherwise.
     */
    public boolean thirdCardMatchesCards() {
        int firstRank = getFirstCard().RANK.toInt();
        int secondRank = getSecondCard().RANK.toInt();
        int thirdRank = getThirdCard().RANK.toInt();
        return firstRank == thirdRank && secondRank == thirdRank;
    }

    /**
     * Returns the first card in the AceyHand object.
     *
     * @return The first card in the AceyHand object.
     */
    public Card getFirstCard() {
        return hand[0];
    }

    /**
     * Returns the second card in the AceyHand object.
     *
     * @return The second card in the AceyHand object.
     */
    public Card getSecondCard() {
        return hand[1];
    }

    /**
     * Returns the third card in the AceyHand object.
     *
     * @return The third card in the AceyHand object.
     */
    public Card getThirdCard() {
        return hand[2];
    }

    /**
     * Returns the difference between the two cards in the AceyHand object.
     *
     * @return The difference between the two cards in the AceyHand object.
     */
    public int getDifference() {
        return difference;
    }
}
