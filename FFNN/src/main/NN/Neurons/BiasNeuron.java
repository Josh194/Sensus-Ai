package main.NN.Neurons;

import java.awt.Color;

import main.NN.Layer;

public class BiasNeuron extends Neuron {

	public BiasNeuron(Double value, Layer layer) {
		super(value, layer);
	}
	
	public String getType() {
		return "BiasNeuron";
	}
	
	public Color getColor() {
		return new Color(255, 153, 248);
	}
}
