// This program finds the smallest positive nonzero float and double values
class FloatLimits {
    public static void main(String[] args) {
        float floatTest = 1.0f;
        // Repeatedly divide floatTest by 2 until it is not distinguishable from 0
        while ((floatTest/2) != 0) {
            floatTest /= 2;
        }
        // Output the smallest positive float
        System.out.println("Smallest positive float is " + floatTest);

        double doubleTest = 1.0;
        // Repeatedly divide doubleTest by 2 until it is not distinguishable from 0
        while ((doubleTest/2) != 0) {
            doubleTest /= 2;
        }
        // Output the smallest positive double
        System.out.println("Smallest positive double is " + doubleTest);
    }
}