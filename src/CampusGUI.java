package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple GUI to display the Campus
 * 
 * @author cbrodish03
 * @version 11/26/2024
 */
public class CampusGUI extends JFrame {

    // - Fields

    private final CampusMap map;

    // - Constructor(s)

    /**
     * Default constructor for the CampusGUI object
     *
     * @param map the map object to visualize
     */
    public CampusGUI(CampusMap map) {
        this.map = map;

    }

    // - Methods

    /**
     * Initializes the gui
     */
    public void initializeUI() {
        setTitle("Campus map GUI");
        setSize(1200, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MapPanel panel = new MapPanel(map, getWidth(), getHeight());
        add(panel);
        setVisible(true);
    }

    /**
     * Custom JPanel to display the graph
     */
    private static class MapPanel extends JPanel {

        // - Fields
        private final CampusMap map;
        private final int panelWidth;
        private final int panelHeight;

        private double minLongitude;
        private double maxLongitude;
        private double minLatitude;
        private double maxLatitude;

        // - Constructor(s)

        /**
         * Modified constructor, taking the map as a parameter
         *
         * @param map the CampusMap object
         * @param panelWidth width of the panel
         * @param panelHeight height of the panel
         */
        public MapPanel(CampusMap map, int panelWidth, int panelHeight) {
            this.map = map;
            this.panelWidth = panelWidth;
            this.panelHeight = panelHeight;
            calculateBounds();
        }

        // - Methods

        /**
         * Calculates the bounds for the given set of nodes
         */
        private void calculateBounds() {
            List<Node> nodes = new ArrayList<>(map.getNodes()); // Convert to a List

            minLongitude = nodes.stream().mapToDouble(Node::getX).min().orElse(0);
            maxLongitude = nodes.stream().mapToDouble(Node::getX).max().orElse(1);
            minLatitude = nodes.stream().mapToDouble(Node::getY).min().orElse(0);
            maxLatitude = nodes.stream().mapToDouble(Node::getY).max().orElse(1);
        }

        /**
         * Converts a longitude value to an x-coordinate on the panel.
         *
         * @param longitude the longitude to convert
         * @return the x-coordinate in pixels
         */
        private int longitudeToX(double longitude) {
            return (int) ((longitude - minLongitude) / (maxLongitude - minLongitude) * (panelWidth - 40)) + 20;
        }

        /**
         * Converts a latitude value to a y-coordinate on the panel.
         *
         * @param latitude the latitude to convert
         * @return the y-coordinate in pixels
         */
        private int latitudeToY(double latitude) {
            return (int) ((latitude - minLatitude) / (maxLatitude - minLatitude) * (panelHeight - 160)) + 20;
        }

        /**
         * Paints the panel to visualize the nodes and edges
         *
         * @param g the <code>Graphics</code> object to protect
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw edges
            g.setColor(Color.GRAY);
            for (Edge edge : map.getEdges()) {
                int x1 = longitudeToX(edge.getFirstNode().getX());
                int y1 = latitudeToY(edge.getFirstNode().getY());
                int x2 = longitudeToX(edge.getSecondNode().getX());
                int y2 = latitudeToY(edge.getSecondNode().getY());
                g.drawLine(x1, y1, x2, y2);
            }

            // Draw nodes
            g.setColor(Color.BLUE);
            for (Node node : map.getNodes()) {
                int x = longitudeToX(node.getX());
                int y = latitudeToY(node.getY());
                g.fillOval(x - 5, y - 5, 7, 7); // Draw nodes as small circles
                //g.drawString(node.getName(), x + 10, y); // Label the node
            }
        }
    }
}