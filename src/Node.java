package src;

/**
 * Class of a "Node" object, should contain the name the node represents and the coordinates
 * 
 * @author cbrodish03
 * @version 11/25/2024
 */
public class Node {

    // - Fields

    private String name;        // name of node
    private double x;           // x coord
    private double y;           // y coord

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
}