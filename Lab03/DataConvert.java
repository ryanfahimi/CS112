// Program converts large data types to smaller ones to demonstrate the limits of casting
class DataConvert {
    public static void main(String[] args) {
        // Initialize float variable to 2.5
        float floatTest = (float) 2.5;

        // Convert float to int and print the result
        int intTest = (int) floatTest;
        System.out.println("2.5 cast to int gives " + intTest);

        // Set the float variable to -4.5 and convert it to int
        floatTest = (float) -4.5;
        intTest = (int) floatTest;
        System.out.println("-4.5 cast to int gives " + intTest);

        // Initialize a double variable to 1.0/3.0 and convert it to float
        double doubleTest = 1.0/3.0;
        floatTest = (float) doubleTest;
        System.out.println("double 1/3 = " + doubleTest + " but float 1/3 = " + floatTest);

        // Assign int variable the value 256 and convert it to byte
        intTest = 256;
        byte byteTest = (byte) intTest;
        System.out.println("byte value of 256 is " + byteTest);

        // Set int variable to 257 and convert it to byte
        intTest = 257;
        byteTest = (byte) intTest;
        System.out.println("byte value of 257 is " + byteTest);

        // Assign int variable the value 258 and convert it to byte
        intTest = 258;
        byteTest = (byte) intTest;
        System.out.println("byte value of 258 is " + byteTest);

        // Set int variable to 511 and convert it to byte
        intTest = 511;
        byteTest = (byte) intTest;
        System.out.println("byte value of 511 is " + byteTest);
    }
}
