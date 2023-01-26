class Program05 {
	public static void main(String[] args) {
		float radius = 5; // centimeters
		double height = 10; // centimeters
		double PI = 3.14159265358979323846;
		double baseArea = PI * radius * radius;
		double coneVolume = baseArea * height / 3;
		System.out.println("The volume of our cone is " + coneVolume);
	}
}