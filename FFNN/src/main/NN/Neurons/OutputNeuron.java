package main.NN.Neurons;

import java.awt.Color;

import main.Run;
import main.NN.AFHandler;
import main.NN.Layer;

public class OutputNeuron extends Neuron {

	public OutputNeuron(Double value, Layer layer) {
		super(value, layer);
	}
	
	public void update() {
		setValue(AFHandler.activationFunction(Run.AF, getValue(), getMinRange(), getMaxRange()));
	}
	
	public String getType() {
		return "OutputNeuron";
	}
	
	public Color getColor() {
		return new Color(255, 98, 36);
	}
}
