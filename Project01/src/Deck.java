class Deck {
    // A deck of playing cards consists of 52 cards in total, one for each suit and
    // value combination
    Card deck[] = new Card[364]; // Instantiate a deck of 364 playing cards
    private int cardsLeft = deck.length; // Initialize the instance variable 'cardsLeft' for the deal and shuffle
                                         // methods

    public Deck() {
        for (int i = 0; i < 7; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    deck[i] = (new Card(rank, suit));
                }
            }
        }
    }

    // Utilize the Fisher-Yates shuffle algorithm to arrange all the cards in the
    // deck randomly with an equal chance for every permutation
    public void shuffle() {
        // Iterate 'current' from the length of the cardDeck array down to 1 to
        // represent the index of the current card in the cardDeck array
        for (int current = deck.length - 1; current > 0; current--) {
            // Set 'random' to a random integer in the range [0,current] to represent the
            // index of a random card in a subset of the cardDeck array
            int random = (int) (Math.random() * (current + 1));
            // Swap the two cards in the cardDeck
            Card temp = deck[current];
            deck[current] = deck[random];
            deck[random] = temp;
        }
        // Reset 'cardsLeft' instance variable to enable dealing
        cardsLeft = deck.length;
    }

    // Deal one card from the deck
    public Card deal() {
        if (cardsLeft < 3) {
            shuffle();
        }
        return deck[--cardsLeft];
    }

    // Create a string representation of the entire deck of cards
    public String toString() {
        String deck = "";
        // Loop through each card in the cardDeck array and add its string
        // representation to the cardDeck string
        for (int i = 0; i <= cardsLeft; i++) {
            Card card = this.deck[i];
            deck += card.toString() + ":";
        }
        // Return the final cardDeck string
        return deck;
    }
}