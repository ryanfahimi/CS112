public class BackAlley {
    public static void main(String[] args) {
        int stack = 20;
        int bet = 1;
        int maxStack = stack;
        Dice dice = new Dice(2);

        while (stack > 0) {
            stack -= bet; // Bet $1
            int roll = dice.roll();
            boolean win = false;

            if (roll == 7 || roll == 11) {
                stack += bet * 2;
                win = true;
            } else if (roll == 2 || roll == 3 || roll == 12) {
                // Do nothing, the bet is lost
            } else {
                int point = roll;
                while (true) {
                    roll = dice.roll();
                    if (roll == point) {
                        stack += bet * 2;
                        win = true;
                        break;
                    } else if (roll == 7) {
                        // Do nothing, the bet is lost
                        break;
                    }
                }
            }

            if (win) {
                System.out.println("$" + stack + " win");
            } else {
                System.out.println("$" + stack + " lose");
            }

            if (stack > maxStack) {
                maxStack = stack;
            }
        }

        System.out.println("The game has ended.");
        System.out.println("The largest amount of money your stack ever reached: $" + maxStack);
    }
}
