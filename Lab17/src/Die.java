import java.util.Random;

/**
 * This class represents a single die with a specified number of sides.
 */
class Die {
    private Random random;
    private int sides;

    /**
     * Constructor for a Die with a given number of sides.
     *
     * @param sides the number of sides on the die
     */
    public Die(int sides) {
        this.sides = sides;
        random = new Random();
    }

    /**
     * Rolls the die and returns the result.
     *
     * @return an integer representing the outcome of the die roll
     */
    public int roll() {
        return random.nextInt(sides) + 1;
    }
}
