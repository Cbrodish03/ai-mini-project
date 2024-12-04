package src;

/**
 * Class of a "Node" object, should contain the name the node represents and the
 * coordinates
 * 
 * @author cbrodish03
 * @version 11/25/2024
 */
public class Node {

    // - Fields

    private String name; // name of node
    private double x; // x coord
    private double y; // y coord
    private boolean isStep; // whether the node is a step or not
    private String surface; // whether the node is on a surface or not

    // - Constructor(s)

    /**
     * Default Node constructor, with name and coords predefined
     * 
     * @param name    the name for the node
     * @param x       the x coordinate for the node
     * @param y       the y coordinate for the node
     * @param surface whether the node is on a surface or not
     * @param isStep  whether the node is a step or not
     */
    public Node(String name, double x, double y, String surface, boolean isStep) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.isStep = isStep;
        this.surface = surface;
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