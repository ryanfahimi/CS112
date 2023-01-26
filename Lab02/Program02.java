class Program02 {
	public static void main(String[] args) {
		double pizzaPrice = 24.50; // dollars
		double taxRate = 0.08625; // percentage
		double tip = 0.20; // percentage
		double total = pizzaPrice + (pizzaPrice * taxRate) + (pizzaPrice * tip); // Add in the tip!
		System.out.println("Total price of a $" + pizzaPrice + " pizza is $" + total);
	}
}