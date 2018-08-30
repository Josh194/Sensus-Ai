package main.NN.Neurons;

import java.awt.Color;
import java.awt.Point;

public class NeuronAnimationHandler {
	
	private Point location;
	private Integer size;
	private Color color;
	
	public NeuronAnimationHandler(Point location, Integer size, Color color) {
		this.location =location;
		this	.size = size;
		this.color = color;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
