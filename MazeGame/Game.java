package MazeGame;

// Dennis Chen 26-5-2022
// Maze game
// Get to the start from the end, you are the orange cube
// Green is beginning, red is end
// click to restart when the game is over

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Game {
	public static void main(String[] args) {
		new Game();
	}
	
	// static variables
	final static int PANW = 1000;
	final static int PANH = 1000;
	
	final static int GRIDR = 40;
	final static int GRIDC = 40;
	int moves = 0;
	boolean win = false;
	
	// grid starts at 1, ends at GRIDR or GRIDC
	// idk I like it better this way
	static Cell[][] grid = new Cell[GRIDR+1][GRIDC+1];
	static boolean[][] vis = new boolean[GRIDR+1][GRIDC+1];
	 
	GameInfoPanel iPanel;
	DrawingPanel dPanel;
	Player player;
	MazeLogic maze;
	KL mainKL = new KL();
	
	Game() {
		init();
		reset();
	}
	
	private void init() {
		JFrame window = new JFrame("Maze Game By Dennis Chen");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		// add game info panel
		iPanel = new GameInfoPanel();
		window.add(iPanel, BorderLayout.NORTH);
		
		// add maze panel
		dPanel = new DrawingPanel();
		window.add(dPanel, BorderLayout.CENTER);
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
	
	private void reset() {
		// reset variables
		moves = 0;
		win = false;
		iPanel.changeMoves(moves);
		
		// create new maze
		maze = new MazeLogic(PANH, PANW, GRIDR,GRIDC);
		grid = maze.getMaze();
		
		// make player after maze generation is done
		Pair<Integer, Integer> start = maze.getStart();
		Cell temp = grid[start.first()][start.second()];
		player = new Player(
			temp.x, 			// x
			temp.y, 			// y
			temp.height, 		// height
			temp.width, 		// width
			Color.ORANGE,		// color
			start.first(),		// row
			start.second(),     // column
			PANH,				
			PANW,			
			GRIDR,
			GRIDC);  
		
		// if you want to print the maze out
		// PrintMaze print = new PrintMaze(grid, GRIDR, GRIDC);
		dPanel.repaint();
	} 
	
	// panel for displaying moves and if you won or not
	@SuppressWarnings("serial")
	private class GameInfoPanel extends JPanel {
		JLabel moveLabel;
		GameInfoPanel() {
			this.setPreferredSize(new Dimension(PANW, 25));
			this.setBackground(Color.YELLOW);
			
			moveLabel = new JLabel("Total moves: " + moves);
			this.add(moveLabel);
		}
		
		void changeMoves(int moves) {
			moveLabel.setText("Total moves: " + moves);
		}
		
		void win() {
			moveLabel.setText("You won!");
		}
	}
	
	// the main panel that displays the maze
	@SuppressWarnings("serial")
	private class DrawingPanel extends JPanel implements MouseListener{
		DrawingPanel() {
			this.setPreferredSize(new Dimension(PANW-25, PANH));
			this.setBackground(Color.BLACK);
			this.setFocusable(true);
			this.addKeyListener(mainKL);
			this.addMouseListener(this);
		}
		
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			
			for (int r = 1; r<=GRIDR; r++) {
				for (int c = 1; c<=GRIDC; c++) {
					g2.setPaint(grid[r][c].color);
					g2.fill(grid[r][c]);
				}	
			}
			
			if (player != null) {
				g2.setPaint(player.getColor());
				g2.fill(player);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// despite being slower mouse is barely used in this game
			// we only need to care about clicking when the game is over
			if (win) reset();
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {}

		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent arg0) {}

		@Override
		public void mouseReleased(MouseEvent arg0) {}
	}
	
	// key listener for player movement
	class KL implements KeyListener {
		
		@Override
		public void keyPressed(KeyEvent arg0) {}

		@Override
		public void keyReleased(KeyEvent arg0) {}

		@Override 
		public void keyTyped(KeyEvent e) {
			// Despite being slower, we want step by step movement
			// return if game is over or if player does not exist
			if (win) return;
			if (player == null) return;
			int r = player.getPos().first();
			int c = player.getPos().second();
			
			// get key pressed and change it to upper case for simpler if statements
			char key = Character.toUpperCase(e.getKeyChar());
			
			// up (w)
			if (key == 'W') r--;
			//down (S)
			if (key == 'S') r++;
			// right (D)
			if (key == 'D') c++;
			// left (A)
			if (key == 'A') c--;

			// check if in bounds
			if (r < 1 || r > GRIDR || c < 1 || c> GRIDC) return;
			if (grid[r][c].type == 'W') return;
			
			// change player position and moves
			player.changePos(r, c);
			moves++;
			iPanel.changeMoves(moves);
			
			// check if player reached the end
			if (grid[r][c].type == 'E') { 
				win = true;
				iPanel.win();
			}
			dPanel.repaint();
		}
		
	}
	
}
