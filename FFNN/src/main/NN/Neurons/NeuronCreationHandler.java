package main.NN.Neurons;

import main.NN.Layer;

public class NeuronCreationHandler {

	public static void createNeuron(String name, double value, Layer layer) {
		switch (name) {
		case "HiddenNeuron":
			layer.addNeuron(new HiddenNeuron(value, layer));
			break;
		case "BiasNeuron":
			layer.addNeuron(new  BiasNeuron(value, layer));
			break;
		case "InputNeuron":
			layer.addNeuron(new InputNeuron(value, layer));
			break;
		case "OutputNeuron":
			layer.addNeuron(new OutputNeuron(value, layer));
			break;
		}
	}

}
