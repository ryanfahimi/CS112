/**
 * An implementation of the Dealer class for the card game Acey-Deucey.
 * Handles the game-specific logic and interactions with the client.
 */
class AceyDealer extends Dealer {
    private static final int DEALER_TAX_ROUND_INTERVAL = 20; // Adjust this value as needed
    private static final int ANTE = 1;
    private static final String HIGH = "high";
    private static final String LOW = "low";
    private static final String MID = "mid";

    private AceyHand hand;
    private int pot;

    /**
     * Constructs a new AceyDealer instance.
     *
     * @param ipPort the IP port to be used for the connection.
     */
    public AceyDealer(int ipPort) {
        super(ipPort);
    }

    public static void main(String[] args) {
        AceyDealer dealer = new AceyDealer(IP_PORT);
        dealer.start();
    }

    /**
     * Plays a single round of the Acey-Deucey game.
     */
    @Override
    protected void playRound() {
        takeAnte();
        if (stack > 0) {
            hand = dealHand();
            String decision = handleTurn();
            String result = determineResult(decision);
            sendStatus(result);
            if (round % DEALER_TAX_ROUND_INTERVAL == 0 && pot > 0) {
                pot--;
            }
        }
    }

    /**
     * Takes the ante from the player.
     */
    private void takeAnte() {
        if (pot == 0) {
            stack -= ANTE;
            pot += ANTE;
            System.out.println("Took ante...");
        }
    }

    /**
     * Deals a new hand to the player.
     *
     * @return the dealt AceyHand.
     */
    private AceyHand dealHand() {
        AceyHand hand = new AceyHand();
        hand.addCard(deck.deal());
        hand.addCard(deck.deal());
        System.out.println("Hand: " + hand);
        return hand;
    }

    /**
     * Handles the player's turn, including sending the play command and parsing the
     * response.
     *
     * @return the player's decision.
     */
    private String handleTurn() {
        sendPlayCommand();
        return parseResponse();
    }

    /**
     * Sends the play command to the client.
     */
    private void sendPlayCommand() {
        String playCommand = PLAY + ":" + pot + ":" + stack + ":" + hand + ":dealt" + deck.getDealtCards();
        connection.write(playCommand);
        System.out.println("Pot: " + pot + ", Stack " + stack);
    }

    /**
     * Parses the response received from the client.
     *
     * @return the player's decision.
     */
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

    /**
     * Checks if the player's decision is valid.
     *
     * @param decision the decision made by the player.
     * @return true if the decision is valid, false otherwise.
     */
    private boolean isValidPlayerDecision(String decision) {
        if (!(decision.equals(HIGH) || decision.equals(LOW) || decision.equals(MID))) {
            System.err.println("Invalid decision from player: " + decision);
            return false;
        }
        if (bet > stack || bet > pot || bet < 0) {
            System.err.println("Cheating detected: Player attempted to bet more or less than allowed.");
            connection.write("done:Cheating");
            return false;
        }
        return true;
    }

    /**
     * Determines the result of the round based on the player's decision.
     *
     * @param decision the decision made by the player.
     * @return a string representing the result of the round ("win" or "lose").
     */
    private String determineResult(String decision) {
        String result;
        hand.addCard(deck.deal());
        boolean isWin = decision.equals(HIGH) && hand.isHigh() || decision.equals(LOW) && hand.isLow()
                || decision.equals(MID) && hand.isMid();

        if (isWin) {
            stack += bet * 2;
            pot -= bet * 2;
            result = WIN;
        } else {
            // The player loses, and the pot remains the same
            // No need to update the dealer's stack, as it's already deducted in
            // parsePlayerResponse()
            result = LOSE;
            if (hand.thirdCardMatchesCard()) {
                if (hand.thirdCardMatchesCards()) {
                    bet *= 2;
                    stack -= bet; // Dealer contributes thrice the bet to the pot
                    pot += bet; // Add thrice the bet to the pot
                } else {
                    stack -= bet; // Dealer contributes twice the bet to the pot
                    pot += bet; // Add twice bet to the pot
                }
            }
        }
        System.out.println("Result: " + result + ", Bet: " + bet + ", Hand: " + hand);
        return result;

    }

    /**
     * Sends the round status to the client.
     *
     * @param result a string representing the result of the round ("win" or
     *               "lose").
     */
    private void sendStatus(String result) {
        String statusCommand = STATUS + ":" + result + ":" + hand;
        System.out.println("Pot: " + pot + ", Stack: " + stack);
        connection.write(statusCommand);
    }

}