package src;
import java.io.FileReader;
import java.util.*;
import com.google.gson.*;

public class CampusMap {

    // - Fields
    private Map<String, Node> nodes; // Map of node coordinates to Node objects
    private List<Edge> edges;        // List of edges connecting nodes

    // - Constructor(s)

    /**
     * Default constructor for CampusMap.
     * Initializes empty collections for nodes and edges.
     */
    public CampusMap() {
        nodes = new HashMap<>();
        edges = new ArrayList<>();
    }

    // - Methods

    /**
     * Loads the map data from a JSON file.
     * 
     * @param filePath Path to the JSON file
     * @throws Exception if file reading or parsing fails
     */
    public void loadFromJson(String filePath) throws Exception {
        Gson gson = new Gson();
        JsonObject mapJson = gson.fromJson(new FileReader(filePath), JsonObject.class);

        // Parse nodes
        JsonArray nodesJson = mapJson.getAsJsonArray("nodes");
        for (JsonElement nodeElement : nodesJson) {
            String[] coordinates = nodeElement.getAsString().split(",");
            double latitude = Double.parseDouble(coordinates[0]);
            double longitude = Double.parseDouble(coordinates[1]);
            String name = "Node(" + latitude + "," + longitude + ")";
            nodes.put(nodeElement.getAsString(), new Node(name, latitude, longitude));
        }

        // Parse edges
        JsonArray edgesJson = mapJson.getAsJsonArray("edges");
        for (JsonElement edgeElement : edgesJson) {
            JsonObject edgeJson = edgeElement.getAsJsonObject();
            String from = edgeJson.get("from").getAsString();
            String to = edgeJson.get("to").getAsString();
            double weight = edgeJson.get("weight").getAsDouble();

            Node n1 = nodes.get(from);
            Node n2 = nodes.get(to);
            if (n1 != null && n2 != null) {
                edges.add(new Edge(n1, n2, weight));
            }
        }
    }

    /**
     * Prints the map for debugging.
     */
    public void printMap() {
        System.out.println("Nodes:");
        for (Node node : nodes.values()) {
            System.out.println("  " + node.getName() + " at (" + node.getX() + ", " + node.getY() + ")");
        }
        System.out.println("Edges:");
        for (Edge edge : edges) {
            System.out.println("  " + edge.getFirstNode().getName() + " -> " +
                edge.getSecondNode().getName() + " with weight " + edge.getWeight());
        }
    }

    /**
     * Gets the nodes in the map.
     * 
     * @return A collection of nodes
     */
    public Collection<Node> getNodes() {
        return nodes.values();
    }

    /**
     * Gets the edges in the map.
     * 
     * @return A list of edges
     */
    public List<Edge> getEdges() {
        return edges;
    }
}
