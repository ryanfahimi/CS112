class AceyDealer extends Dealer {
    private static final int DEALER_TAX_ROUND_INTERVAL = 10; // Adjust this value as needed
    private static final int ANTE = 1;
    private static final String HIGH = "high";
    private static final String LOW = "low";
    private static final String MID = "mid";

    private AceyHand hand;
    private int pot;

    public AceyDealer(int ipPort) {
        super(ipPort);
    }

    @Override
    protected void playRound() {
        takeAnte();
        if (stack > 0) {
            hand = dealHand();
            String decision = handleTurn();
            String result = determineResult(decision);
            sendStatus(result);
            if (round % DEALER_TAX_ROUND_INTERVAL == 0) {
                pot--; // Remove one chip from the pot
            }
        }
    }

    private void takeAnte() {
        if (pot == 0) {
            stack -= ANTE;
            pot += 2 * ANTE;
            System.out.println("Took ante...");
        }
    }

    private AceyHand dealHand() {
        AceyHand hand = new AceyHand();
        hand.addCard(deck.deal());
        hand.addCard(deck.deal());
        System.out.println("Hand: " + hand);
        return hand;
    }

    private String handleTurn() {
        sendPlayCommand();
        return parseResponse();
    }

    private void sendPlayCommand() {
        String playCommand = "play:" + pot + ":" + stack + ":" + hand + "dealt:" + deck.getDealtCards();
        connection.write(playCommand);
        System.out.println("Pot: " + pot + ", Stack " + stack);
    }

    private String parseResponse() {
        String response = connection.read();
        System.out.println("Received play response... ");
        String[] responseParts = response.split(":");
        String decision = responseParts[0];
        bet = Integer.parseInt(responseParts[1]);
        System.out.println("Decision: " + decision + ", Bet: " + bet);

        if (isValidPlayerDecision(decision)) {

            pot += bet;
            stack -= bet;
            System.out.println("Pot: " + pot + ", Stack: " + stack);
            return decision;
        }
        return null;
    }

    private boolean isValidPlayerDecision(String decision) {
        if (!(decision.equals(HIGH) || decision.equals(LOW) || decision.equals(MID))) {
            System.err.println("Invalid decision from player: " + decision);
            return false;
        }
        if (bet > stack || bet > pot) {
            System.err.println("Cheating detected: Player attempted to bet more than allowed.");
            connection.write("done:Cheating");
            return false;
        }
        return true;
    }

    private String determineResult(String decision) {
        hand.addCard(deck.deal());
        boolean isWin = decision.equals("high") && hand.isHigh() || decision.equals("low") && hand.isLow()
                || decision.equals("mid") && hand.isMid();

        if (isWin) {
            bet *= 2;
            stack += bet; // Dealer takes the player's bet from the pot
            pot -= bet; // Deduct the player's bet from the pot
        } else {
            // The player loses, and the pot remains the same
            // No need to update the dealer's stack, as it's already deducted in
            // parsePlayerResponse()
            if (hand.hasMatchingCard()) {
                if (hand.hasAllMatchingCards()) {
                    bet *= 2;
                    stack -= bet; // Dealer contributes thrice the bet to the pot
                    pot += bet; // Add thrice the bet to the pot
                } else {
                    stack -= bet; // Dealer contributes twice the bet to the pot
                    pot += bet; // Add twice bet to the pot
                }
            }
        }
        String result = isWin ? "win" : "lose";
        System.out.println("Result: " + result + ", Bet: " + bet + "Hand: " + hand);
        return result;

    }

    private void sendStatus(String roundResult) {
        String statusCommand = "status:" + roundResult + ":" + hand;
        System.out.println("Pot:" + pot + ", Stack:" + stack);
        connection.write(statusCommand);
    }

    public static void main(String[] args) {
        AceyDealer dealer = new AceyDealer(IP_PORT);
        dealer.start();
    }
}