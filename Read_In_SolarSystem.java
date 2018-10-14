
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Solar_System {

    public static ArrayList<planet> planets = new ArrayList<>();
    public static ArrayList<String> line = new ArrayList<>();

    public static void main(String[] args) {
        File file = new File("solarsystem.dat");
        try {
            readFile(file);

        } catch (IOException ex) {
            Logger.getLogger(Solar_System.class.getName()).log(Level.SEVERE, null, ex);
        }

        savePlanet(line, planets);
//System.out.println(planets.get(0).getname());
        for (int i = 0; i < planets.size(); i++) {
            System.out.println(planets.get(i) + "\n");
        }
    }

    public static class planet {

        private String name;
        private String orbits;
        private double mass;
        private double diameter;
        private double distance;

        planet(String name, String orbits, double mass, double diameter, double perihelion, double aphelion) {
            this.name = name;
            this.orbits = orbits;
            this.mass = mass;
            this.diameter = diameter;
            this.distance = (perihelion + aphelion) / 2;
        }

        String getname() {
            return this.name;
        }

        @Override
        public String toString() {
            return "Name : " + name + "\nOrbits : " + orbits + "\nMass : " + mass + "\nDiameter : " + diameter + "\nDiatance : " + distance;
        }
    }

    public static void savePlanet(ArrayList<String> line, ArrayList<planet> planets) {
// here for my mate
        for (int i = 1; i < line.size(); i++) {
            String[] date = line.get(i).trim().split("\\s+", 0);

            planet planet = new planet(date[0], date[1], Double.parseDouble(date[2]), Double.parseDouble(date[3]),
                    Double.parseDouble(date[4]), Double.parseDouble(date[5]));
            if (date[0].equals("Sun") || date[0].equals("Earth") || date[0].equals("Moon") || date[0].equals("Venus")) {
                planets.add(planet);
            }
        }

    }

    public static void readFile(File file) throws FileNotFoundException, IOException {
// here my for mate
        Scanner reader = new Scanner(file);
        for (int i = 0; reader.hasNextLine(); i++) {
            line.add(reader.nextLine());;
        }
        reader.close();

}
