package src;

import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import com.google.gson.*;

/**
 * CanvasMap class, holds the nodes and edges of the map
 * 
 * @author cbrodish03 hosamk Zachvhincent
 * @version 11/25/2024
 */

public class CampusMap {

    // - Fields
    private Map<String, Node> nodes; // Map of node coordinates to Node objects
    private ArrayList<Edge> edges; // List of edges connecting nodes

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
    public void loadFromJson(Reader reader) throws Exception {
        Gson gson = new Gson();
        JsonObject mapJson = gson.fromJson(reader, JsonObject.class);

        // Parse nodes
        JsonArray nodesJson = mapJson.getAsJsonArray("nodes");
        for (JsonElement nodeElement : nodesJson) {
            JsonObject nodeObject = nodeElement.getAsJsonObject();

            String coordinates = nodeObject.get("coordinates").getAsString();
            String[] latLong = coordinates.split(",");
            double latitude = Double.parseDouble(latLong[0]);
            double longitude = Double.parseDouble(latLong[1]);

            boolean isStep = nodeObject.get("isStep").getAsBoolean();
            String surface = (nodeObject.has("surface") && !nodeObject.get("surface").isJsonNull())
                    ? nodeObject.get("surface").getAsString()
                    : null;
            String name = "Node(" + latitude + "," + longitude + ")";
            Node node = new Node(name, latitude, longitude);

            nodes.put(coordinates, node);
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
    public ArrayList<Edge> getEdges() {
        return edges;
    }
}
