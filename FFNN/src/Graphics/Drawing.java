package Graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import main.Run;

public class Drawing {

	public static ArrayList<Double> graphPoints = new ArrayList<Double>();
	public static ArrayList<Double> outputPoints = new ArrayList<Double>();
	public static Dimension graphSize;
	public static Point graphLocation;
	public static Dimension outputSize;
	public static Point outputLocation;
	public static Timer animation = new Timer();
	public static Double animationX = 0d;
	public static boolean Added = false;

	public static void startAnimation() {
		animation.schedule(new updateAnimation(), 10l);
	}

	static class updateAnimation extends TimerTask {
		public void run() {
			animationX = animationX + 0.02;
			if (animationX < 1 + (Run.neuralNetwork.layers.size() * 2)) {
				startAnimation();
			} else {
				animation.cancel();
			}
		}
	}

	public static void update(Graphics2D g2) {

		for (Double point : graphPoints) {
			g2.setColor(Color.BLACK);
			g2.fillOval(graphLocation.x + ((graphSize.width / graphPoints.size()) * graphPoints.indexOf(point)),
					graphLocation.y - (int) (Math.abs(point) * (graphLocation.y / Run.neuralNetwork.getMaxRange()))
							+ 15,
					5, 5);
		}

		if (graphPoints.size() > 30) {
			graphPoints.remove(0);
		}
	}

	public static void updateOutput(Graphics2D g2) {

		for (Double point : outputPoints) {
			g2.setColor(Color.BLACK);
			g2.fillOval(outputLocation.x + ((outputSize.width / outputPoints.size()) * outputPoints.indexOf(point)),
					(int) ((outputLocation.y
							* (point + Math.abs(Run.neuralNetwork.getMinRange() * Run.neuralNetwork.getMaxRange()) / 2))
							/ Math.abs(Run.neuralNetwork.getMinRange() + Run.neuralNetwork.getMaxRange())) + 40,
					5, 5);
		}

		if (outputPoints.size() > 30) {
			outputPoints.remove(0);
		}
	}

	public static void drawText(Graphics2D g2, Rectangle rect, String text, Font font) {
		FontMetrics metrics = g2.getFontMetrics(font);
		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
		int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		g2.setFont(font);
		g2.drawString(text, x, y);
	}

	public static double animationPercent(double timeOffset) {
		double temp = 0.0;

		temp = 1 - (1 / ((0.5 * (-2 + Math.sqrt(12))) + (Math.min(Math.max(animationX + timeOffset, 0), 2))))
				+ (1 / ((0.5 * (-2 + Math.sqrt(12))) + 2));
		return temp;
	}

	public static void drawCircle(Graphics2D g2, int x, int y, int r, double timeOffset) {
		if (Added) {

		} else {
			Run.shapes.add(new Ellipse2D.Double(x - (r / 2), y - (r / 2), r, r));
		}

		r = (int) (r * animationPercent(timeOffset));
		x = x - (r / 2);
		y = y - (r / 2);

		g2.fill(new Ellipse2D.Double(x, y, r, r));
	}

	public static void drawLine(Graphics2D g2, int x1, int y1, int x2, int y2, int size, double timeOffset,
			Color color) {
		g2.setColor(color);
		g2.setStroke(new BasicStroke(java.lang.Math.abs(size)));
		g2.drawLine(x1, y1, (int) (((x2 - x1) * animationPercent(timeOffset)) + x1),
				(int) (((y2 - y1) * animationPercent(timeOffset)) + y1));
		if (Added) {

		} else {
			Run.shapes.add(new Line2D.Double(x1, y1, x2, y2));
		}
	}
}