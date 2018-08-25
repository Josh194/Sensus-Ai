package main.util;

import java.util.ArrayList;

public class PolyLineSimplification {

	private double epsilon;
	private Vector2D startPoint = null;
	private Vector2D endPoint = null;
	private ArrayList<Vector2D> out = null;

	public PolyLineSimplification(double epsilon) {
		if (epsilon <= 0) {
			throw new IllegalArgumentException("Epsilon nust be > 0");
		}

		this.epsilon = epsilon;
	}

	public ArrayList<Vector2D> filter(ArrayList<Vector2D> data) {

		startPoint = null;
		endPoint = null;
		out = null;

		return simplify(data, 0, data.size() - 1);
	}

	protected double distanceToLine(Vector2D point, Vector2D lineStart, Vector2D lineEnd) {

		double slope = (lineStart.y - lineEnd.y) / (lineStart.x - lineEnd.x);
		double oppositeSlope = -1 / slope;

		double y = (slope * (lineStart.x - lineEnd.x)) + lineStart.y;
		double x = ((y - point.y) / oppositeSlope) + point.x;

		Vector2D closestPoint = new Vector2D(x, y);

		return Math.sqrt(Math.pow(closestPoint.x - point.x, 2) + Math.pow(closestPoint.y - point.y, 2));
	}

	protected ArrayList<Vector2D> simplify(ArrayList<Vector2D> points, int startIndex, int endIndex) {

		double maxDistance = 0.0;
		Vector2D farthestPoint = null;

		if (out != null) {
			if (startIndex == out.size() - 1) {
				return out;
			} else if (startIndex == endIndex - 1) {
				startPoint = endPoint;
				endPoint = out.get(out.size() - 1);
			}
		}

		if (out == null) {
			out = points;
			startPoint = out.get(startIndex);
			endPoint = out.get(endIndex);
		}

		for (int i = out.indexOf(startPoint) + 1; i < out.indexOf(endPoint); i++) {
			if (distanceToLine(out.get(i), startPoint, endPoint) > maxDistance) {
				maxDistance = distanceToLine(out.get(i), startPoint, endPoint);
				farthestPoint = out.get(i);
			}
		}

		if (maxDistance >= epsilon) {

			endPoint = farthestPoint;

			return simplify(out, out.indexOf(startPoint), out.indexOf(endPoint));

		} else {

			ArrayList<Vector2D> removed = new ArrayList<Vector2D>();

			for (int i = out.indexOf(startPoint) + 1; i < out.indexOf(endPoint); i++) {
				removed.add(out.get(i));
			}

			startPoint = endPoint;
			endPoint = out.get(out.size() - 1);

			out.removeAll(removed);

			return simplify(out, out.indexOf(startPoint), out.indexOf(endPoint));

		}
	}

	public double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(double epsilon) {
		if (epsilon <= 0) {
			throw new IllegalArgumentException("Epsilon nust be > 0");
		}
		this.epsilon = epsilon;
	}

}