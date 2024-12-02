import javax.swing.*;
import java.awt.*;

/**
 * Simple GUI to display the Campus
 * 
 * @author cbrodish03
 * @version 11/26/2024
 */
public class CampusGUI extends JFrame {

    // - Fields

    private CampusMap map;

    // - Constructor(s)

    /**
     *
     */
    public CampusGUI() {
        map = new CampusMap();
        initializeUI();
    }

    // - Methods

    /**
     *
     */
    private void initializeUI() {
        setTitle("Campus Map GUI");
        setSize(1000, 1200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MapPanel panel = new MapPanel(map);
        add(panel);
        setVisible(true);
    }


    /**
     * Runs the program
     *
     * @param args arguments to run with
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CampusGUI::new); // Ensure GUI updates happen on the Event Dispatch Thread
    }

    /**
     * Custom JPanel to display the graph
     */
    private static class MapPanel extends JPanel {

        // - Fields
        private final CampusMap map;

        // - Constructor(s)

        /**
         * Modified constructor, taking the map as a parameter
         *
         * @param map the CampusMap object
         */
        public MapPanel(CampusMap map) {
            this.map = map;
        }

        // - Methods

        /**
         *
         * @param g the <code>Graphics</code> object to protect
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw edges
            g.setColor(Color.GRAY);
            for (Edge edge : map.getEdges()) {
                int x1 = (int) edge.getFirstNode().getX();
                int y1 = (int) edge.getFirstNode().getY();
                int x2 = (int) edge.getSecondNode().getX();
                int y2 = (int) edge.getSecondNode().getY();
                g.drawLine(x1, y1, x2, y2);
            }

            // Draw nodes
            g.setColor(Color.BLUE);
            for (Node node : map.getNodes()) {
                int x = (int) node.getX();
                int y = (int) node.getY();
                g.fillOval(x - 5, y - 5, 10, 10); // Draw nodes as small circles
                g.drawString(node.getName(), x + 10, y); // Label the node
            }
        }
    }
}