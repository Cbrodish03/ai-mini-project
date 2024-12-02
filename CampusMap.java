import java.util.ArrayList;

/**
 * Intended to handle most of the map information, houses the backend map data
 * 
 * @author cbrodish03
 * @version 11/25/2024
 */
public class CampusMap {

    // - Fields
    private final ArrayList<Node> nodes;
    private final ArrayList<Edge> edges;

    // - Constructor(s)
    /**
     * Default CampusMap constructor, defining node and edge lists and initializing the data
     */
    public CampusMap() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        initializeMapData();
    }

    // - Methods
    /**
     * Initializes the map data by populating the node and edge lists
     */
    public void initializeMapData() {
        // Create nodes
        Node goodwin = new Node("Goodwin Hall", 300, 200);
        nodes.add(goodwin);
        Node dds = new Node("D&DS Bulding", 175, 300);
        nodes.add(dds);

        // Create edges
        edges.add(new Edge(goodwin, dds, 5.0));
    }

    /**
     * Returns the list of nodes
     *
     * @return nodes
     */
    public ArrayList<Node> getNodes() { return nodes; }

    /**
     * Returns the list of edges
     *
     * @return edges
     */
    public ArrayList<Edge> getEdges() { return edges; }
}