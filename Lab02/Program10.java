class Program10 {
	public static void main(String[] args) {
		// Initialize char with int.
		// Figure out the answer and fill in correct name.
		char special = 169;
		System.out.print("The common name for the " + special + " symbol is the ");
		System.out.print("question");
		System.out.println(" symbol.");

		// Initialize char with Unicode constant.
		char another = '\u00a2';
		System.out.print("This " + another + " char is the ");
		System.out.print("cent");
		System.out.println(" symbol.");
	}
}