/**
 * The Card class represents a playing card with a rank and suit.
 */
class Card {
    final Rank rank;
    final Suit suit;

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