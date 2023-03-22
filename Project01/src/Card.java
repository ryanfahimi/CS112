/**
 * The Card class represents a playing card with a rank and suit.
 */
class Card {
    final Rank rank;
    final Suit suit;

    /**
     * The Rank enumeration defines the 13 possible card ranks in a standard 52-card
     * deck.
     */
    enum Rank {
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
        public String getString() {
            return rankString;
        }

        /**
         * Returns the rank as an int.
         *
         * @return The rank as an int.
         */
        public int getInt() {
            return rankInt;
        }

        /**
         * Returns the Rank enumeration that matches the given string.
         *
         * @param rankString The rank as a string.
         * @return The corresponding Rank enumeration, or null if no match is found.
         */
        public static Rank fromString(String rankString) {
            for (Rank rankEnum : Rank.values()) {
                if (rankEnum.getString().equals(rankString)) {
                    return rankEnum;
                }
            }
            return null;
        }
    }

    /**
     * The Suit enumeration defines the 4 possible card suits in a standard 52-card
     * deck.
     */
    enum Suit {
        SPADES("S"), HEARTS("H"), DIAMONDS("D"), CLUBS("C");

        private final String suitString;

        /**
         * Constructor for the Suit enumeration.
         *
         * @param suitString The suit as a string.
         */
        Suit(String suitString) {
            this.suitString = suitString;
        }

        /**
         * Returns the suit as a string.
         *
         * @return The suit as a string.
         */
        public String getString() {
            return suitString;
        }

        /**
         * Returns the Suit enumeration that matches the given string.
         *
         * @param suitString The suit as a string.
         * @return The corresponding Suit enumeration, or null if no match is found.
         */
        public static Suit fromString(String suitString) {
            for (Suit suitEnum : Suit.values()) {
                if (suitEnum.getString().equals(suitString)) {
                    return suitEnum;
                }
            }
            return null;
        }
    }

    /**
     * Constructs a Card object given a Rank and Suit.
     *
     * @param rank The Rank of the card.
     * @param suit The Suit of the card.
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public String toString() {
        return rank.getString() + suit.getString();
    }

    /**
     * Constructs a Card object given a card string in the format "rank + suit".
     *
     * @param card The card string in the format "rank + suit".
     * @throws IllegalArgumentException If the card string is invalid.
     */
    public static Card fromString(String card) throws IllegalArgumentException {
        if (card == null || card.isEmpty() || card.length() < 2) {
            throw new IllegalArgumentException("Null or empty card string");
        }
        for (int i = 1; i <= 2; i++) {
            String rankString = card.substring(0, i).toUpperCase();
            String suitString = card.substring(i).toUpperCase();
            Rank rankEnum = Rank.fromString(rankString);
            Suit suitEnum = Suit.fromString(suitString);
            if (rankEnum != null && suitEnum != null) {
                return new Card(rankEnum, suitEnum);
            }
        }
        throw new IllegalArgumentException("Invalid card format: " + card);
    }
}