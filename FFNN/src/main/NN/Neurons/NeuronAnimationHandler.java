package main.NN.Neurons;

import java.awt.Color;

import main.util.Vector2D;

public class NeuronAnimationHandler {
	
	private Vector2D location;
	private Integer size;
	private Color color;
	
	public NeuronAnimationHandler(Vector2D location, Integer size, Color color) {
		this.location = location;
		this	.size = size;
		this.color = color;
	}

	public Vector2D getLocation() {
		return location;
	}

	public void setLocation(Vector2D location) {
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
