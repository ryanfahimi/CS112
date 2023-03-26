class AceyDealer extends Dealer {
    private static final int ANTE = 1;
    private static final String HIGH = "high";
    private static final String LOW = "low";
    private static final String MID = "mid";

    private int pot;

    public AceyDealer(int ipPort) {
        super(ipPort);
    }

    @Override
    protected void playRound(Connection connection) {
        takeAnte();
        AceyHand hand = dealHand();
        String decision = handlePlayerTurn(connection, hand);
        String roundResult = determineRoundResult(decision, hand);
        sendStatus(roundResult, hand, connection);
    }

    private void takeAnte() {
        if (pot == 0) {
            stack -= ANTE;
            pot += ANTE;
        }
    }

    private AceyHand dealHand() {
        AceyHand hand = new AceyHand();
        hand.addCard(deck.deal());
        hand.addCard(deck.deal());
        System.out.println("Hand: " + hand);
        return hand;
    }

    private void sendPlayCommand(Connection connection, AceyHand hand) {
        String playCommand = "play:" + pot + ":" + stack + ":" + hand;
        connection.write(playCommand);
    }

    private boolean isValidPlayerDecision(String decision, int bet, Connection connection) {
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

    private String parsePlayerResponse(Connection connection, AceyHand hand) {
        String response = connection.read();
        System.out.println("Received play response: " + response);
        String[] responseParts = response.split(":");
        String decision = responseParts[0];
        int bet = Integer.parseInt(responseParts[1]);

        if (isValidPlayerDecision(decision, bet, connection)) {

            pot += bet;
            stack -= bet;
            System.out.println("Round " + round + " - Player bet " + bet + " chips. Pot is now " + pot
                    + ". Stack is now " + stack);
            return decision;
        }
        return null;
    }

    private String handlePlayerTurn(Connection connection, AceyHand hand) {
        sendPlayCommand(connection, hand);
        return parsePlayerResponse(connection, hand);
    }

    private String determineRoundResult(String decision, AceyHand hand) {
        hand.addCard(deck.deal());
        if (decision.equals("high") && hand.isHigh() || decision.equals("low") && hand.isLow()
                || decision.equals("mid") && hand.isMid()) {
            stack += pot;
            pot = 0;
            return "win";
        } else {
            return "lose";
        }
    }

    private void sendStatus(String roundResult, AceyHand hand, Connection connection) {
        String statusCommand = "status:" + roundResult + ":" + hand;
        System.out.println(statusCommand);
        connection.write(statusCommand);
    }

    public static void main(String[] args) {
        AceyDealer dealer = new AceyDealer(IP_PORT);
        dealer.start();
    }
}