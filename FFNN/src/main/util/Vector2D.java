package main.util;

import java.awt.Point;

public class Vector2D {

	public double x;
	public double y;

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point toPoint() {
		return new Point((int) x, (int) y);
	}
	
	public boolean isEqualTo(Vector2D point) {
		if (point.x == x && point.y == y) {
			return true;
		} else {
			return false;
		}
	}

}
