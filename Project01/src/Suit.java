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
    public String toString() {
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
            if (suitEnum.toString().equals(suitString)) {
                return suitEnum;
            }
        }
        return null;
    }
}