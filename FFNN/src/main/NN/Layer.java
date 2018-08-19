package main.NN;

import java.util.ArrayList;

import main.NN.Neurons.Neuron;

public class Layer {

	public ArrayList<Neuron> neurons = new ArrayList<Neuron>();

	public Layer() {

	}

	public void addNeuron(Neuron neuron) {
		neurons.add(neuron);
	}

}