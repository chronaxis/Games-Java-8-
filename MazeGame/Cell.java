package MazeGame;

import java.awt.*;

// a single square on the grid
// types:
//     'W' = Wall
//     'O' = Open (path)
//     'S' = start position
//     'E' = end position
class Cell extends Rectangle {
	Color color;
	char type;
	Cell(int x, int y, int h, int w, Color color, char type) {
		this.x = x;
		this.y = y;
		this.height = h;
		this.width = w;
		this.color = color;
		this.type = type;
	}
}
