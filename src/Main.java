package src;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Main class, loads the map and runs the GUI
 * 
 * @author cbrodish03 hosamk Zachvhincent
 * @version 11/25/2024
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Create a new CampusMap instance
            CampusMap campusMap = new CampusMap();
            CampusGUI campusGUI = new CampusGUI(campusMap);

            // Load the map from the JSON file bundled inside the JAR
            InputStream inputStream = Main.class.getResourceAsStream("/graphUpdated.json");
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: graphUpdated.json");
            }
            campusMap.loadFromJson(new InputStreamReader(inputStream));

            // Print the map for debugging
            campusMap.printMap();
            campusGUI.initializeUI();

            // Example: Use the nodes and edges for pathfinding
            System.out.println("Number of nodes: " + campusMap.getNodes().size());
            System.out.println("Number of edges: " + campusMap.getEdges().size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
