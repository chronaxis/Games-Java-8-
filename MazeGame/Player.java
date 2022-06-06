package MazeGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// player class, basically a cell with some more info
public class Player extends Rectangle {
	private int PANW;
	private int PANH;
	private int GRIDR;
	private int GRIDC;
	
	private Color color;
	// position on grid
	private Pair<Integer, Integer> pos;
	Player(int x, int y, int h, int w, Color color, int r, int c, int PANW, int PANH, int GRIDR, int GRIDC) {
		this.x = x;
		this.y = y;
		this.height = h;
		this.width = w;
		this.color = color;
		pos = new Pair<Integer, Integer>(r, c);
		this.PANW = PANW;
		this.PANH = PANH;
		this.GRIDR = GRIDR;
		this.GRIDC = GRIDC;
	}
	
	// change player position on grid
	void changePos(int r, int c) {
		this.pos = new Pair<Integer, Integer>(r, c);
		this.x = (c-1) * this.PANW/this.GRIDC;
		this.y = (r-1) * this.PANH/this.GRIDR;
	}
	
	// getters
	Color getColor() {
		return this.color;
	}
	
	Pair<Integer, Integer> getPos() {
		return this.pos;
	}
}
