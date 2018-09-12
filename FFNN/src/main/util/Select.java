package main.util;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Select {

	public static Shape getObjectUnderCursor(Vector2D mouseLocation, ArrayList<Shape> shapes) {

		Double minDistance = null;
		Shape closestShape = null;

		for (Shape shape : shapes) {
			if (shape instanceof Ellipse2D) {
				if (shape.contains(mouseLocation.toPoint())) {
					return shape;
				}
			} else if (shape instanceof Line2D) {
				Vector2D P1 = Cast.asVector2D(((Line2D) shape).getP1());
				Vector2D P2 = Cast.asVector2D(((Line2D) shape).getP2());

				if (minDistance == null) {
					minDistance = Calculations.distanceToLine(mouseLocation, P1, P2);
					closestShape = shape;
				}

				if (Calculations.distanceToLine(mouseLocation, P1, P2) < minDistance) {
					minDistance = Calculations.distanceToLine(mouseLocation, P1, P2);
					closestShape = shape;
				}
			}
		}
		return closestShape;
	}

	public static boolean isShapeInRange(Vector2D mouseLocation, Shape shape, double minimumDistance) {
		if (shape instanceof Line2D) {
			Vector2D P1 = Cast.asVector2D(((Line2D) shape).getP1());
			Vector2D P2 = Cast.asVector2D(((Line2D) shape).getP2());

			if (Calculations.distanceToLine(mouseLocation, P1, P2) <= minimumDistance) {
				return true;
			} else {
				return false;
			}
		} else if (shape instanceof Ellipse2D) {
			if (shape.contains(mouseLocation.toPoint())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
