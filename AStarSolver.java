package mazeSolver;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class AStarSolver {
    
    private static class Node {
        int x;
        int y;
        int g; 
        int h; 
        int f; 
        Node parent; 
        
        public Node(int x, int y, int g, int h, Node parent) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.h = h;
            this.f = g + h;
            this.parent = parent;
        }
    }
    
    public static List<Point> solveMazeAStar(int[][] maze, int startX, int startY, int endX, int endY) {

        PriorityQueue<Node> openNodes = new PriorityQueue<>(Comparator.comparingInt(node -> node.f));
        
       
        boolean[][] closedSet = new boolean[maze.length][maze[0].length];
        
        
        Node startNode = new Node(startX, startY, 0, heuristic(startX, startY, endX, endY), null);
        openNodes.offer(startNode);
        
        while (!openNodes.isEmpty()) {
            
            Node currentNode = openNodes.poll();
            closedSet[currentNode.x][currentNode.y] = true;
  
            if (currentNode.x == endX && currentNode.y == endY) {
                return reconstructPath(currentNode);
            }
            
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if ((dx == 0 && dy == 0) || (dx != 0 && dy != 0)) {
                        continue;
                    }
                    
                    int nextX = currentNode.x + dx;
                    int nextY = currentNode.y + dy;
           
                    if (nextX >= 0 && nextX < maze.length && nextY >= 0 && nextY < maze[0].length
                            && maze[nextX][nextY] != 1 && !closedSet[nextX][nextY]) {
                        int tentativeG = currentNode.g + 1; 
                        Node neighborNode = null;
                        for (Node node : openNodes) {
                            if (node.x == nextX && node.y == nextY) {
                                neighborNode = node;
                                break;
                            }
                        }
                        
                        if (neighborNode == null || tentativeG < neighborNode.g) {
                            int neighborH = heuristic(nextX, nextY, endX, endY);
                            Node updatedNeighbor = new Node(nextX, nextY, tentativeG, neighborH, currentNode);
                            openNodes.offer(updatedNeighbor);
                        }
                    }
                }
            }
        }
        
        // No path found
        return new ArrayList<>();
    }
    
    private static int heuristic(int x, int y, int endX, int endY) {
        return Math.abs(x - endX) + Math.abs(y - endY);
    }
    
    private static List<Point> reconstructPath(Node endNode) {
        List<Point> path = new ArrayList<>();
        Node currentNode = endNode;
        while (currentNode != null) {
            path.add(0, new Point(currentNode.x, currentNode.y));
            currentNode = currentNode.parent;
        }
        return path;
    }
}
