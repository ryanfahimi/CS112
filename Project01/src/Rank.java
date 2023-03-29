/**
 * The Rank enumeration defines the 13 possible card ranks in a standard 52-card
 * deck.
 */
enum Rank {
    TWO("2", 2), THREE("3", 3), FOUR("4", 4), FIVE("5", 5), SIX("6", 6), SEVEN("7", 7), EIGHT("8", 8), NINE("9", 9),
    TEN("10", 10), JACK("J", 10), QUEEN("Q", 10), KING("K", 10), ACE("A", 11);

    private final String RANK_STRING;
    private final int RANK_INT;

    /**
     * Constructor for the Rank enumeration.
     *
     * @param rankString The rank as a string.
     * @param rankInt    The rank as an int.
     */
    Rank(String rankString, int rankInt) {
        this.RANK_STRING = rankString;
        this.RANK_INT = rankInt;
    }

    /**
     * Returns the rank as a string.
     *
     * @return The rank as a string.
     */
    @Override
    public String toString() {
        return RANK_STRING;
    }

    /**
     * Returns the rank as an int.
     *
     * @return The rank as an int.
     */
    public int toInt() {
        return RANK_INT;
    }

    /**
     * Returns the Rank enumeration that matches the given string.
     *
     * @param rankString The rank as a string.
     * @return The corresponding Rank enumeration, or null if no match is found.
     */
    public static Rank fromString(String rankString) {
        for (Rank rank : Rank.values()) {
            if (rank.toString().equals(rankString)) {
                return rank;
            }
        }
        return null;
    }
}