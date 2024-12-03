package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;  // This was missing - needed for MouseAdapter, MouseEvent, etc.
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

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
        private double scale = 0.1;  // Start zoomed out
        private double offsetX = 0;
        private double offsetY = 0;
        private Point lastDragPoint = null;

        // QGIS map bounds
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
            
            // Debug: Print coordinates
            System.out.println("DEBUG: Map bounds:");
            System.out.println("Longitude (X): " + MAP_XMIN + " to " + MAP_XMAX);
            System.out.println("Latitude (Y): " + MAP_YMIN + " to " + MAP_YMAX);
        }

        private void loadMapImage() {
            try {
                mapImage = ImageIO.read(new File("map_image.png"));
            } catch (Exception e) {
                System.err.println("Error loading map image: " + e.getMessage());
            }
        }

        private int longitudeToX(double longitude) {
            if (longitude > 0) {  // If it's actually a latitude
                return -1;  // Invalid conversion
            }
            double proportion = (longitude - MAP_XMIN) / (MAP_XMAX - MAP_XMIN);
            int pixelX = (int) (proportion * MAP_WIDTH);
            return pixelX;
        }

        private int latitudeToY(double latitude) {
            if (latitude < 0) {  // If it's actually a longitude
                return -1;  // Invalid conversion
            }
            double proportion = (latitude - MAP_YMIN) / (MAP_YMAX - MAP_YMIN);
            int pixelY = MAP_HEIGHT - (int) (proportion * MAP_HEIGHT);  // Invert Y coordinate
            return pixelY;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Enable antialiasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Store the original transform
            AffineTransform originalTransform = g2d.getTransform();

            // Apply zoom and pan
            g2d.scale(scale, scale);
            g2d.translate(offsetX, offsetY);

            // Draw the map image
            if (mapImage != null) {
                g2d.drawImage(mapImage, 0, 0, MAP_WIDTH, MAP_HEIGHT, null);
            }

            // Draw edges
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3.0f));
            for (Edge edge : map.getEdges()) {
                // Note the swapped coordinates
                int x1 = longitudeToX(edge.getFirstNode().getY());  // Y holds longitude
                int y1 = latitudeToY(edge.getFirstNode().getX());   // X holds latitude
                int x2 = longitudeToX(edge.getSecondNode().getY());
                int y2 = latitudeToY(edge.getSecondNode().getX());
                
                // Only draw if coordinates are valid
                if (x1 >= 0 && y1 >= 0 && x2 >= 0 && y2 >= 0) {
                    g2d.drawLine(x1, y1, x2, y2);
                }
            }

            // Draw nodes
            g2d.setColor(Color.BLUE);
            int nodeSize = 10;
            for (Node node : map.getNodes()) {
                // Note the swapped coordinates
                int x = longitudeToX(node.getY());  // Y holds longitude
                int y = latitudeToY(node.getX());   // X holds latitude
                
                // Only draw if coordinates are valid
                if (x >= 0 && y >= 0) {
                    g2d.fillOval(x - nodeSize/2, y - nodeSize/2, nodeSize, nodeSize);
                }
            }

            // Reset transform
            g2d.setTransform(originalTransform);
            
            // Draw debug info
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString("Scale: " + String.format("%.2f", scale), 10, 20);
            g2d.drawString("Offset: (" + String.format("%.0f", offsetX) + ", " + String.format("%.0f", offsetY) + ")", 10, 40);
        }

        private void setupListeners() {
            // Zooming with mouse wheel
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

            // Panning with mouse drag
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    lastDragPoint = e.getPoint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    lastDragPoint = null;
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
    }
}