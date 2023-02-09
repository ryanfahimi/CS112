public class ReadArgs {
    public static void main(String[] args) {
        // Initialize the class variables
        int numberOfArgs = args.length;
        int i = 0;

        // Print the number of arguments passed in
        System.out.println("Program called with " + numberOfArgs + " arguments:");
        
        // Loop through all the arguments and print each one with an explanatory message
        while (i < numberOfArgs) {
            System.out.println("Argument #" + i + " is " + args[i]);
            i++;
        }
    }
}