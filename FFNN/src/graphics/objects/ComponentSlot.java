package graphics.objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import graphics.Drawing;
import main.util.components.Component;

public class ComponentSlot extends Object {
	
	private Component component;

	public ComponentSlot(Component component) {
		setShape(new Polygon(new int[] {75, 150, 75, -75, -150, -75}, new int[] {-130, 0, 130, 130, 0, -130}, 6));
		
		setComponent(component);
	}
	
	public void draw(Graphics2D g2) {
		g2.setColor(new Color(0, 55, 75));
		
		g2.fill(getShape());
		
		for (int i = 0; i < component.getInputs().length; i++) {
			Point location = component.getInputs()[i].getLocation();
			
			g2.setColor(new Color(16, 127, 65));
			
			g2.fillOval(location.x, location.y, 25, 25);
			
			g2.setColor(new Color(200, 200, 200));
			
			g2.fillOval(location.x + 5, location.y + 5, 15, 15);
		}
		
		g2.setColor(new Color(200, 200, 200));
		
		for (int i = 0; i < component.getOutputs().length; i++) {
			Point location = component.getOutputs()[i].getLocation();
			
			g2.setColor(new Color(16, 127, 65));
			
			g2.fillOval(location.x, location.y, 25, 25);
			
			g2.setColor(new Color(200, 200, 200));
			
			g2.fillOval(location.x + 5, location.y + 5, 15, 15);
		}
		
		Drawing.drawText(g2, new Rectangle(getLocation().x - 50, getLocation().y - 25, 100, 50), component.getName(), new Font("Monaco", 1, 20));
	}
	
	@SuppressWarnings("rawtypes")
	public void drawConnections(Graphics2D g2) {
		g2.setColor(new Color(200, 200, 200));
		
		g2.setStroke(new BasicStroke(7));
		
		for (OutputSocket outputSocket : component.getOutputs()) {
			if (outputSocket.hasConnection()) {
				Point location = outputSocket.getLocation();
				Point targetLocation = outputSocket.getConnection().getLocation();
				
				g2.drawLine(location.x + 13, location.y + 13, targetLocation.x + 13, targetLocation.y + 13);
			}
		}
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}
	
	public void setLocation(Point location) {
		for (int i = 0; i < shape.npoints; i++) {
			shape.xpoints[i] += location.x - this.location.x;
			shape.ypoints[i] += location.y - this.location.y;
		}
		
		this.location = location;
		
		for (int i = 0; i < component.getInputs().length; i++) {
			int x = (int) (75 * ((double) (i + 1) / (component.getInputs().length + 1)));
			int y = (int) (-130 * ((double) (i + 1) / (component.getInputs().length + 1)));
			
			component.getInputs()[i].setLocation(new Point(getLocation().x - 150 + x - 13, getLocation().y - y - 13));
		}
		
		for (int i = 0; i < component.getOutputs().length; i++) {
			int x = (int) (75 * ((double) (i + 1) / (component.getOutputs().length + 1)));
			int y = (int) (-130 * ((double) (i + 1) / (component.getOutputs().length + 1)));
			
			component.getOutputs()[i].setLocation(new Point(getLocation().x + 150 - x - 13, getLocation().y + y - 13));
		}
	}

}