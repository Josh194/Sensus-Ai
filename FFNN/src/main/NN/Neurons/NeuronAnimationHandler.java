package main.NN.Neurons;

import main.util.Vector2D;

public class NeuronAnimationHandler {
	
	private Vector2D location;
	private Integer size;
	
	public NeuronAnimationHandler(Vector2D location, Integer size) {
		this.location = location;
		this	.size = size;
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
}
