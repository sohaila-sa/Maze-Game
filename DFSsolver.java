package mazeSolver;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class DFSsolver {
	public static List<Point> solveMazeDFS(int[][] maze, int startX, int startY, int endX, int endY) {
	     List<Point> solutionPath = new ArrayList<>();
	     boolean[][] visited = new boolean[maze.length][maze[0].length];
	     dfs(maze, startX, startY, endX, endY, visited, solutionPath);
	     return solutionPath;
     
 }

 private static boolean dfs(int[][] maze, int x, int y, int endX, int endY, boolean[][] visited, List<Point> path) {
		    // Check if the current cell is within the maze boundaries and is not a wall or already visited
		    if (x < 0 || x >= maze.length || y < 0 || y >= maze[0].length || maze[x][y] == 1 || visited[x][y]) {
		        return false;
		    }

		    // Mark the current cell as visited
		    visited[x][y] = true;

		    // Add the current cell to the path
		    path.add(new Point(x, y));

		    // Check if the current cell is the end point
		    if (x == endX && y == endY) {
		        return true;
		    }

		    // Explore all possible directions (up, down, left, right)
		    if (dfs(maze, x - 1, y, endX, endY, visited, path) || dfs(maze, x + 1, y, endX, endY, visited, path) ||
		        dfs(maze, x, y - 1, endX, endY, visited, path) || dfs(maze, x, y + 1, endX, endY, visited, path)) {
		        return true;
		    }

		    // If no path is found, backtrack
		    path.remove(path.size() - 1);
		    return false;
		}

 }
