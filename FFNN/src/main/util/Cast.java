package main.util;

import java.awt.Point;
import java.awt.geom.Point2D;

public class Cast {
	
	public static Vector2D asVector2D(Point point) {
		return new Vector2D(point.getX(), point.getY());
	}
	
	public static Vector2D asVector2D(Point2D point) {
		return new Vector2D(point.getX(), point.getY());
	}
	
}
