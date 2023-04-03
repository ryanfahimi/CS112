/**
 * filename: McDonald.java
 *
 * The classes in this file implement several types of animals,
 * all derived from the abstract class Animal.
 *
 * The main() method in class McDonald selects ten animals at random,
 * and generates the lyrics of a random "Old McDonald" song.
 *
 * @author: Ryan Fahimi
 */

/**
 * The abstract Animal class serves as a base class for different types of
 * animals.
 */
abstract class Animal {
	/**
	 * Abstract method to return the sound the animal makes.
	 * 
	 * @return the sound the animal makes as a String
	 */
	public abstract String sound();

	/**
	 * Abstract method to return the name of the animal.
	 * 
	 * @return the name of the animal as a String
	 */
	@Override
	public abstract String toString();

	/**
	 * Writes a verse of the "Old McDonald" song using the animal's name and sound.
	 */
	public void WriteVerse() {
		System.out.println("Old McDonald had a farm, EIEIO,");
		System.out.print("And on his farm he had a " + this.toString());
		System.out.println(", EIEIO,");

		String s = this.sound();
		System.out.print("With a " + s + ", " + s + " here, ");
		System.out.println("a " + s + ", " + s + " there,");
		System.out.println("Here a " + s + ", there a " + s + ", everywhere a " + s + ", " + s + ",");
		System.out.println("Old McDonald had a farm, EIEIO.");
	}
}

// STUDENTS: make 5 classes for 5 different types of animals
// All derived from class Animal.

/**
 * Represents a Pig, a derived class from the Animal class.
 */
class Pig extends Animal {
	/**
	 * Returns the sound a pig makes.
	 * 
	 * @return the sound "oink"
	 */
	public String sound() {
		return "oink";
	}

	/**
	 * Returns the name of the animal.
	 * 
	 * @return the name "pig"
	 */
	@Override
	public String toString() {
		return "pig";
	}
}

/**
 * Represents a Horse, a derived class from the Animal class.
 */
class Horse extends Animal {
	/**
	 * Returns the sound a horse makes.
	 * 
	 * @return the sound "neigh"
	 */
	public String sound() {
		return "neigh";
	}

	/**
	 * Returns the name of the animal.
	 * 
	 * @return the name "horse"
	 */
	@Override
	public String toString() {
		return "horse";
	}
}

/**
 * Represents a Duck, a derived class from the Animal class.
 */
class Duck extends Animal {
	/**
	 * Returns the sound a duck makes.
	 * 
	 * @return the sound "quack"
	 */
	public String sound() {
		return "quack";
	}

	/**
	 * Returns the name of the animal.
	 * 
	 * @return the name "duck"
	 */
	@Override
	public String toString() {
		return "duck";
	}
}

/**
 * Represents a Rooster, a derived class from the Animal class.
 */
class Rooster extends Animal {
	/**
	 * Returns the sound a rooster makes.
	 * 
	 * @return the sound "cock-a-doodle-doo"
	 */
	public String sound() {
		return "cock-a-doodle-doo";
	}

	/**
	 * Returns the name of the animal.
	 * 
	 * @return the name "rooster"
	 */
	@Override
	public String toString() {
		return "rooster";
	}
}

/**
 * Represents a Frog, a derived class from the Animal class.
 */
class Frog extends Animal {
	/**
	 * Returns the sound a frog makes.
	 * 
	 * @return the sound "ribbit"
	 */
	public String sound() {
		return "ribbit";
	}

	/**
	 * Returns the name of the animal.
	 * 
	 * @return the name "frog"
	 */
	@Override
	public String toString() {
		return "frog";
	}
}

/**
 * The McDonald class generates the lyrics of a random "Old McDonald" song
 * with 10 verses using different animals that inherit from the Animal class.
 */
public class McDonald {

	/**
	 * The main method that generates the lyrics of a random "Old McDonald" song
	 * with 10 verses using different animals that inherit from the Animal class.
	 * 
	 * @param args Command-line arguments (not used)
	 */
	public static void main(String[] args) {
		Animal[] myAnimals = new Animal[5]; // a list of Animals.
		// Adding new animals to the array.
		myAnimals[0] = new Pig();
		myAnimals[1] = new Horse();
		myAnimals[2] = new Duck();
		myAnimals[3] = new Rooster();
		myAnimals[4] = new Frog();

		// Writing 10 verses of the song, picking at RANDOM which animal in the array to
		// use.
		for (int i = 0; i < 10; i++) {
			int randomIntFromZeroThroughFour = (int) (5 * Math.random());
			myAnimals[randomIntFromZeroThroughFour].WriteVerse();
			System.out.println(); // one blank line between verses
		}
	}

} // end class McDonald
