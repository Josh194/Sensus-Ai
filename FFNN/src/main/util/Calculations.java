package main.util;

public class Calculations {

	public static double distanceToLine(Vector2D point, Vector2D lineStart, Vector2D lineEnd) {
		double slope = (lineStart.y - lineEnd.y) / (lineStart.x - lineEnd.x);
		double oppositeSlope = -1 / slope;

		double x;
		double y;
		
		if (slope == 0.0) {
			x = point.x;
			y = lineStart.y;
		} else {
			x = ((slope * (point.y + (slope * lineStart.x) - lineStart.y)) + point.x) / (Math.pow(slope, 2) + 1);
			y = (oppositeSlope * (x - point.x)) + point.y;
		}
		
		if (x >= lineStart.x && x <= lineEnd.x) {
			return distanceToPoint(point, new Vector2D(x, y));
		} else {
			if (x < lineStart.x) {
				return distanceToPoint(point, lineStart);
			} else {
				return distanceToPoint(point, lineEnd);
			}
		}	
	}
	
	public static double distanceToPoint(Vector2D firstPoint, Vector2D secondPoint) {
		return Math.sqrt(Math.pow(firstPoint.x - secondPoint.x, 2) + Math.pow(firstPoint.y - secondPoint.y, 2));
	}
	
	public static Vector2D getCircleCenter(Vector2D location, double size) {
		double x = location.x + (size / 2);
		double y = location.y + (size / 2);
		
		return new Vector2D(x, y);
	}
	
}
