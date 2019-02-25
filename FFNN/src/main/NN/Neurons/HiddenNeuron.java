package main.NN.Neurons;

import java.awt.Color;

import main.Run;
import main.NN.AFHandler;
import main.NN.Layer;

public class HiddenNeuron extends Neuron {

	public HiddenNeuron(Double value, Layer layer) {
		super(value, layer);
	}
	
	public void update() {
		setValue(AFHandler.activationFunction(Run.AF, getValue(), getMinRange(), getMaxRange()));
	}
	
	public String getType() {
		return "HiddenNeuron";
	}
	
	public Color getColor() {
		return new Color(81, 204, 65);
	}
}
