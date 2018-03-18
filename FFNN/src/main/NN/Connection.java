package main.NN;

import java.awt.Color;

public class Connection {

	Double Value;
	public Neuron N1;
	public Neuron N2;
	Color Color;

	public Connection(Double value, Neuron n1, Neuron n2, Color color) {
		Value = value;
		N1 = n1;
		N2 = n2;
		Color = color;
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
