package src;

public class Main {
    public static void main(String[] args) {
        try {
            // Create a new CampusMap instance
            CampusMap campusMap = new CampusMap();
            CampusGUI campusGUI = new CampusGUI(campusMap);

            // Load the map from a JSON file
            campusMap.loadFromJson("./graphUpdated.json");

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
