package main.NN.Neurons;

import java.awt.Color;

import main.Run;
import main.NN.AFHandler;
import main.NN.Layer;

public class Neuron {

	private Double value;
	private Double error = 0.0;
	private Double minRange;
	private Double maxRange;
	private int type; // 1: FF, 2: Input, 3: Output, 4: Bias, 5: Recursive
	private Layer layer;
	
	public NeuronAnimationHandler animationHandler = new NeuronAnimationHandler(null, null, null);

	public Neuron(Double value, Layer layer, int type, Color color) {
		this.value = value;
		this.layer = layer;
		this.type = type;

		animationHandler.setColor(color);
	}

	public void update() {
		if (type == 1) {
			value = AFHandler.activationFunction(Run.AF, value, minRange, maxRange);
		} else if (type == 2) {
			value = AFHandler.activationFunction(Run.AF, value, minRange, maxRange);
		} else if (type == 3) {
			value = AFHandler.activationFunction(Run.AF, value, minRange, maxRange);
		}
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Layer getLayer() {
		return layer;
	}

	public void setLayer(Layer layer) {
		this.layer = layer;
	}

}