/**
 * This class represents a set of dice with a specified number of dice.
 */
class Dice {
    private Die[] dice;

    /**
     * Constructor for a set of dice with a given number of dice.
     *
     * @param numDice the number of dice in the set
     */
    public Dice(int numDice) {
        dice = new Die[numDice];
        for (int i = 0; i < numDice; i++) {
            dice[i] = new Die(6);
        }
    }

    /**
     * Rolls all dice in the set and returns the total.
     *
     * @return an integer representing the sum of all dice outcomes
     */
    public int roll() {
        int total = 0;
        for (Die die : dice) {
            total += die.roll();
        }
        return total;
    }
}
