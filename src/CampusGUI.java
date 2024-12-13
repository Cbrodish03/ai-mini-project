package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.List;
/**
 * Campus GUI, loads the map and runs the GUI
 * 
 * @author cbrodish03 hosamk Zachvhincent
 * @version 11/25/2024
 */

public class CampusGUI extends JFrame {
    private final CampusMap map;

    public CampusGUI(CampusMap map) {
        this.map = map;
    }

    public void initializeUI() {
        setTitle("Campus map GUI");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MapPanel panel = new MapPanel(map);
        add(new JScrollPane(panel));
        setVisible(true);
    }

    private static class MapPanel extends JPanel {
        private final CampusMap map;
        private BufferedImage mapImage;
        private double scale = 0.1;
        private double offsetX = 0;
        private double offsetY = 0;
        private Point lastDragPoint = null;
        
        // Fields for path selection
        private Node selectedStart = null;
        private Node selectedEnd = null;
        private List<Node> currentPath = null;
        private double currentPathDistance = 0.0;
        
        private final double MAP_XMIN = -80.443586959;
        private final double MAP_XMAX = -80.391861732;
        private final double MAP_YMIN = 37.211987932;
        private final double MAP_YMAX = 37.235746171;
        private final int MAP_WIDTH = 15628;
        private final int MAP_HEIGHT = 7178;

