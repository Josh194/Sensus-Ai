package main.NN;

import java.awt.Color;

import main.NN.Neurons.Neuron;

public class Connection {

	Double Value;
	public Neuron N1;
	public Neuron N2;
	public Double RandomAnimOffset = Math.random();

	public Connection(Neuron n1, Neuron n2) {
		Value = 0.1 * (Math.random() - 0.5);
		N1 = n1;
		N2 = n2;
	}

	public void setValue(Double value) {
		Value = value;
	}

	public Double getValue() {
		return Value;
	}

	public Color getColor() {
		if (Value >= 0) {
			return (new Color(225, 129, 45));
		} else {
			return (new Color(0, 100, 157));
		}
	}
}
