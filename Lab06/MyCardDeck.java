// Define basic playing card class
class Card {
    // Declare instance variables
    private int value; // Holds the value of the card
    private String suit; // Holds the suit of the card

    // Constructor that sets the value and suit of the card
    public Card(int value, String suit) {
        if (isValidNumber(value) && isValidSuit(suit.charAt(0))) {
            // Assigns the value of the parameter 'value' to the instance variable 'value'
            this.value = value;
            // Assigns the value of the parameter 'suit' to the instance variable 'suit'
            this.suit = suit.substring(0,1).toUpperCase();
        // If the card is not valid
        } else {
            // Assign 0 to the value class variable
            this.value = 0;
            // Assign "ERROR" to the suit class variable
            this.suit = "ERROR";
        }
    }

    // Constructor that creates a card based on a string representation of a card
    public Card(String card) {
        // Initialize a boolean variable to keep track of if the input string is a valid card
        boolean isValidCard = false;
        // Initialize a string variable to store the value of the card
        String value = "";

        // Check if the first character of the parameter 'card' is a valid number or letter and the second character of the parameter 'card' is a valid suit
        if ((isValidNumber(card.charAt(0)) || isValidLetter(card.charAt(0))) && (isValidSuit(card.charAt(1)))) {
            // If both checks pass, assign the first character of the parameter 'card' to the value variable
            value = card.substring(0, 1);
            // Assign the second character of the parameter 'card' converted to uppercase to the instance variable 'suit'
            this.suit = card.substring(1, 2).toUpperCase();
            // Set the isValidCard variable to true
            isValidCard = true;
            }
        // Check if the first two characters of the parameter 'card' are "10" and the third character of the parameter 'card' is a valid suit
        else if (isValidValue(card.substring(0, 2)) && isValidSuit(card.charAt(2))) {
            // If the check passes, assign "10" to the value variable
            value = card.substring(0, 2);
            // Assign the third character of the parameter 'card' converted to uppercase to the instance variable 'suit'
            this.suit = card.substring(2, 3).toUpperCase();
            // Set the isValidCard variable to true
            isValidCard = true;
        }

        // If the parameter 'card' is a valid card
        if (isValidCard) {
        // Map the value string to an integer representation of the card's value
            if (isValidNumber(value.charAt(0))) {
                this.value = value.charAt(0) - '0';
              } else if (value.equals("10")) {
                this.value = 10;
              } else if (value.equalsIgnoreCase("J")) {
                this.value = 11;
            } else if (value.equalsIgnoreCase("Q")) {
                this.value = 12;
            } else if (value.equalsIgnoreCase("K")) {
                this.value = 13;
            } else if (value.equalsIgnoreCase("A")) {
                this.value = 14;
            }
        } 
        // If the parameter 'card' is not a valid card
        else {
            // Assign 0 to the value class variable
            this.value = 0;
            // Assign "ERROR" to the suit class variable
            this.suit = "ERROR";
        }
    }


    // This constructor creates a card based on the 'value' and 'suit' string parameters
    public Card(String value, String suit) {
        boolean isValidCard = false;

        // Validate the value and suit of the card
        if (isValidValue(value) && isValidSuit(suit.charAt(0))) {
            this.suit = suit.substring(0, 1).toUpperCase();
            isValidCard = true;
        }

        // If the card is valid, set its value and suit
        if (isValidCard) {
            // Check if the card's value is a number
            if (isValidNumber(value.charAt(0))) {
                this.value = value.charAt(0) - '0';
            } else if (value.equals("10")) {
                this.value = 10;
            } else if (value.equalsIgnoreCase("J")) {
                this.value = 11;
            } else if (value.equalsIgnoreCase("Q")) {
                this.value = 12;
            } else if (value.equalsIgnoreCase("K")) {
                this.value = 13;
            } else if (value.equalsIgnoreCase("A")) {
                this.value = 14;
            }
        } else {
            // If the card is not valid, set its value to 0 and suit to "ERROR"
            this.value = 0;
            this.suit = "ERROR";
        }
    }

    // A helper method that checks if a given character is a valid number card value
    private boolean isValidNumber(char value) {
        return '2' <= value && value <= '9';
    }

    // A helper method that checks if a given integer is a valid card value
    private boolean isValidNumber(int value) {
        return 2 <= value && value <= 14;
    }

    // A helper method that checks if a given string is a valid card value
    private boolean isValidValue(String value) {
        return value.equals("2") || value.equals("3") || value.equals("4") || 
        value.equals("5") || value.equals("6") || value.equals("7") || 
        value.equals("8") || value.equals("9") || value.equals("10") || 
        value.equalsIgnoreCase("J") || value.equalsIgnoreCase("Q") || 
        value.equalsIgnoreCase("K") || value.equalsIgnoreCase("A");
    }

