// Dennis Chen 5/12/2022
// Tic Tac Toe for 2 players
// Grid size can change, will run into errors if the amount of tiles is too high (due to limited resolution)

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe {
	public static void main(String[] args) {
		new TicTacToe();
	}
	
	final static int SIZE = 3; // grid size
	final static int PX = 1;
	final static int PO = -1;
	
	boolean turnX = true; // x turn is true, o turn is false
	boolean gameEnd = false; 
	
	int xWins = 0;
	int oWins = 0;
	int ties = 0;
	
	int[][] board = new int[SIZE][SIZE];
	JLabel turn = new JLabel();
	JLabel wins = new JLabel();
	
	TicTacToe() {
		init();
		createGUI();
	}
	
	void init() { // reset the game
		turnX = true; // x turn is true, o turn is false
		gameEnd = false; 
		
		board = new int[SIZE][SIZE];
		turn.setText("X's Turn");
	}
	
	void createGUI() {
		JFrame frame = new JFrame("TicTacToe");
		
		// top panel for messages
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(2,1));

		topPanel.add(turn);
		topPanel.add(wins);
		
		topPanel.setBackground(new Color(255, 255, 200));
		
		turn.setText("X's Turn");
		turn.setFont(new Font("Dialog", Font.BOLD, 18));
		turn.setHorizontalAlignment(JLabel.CENTER);
		
		wins.setText("X wins: " + xWins + "    O wins: " + oWins + "    Ties: " + ties);
		wins.setFont(new Font("Dialog", Font.BOLD, 15));
		wins.setHorizontalAlignment(JLabel.CENTER);

		frame.add(topPanel, BorderLayout.NORTH);
		
		// Drawing panel for game
		DrawingPanel dPanel = new DrawingPanel();
		frame.add(dPanel, BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600,500);;
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private class DrawingPanel extends JPanel implements MouseListener {
		int panW, panH; // size of panel
		int boxW, boxH; // size of one box/square
		
		DrawingPanel() {
			this.setBackground(new Color(240,240,240));
			this.addMouseListener(this);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			initGrid();
			
			// draw grid
			g.setColor(Color.GRAY);
			for (int i = 0; i<SIZE; i++) {
				g2.drawLine(boxW*i, 0, boxW*i, panH);
				g2.drawLine(0, boxH*i, panW, boxH*i);
			}
			
			// draw X or O
			g2.setColor(Color.RED);
			g2.setStroke(new BasicStroke(2));
			for (int r = 0; r<SIZE; r++) {
				for (int c = 0; c<SIZE; c++) {
					// draw X
					if (board[r][c] == PX) {
						g2.drawLine(c*boxW, r*boxH, (c+1)*boxW, (r+1)*boxH);
						g2.drawLine((c+1)*boxW, r*boxH, c*boxW, (r+1)*boxH);
					} 
					
					// draw O
					if (board[r][c] == PO) {
						g2.drawOval(c*boxW, r*boxH, boxW, boxH);
					}
				}
			}
		}
		
		void initGrid() {
			panW = this.getSize().width;
			panH = this.getSize().height;
			boxW = (int) (panW/SIZE + 0.5);
			boxH = (int) (panH/SIZE + 0.5);
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {}
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@Override
		public void mousePressed(MouseEvent arg0) {}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// get row and col
			int x = e.getX()/boxW;
			int y = e.getY()/boxH;
			
			if (gameEnd) {
				init();
				this.repaint();
				return;
			}
			
			if (board[y][x] != 0) return;
			
			if (turnX) {
				board[y][x] = PX;
				turn.setText("O's Turn");
			} else {
				board[y][x] = PO;
				turn.setText("X's Turn");
			}
			checkBoard();
			turnX = !turnX;
			
			wins.setText("X wins: " + xWins + "    O wins: " + oWins + "    Ties: " + ties);
			this.repaint();
		}
		
		void checkBoard() {
			// check sum of rows and cols and diagonals
			// check if there are any empty spaces, if not then there is a tie
			
			boolean filled = true;
			
			// left and right diagonal sums
			int lDiagSum = 0;
			int rDiagSum = 0;
			
			for (int r = 0; r<SIZE; r++) {
				int cSUM = 0;
				int rSUM = 0;
				
				// row and column sums
				for (int c = 0; c<SIZE; c++) {
					cSUM += board[r][c];
					rSUM += board[c][r];
					if (board[r][c] == 0) filled = false;
				}
				
				// diagonal sums
				lDiagSum += board[r][r];
				rDiagSum += board[SIZE-r-1][r];
				
				// x wins
				if (cSUM == SIZE || rSUM == SIZE || lDiagSum == SIZE || rDiagSum == SIZE) {
					gameEnd = true;
					xWins++;
					turn.setText("Game Over, X Wins!");
					return;
				}
				
				// o wins
				if (cSUM == SIZE * -1 || rSUM == SIZE * -1 || lDiagSum == SIZE * -1 || rDiagSum == SIZE * -1) {
					gameEnd = true;
					oWins++;
					turn.setText("Game Over, O Wins!");
					return;
				}
			}	
			
			// tied
			if (filled) {
				gameEnd = true;
				turn.setText("Game Over, Tied");
				ties++;
			}
		}
	}
			
}
