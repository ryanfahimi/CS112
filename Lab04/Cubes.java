// Class that computes and prints cubes of integers while the cube is less than 2000
class Cubes {
    public static void main(String[] args) {
        // Initializing the root of the cube to 1
        int root = 1;
        int cube;

        // Checking if the cube of the root is less than 2000
        while ((Math.pow(root,3)) < 2000) {
            // Calculating the cube of the root
            cube = (int) Math.pow(root++,3);
            // Printing the calculated cube
            System.out.println(cube);
        }
    }
}
