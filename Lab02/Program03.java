class Program03 {
	public static void main(String[] args) {
		int daysPerWeek = 7;
		double weeksPerMonth = 4.34524;
		int monthsPerYear = 12;
		int daysPerYear = (int) (daysPerWeek * weeksPerMonth * monthsPerYear);
		System.out.println("There are " + daysPerYear + " days in a year");
	}
}