abstract class Hand {
    protected final Card[] hand;
    protected int numCards;
    protected int value;

    public Hand(int maxCards) {
        hand = new Card[maxCards];
        numCards = 0;
        value = 0;
    }

    public void addCard(Card card) {
        if (numCards < hand.length) {
            hand[numCards] = card;
            value += card.rank.toInt();
            numCards++;
        }
    }

    public Card[] getHand() {
        return hand;
    }

    public int getNumCards() {
        return numCards;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        String hand = "";
        for (int i = 0; i < numCards; i++) {
            Card card = this.hand[i];
            hand += card.toString() + ":";
        }
        return hand;
    }
}
