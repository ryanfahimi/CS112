public class Plagiarism {
    public static void main(String[] args) {
        // Check if there is exactly one argument provided
        if (args.length != 1) {
        // Print an error message and return if the argument is not provided
            System.out.println("Please provide exactly one argument.");
            return;
        }
        // Store the first argument in the input variable
        String input = args[0];

        // Split the input string into an array of words
        String[] words = input.split(" ");

        // Create an instance of WordCounter class
        WordCounter instance = new WordCounter();

        //Iterate through each word in the array of words
        for (String word : words) {
            // Convert each word to lowercase and count it using countWord method
            instance.countWord(word.toLowerCase());
        }

        // Get the percentage of the words that are "the"
        System.out.println("Percentage of THE words is " + instance.getPercentageOfThe());
        // Get the percentage of the words that are "a" or "an"
        System.out.println("Percentage of A or AN words is " + instance.getPercentageofAOrAn());
    }
}

class WordCounter {
    // Total number of words in the input string
    private int totalWords = 0;
    // Total number of words that are "the"
    private int countOfThe = 0;
    // Total number of words that are "a" or "an"
    private int countOfAOrAn = 0;

    // Method to count a word
    public void countWord(String word) {
        // Increment the total number of words
        totalWords++;

        // Check if the word is "the"
        if (word.equals("the")) {
        // Increment the count of "the" words
        countOfThe++;
        } else if (word.equals("a") || word.equals("an")) {
        // Increment the count of "a" or "an" words
        countOfAOrAn++;
        }
    }

    // Method to get the percentage of "the" words in the input string
    public int getPercentageOfThe() {
        // Calculate the percentage of "the" words
        int percentageOfThe = (countOfThe * 100) / totalWords;
        return percentageOfThe;
    }

    // Method to get the percentage of "a" or "an" words in the input string
    public int getPercentageofAOrAn() {
        // Calculate the percentage of "a" or "an" words
        int percentageOfAOrAn = (countOfAOrAn * 100) / totalWords;
        return percentageOfAOrAn;
    }
}
