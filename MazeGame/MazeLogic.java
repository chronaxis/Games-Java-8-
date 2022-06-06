package MazeGame;

import java.awt.Color;
import java.util.Stack;

// all the logic for the maze generation
public class MazeLogic {
	// variables from the main game
	// will be initialized with constructors for ease of use
	// using Game.PANW all the time would be rather annoying
	private int PANW;
	private int PANH;
	private int GRIDR;
	private int GRIDC;
	
	private Cell grid[][];
	private boolean vis[][];
	private Stack<Pair<Integer, Integer>> path = new Stack<Pair<Integer, Integer>>();
	
	// return this for player start
	private Pair<Integer, Integer> start;
	
	// constructor, also initializes grid 
	MazeLogic(int PANH, int PANW, int GRIDR, int GRIDC) {
		this.PANH = PANH;
		this.PANW = PANW;
		this.GRIDR = GRIDR;
		this.GRIDC = GRIDC;
		grid = new Cell[GRIDR+1][GRIDC+1];
		vis = new boolean[GRIDR+1][GRIDC+1];
		
		// reset the maze and fill it
		// set everything to default (wall)
		for (int i = 1; i<=GRIDR; i++) {
			for (int j = 1; j<=GRIDC; j++) {
				Cell temp = new Cell(
						(j-1) * PANW/GRIDC, // x
						(i-1) * PANH/GRIDR, // y
						PANH/GRIDR, 		// height
						PANW/GRIDC, 		// width
						Color.BLACK, 		// color
						'W'); 				// type
				grid[i][j] = temp;
			}		
		}
		
		// get random starting point for maze generation
		start = new Pair<Integer, Integer>((int) (Math.random() * GRIDR) + 1, (int) (Math.random() * GRIDC) + 1);
		
		// clear stack and generate maze with the start as the first cell
		path.clear();
		genMaze(start);
		
		// make start and end
		// set start
		grid[start.first()][start.second()].type = 'S';
		grid[start.first()][start.second()].color = Color.GREEN;
		
		// get random point for end
		int r = (int) (Math.random() * GRIDR) + 1;
		int c = (int) (Math.random() * GRIDC) + 1;
		while (grid[r][c].type != 'O') {
			r = (int) (Math.random() * GRIDR) + 1;
			c = (int) (Math.random() * GRIDC) + 1;
		}
		
		// set end
		grid[r][c].type = 'E';
		grid[r][c].color = Color.RED;
	}
	
	// the backtracking algorithm:
	// get current cell position
	// if it has not been visited before:
	//     add it to the path stack
	// set current cell to white color, make its type 'O' for open
	// get neighbours of the current cell
	// if there are not neighbours
	//	   pop the path stack
	//	   if the path is empty
	//         return (maze should be done generating)
	//     get the previous cell of the path
	//	   recurse with that
	// if there are neighbours of the cell
	//     recurse with the neighbour
	// ends when the stack is empty (reached the beginning after generating all the cells)
	void genMaze(Pair<Integer, Integer> cell) {
		int r = cell.first();
		int c = cell.second();
		if (!vis[r][c]) {
			vis[r][c] = true;
			path.push(cell);
		}
		
		grid[r][c].color = Color.WHITE;
		grid[r][c].type = 'O';

		Pair<Integer, Integer> neighbour = getNeighbour(cell);
		if (neighbour == null) {
			path.pop();
			if (path.empty()) return;
			genMaze(path.peek());
		} else {
			genMaze(neighbour);
		}
		
	}
	
	// 4 directions for neighbours: up, right, down left
	// randomize the order you check the direction to get random path
	// the direction is valid if
	// 	   get the next cell in that direction (not out of bounds)
	//     the 5 squares around the new cell (in the direction the neighbour is facing) is empty
	//	   this is because a valid path does not touch another path (even diagonally) except from where it came from
	//	   therefore everything to the front and sides should not be paths
	// return the neighbour (null if none)
	Pair<Integer, Integer> getNeighbour(Pair<Integer, Integer> cell) {
		int directions[] = {0, 1, 2, 3};
		
		// up down left right (r, c)
		// add these to row and column to get neighbour cell depending on direction
		int udrl[][] = {{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
		
		// similar to udrl, but it checks the five cells around it instead
		// clockwise order (r, c)
		int fiveCheck[][][] = {
				{{0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}}, // up
				{{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}}, // down
				{{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}}, // right
				{{1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}} // left
		};
		
		for (int i = 0; i<4; i++) {
			int temp = directions[i];
			int swap = (int) (Math.random()*4);
			directions[i] = directions[swap];
			directions[swap] = temp;
		} 
		
		// boolean isNeighbour must remain true for all tests
		// if one test fails then it is not possible
		for (int i = 0; i<4; i++) {
			int r = cell.first() + udrl[directions[i]][0];
			int c = cell.second() + udrl[directions[i]][1];
			boolean isNeighbour = true;
			
			// check if out of bounds
			if (r < 1 || r > GRIDR || c < 1 || c> GRIDC) continue;
			
			// check 5 squares around
			for (int j = 0; j<5; j++) {
				int cr = r + fiveCheck[directions[i]][j][0];
				int cc = c + fiveCheck[directions[i]][j][1];
				
				// check if out of bounds
				if (cr < 1 || cr > GRIDR || cc < 1 || cc > GRIDC) continue;
				
				// if there is an path in one of the five squares, invalid direction    

				if (grid[cr][cc].type != 'W') { 
					isNeighbour = false;
					break;
				}
			}
			
			// all tests pass
			if (isNeighbour) {
				return new Pair<Integer, Integer>(r, c);
			}
 		}
		
		// all directions fail
		return null;
	}
	
	// getters
	Cell[][] getMaze() {
		return this.grid;
	}
	
	Pair<Integer, Integer> getStart() {
		return this.start;
	}

}
