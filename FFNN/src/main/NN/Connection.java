package main.NN;

import java.awt.Color;

import main.NN.Neurons.Neuron;

public class Connection {

	private Double value;
	private Neuron firstNeuron;
	private Neuron secondNeuron;
	private Double randomAnimOffset = Math.random();

	public Connection(Neuron firstNeuron, Neuron secondNeuron) {
		setValue(0.1 * (Math.random() - 0.5));
		setFirstNeuron(firstNeuron);
		setSecondNeuron(secondNeuron);
	}
	
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	public Neuron getFirstNeuron() {
		return firstNeuron;
	}

	public void setFirstNeuron(Neuron firstNeuron) {
		this.firstNeuron = firstNeuron;
	}
	
	public Neuron getSecondNeuron() {
		return secondNeuron;
	}

	public void setSecondNeuron(Neuron secondNeuron) {
		this.secondNeuron = secondNeuron;
	}
	
	public Double getRandomAnimOffset() {
		return randomAnimOffset;
	}

	public void setRandomAnimOffset(Double randomAnimOffset) {
		this.randomAnimOffset = randomAnimOffset;
	}

	public Color getColor() {
		if (value >= 0) {
			return (new Color(225, 129, 45));
		} else {
			return (new Color(0, 100, 157));
		}
	}
}
