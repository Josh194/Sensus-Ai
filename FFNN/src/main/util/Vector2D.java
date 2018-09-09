package main.util;

import java.awt.Point;

public class Vector2D {

	public double x;
	public double y;

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point toPoint(Vector2D point) {
		return new Point((int) point.x, (int) point.y);
	}

}
