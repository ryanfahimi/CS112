/**
 * Represents a deck of playing cards.
 * The deck consists of 364 cards, which is a combination of 7 standard 52-card
 * decks.
 */
class Deck {
    Card[] deck; // Array to hold the deck of 364 playing cards
    private Card[] dealtCards;
    private int numCards; // Instance variable to track the number of cards left for dealing

    /**
     * Constructs a new deck of 364 playing cards.
     */
    public Deck() {
        numCards = 0;
        deck = new Card[364];
        dealtCards = new Card[364];
        initializeDeck();
    }

    /**
     * Initializes the deck by populating it with 7 sets of standard 52 playing
     * cards.
     */
    private void initializeDeck() {
        for (int i = 0; i < 7; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    deck[numCards] = (new Card(rank, suit));
                    numCards++;
                }
            }
        }
        shuffle();
    }

    /**
     * Shuffles the deck of cards using the Fisher-Yates algorithm.
     * This method ensures that all the cards in the deck are arranged randomly
     * with an equal chance for every permutation.
     */
    public void shuffle() {
        for (int current = deck.length - 1; current > 0; current--) {
            int random = (int) (Math.random() * (current + 1));
            Card temp = deck[current];
            deck[current] = deck[random];
            deck[random] = temp;
        }
        numCards = deck.length;
        dealtCards = new Card[364];
    }

    /**
     * Deals one card from the deck.
     * If there are less than 3 cards left in the deck, it shuffles the deck before
     * dealing a card.
     *
     * @return the dealt card
     */
    public Card deal() {
        Card dealtCard;
        if (numCards < 3) {
            shuffle();
        }
        dealtCard = deck[--numCards];
        dealtCards[numCards] = dealtCard;
        return dealtCard;
    }

    /**
     * Returns a string representation of the cards that have been dealt.
     *
     * @return the string representation of the dealt cards
     */
    public String getDealtCards() {
        String dealtCards = "";
        for (int i = deck.length - 1; i >= numCards; i--) {
            Card dealtCard = this.dealtCards[i];
            dealtCards += ":" + dealtCard.toString();
        }
        return dealtCards;
    }

    public int getNumCards() {
        return numCards;
    }

    /**
     * Returns a string representation of the entire deck of cards.
     *
     * @return the string representation of the deck
     */
    @Override
    public String toString() {
        String deck = "";
        deck += this.deck[0].toString();
        for (int i = 1; i < numCards; i++) {
            Card card = this.deck[i];
            deck += ":" + card.toString();
        }
        return deck;
    }
}
