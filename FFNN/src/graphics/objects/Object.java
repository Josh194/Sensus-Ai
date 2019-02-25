package graphics.objects;

import java.awt.Point;
import java.awt.Polygon;

public class Object {
	
	protected Point location = new Point(0, 0);
	protected Polygon shape;
	
	public Point getLocation() {
		return location;
	}
	
	public void setLocation(Point location) {
		for (int i = 0; i < shape.npoints; i++) {
			shape.xpoints[i] += location.x - this.location.x;
			shape.ypoints[i] += location.y - this.location.y;
		}
		
		this.location = location;
	}

	public Polygon getShape() {
		return shape;
	}

	public void setShape(Polygon shape) {
		this.shape = shape;
	}
	
}
