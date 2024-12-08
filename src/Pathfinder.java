package src;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Pathfinder {
    
    private static final double COORDINATE_EPSILON = 0.0000001;
    
    public static class PathResult {
        private final List<Node> path;
        private final double distance;
        
        public PathResult(List<Node> path, double distance) {
            this.path = path;
            this.distance = distance;
        }
        
        public List<Node> getPath() {
            return path;
        }
        
        public double getDistance() {
            return distance;
        }
    }
    
    public static PathResult aStarSearch(Node start, Node goal, ArrayList<Edge> edges) {
        System.out.println("Starting A* search from " + start.getName() + " to " + goal.getName());
        
        // Create a map of nodes to track their states
        Set<Node> allNodes = new HashSet<>();
        for (Edge edge : edges) {
            allNodes.add(edge.getFirstNode());
            allNodes.add(edge.getSecondNode());
        }
        
        // Reset all nodes
        for (Node node : allNodes) {
            node.cost = Double.MAX_VALUE;
            node.parent = null;
            node.total = Double.MAX_VALUE;
            node.heuristic = 0;
        }
        
        PriorityQueue<Node> openList = new PriorityQueue<>();
        Set<Node> closedList = new HashSet<>();
        
        // Initialize start node
        start.cost = 0;
        start.heuristic = calculateHeuristic(start, goal);
        start.total = start.heuristic;
        openList.add(start);
        
        int iterations = 0;
        int maxIterations = allNodes.size() * 2;
        
        while (!openList.isEmpty() && iterations < maxIterations) {
            iterations++;
            Node current = openList.poll();
            
            // Check if we've reached the goal
            if (areNodesEqual(current, goal)) {
                return reconstructPathWithDistance(current, edges);
            }
            
            closedList.add(current);
            
            // Get all neighbors through edges
            List<Edge> relevantEdges = new ArrayList<>();
            for (Edge edge : edges) {
                if (areNodesEqual(edge.getFirstNode(), current) || areNodesEqual(edge.getSecondNode(), current)) {
                    relevantEdges.add(edge);
                }
            }
            
            for (Edge edge : relevantEdges) {
                Node neighbor = areNodesEqual(edge.getFirstNode(), current) ? 
                              edge.getSecondNode() : edge.getFirstNode();
                
                if (neighbor == null || closedList.contains(neighbor)) {
                    continue;
                }
                
                double tentativeCost = current.cost + edge.getWeight();
                
                if (tentativeCost < neighbor.cost) {
                    neighbor.parent = current;
                    neighbor.cost = tentativeCost;
                    neighbor.heuristic = calculateHeuristic(neighbor, goal);
                    neighbor.total = neighbor.cost + neighbor.heuristic;
                    
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }
        
        return new PathResult(Collections.emptyList(), 0.0);
    }
    
    private static boolean areNodesEqual(Node n1, Node n2) {
        return Math.abs(n1.getX() - n2.getX()) < COORDINATE_EPSILON && 
               Math.abs(n1.getY() - n2.getY()) < COORDINATE_EPSILON;
    }
    
    public static double calculateHeuristic(Node from, Node to) {
        double dx = from.getX() - to.getX();
        double dy = from.getY() - to.getY();
        return Math.sqrt(dx * dx + dy * dy) * 100000;
    }
    
    private static PathResult reconstructPathWithDistance(Node current, ArrayList<Edge> edges) {
        List<Node> path = new ArrayList<>();
        Node node = current;
        double totalDistance = 0.0;
        
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);
        
        for (int i = 0; i < path.size() - 1; i++) {
            Node currentNode = path.get(i);
            Node nextNode = path.get(i + 1);
            
            for (Edge edge : edges) {
                if ((areNodesEqual(edge.getFirstNode(), currentNode) && areNodesEqual(edge.getSecondNode(), nextNode)) ||
                    (areNodesEqual(edge.getFirstNode(), nextNode) && areNodesEqual(edge.getSecondNode(), currentNode))) {
                    totalDistance += edge.getWeight();
                    break;
                }
            }
        }
        
        System.out.println("Path found!");
        System.out.println("Number of nodes in path: " + path.size());
        System.out.println("Total distance: " + String.format("%.2f meters", totalDistance));
        
        return new PathResult(path, totalDistance);
    }
}