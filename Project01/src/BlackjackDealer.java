/**
 * An implementation of the Dealer class for the card game Blackjack.
 * Handles the game-specific logic and interactions with the client.
 */
class BlackjackDealer extends Dealer {
    private static final String BET = "bet";
    private static final String HIT = "hit";
    private static final String STAND = "stand";
    private static final String DOUBLE = "double";
    private static final String SPLIT = "split";
    private static final String PUSH = "push";

    private BlackjackHand playerHand;
    private BlackjackHand dealerHand;

    /**
     * Constructs a new BlackjackDealer instance.
     *
     * @param ipPort the IP port to be used for the connection.
     */
    public BlackjackDealer(int ipPort) {
        super(ipPort);
    }

    public static void main(String[] args) {
        BlackjackDealer dealer = new BlackjackDealer(IP_PORT);
        dealer.start();
    }

    /**
     * Plays a single round of the Blackjack game.
     */
    @Override
    protected void playRound() {
        handleBet();
        dealInitialCards();

        handlePlayerTurn();
        if (!playerHand.isBust()) {
            handleDealerTurn();
        }

        String result = determineResult();
        sendStatus(result);
    }

    /**
     * Handles the player's bet.
     */
    private void handleBet() {
        connection.write(BET + ":" + stack + deck.getDealtCards());
        String betResponse = connection.read();
        String[] betResponseParts = betResponse.split(":");
        bet = Integer.parseInt(betResponseParts[1]);
        bet = Math.min(bet, stack);
        System.out.println("Bet: " + bet);
    }

    /**
     * Deals the initial cards to the player and the dealer.
     */
    public void dealInitialCards() {
        playerHand = dealHand();
        dealerHand = dealHand();
        System.out.println("Player Hand: " + playerHand + ", Value: " + playerHand.getValue() + ", Dealer Up Card: "
                + dealerHand.getHand()[1]);
    }

    /**
     * Deals a new Blackjack hand.
     *
     * @return the dealt BlackjackHand.
     */
    private BlackjackHand dealHand() {
        BlackjackHand hand = new BlackjackHand();
        hand.addCard(deck.deal());
        hand.addCard(deck.deal());
        return hand;
    }

    /**
     * Handles the dealer's turn.
     */
    private void handleDealerTurn() {
        while (dealerHand.getValue() < 17) {
            dealerHit();
        }
    }

    /**
     * Performs a hit action for the dealer.
     */
    public void dealerHit() {
        dealerHand.addCard(deck.deal());
    }

    /**
     * Handles the player's turn, including sending the play command and processing
     * the
     * player decision.
     */
    private void handlePlayerTurn() {
        boolean roundOver = false;
        while (!roundOver) {
            sendPlayCommand();
            roundOver = processPlayerDecision();
        }
    }

    /**
     * Sends the play command to the client.
     */
    private void sendPlayCommand() {
        Card dealerUpCard = dealerHand.getHand()[dealerHand.getNumCards() - 1];
        String playCommand = PLAY + ":dealer:" + dealerUpCard + ":you:" + playerHand;
        connection.write(playCommand);
    }

    /**
     * Processes the player's decision.
     *
     * @return true if the round is over, false otherwise.
     */
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
                            "Player Decision: " + decision.toUpperCase() + ", Player Hand: " + playerHand + ", Value: "
                                    + playerHand.getValue());
                    handleSplit();
                    return true;
            }

            if (playerHand.isBust()) {
                roundOver = true;
            }

            System.out.println(
                    "Player Decision: " + decision.toUpperCase() + ", Player Hand: " + playerHand + ", Value: "
                            + playerHand.getValue());
        } else {
            System.err.println("Invalid response from player: " + decision);
            roundOver = true;
        }
        return roundOver;
    }

    /**
     * Checks if the player's decision is valid.
     *
     * @param decision the decision made by the player.
     * @return true if the decision is valid, false otherwise.
     */
    private boolean isValidPlayerDecision(String decision) {
        if (decision == null || !(decision.equals(HIT) || decision.equals(STAND) || decision.equals(DOUBLE)
                || decision.equals(SPLIT))) {
            return false;
        }
        return true;
    }

    /**
     * Performs a hit action for the player.
     */
    private void playerHit() {
        playerHand.addCard(deck.deal());
    }

    /**
     * Handles the player's double decision.
     *
     * @return true if the round is over, false otherwise.
     */
    private boolean handleDouble() {
        if (stack >= bet * 2) {
            bet *= 2;
            playerHand.addCard(deck.deal());
        } else {
            System.err.println("Not enough stack to double.");
        }
        return true;
    }

    /**
     * Handles the player's split decision.
     */
    private void handleSplit() {
        if (playerHand.isPair()) {
            BlackjackHand[] splitHands = { dealSplitHand(playerHand.getHand()[0]),
                    dealSplitHand(playerHand.getHand()[1]) };

            if (stack >= bet * 2) {
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

    /**
     * Deals a split hand for the player.
     *
     * @param splitCard the card to be split.
     * @return the dealt BlackjackHand.
     */
    private BlackjackHand dealSplitHand(Card splitCard) {
        BlackjackHand splitHand = new BlackjackHand();
        splitHand.addCard(splitCard);
        splitHand.addCard(deck.deal());
        return splitHand;
    }

    /**
     * Determines the result of the round based on the player's and dealer's hands.
     *
     * @return a string representing the result of the round ("win", "lose", or
     *         "push").
     */
    private String determineResult() {
        if (playerHand.isBust()) {
            stack -= bet;
            return LOSE;
        } else if (playerHand.isBlackjack() && !dealerHand.isBlackjack()) {
            bet *= 1.5;
            stack += bet; // Blackjack pays 3:2
            return WIN;
        } else if (dealerHand.isBust() || playerHand.getValue() > dealerHand.getValue()) {
            stack += bet;
            return WIN;
        } else if (playerHand.getValue() < dealerHand.getValue()) {
            stack -= bet;
            return LOSE;
        } else {
            return PUSH;
        }
    }

    /**
     * Sends the round status to the client.
     *
     * @param result a string representing the result of the round ("win", "lose",
     *               or "push").
     */
    private void sendStatus(String result) {
        if (playerHand.isBust()) {
            connection.write("status:" + result + ":you:" + playerHand.getValue());
        } else if (playerHand.isBlackjack() && !dealerHand.isBlackjack()) {
            connection.write("status:" + result + ":you:blackjack");
        } else {
            connection.write("status:" + result + ":dealer:" + dealerHand.getValue() + ":you:" + playerHand.getValue());
        }

        System.out.println(
                "Result: " + result.toUpperCase() + ", Dealer Score:" + dealerHand.getValue()
                        + ", Player Score:" + playerHand.getValue() + ", Bet: " + bet
                        + ", Stack: " + stack);
    }
}
