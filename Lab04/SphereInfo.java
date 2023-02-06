// Class to represent a sphere with methods to calculate its properties
class Sphere {
    // Class variable to store the diameter of the sphere
    private double diameter;

    // Method to set the diameter of the sphere
    public void setDiameter(double diameter) {
        // Uses built-in "this" keyword to differentiate the class variable
        // from the local variable even though they have the same name
        this.diameter = diameter;
    }

    // Method to get the radius of the sphere
    public double radius() {
        double radius = diameter / 2; // Radius formula
        return radius;
    }

    // Method to get the diameter of the sphere
    public double diameter() {
        return diameter;
    }

    // Method to get the surface area of the sphere
    public double surfaceArea() {
        double surfaceArea = 4 * Math.PI * Math.pow(radius(), 2); // Surface Area formula
        return surfaceArea;
    }

    // Method to get the volume of the sphere
    public double volume() {
        double volume = 4 * Math.PI * Math.pow(radius(), 3) / 3; // Volume formula
        return volume;
    }
}

// Class to store main method
public class SphereInfo {
    public static void main(String[] args) {
        // Create three objects of type Sphere
        Sphere smallSphere = new Sphere();
        smallSphere.setDiameter(0.0);
        System.out.println("A sphere of radius " + smallSphere.radius() + " has surface area " + smallSphere.surfaceArea() + " and volume " + smallSphere.volume());

        Sphere midSphere = new Sphere();
        midSphere.setDiameter(1.0);
        System.out.println("A sphere of radius " + midSphere.radius() + " has surface area " + midSphere.surfaceArea() + " and volume " + midSphere.volume());

        Sphere bigSphere = new Sphere();
        bigSphere.setDiameter(7.5);
        System.out.println("A sphere of radius " + bigSphere.radius() + " has surface area " + bigSphere.surfaceArea() + " and volume " + bigSphere.volume());
    }
}
