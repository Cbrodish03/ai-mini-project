package src;

public class Main {
    public static void main(String[] args) {
        try {
            // Create a new CampusMap instance
            CampusMap campusMap = new CampusMap();

            // Load the map from a JSON file
            campusMap.loadFromJson("./graph.json");

            // Print the map for debugging
            campusMap.printMap();

            // Example: Use the nodes and edges for pathfinding
            System.out.println("Number of nodes: " + campusMap.getNodes().size());
            System.out.println("Number of edges: " + campusMap.getEdges().size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
