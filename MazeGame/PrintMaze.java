package MazeGame;

// just for printing out the maze
// zein wanted this
// walls = 0
// path = 1
// start = 2
// end = 3
public class PrintMaze {
	PrintMaze(Cell[][] grid, int GRIDR, int GRIDC) {
		for (int i = 1; i <= GRIDR; i++) {
			for (int j = 1; j <= GRIDC; j++) {
				if (grid[i][j].type == 'W') {
					System.out.print(0 + " ");
				}
				
				if (grid[i][j].type == 'O') {
					System.out.print(1 + " ");
				}
				
				if (grid[i][j].type == 'S') {
					System.out.print(2 + " ");
				}
				
				if (grid[i][j].type == 'E') {
					System.out.print(3 + " ");
				}
			}
			
			System.out.println();
		}
	}
}
