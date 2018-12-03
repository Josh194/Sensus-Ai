package main.NN.Neurons;

import java.awt.Color;

import main.NN.Layer;

public class InputNeuron extends Neuron {

	public InputNeuron(Double value, Layer layer) {
		super(value, layer);
	}
	
	public String getType() {
		return "InputNeuron";
	}
	
	public Color getColor() {
		return new Color(255, 203, 69);
	}
}
