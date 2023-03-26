class BlackjackDealer extends Dealer {
    private static final String HIT = "hit";
    private static final String STAND = "stand";
    private static final String DOUBLE = "double";
    private static final String SPLIT = "split";

    private BlackjackHand playerHand;
    private BlackjackHand dealerHand;
    private int bet;
    private String result;

    public BlackjackDealer(int ipPort) {
        super(ipPort);
    }

    public static void main(String[] args) {
        BlackjackDealer dealer = new BlackjackDealer(IP_PORT);
        dealer.start();
    }

    @Override
    protected void playRound(Connection connection) {
        handleBet(connection);
        // Deal cards
        dealInitialCards();

        // Play the round
        handlePlayerTurn(connection);
        handleDealerTurn();

        // Determine the winner and update the stack
        determineRoundResult();
        sendStatus(connection);
    }

    private void handleBet(Connection connection) {
        // Send bet command
        connection.write("bet:" + stack + deck.getDealtCards());
        String betResponse = connection.read();
        System.out.println("Received bet response: " + betResponse);
        String[] betResponseParts = betResponse.split(":");
        bet = Integer.parseInt(betResponseParts[1]);
    }

    public void dealInitialCards() {
        playerHand = dealHand();
        dealerHand = dealHand();
        System.out.println("Player Hand: " + playerHand + ", Value: " + playerHand.getValue() + ", Dealer Up Card: "
                + dealerHand.getHand()[1]);
    }

    private BlackjackHand dealHand() {
        BlackjackHand hand = new BlackjackHand();
        hand.addCard(deck.deal());
        hand.addCard(deck.deal());
        return hand;
    }

    public void playerHit() {
        playerHand.addCard(deck.deal());
    }

    public void dealerHit() {
        dealerHand.addCard(deck.deal());
    }

    private void handleDealerTurn() {
        // Play dealer's hand according to the game rules
        while (dealerHand.getValue() < 17) {
            dealerHit();
        }
    }

    private void sendPlayCommand(Connection connection) {
        Card dealerUpCard = dealerHand.getHand()[dealerHand.getNumCards() - 1];
        String playCommand = "play:dealer:" + dealerUpCard + ":you:" + playerHand;
        connection.write(playCommand);
    }

    private BlackjackHand handleSplitHands(Card splitCard) {
        // Create a new hand with the split card and deal a new card
        BlackjackHand splitHand = new BlackjackHand();
        splitHand.addCard(splitCard);
        splitHand.addCard(deck.deal());
        return splitHand;
    }

    private void handleSplit(Connection connection) {
        if (playerHand.isSplittable()) {
            BlackjackHand[] playerHands = { handleSplitHands(playerHand.getHand()[0]),
                    handleSplitHands(playerHand.getHand()[1]) };

            // Request bet for split hand
            for (int i = 0; i < playerHands.length; i++) {
                boolean isLastSplit = (i == playerHands.length - 1);
                System.out.println("Received bet response: bet:" + bet);
                this.playerHand = playerHands[i];
                System.out.println(
                        "Player Hand: " + playerHand + ", Value: " + playerHand.getValue() + ", Dealer Up Card: "
                                + dealerHand.getHand()[1]);
                handlePlayerTurn(connection);
                if (!(isLastSplit)) {
                    handleDealerTurn();
                    determineRoundResult();
                    sendStatus(connection);
                }
            }
        } else {
            System.err.println("Unsplittable");
        }
    }

    private boolean isValidPlayerDecision(String decision) {
        if (!(decision.equals(HIT) || decision.equals(STAND) || decision.equals(DOUBLE) || decision.equals(SPLIT))) {
            return false;
        }
        return true;
    }

    private boolean processPlayerDecision(Connection connection) {
        String decision = connection.read();
        boolean roundOver = false;

        if (isValidPlayerDecision(decision)) {

            switch (decision) {
                case HIT:
                    playerHand.addCard(deck.deal());
                    break;

                case STAND:
                    roundOver = true;
                    break;

                case DOUBLE:
                    bet *= 2;
                    playerHand.addCard(deck.deal());
                    roundOver = true;
                    break;

                case SPLIT:
                    System.out.println(
                            "Player Decision: " + decision.toUpperCase() + ", Player Hand " + playerHand + ", Value: "
                                    + playerHand.getValue());
                    handleSplit(connection);
                    return true;
            }

            // Check for player bust
            if (playerHand.isBust()) {
                roundOver = true;
            }

            System.out.println(
                    "Player Decision: " + decision.toUpperCase() + ", Player Hand " + playerHand + ", Value: "
                            + playerHand.getValue());
        } else {
            System.err.println("Invalid response from player: " + decision);
            roundOver = true;
        }
        return roundOver;
    }

    private void handlePlayerTurn(Connection connection) {
        boolean roundOver = false;
        while (!roundOver) {
            sendPlayCommand(connection);
            roundOver = processPlayerDecision(connection);
        }
    }

    private void determineRoundResult() {
        if (playerHand.isBust() || (!dealerHand.isBust() && dealerHand.getValue() > playerHand.getValue())) {
            result = "lose";
            stack -= bet;
        } else if (dealerHand.isBust() || playerHand.getValue() > dealerHand.getValue()) {
            result = "win";
            stack += bet;
        } else {
            result = "push";
        }
    }

    private void sendStatus(Connection connection) {
        // Send round result to the player
        connection.write("status:" + result + ":dealer:" + dealerHand.getValue() + ":you:" + playerHand.getValue());

        // Print the round result
        System.out.println(
                "Round " + round + "- Result: " + result.toUpperCase() + ", Dealer Score:" + dealerHand.getValue()
                        + ", Player Score:" + playerHand.getValue() + ", Player Bet: " + bet
                        + " chips. Stack is now " + stack);
    }
}
