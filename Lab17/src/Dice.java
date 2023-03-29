class Dice {
    private Die[] dice;

    public Dice(int numDice) {
        dice = new Die[numDice];
        for (int i = 0; i < numDice; i++) {
            dice[i] = new Die(6);
        }
    }

    public int roll() {
        int total = 0;
        for (Die die : dice) {
            total += die.roll();
        }
        return total;
    }
}
