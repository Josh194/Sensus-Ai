package main.util;

import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

public class Select {

	public static Shape getObjectUnderCursor(MouseEvent mouseEvent, Shape[] shapes, double minimumDistance) {
		
		Double minDistance = null;
		Shape closestShape = null;
		
		for (Shape shape : shapes) {
			if (shape instanceof Line2D) {
				Vector2D mousePosition = Cast.asVector2D(mouseEvent.getPoint());
				Vector2D P1 = Cast.asVector2D(((Line2D) shape).getP1());
				Vector2D P2 = Cast.asVector2D(((Line2D) shape).getP2());
				
				if (minDistance == null) {
					minDistance = Calculations.distanceToLine(mousePosition, P1, P2);
					closestShape = shape;
				}
				
				if (Calculations.distanceToLine(mousePosition, P1, P2) < minDistance) {
					minDistance = Calculations.distanceToLine(mousePosition, P1, P2);
					closestShape = shape;
				}
			}
		}
		return closestShape;
	}
}
