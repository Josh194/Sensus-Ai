package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import main.NN.Connection;
import main.NN.Layer;
import main.NN.Neuron;

public class NeuralNetwork {

    public ArrayList < Double > Input = new ArrayList <Double> ();
    public ArrayList < Layer > layers = new ArrayList <Layer> ();
    public ArrayList < Connection > connections = new ArrayList <Connection> ();
    Color InputColor = new Color(255, 203, 69);
    Color HiddenColor = new Color(81, 204, 65);
    Color OutputColor = new Color(255, 98, 36);
    Color BiasColor = new Color(255, 153, 248);
    Color color = new Color(0, 0, 0);
    Double TargetOutput;
    Double Error;
    Double TeachingRate;
    Double MinRange = -2.0;
    Double MaxRange = 2.0;

    public NeuralNetwork(int[] NeuralNetworkComposition, int[] NeuralNetworkBiasComposition, Double teachingRate) {
        TeachingRate = teachingRate;
        for (int i = 0; i < NeuralNetworkComposition.length; i++) {
            layers.add(new Layer());
        }

        for (Layer layer: layers) {
            if (layers.indexOf(layer) == 0) {
                for (int i = 0; i < NeuralNetworkComposition[layers.indexOf(layer)]; i++) {
                    layer.addNeuron(new Neuron(0d, layer, 1, InputColor));
                }
            } else if (layers.indexOf(layer) == (layers.size() - 1)) {
                for (int i = 0; i < NeuralNetworkComposition[layers.indexOf(layer)]; i++) {
                    layer.addNeuron(new Neuron(0d, layer, 1, OutputColor));
                    for (Neuron neuron: layers.get(layers.indexOf(layer) - 1).neurons) {
                        connections.add(new Connection(1d, neuron, layer.neurons.get(i), new Color(0, 0, 0)));
                    }
                }
            } else {
                for (int i = 0; i < NeuralNetworkComposition[layers.indexOf(layer)]; i++) {
                		if (i==NeuralNetworkComposition[layers.indexOf(layer)]-1) {
                			if(NeuralNetworkBiasComposition[layers.indexOf(layer)]==1) {
                				layer.addNeuron(new Neuron(1d, layer, 2, BiasColor));
                			} else {
                				layer.addNeuron(new Neuron(0d, layer, 1, HiddenColor));
                			}
                		} else {
                			layer.addNeuron(new Neuron(0d, layer, 1, HiddenColor));
                		}
                    for (Neuron neuron: layers.get(layers.indexOf(layer) - 1).neurons) {
                    		if (layer.neurons.get(i).Type==1) {
                    			connections.add(new Connection(1d, neuron, layer.neurons.get(i), new Color(0, 0, 0)));
                    		}
                    }
                }
            }
        }

        for (Layer layer: layers) {
            for (Neuron neuron: layer.neurons) {
                neuron.setMinRange(MinRange);
                neuron.setMaxRange(MaxRange);
            }
        }
    }

    public Double sigmoidDerivative(Double input) {
        return (((MaxRange - MinRange) * Math.pow(2.71828, input)) / Math.pow((Math.pow(2.71828, input) + 1), 2));
    }
    
    public void setInput(ArrayList < Double > input) {
        Input = input;
        TargetOutput = Input.get(Input.size() - 1);
        for (Neuron neuron: layers.get(0).neurons) {
            neuron.setValue(input.get(layers.get(0).neurons.indexOf(neuron)));
        }
    }

    public void feedForward() {
        for (Connection connection: connections) {
            connection.N2.setValue((connection.N2.getValue() + connection.N1.getValue() * connection.getValue()));
        }

        for (Layer layer: layers) {
            for (Neuron neuron: layer.neurons) {
                if (neuron.getColor() == InputColor) {} else {
                    neuron.update();
                }
            }
        }

        for (Neuron neuron: layers.get(layers.size() - 1).neurons) {
            Error = TargetOutput - neuron.getValue();
            neuron.setError(Error);
        }
        System.out.println(Error);
    }

    public void feedBackward() {
        Collections.reverse(connections);
        for (Connection connection: connections) {
            connection.N1.setError((connection.N1.getError() + connection.N2.getError() * connection.getValue()));
        }
        Collections.reverse(connections);
        for (Connection connection: connections) {
            connection.setValue(connection.getValue() + (TeachingRate * connection.N2.getError() * sigmoidDerivative(connection.N2.getValue()) * connection.N1.getValue()));
        }
    }
}