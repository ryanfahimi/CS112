class AceyDealer extends Dealer {
    private static final int ANTE = 1;

    private int pot;

    public AceyDealer(int ipPort) {
        super(ipPort);
    }

    @Override
    protected void playRound(Connection connection) {
        takeAnte();
        AceyHand hand = dealHand();
        sendPlayCommand(connection, hand);
        String response = connection.read();
        processPlayerResponse(connection, hand, response);
    }

    private AceyHand dealHand() {
        AceyHand hand = new AceyHand();
        hand.addCard(deck.deal());
        hand.addCard(deck.deal());
        hand.addCard(deck.deal());
        return hand;
    }

    private void sendPlayCommand(Connection connection, AceyHand hand) {
        Card firstCard = hand.getFirstCard();
        Card secondCard = hand.getSecondCard();

        String playCommand = "play:" + pot + ":" + stack + ":" + firstCard + ":" + secondCard;
        connection.write(playCommand);
    }

    private void processPlayerResponse(Connection connection, AceyHand hand, String response) {
        System.out.println("Received play response: " + response);
        String[] responseParts = response.split(":");
        String decision = responseParts[0];
        int bet = Integer.parseInt(responseParts[1]);

        if (!(decision.equals("high") || decision.equals("low") || decision.equals("mid"))) {
            System.err.println("Invalid decision from player: " + decision);
            return;
        }
        if (bet > stack || bet > pot) {
            System.err.println("Cheating detected: Player attempted to bet more than allowed.");
            connection.write("done:Cheating");
            return;
        }

        pot += bet;
        stack -= bet;
        System.out.println("Round " + round + " - Player bet " + bet + " chips. Pot is now " + pot
                + ". Stack is now " + stack);

        String statusCommand;
        if (playerWins(decision, hand)) {
            stack += pot;
            pot = 0;
            statusCommand = "status:win:" + hand.getFirstCard() + ":" + hand.getSecondCard() + ":"
                    + hand.getThirdCard();
        } else {
            statusCommand = "status:lose:" + hand.getFirstCard() + ":" + hand.getSecondCard() + ":"
                    + hand.getThirdCard();
        }
        System.out.println(statusCommand);
        connection.write(statusCommand);

        round++;
    }

    private void takeAnte() {
        if (pot == 0) {
            stack -= ANTE;
            pot += ANTE;
        }
    }

    private boolean playerWins(String decision, AceyHand hand) {
        if (decision.equals("high")) {
            return hand.isHigh();
        } else if (decision.equals("low")) {
            return hand.isLow();
        } else {
            return hand.isMid();
        }
    }

    public static void main(String[] args) {
        AceyDealer dealer = new AceyDealer(IP_PORT);
        dealer.start();
    }
}