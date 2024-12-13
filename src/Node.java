package src;
/**
 * Class of a "Node" object, should contain the name the node represents and the coordinates
 * 
 * @author cbrodish03 hosamk Zachvhincent
 * @version 11/25/2024
 */
public class Node implements Comparable<Node> {

    // - Fields

    private String name;        // name of node
    private double x;           // x coord
    private double y;           // y coord
    public double cost;
    public double heuristic;
    public double total;
    Node parent;
    
    // - Constructor(s)
    
    /**
     * Default Node constructor, with name and coords predefined
     * 
     * @param name the name for the node
     * @param x the x coordinate for the node
     * @param y the y coordinate for the node
     */
    public Node(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.cost = 0;
        this.heuristic = 0;
        this.total = 0;
        this.parent = null;
    }
    

    // - Methods

    /**
     * Gets the name of the node
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the x coordinate of the node
     * 
     * @return x
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y coordinate of the node
     * 
     * @return y
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the cost of the node
     * 
     * @return cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * Gets the heuristic value of the node
     * 
     * @return heuristic
     */
    public double getHeuristic() {
        return heuristic;
    }

    /**
     * Gets the total cost of the node
     * 
     * @return total
     */
    public double getTotal() {
        return total;
    }

    /**
     * Compares nodes based on their total cost (f = g + h)
     * Required for PriorityQueue in A* implementation
     * 
     * @param other the node to compare with
     * @return negative if this node has lower total cost, positive if higher, 0 if equal
     */
    @Override
    public int compareTo(Node other) {
        return Double.compare(this.total, other.total);
    }

    /**
     * Override equals method to properly compare nodes
     * 
     * @param obj the object to compare with
     * @return true if the nodes are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node other = (Node) obj;
        return Double.compare(other.x, x) == 0 && 
               Double.compare(other.y, y) == 0;
    }
}