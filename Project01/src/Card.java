/**
 * The Card class represents a playing card with a rank and suit.
 */
public class Card {
    private Rank rank;
    private Suit suit;

    /**
     * The Rank enumeration defines the 13 possible card ranks in a standard 52-card
     * deck.
     */
    private enum Rank {
        TWO("2", 2), THREE("3", 3), FOUR("4", 4), FIVE("5", 5), SIX("6", 6), SEVEN("7", 7), EIGHT("8", 8), NINE("9", 9),
        TEN("10", 10), JACK("J", 10), QUEEN("Q", 10), KING("K", 10), ACE("A", 10);

        private final String rankString;
        private final int rankInt;

        /**
         * Constructor for the Rank enumeration.
         *
         * @param rankString The rank as a string.
         * @param rankInt    The rank as an int.
         */
        Rank(String rankString, int rankInt) {
            this.rankString = rankString;
            this.rankInt = rankInt;
        }

        /**
         * Returns the rank as a string.
         *
         * @return The rank as a string.
         */
        public String getRankString() {
            return rankString;
        }

        /**
         * Returns the rank as an int.
         *
         * @return The rank as an int.
         */
        public int getRankInt() {
            return rankInt;
        }

        /**
         * Returns the Rank enumeration that matches the given string.
         *
         * @param rankString The rank as a string.
         * @return The corresponding Rank enumeration, or null if no match is found.
         */
        public static Rank fromString(String rankString) {
            for (Rank rank : Rank.values()) {
                if (rank.getRankString().equals(rankString)) {
                    return rank;
                }
            }
            return null;
        }
    }

    /**
     * The Suit enumeration defines the 4 possible card suits in a standard 52-card
     * deck.
     */
    private enum Suit {
        SPADES("S"), HEARTS("H"), DIAMONDS("D"), CLUBS("C");

        private final String suit;

        /**
         * Constructor for the Suit enumeration.
         *
         * @param suit The suit as a string.
         */
        Suit(String suit) {
            this.suit = suit;
        }

        /**
         * Returns the suit as a string.
         *
         * @return The suit as a string.
         */
        public String getSuit() {
            return suit;
        }

        /**
         * Returns the Suit enumeration that matches the given string.
         *
         * @param suitString The suit as a string.
         * @return The corresponding Suit enumeration, or null if no match is found.
         */
        public static Suit fromString(String suitString) {
            for (Suit suit : Suit.values()) {
                if (suit.getSuit().equals(suitString)) {
                    return suit;
                }
            }
            return null;
        }
    }

    /**
     * Constructs a Card object given a card string in the format "rank + suit".
     *
     * @param card The card string in the format "rank + suit".
     * @throws IllegalArgumentException If the card string is invalid.
     */
    public Card(String card) throws IllegalArgumentException {
        Object[] parsedCard = parseCard(card);
        if (parsedCard != null) {
            this.rank = (Rank) parsedCard[0];
            this.suit = (Suit) parsedCard[1];
        } else {
            throw new IllegalArgumentException("Invalid card format: " + card);
        }
    }

    // Getters

    public String getRankString() {
        return rank.getRankString();
    }

    public int getRankInt() {
        return rank.getRankInt();
    }

    public String getSuit() {
        return suit.getSuit();
    }

    @Override
    public String toString() {
        return rank.getRankString() + suit.getSuit();
    }

    /**
     * Parses the card string to extract the rank and suit.
     *
     * @param card The card string in the format "rank + suit"
     * @return An object array containing the rank and suit, or null if the format
     *         is
     *         invalid
     * @throws IllegalArgumentException If the card string is null or empty
     */
    private Object[] parseCard(String card) throws IllegalArgumentException {
        if (card == null || card.isEmpty()) {
            throw new IllegalArgumentException("Null or empty card string");
        }
        for (int i = 1; i <= 2; i++) {
            String rankString = card.substring(0, i).toUpperCase();
            String suitString = card.substring(i).toUpperCase();
            Rank rank = Rank.fromString(rankString);
            Suit suit = Suit.fromString(suitString);
            if (rank != null && suit != null) {
                return new Object[] { rank, suit };
            }
        }
        return null;
    }
}