package Graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Drawing {
	
	public static ArrayList<Double> graphPoints = new ArrayList<Double>();
	public static Dimension graphSize;
	public static Point graphLocation;
	
	public static void update(Graphics2D g2) {
		
		g2.setColor(Color.WHITE);
		g2.drawLine(graphLocation.x,graphLocation.y, graphLocation.x+graphSize.width, graphLocation.y);
		
		for (Double point : graphPoints) {
			g2.setColor(Color.BLACK);
			g2.fillOval(graphLocation.x+((graphSize.width/graphPoints.size())*graphPoints.indexOf(point)),graphLocation.y-(int) (point*graphSize.height), 5, 5);
		}
		
		if (graphPoints.size() > 25) {
			graphPoints.remove(0);
		}
	}
	
    public static void drawCircle(Graphics2D g2, int x, int y, int r) {
        x = x - (r / 2);
        y = y - (r / 2);
        g2.fillOval(x, y, r, r);
    }

    public static void drawText(Graphics2D g2, Rectangle rect, String text, Font font) {
        FontMetrics metrics = g2.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.setFont(font);
        g2.drawString(text, x, y);
    }

    public static void drawLine(Graphics2D g2, int x1, int y1, int x2, int y2, int size, Color color) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(java.lang.Math.abs(size)));
        g2.drawLine(x1, y1, x2, y2);
    }
}