// Class to represent a sphere with methods to calculate its properties
class Sphere {
    // Class variable to store the diameter of the sphere
    double diameter;

    // Method to set the diameter of the sphere
    void setDiameter(double input) {
        diameter = input;
    }

    // Method to get the radius of the sphere
    double radius() {
        double radius = diameter / 2;
        return radius;
    }

    // Method to get the diameter of the sphere
    double diameter() {
        return diameter;
    }

    // Method to get the surface area of the sphere
    double surfaceArea() {
        double surfaceArea = 4 * Math.PI * radius() * radius();
        return surfaceArea;
    }

    // Method to get the volume of the sphere
    double volume() {
        double volume = 4 * Math.PI * radius() * radius() * radius() / 3;
        return volume;
    }
}

// Class to store main method
class SphereInfo {
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
