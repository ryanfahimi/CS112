class BlackjackDealer extends Dealer {
    private static final String HIT = "hit";
    private static final String STAND = "stand";
    private static final String DOUBLE = "double";
    private static final String SPLIT = "split";

    private BlackjackHand playerHand;
    private BlackjackHand dealerHand;

    public BlackjackDealer(int ipPort) {
        super(ipPort);
    }

    public static void main(String[] args) {
        BlackjackDealer dealer = new BlackjackDealer(IP_PORT);
        dealer.start();
    }

    @Override
    protected void playRound() {
        handleBet();
        // Deal cards
        dealInitialCards();

        // Play the round
        handlePlayerTurn();
        if (!playerHand.isBust()) {
            handleDealerTurn();
        }

        // Determine the winner and update the stack
        String result = determineResult();
        sendStatus(result);
    }

    private void handleBet() {
        // Send bet command
        connection.write("bet:" + stack + deck.getDealtCards());
        String betResponse = connection.read();
        System.out.println("Received bet response... ");
        String[] betResponseParts = betResponse.split(":");
        bet = Integer.parseInt(betResponseParts[1]);
        bet = Math.min(bet, stack);
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

    private void handleDealerTurn() {
        // Play dealer's hand according to the game rules
        while (dealerHand.getValue() < 17) {
            dealerHit();
        }
    }

    public void dealerHit() {
        dealerHand.addCard(deck.deal());
    }

    private void handlePlayerTurn() {
        boolean roundOver = false;
        while (!roundOver) {
            sendPlayCommand();
            roundOver = processPlayerDecision();
        }
    }

    private void sendPlayCommand() {
        Card dealerUpCard = dealerHand.getHand()[dealerHand.getNumCards() - 1];
        String playCommand = "play:dealer:" + dealerUpCard + ":you:" + playerHand;
        connection.write(playCommand);
    }

    private boolean processPlayerDecision() {
        String decision = connection.read();
        boolean roundOver = false;

        if (isValidPlayerDecision(decision)) {

            switch (decision) {
                case HIT:
                    playerHit();
                    break;

                case STAND:
                    roundOver = true;
                    break;

                case DOUBLE:
                    roundOver = handleDouble();
                    break;
                case SPLIT:
                    System.out.println(
                            "Player Decision: " + decision.toUpperCase() + ", Player Hand " + playerHand + ", Value: "
                                    + playerHand.getValue());
                    handleSplit();
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

    private boolean isValidPlayerDecision(String decision) {
        if (decision == null || !(decision.equals(HIT) || decision.equals(STAND) || decision.equals(DOUBLE)
                || decision.equals(SPLIT))) {
            return false;
        }
        return true;
    }

    private void playerHit() {
        playerHand.addCard(deck.deal());
    }

    private boolean handleDouble() {
        if (stack >= bet * 2) {
            bet *= 2;
            playerHand.addCard(deck.deal());
        } else {
            System.err.println("Not enough stack to double.");
        }
        return true;
    }

    private void handleSplit() {
        if (playerHand.isSplittable()) {
            BlackjackHand[] splitHands = { dealSplitHand(playerHand.getHand()[0]),
                    dealSplitHand(playerHand.getHand()[1]) };

            if (stack >= bet * 2) {
                // Request bet for split hand
                for (int i = 0; i < splitHands.length; i++) {
                    boolean isLastSplit = (i == 1);
                    System.out.println("Received bet response: bet:" + bet);
                    playerHand = splitHands[i];
                    System.out.println(
                            "Player Hand: " + playerHand + ", Value: " + playerHand.getValue() + ", Dealer Up Card: "
                                    + dealerHand.getHand()[1]);
                    handlePlayerTurn();
                    if (!isLastSplit) {
                        if (!playerHand.isBust()) {
                            handleDealerTurn();
                        }
                        String result = determineResult();
                        sendStatus(result);
                    }
                }
            } else {
                System.err.println("Not enough stack to split.");
            }
        } else {
            System.err.println("Unsplittable");
        }
    }

    private BlackjackHand dealSplitHand(Card splitCard) {
        // Create a new hand with the split card and deal a new card
        BlackjackHand splitHand = new BlackjackHand();
        splitHand.addCard(splitCard);
        splitHand.addCard(deck.deal());
        return splitHand;
    }

    private String determineResult() {
        if (playerHand.isBust()) {
            stack -= bet;
            return "lose";
        } else if (playerHand.isBlackjack() && !dealerHand.isBlackjack()) {
            bet *= 1.5;
            stack += bet; // Blackjack pays 3:2
            return "win";
        } else if (dealerHand.isBust() || playerHand.getValue() > dealerHand.getValue()) {
            stack += bet;
            return "win";
        } else if (playerHand.getValue() < dealerHand.getValue()) {
            stack -= bet;
            return "lose";
        } else {
            return "push";
        }
    }

    private void sendStatus(String result) {
        // Send round result to the player
        connection.write("status:" + result + ":dealer:" + dealerHand.getValue() + ":you:" + playerHand.getValue());

        // Print the round result
        System.out.println(
                "Result: " + result.toUpperCase() + ", Dealer Score:" + dealerHand.getValue()
                        + ", Player Score:" + playerHand.getValue() + ", Bet: " + bet
                        + ", Stack: " + stack);
    }
}
