class BlackjackDealer extends Dealer {
    private int bet;

    public BlackjackDealer(int ipPort) {
        super(ipPort);
    }

    @Override
    protected void playRound(Connection connection) {
        int bet = handleBet(connection);
        // Deal cards
        BlackjackHand playerHand = dealHand();

        BlackjackHand dealerHand = dealHand();

        // Play the round
        boolean roundOver = false;
        while (!roundOver) {
            roundOver = handlePlayerTurn(connection, playerHand, dealerHand, roundOver);
            roundOver = handleDealerTurn(dealerHand, roundOver);
        }

        // Determine the winner and update the stack
        String roundResult = determineRoundResult(playerHand, dealerHand, bet);

        // Send round result to the player
        connection
                .write("status:" + roundResult + ":dealer:" + dealerHand.getValue() + ":you:" + playerHand.getValue());

        // Print the round result
        System.out.println("Round " + round + " - Result: " + roundResult.toUpperCase() + ", Player bet " + bet
                + " chips. Stack is now " + stack);
        round++;
    }

    private int handleBet(Connection connection) {
        // Send bet command
        connection.write("bet:" + stack);
        String betResponse = connection.read();
        System.out.println("Received bet response: " + betResponse);
        String[] betResponseParts = betResponse.split(":");
        bet = Integer.parseInt(betResponseParts[1]);
        return bet;
    }

    private BlackjackHand dealHand() {
        BlackjackHand hand = new BlackjackHand();
        hand.addCard(deck.deal());
        hand.addCard(deck.deal());
        return hand;
    }

    private boolean handlePlayerTurn(Connection connection, BlackjackHand playerHand, BlackjackHand dealerHand,
            boolean roundOver) {
        Card dealerUpCard = dealerHand.getHand()[dealerHand.getNumCards() - 1];

        String playCommand = "play:dealer:" + dealerUpCard + ":you:" + playerHand;
        connection.write(playCommand);
        // Parse player response
        String response = connection.read();
        System.out.println("Received play response: " + response);

        // Handle player response and update hands accordingly
        switch (response) {
            case "hit":
                playerHand.addCard(deck.deal());
                break;
            case "stand":
                roundOver = true;
                break;
            case "double":
                bet *= 2;
                playerHand.addCard(deck.deal());
                roundOver = true;
                break;
            case "split":
                // Implement split logic if desired
                break;
            default:
                System.err.println("Invalid response from player: " + response);
                break;
        }

        // Check for player bust
        if (playerHand.isBust()) {
            roundOver = true;
        }

        return roundOver;
    }

    private boolean handleDealerTurn(BlackjackHand dealerHand, boolean roundOver) {
        // Play dealer's hand according to the game rules
        if (dealerHand.getValue() < 17) {
            dealerHand.addCard(deck.deal());
            roundOver = false;
        }
        // Check for player bust
        if (dealerHand.isBust()) {
            roundOver = true;
        }

        return roundOver;
    }

    private String determineRoundResult(BlackjackHand playerHand, BlackjackHand dealerHand, int bet) {
        String roundResult;
        if (playerHand.isBust() || (!dealerHand.isBust() && dealerHand.getValue() > playerHand.getValue())) {
            roundResult = "lose";
            stack -= bet;
        } else if (dealerHand.isBust() || playerHand.getValue() > dealerHand.getValue()) {
            roundResult = "win";
            stack += bet;
        } else {
            roundResult = "push";
        }

        return roundResult;
    }

    public static void main(String[] args) {
        BlackjackDealer dealer = new BlackjackDealer(IP_PORT);
        dealer.start();
    }
}
