package Graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Drawing {
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