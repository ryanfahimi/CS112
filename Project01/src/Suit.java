/**
 * The Suit enumeration defines the 4 possible card suits in a standard 52-card
 * deck.
 */
enum Suit {
    SPADES("S"), HEARTS("H"), DIAMONDS("D"), CLUBS("C");

    private final String SUIT_STRING;

    /**
     * Constructor for the Suit enumeration.
     *
     * @param suitString The suit as a string.
     */
    Suit(String suitString) {
        this.SUIT_STRING = suitString;
    }

    /**
     * Returns the suit as a string.
     *
     * @return The suit as a string.
     */
    @Override
    public String toString() {
        return SUIT_STRING;
    }

    /**
     * Returns the Suit enumeration that matches the given string.
     *
     * @param suitString The suit as a string.
     * @return The corresponding Suit enumeration, or null if no match is found.
     */
    public static Suit fromString(String suitString) {
        for (Suit suit : Suit.values()) {
            if (suit.toString().equals(suitString)) {
                return suit;
            }
        }
        return null;
    }
}