        public MapPanel(CampusMap map) {
            this.map = map;
            loadMapImage();
            setupListeners();
            setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));
        }

        private void loadMapImage() {
            try {
                mapImage = ImageIO.read(new File("map_image.png"));
            } catch (Exception e) {
                System.err.println("Error loading map image: " + e.getMessage());
            }
        }

        private Node findNearestNode(Point clickPoint) {
            Node nearest = null;
            double minDistance = Double.MAX_VALUE;
            
            double mapX = (clickPoint.x / scale) - offsetX;
            double mapY = (clickPoint.y / scale) - offsetY;
            
            for (Node node : map.getNodes()) {
                int nodeX = longitudeToX(node.getY());
                int nodeY = latitudeToY(node.getX());
                
                double distance = Math.sqrt(
                    Math.pow((mapX - nodeX), 2) + 
                    Math.pow((mapY - nodeY), 2)
                );
                
                if (distance < minDistance && distance < 50) {
                    minDistance = distance;
                    nearest = node;
                }
            }
            
            return nearest;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            AffineTransform originalTransform = g2d.getTransform();

            g2d.scale(scale, scale);
            g2d.translate(offsetX, offsetY);

            if (mapImage != null) {
                g2d.drawImage(mapImage, 0, 0, MAP_WIDTH, MAP_HEIGHT, null);
            }

            // Draw edges
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3.0f));
            for (Edge edge : map.getEdges()) {
                int x1 = longitudeToX(edge.getFirstNode().getY());
                int y1 = latitudeToY(edge.getFirstNode().getX());
                int x2 = longitudeToX(edge.getSecondNode().getY());
                int y2 = latitudeToY(edge.getSecondNode().getX());
                
                if (x1 >= 0 && y1 >= 0 && x2 >= 0 && y2 >= 0) {
                    g2d.drawLine(x1, y1, x2, y2);
                }
            }

            // Draw the path if it exists
            if (currentPath != null && currentPath.size() > 1) {
                g2d.setColor(Color.YELLOW);
                g2d.setStroke(new BasicStroke(5.0f));
                
                for (int i = 0; i < currentPath.size() - 1; i++) {
                    Node current = currentPath.get(i);
                    Node next = currentPath.get(i + 1);
                    
                    int x1 = longitudeToX(current.getY());
                    int y1 = latitudeToY(current.getX());
                    int x2 = longitudeToX(next.getY());
                    int y2 = latitudeToY(next.getX());
                    
                    if (x1 >= 0 && y1 >= 0 && x2 >= 0 && y2 >= 0) {
                        g2d.drawLine(x1, y1, x2, y2);
                    }
                }
            }

            // Draw nodes
            int nodeSize = 10;
            for (Node node : map.getNodes()) {
                int x = longitudeToX(node.getY());
                int y = latitudeToY(node.getX());
                
                if (x >= 0 && y >= 0) {
                    if (node == selectedStart || node == selectedEnd) {
                        g2d.setColor(Color.YELLOW);
                        nodeSize = 15;
                    } else {
                        g2d.setColor(Color.BLUE);
                        nodeSize = 10;
                    }
                    g2d.fillOval(x - nodeSize/2, y - nodeSize/2, nodeSize, nodeSize);
                }
            }

            // Draw distance information if path exists
            if (currentPath != null && !currentPath.isEmpty()) {
                g2d.setTransform(originalTransform);
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                g2d.drawString(String.format("Distance: %.2f meters", currentPathDistance), 10, 60);
                g2d.drawString(String.format("Nodes in path: %d", currentPath.size()), 10, 80);
            }

            g2d.setTransform(originalTransform);
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString("Scale: " + String.format("%.2f", scale), 10, 20);
            g2d.drawString("Offset: (" + String.format("%.0f", offsetX) + ", " + String.format("%.0f", offsetY) + ")", 10, 40);
        }

        private void setupListeners() {
            addMouseWheelListener(e -> {
                Point mousePoint = e.getPoint();
                double oldScale = scale;
                double delta = -0.1 * e.getPreciseWheelRotation();
                scale = Math.max(0.1, Math.min(5.0, scale + delta));
                double factor = scale / oldScale - 1.0;
                offsetX -= mousePoint.x * factor;
                offsetY -= mousePoint.y * factor;
                repaint();
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        Node clickedNode = findNearestNode(e.getPoint());
                        if (clickedNode != null) {
                            if (selectedStart == null) {
                                selectedStart = clickedNode;
                                currentPath = null;
                                currentPathDistance = 0.0;
                                System.out.println("Selected start node: " + clickedNode.getName());
                            } else if (selectedEnd == null && clickedNode != selectedStart) {
                                selectedEnd = clickedNode;
                                System.out.println("Selected end node: " + clickedNode.getName());
                                Pathfinder.PathResult result = Pathfinder.aStarSearch(selectedStart, selectedEnd, map.getEdges());
                                currentPath = result.getPath();
                                currentPathDistance = result.getDistance();
                                
                                if (!currentPath.isEmpty()) {
                                    System.out.println("Path found with " + currentPath.size() + " nodes and distance " + currentPathDistance + " meters");
                                } else {
                                    System.out.println("No path found between selected nodes");
                                }
                            } else {
                                selectedStart = clickedNode;
                                selectedEnd = null;
                                currentPath = null;
                                currentPathDistance = 0.0;
                                System.out.println("Reset to new start node: " + clickedNode.getName());
                            }
                            repaint();
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        selectedStart = null;
                        selectedEnd = null;
                        currentPath = null;
                        currentPathDistance = 0.0;
                        System.out.println("Reset all selections");
                        repaint();
                    }
                    lastDragPoint = e.getPoint();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (lastDragPoint != null) {
                        int dx = e.getX() - lastDragPoint.x;
                        int dy = e.getY() - lastDragPoint.y;
                        offsetX += dx / scale;
                        offsetY += dy / scale;
                        lastDragPoint = e.getPoint();
                        repaint();
                    }
                }
            });
        }

        private int longitudeToX(double longitude) {
            if (longitude > 0) return -1;
            double proportion = (longitude - MAP_XMIN) / (MAP_XMAX - MAP_XMIN);
            return (int) (proportion * MAP_WIDTH);
        }

        private int latitudeToY(double latitude) {
            if (latitude < 0) return -1;
            double proportion = (latitude - MAP_YMIN) / (MAP_YMAX - MAP_YMIN);
            return MAP_HEIGHT - (int) (proportion * MAP_HEIGHT);
        }
    }
}