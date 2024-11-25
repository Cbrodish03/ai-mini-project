/**
 * Edge object, holding two nodes
 * 
 * @author cbrodish03
 * @version 11/25/2024
 */
public class Edge {

    // - Fields

    private Node n1;        // first node in edge
    private Node n2;        // second node in edge
    private double weight;  // weight of the edge

    // - Constructor(s)

    /**
     * Default Edge constructor, with predefined nodes and weight
     * 
     * @param n1 first node
     * @param n2 second edge
     * @param weight the weight (distance/travel time) of the edge
     */
    public Edge(Node n1, node n2, double weight) {
        this.n1 = n1;
        this.n2 = n2;
        this.weight = weight;
    }

    // - Methods

    /**
     * Gets the first node of the edge
     * 
     * @return n1
     */
    public Node getFirstNode() {
        return n1;
    }

    /**
     * Gets the second node of the edge
     * 
     * @return n2
     */
    public Node getSecondNode() {
        return n2;
    }

    /**
     * Gets the weight of the edge
     * 
     * @return weight
     */
    public double getWeight() {
        return weight;
    }
}