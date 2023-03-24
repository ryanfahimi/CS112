/**
 * The Rank enumeration defines the 13 possible card ranks in a standard 52-card
 * deck.
 */
enum Rank {
    TWO("2", 2), THREE("3", 3), FOUR("4", 4), FIVE("5", 5), SIX("6", 6), SEVEN("7", 7), EIGHT("8", 8), NINE("9", 9),
    TEN("10", 10), JACK("J", 11), QUEEN("Q", 12), KING("K", 13), ACE("A", 14);

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
    @Override
    public String toString() {
        return rankString;
    }

    /**
     * Returns the rank as an int.
     *
     * @return The rank as an int.
     */
    public int toInt() {
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
            if (rankEnum.toString().equals(rankString)) {
                return rankEnum;
            }
        }
        return null;
    }
}