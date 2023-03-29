import java.util.Random;

class Die {
    private Random random;
    private int sides;

    public Die(int sides) {
        this.sides = sides;
        random = new Random();
    }

    public int roll() {
        return random.nextInt(sides) + 1;
    }
}
