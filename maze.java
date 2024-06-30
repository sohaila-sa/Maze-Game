package mazeSolver;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class maze extends JFrame implements ActionListener {
	
	JButton speedBtn = new JButton("Solve Maze");
	JButton solveBtn = new JButton("Speed");
	JButton reset = new JButton("Reset Maze");
	JPanel btnPanel = new JPanel();
	private JCheckBox dfsCheckbox;
	private JCheckBox aStarCheckbox;
    /*
     * Values: 0 = open wall
     *         1 = wall (blocked)
     *         2 = visited node
     *         9 = target node
     */
    // With DFS algorithm the maze works properly but with A* the reset and speed might not work properly
    public static int [][] maze = 
        { {1,1,1,1,1,1,1,1,1,1,1,1,1},
          {1,0,1,0,1,0,1,0,0,0,0,0,1},
          {1,0,1,0,0,0,1,0,1,1,1,0,1},
          {1,0,0,0,1,1,1,0,0,0,0,0,1},
          {1,0,1,0,0,0,0,0,1,1,1,0,1},
          {1,0,1,0,1,1,1,0,1,0,0,0,1},
          {1,0,1,0,1,0,0,0,1,1,1,0,1},
          {1,0,1,0,1,1,1,0,1,0,1,0,1},
          {1,0,0,0,0,0,0,0,0,0,1,0,1},
          {1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
    
    public JButton [][] gridButtons = new JButton[maze.length][maze[0].length];
    JPanel mazePanel = new JPanel(new GridLayout(maze.length, maze[0].length));
    private List<Point> path = new ArrayList<>();
	int startX, startY, endX, endY;
	private Timer timer;
	private int delay = 500;
	
    public  maze() {
        setTitle("Simple Maze Solver");
        setSize(640, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeButtons();
        setStartAndEndPoints();
        
        getContentPane().add(mazePanel, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
        
        
        dfsCheckbox = new JCheckBox("Depth First Search");
        aStarCheckbox = new JCheckBox("A* Search");
        btnPanel.add(new JLabel("Select Algorithms:"));
        btnPanel.add(dfsCheckbox);
        btnPanel.add(aStarCheckbox);
        
        btnPanel.add(speedBtn);
        btnPanel.add(solveBtn);
        btnPanel.add(reset);
        solveBtn.addActionListener(this);
        speedBtn.addActionListener(this);
        reset.addActionListener(this);
        
        timer = new Timer(); 
        
    }
    
  
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	 maze view = new  maze();
                view.setVisible(true);
            }
        });
    }

	@Override
	
	public void actionPerformed(ActionEvent e) {	
		if (e.getActionCommand().equals("Solve Maze")) {
			   if (isAStarSelected()) {
		            path = AStarSolver.solveMazeAStar(maze, startX, startY, endX, endY);
		            solveMazeWithDelayAStar(delay);
		        } else {
		            path = DFSsolver.solveMazeDFS(maze, startX, startY, endX, endY);
		            solveMazeWithDelay(delay); 
		        } 	
            
        } else if (e.getActionCommand().equals("Speed")){
        	 String[] options = {"Slow", "Medium", "Fast"};
             int selectedOption = JOptionPane.showOptionDialog(null, "Select speed:", "Speed Options",
             JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
             switch (selectedOption) {
                 case 0: delay = 1000; break;
                 case 1: delay = 500; break;
                 case 2: delay = 100; break;
                 default: delay = 500;
             }
             if (isAStarSelected()) {
                 solveMazeWithDelayAStar(delay);
             } else {
                 solveMazeWithDelay(delay);
             }    
        }
		
        else if (e.getActionCommand().equals("Reset Maze")){
        	resetMaze();
        	
        }
        else {
			for (int row = 0; row < maze.length; row++) {
	            for (int col = 0; col < maze[0].length; col++) {
	                	if (gridButtons[row][col] == e.getSource() && maze[row][col] != 2 && maze[row][col] != 9) {
	                        maze[row][col] = (maze[row][col] == 0) ? 1 : 0;
	                        updateButtons(gridButtons[row][col], maze[row][col]); // Update button color
	                        return;
	                	}
	            	}
	        	}
        }      
	}


	
	 public void setStartAndEndPoints() {
	        startX = 1;
	        startY = (int)(Math.random() * maze[0].length);
	        endX = maze.length - 2;
	        endY = (int)(Math.random() * maze[0].length);
	        
	        maze[startX][startY] = 2;
	        updateButtons(gridButtons[startX][startY], 2); 
	        
	        maze[endX][endY] = 9;
	        updateButtons(gridButtons[endX][endY], 9);
	 }
	 
	
	    public void initializeButtons() {
	        for (int row = 0; row < maze.length; row++) {
	            for (int col = 0; col < maze[0].length; col++) {
	                JButton button = new JButton();
	                gridButtons[row][col] = button;
	                Color color;
		            switch (maze[row][col]) {
		            	case 1: color = Color.BLACK; break;
		            	case 2:color = Color.GREEN; break;
		            	case 3: color = Color.BLUE; break;
		                case 9: color = Color.RED; break;
		                default: color = Color.WHITE;
		            }
		            button.setBackground(color);
		            button.addActionListener(this);
	                mazePanel.add(button); 
	            }
	        }
	    }
	    
	    public void updateButtons(JButton button, int value) {
			 Color color;
		        switch (value) {
		            case 1: color = Color.BLACK; break; 
		            case 2: color = Color.GREEN; break; 
		            case 3: color = Color.BLUE; break; 
		            case 9: color = Color.RED; break; 
		            default: color = Color.WHITE; 
		        }
		        
		        button.setBackground(color);
		}
	    private void showSolutionPath(List<Point> solutionPath) {
	        for (Point point : solutionPath) {
	            int x = (int) point.getX();
	            int y = (int) point.getY();
	            if (maze[x][y] != 2 && maze[x][y] != 9) {
	            	//maze[x][y] = 3;
	                updateButtons(gridButtons[x][y], 3); // Let's use Color.BLUE for the path
	            }
	        }
	    }

	    private void solveMazeWithDelay(int delay) {
	    	timer = new Timer();
	        TimerTask task = new TimerTask() {
	            int step = 0;

	            @Override
	            public void run() {
	                if (step < path.size()) {
	                    Point currentPoint = path.get(step);
	                    int currentX = (int) currentPoint.getX();
	                    int currentY = (int) currentPoint.getY();
	                    if (maze[currentX][currentY] != 2 && maze[currentX][currentY] != 9) {
	                        updateButtons(gridButtons[currentX][currentY], 2); // Mark the current step as visited
	                    }
	                    step++;
	                } else {
	                    // Reached the end of the path, mark the final path in blue
	                    for (Point point : path) {
	                        int x = (int) point.getX();
	                        int y = (int) point.getY();
	                        if (maze[x][y] != 2 && maze[x][y] != 9) {
	                            updateButtons(gridButtons[x][y], 3);
	                        }
	                    }
	                    cancel(); // Stop the timer
	                }
	            }
	        };
	        timer.scheduleAtFixedRate(task, delay, delay);
	    }
	    
	    private void solveMazeWithDelayAStar(int delay) {
	        timer = new Timer();
	        TimerTask task = new TimerTask() {
	            int step = 0;
	            List<Point> solutionPath = AStarSolver.solveMazeAStar(maze, startX, startY, endX, endY);

	            @Override
	            public void run() {
	                if (step < solutionPath.size()) {
	                    Point currentPoint = solutionPath.get(step);
	                    int currentX = (int) currentPoint.getX();
	                    int currentY = (int) currentPoint.getY();
	                    if (maze[currentX][currentY] != 2 && maze[currentX][currentY] != 9) {
	                        updateButtons(gridButtons[currentX][currentY], 2); 
	                    }
	                    step++;
	                } else {
	                    
	                    for (Point point : solutionPath) {
	                        int x = (int) point.getX();
	                        int y = (int) point.getY();
	                        if (maze[x][y] != 2 && maze[x][y] != 9) {
	                            updateButtons(gridButtons[x][y], 3);
	                        }
	                    }
	                    cancel(); 
	                }
	            }
	        };
	        timer.scheduleAtFixedRate(task, delay, delay);
	    }
	    private boolean isAStarSelected() {
	        return aStarCheckbox.isSelected();
	    }
	    
	    private void resetMaze() {
	        for (int row = 0; row < maze.length; row++) {
	            for (int col = 0; col < maze[0].length; col++) {
	                maze[row][col] = (maze[row][col] == 2 || maze[row][col] == 9) ? 0 : maze[row][col];
	                updateButtons(gridButtons[row][col], maze[row][col]);
	            }
	        }
	        setStartAndEndPoints();
	        path = new ArrayList<>();
	        timer.cancel(); 
	    }
	}


