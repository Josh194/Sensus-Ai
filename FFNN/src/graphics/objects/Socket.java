package graphics.objects;

import java.awt.Point;

public class Socket<T> {
	
	private Point location = new Point(0, 0);
	private T value;
	
	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
}
