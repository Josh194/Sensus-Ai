package main.NN;

import java.util.ArrayList;

public class Layer {

    public ArrayList < Neuron > neurons = new ArrayList < Neuron > ();

    public Layer() {

    }

    public void addNeuron(Neuron neuron) {
        neurons.add(neuron);
    }

}