package graphics;

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
import main.util.PolyLineSimplification;
import main.util.Vector2D;

public class Drawing {

	public static ArrayList<Vector2D> graphPoints = new ArrayList<Vector2D>();
	public static ArrayList<Double> outputPoints = new ArrayList<Double>();
	public static PolyLineSimplification simplify = new PolyLineSimplification(0.0001);
	public static int pointNumber = 0;
	public static Dimension graphSize;
	public static Point graphLocation;
	public static Dimension outputSize;
	public static Point outputLocation;
	public static Timer animation = new Timer();
	public static Double animationX = 0d;
	public static boolean Added = false;

	public static void startAnimation() {
		animation.schedule(new UpdateAnimation(), 10l);
	}

	static class UpdateAnimation extends TimerTask {
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
		try {
			for (Vector2D point : graphPoints) {
				g2.setColor(Color.BLACK);
				drawOutput(g2, (int) ((graphSize.width * point.x) / pointNumber) + 20, (graphLocation.y + 20)
						- (int) ((Math.abs(point.y) * (graphLocation.y / (Run.neuralNetwork.maxError-Run.neuralNetwork.minError)))
							- ((Run.neuralNetwork.minError) * graphLocation.y) / (Run.neuralNetwork.maxError - Run.neuralNetwork.minError)), 5);
			}

			for (int i = 1; i < graphPoints.size(); i++) {
				g2.drawLine((int) (((graphSize.width * graphPoints.get(i - 1).x) / pointNumber) + 20),
						(graphLocation.y + 20)
						- (int) ((Math.abs(graphPoints.get(i - 1).y) * (graphLocation.y / (Run.neuralNetwork.maxError-Run.neuralNetwork.minError)))
							- ((Run.neuralNetwork.minError) * graphLocation.y) / (Run.neuralNetwork.maxError - Run.neuralNetwork.minError)),
						(int) (((graphSize.width * graphPoints.get(i).x) / pointNumber) + 20),
						(graphLocation.y + 20)
						- (int) ((Math.abs(graphPoints.get(i).y) * (graphLocation.y / (Run.neuralNetwork.maxError-Run.neuralNetwork.minError)))
							- ((Run.neuralNetwork.minError) * graphLocation.y) / (Run.neuralNetwork.maxError - Run.neuralNetwork.minError)));
			}

			if (graphPoints.size() > 100) {
				if (graphPoints.size() > 200) {
					graphPoints.remove(1);
				}
				graphPoints = simplify.filter(graphPoints);
			}
		} catch (NullPointerException e) {
			
		}
	}

	public static void updateOutput(Graphics2D g2) {
		for (Double point : outputPoints) {
			g2.setColor(Color.BLACK);
			drawOutput(g2, outputLocation.x + ((outputSize.width / outputPoints.size()) * outputPoints.indexOf(point)),
					(outputLocation.y + 20)
					- (int) ((point * (outputLocation.y / (Run.neuralNetwork.maxOutput-Run.neuralNetwork.minOutput)))
						- ((Run.neuralNetwork.minOutput) * outputLocation.y) / (Run.neuralNetwork.maxOutput-Run.neuralNetwork.minOutput)),
					5);
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

	public static void drawOutput(Graphics2D g2, int x, int y, int r) {
		// Combine with drawCircle()!

		x = x - (r / 2);
		y = y - (r / 2);

		g2.fill(new Ellipse2D.Double(x, y, r, r));
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
		g2.setStroke(new BasicStroke(Math.abs(size)));
		g2.drawLine(x1, y1, (int) (((x2 - x1) * animationPercent(timeOffset)) + x1),
				(int) (((y2 - y1) * animationPercent(timeOffset)) + y1));
		if (!Added) {
			Run.shapes.add(new Line2D.Double(x1, y1, x2, y2));
		}
	}
}