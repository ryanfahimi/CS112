/**
 * Represents a deck of playing cards.
 * The deck consists of 364 cards, which is a combination of 7 standard 52-card
 * decks.
 */
class Deck {
    Card[] deck; // Array to hold the deck of 364 playing cards
    private Card[] dealtCards;
    private int cardsLeft; // Instance variable to track the number of cards left for dealing

    /**
     * Constructs a new deck of 364 playing cards.
     */
    public Deck() {
        deck = new Card[364];
        dealtCards = new Card[364];
        cardsLeft = deck.length;
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
                    deck[i] = (new Card(rank, suit));
                }
            }
        }
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
        cardsLeft = deck.length;
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
        if (cardsLeft < 3) {
            shuffle();
        }
        dealtCard = deck[--cardsLeft];
        dealtCards[cardsLeft] = dealtCard;
        return dealtCard;
    }

    /**
     * Returns a string representation of the entire deck of cards.
     *
     * @return the string representation of the deck
     */
    @Override
    public String toString() {
        String deckString = "";
        for (int i = 0; i <= cardsLeft; i++) {
            Card card = this.deck[i];
            deckString += card.toString() + ":";
        }
        return deckString;
    }
}