    // A helper method that checks if a given character is a valid letter card value
    private boolean isValidLetter(char value) {
        return "JQKAjqka".indexOf(value) != -1;
    }

    // A helper method that checks if a given character is a valid card suit
    private boolean isValidSuit(char suit) {
        return "SHCDshcd".indexOf(suit) != -1;
    }


    public int value() {
        // Returns the value stored in the instance variable 'value'
        return this.value;
    }

    public String suit() {
        // Returns the suit stored in the instance variable 'suit'
        return this.suit;
    }

    // Returns a string representation of the card
    public String print() {
        // Checks if the value of the card is equal to 11
        if (this.value == 11) {
            // Returns 'J' + the suit of the card
            return "J" + this.suit;
        } 
        // Checks if the value of the card is equal to 12
        else if (this.value == 12) {
            // Returns 'Q' + the suit of the card
            return "Q" + this.suit;
        } 
        // Checks if the value of the card is equal to 13
        else if (this.value == 13) {
            // Returns 'K' + the suit of the card
            return "K" + this.suit;
        } 
        // Checks if the value of the card is equal to 14
        else if (this.value == 14) {
            // Returns 'A' + the suit of the card
            return "A" + this.suit;
        } 
        // The value of the card is not 11, 12, 13, or 14
        else {
            // Returns the value + the suit of the card
            return this.value + this.suit;
        }
    }
}


class Deck {
    // A deck of playing cards consists of 52 cards in total, one for each suit and value combination
    Card cardDeck[] = new Card[52]; // Instantiate a deck of 52 playing cards
    private int cardsLeft = cardDeck.length; // Initialize the instance variable 'cardsLeft' for the deal and shuffle methods

    public Deck() {
        int index = 0;
        // Create an array of 4 suits to represent the suits of the playing cards
        String[] suits = {"S", "H", "C", "D"};
        // Iterate through each card value from 2 to 14 and assign a unique card with each value to each index in the cardDeck array
        for (int value = 2; value <= 14; value++) {
            // Iterate through each card suit and assign a unique card with each suit to each index in the cardDeck array
            for (String suit : suits) {
                // Create a new card instance with the current value and suit and assign it to the current index in the cardDeck array
                cardDeck[index++] = new Card(value, suit);
            }
        }
    }

    // Create a string representation of the entire deck of cards
    public String print() {
        String cardDeck = "";
        // Loop through each card in the cardDeck array and add its string representation to the cardDeck string
        for (Card card : this.cardDeck) {
            cardDeck += card.print() + " ";
        }
        // Return the final cardDeck string
        return cardDeck;
    }

    // Utilize the Fisher-Yates shuffle algorithm to arrange all the cards in the deck randomly with an equal chance for every permutation
    public void shuffle() {
        // Iterate 'current' from the length of the cardDeck array down to 1 to represent the index of the current card in the cardDeck array
        for (int current = cardDeck.length - 1; current > 0; current--) {
            // Set 'random' to a random integer in the range [1,current] to represent the index of a random card in a subset of the cardDeck array
            int random = (int) (Math.random() * (current + 1));
            // Swap the two cards in the cardDeck
            Card temp = cardDeck[current];
            cardDeck[current] = cardDeck[random];
            cardDeck[random] = temp;
        }
        // Reset 'cardsLeft' instance variable to enable dealing
        cardsLeft = cardDeck.length;
    }

    // Deal one card from the deck
    public Card deal() {
        if (cardsLeft > 0) {
            return cardDeck[--cardsLeft];
        // If all the cards have been dealt, return an error card
        } else {
            return new Card(0, "ERROR");
        }
    }
}


public class MyCardDeck {
    public static void main(String[] args) {
        // If the user provides an argument when executing the program
        if (args.length > 0) {
            // Create a new Card object with the user-specified argument
            Card myCard = new Card(args[0]);
            // Print the card in string format
            System.out.println(myCard.print());
        } else {
            // If the user does not provide an argument, create a new Deck object
            Deck myCardDeck = new Deck();
            // Print the deck of cards in string format
            System.out.println(myCardDeck.print());

            //Card myCard = new Card("10", "spades"); System.out.println(myCard.print()); // Third constructor test
            //myCardDeck.shuffle();System.out.println(myCardDeck.print()); // shuffle() test
            //System.out.println(myCardDeck.deal().print()); // deal() test
            //for (int i=0; i<53; i++) {System.out.println(myCardDeck.deal().print());} // deal() limit test
            //for (int i=0; i<53; i++) {System.out.println(myCardDeck.deal().print());} myCardDeck.shuffle(); for (int i=0; i<53; i++) {System.out.println(myCardDeck.deal().print());} // deal() limit + shuffle() test
        }
    }
}
