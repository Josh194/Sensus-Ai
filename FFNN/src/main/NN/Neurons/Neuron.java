package main.NN.Neurons;

import java.awt.Color;

import main.NN.Layer;

public class Neuron {

	private Double value;
	private Double error = 0.0;
	private Double minRange;
	private Double maxRange;
	private Layer layer;
	
	public NeuronAnimationHandler animationHandler = new NeuronAnimationHandler(null, null);

	public Neuron(Double value, Layer layer) {
		this.value = value;
		this.layer = layer;
	}

	public void update() {
		
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getError() {
		return error;
	}

	public void setError(Double error) {
		this.error = error;
	}

	public Double getMinRange() {
		return minRange;
	}

	public void setMinRange(Double minRange) {
		this.minRange = minRange;
	}

	public Double getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(Double maxRange) {
		this.maxRange = maxRange;
	}

	public String getType() {
		return "None";
	}
	
	public Color getColor() {
		return new Color(0, 0, 0);
	}

	public Layer getLayer() {
		return layer;
	}

	public void setLayer(Layer layer) {
		this.layer = layer;
	}

}