package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import Graphics.Drawing;
import main.NN.AFHandler;
import main.NN.Connection;
import main.NN.Layer;
import main.NN.Neurons.Neuron;
import main.util.Vector2D;

public class NeuralNetwork {

	public ArrayList<Double> Input = new ArrayList<Double>();
	public ArrayList<Double> TargetOutput = new ArrayList<Double>();
	public ArrayList<Layer> layers = new ArrayList<Layer>();
	public ArrayList<Connection> connections = new ArrayList<Connection>();
	public Double TeachingRate;
	Color InputColor = new Color(255, 203, 69);
	Color HiddenColor = new Color(81, 204, 65);
	Color OutputColor = new Color(255, 98, 36);
	Color BiasColor = new Color(255, 153, 248);
	Double Error;
	public Double maxError;
	public Double minError;
	Double Output;
	public Double maxOutput;
	public Double minOutput;
	private Double MinRange = 0d;
	private Double MaxRange = 1d;
	private static int NX;
	private static int NY;
	private static int PointX = 0;

	public NeuralNetwork(int[] NeuralNetworkComposition, int[] NeuralNetworkBiasComposition, Double teachingRate) {
		connections.clear();
		layers.clear();
		TeachingRate = teachingRate;
		for (int i = 0; i < NeuralNetworkComposition.length; i++) {
			layers.add(new Layer());
		}

		for (Layer layer : layers) {
			if (layers.indexOf(layer) == 0) {
				for (int i = 0; i < NeuralNetworkComposition[layers.indexOf(layer)]; i++) {
					if (i == NeuralNetworkComposition[layers.indexOf(layer)] - 1) {
						if (NeuralNetworkBiasComposition[layers.indexOf(layer)] == 1) {
							layer.addNeuron(new Neuron(0d, layer, 2, InputColor));
							layer.addNeuron(new Neuron(1d, layer, 4, BiasColor));
						} else {
							layer.addNeuron(new Neuron(0d, layer, 2, InputColor));
						}
					} else {
						layer.addNeuron(new Neuron(0d, layer, 2, InputColor));
					}
				}
			} else if (layers.indexOf(layer) == (layers.size() - 1)) {
				for (int i = 0; i < NeuralNetworkComposition[layers.indexOf(layer)]; i++) {
					layer.addNeuron(new Neuron(0d, layer, 3, OutputColor));
					for (Neuron neuron : layers.get(layers.indexOf(layer) - 1).neurons) {
						connections.add(new Connection(1d, neuron, layer.neurons.get(i), new Color(0, 0, 0)));
					}
				}
			} else {
				for (int i = 0; i < NeuralNetworkComposition[layers.indexOf(layer)]; i++) {
					if (i == NeuralNetworkComposition[layers.indexOf(layer)] - 1) {
						if (NeuralNetworkBiasComposition[layers.indexOf(layer)] == 1) {
							layer.addNeuron(new Neuron(0d, layer, 1, HiddenColor));
							layer.addNeuron(new Neuron(1d, layer, 4, BiasColor));
						} else {
							layer.addNeuron(new Neuron(0d, layer, 1, HiddenColor));
						}
					} else {
						layer.addNeuron(new Neuron(0d, layer, 1, HiddenColor));
					}
					for (Neuron neuron : layers.get(layers.indexOf(layer) - 1).neurons) {
						if (layer.neurons.get(i).getType() == 1) {
							connections.add(new Connection(1d, neuron, layer.neurons.get(i), new Color(0, 0, 0)));
						}
					}
				}
			}

			for (Neuron neuron : layer.neurons) {
				NX = Run.CANVAS_WIDTH / (layers.size() + 1) * (layers.indexOf(layer) + 1);
				NY = Run.CANVAS_HEIGHT / (layer.neurons.size() + 1) * (layer.neurons.indexOf(neuron) + 1);
				
				neuron.animationHandler.setLocation(new Vector2D(NX, NY));
				
				if (neuron.getLayer().neurons.size() > 8) {
					neuron.animationHandler.setSize(900 / neuron.getLayer().neurons.size());
				} else {
					neuron.animationHandler.setSize(100);
				}
			}
		}

		for (Layer layer : layers) {
			for (Neuron neuron : layer.neurons) {
				neuron.setMinRange(getMinRange());
				neuron.setMaxRange(getMaxRange());
			}
		}
	}

	public void setInput(ArrayList<Double> input) {
		Input = input;
		TargetOutput = new ArrayList<Double>(input.subList(
				Math.max(input.size() - layers.get(layers.size() - 1).neurons.size() - Run.BiasComposition[0], 0),
				input.size()));
		for (Neuron neuron : layers.get(0).neurons) {
			if (neuron.getType() != 4) {
				neuron.setValue(input.get(layers.get(0).neurons.indexOf(neuron)));
			}
		}
	}

	public void feedForward() {
		for (Connection connection : connections) {
			connection.N2.setValue((connection.N2.getValue() + connection.N1.getValue() * connection.getValue()));
		}

		for (Layer layer : layers) {
			for (Neuron neuron : layer.neurons) {
				if (neuron.animationHandler.getColor() == InputColor) {

				} else {
					neuron.update();
				}
			}
		}
	}

	public void feedBackward() {
		for (Neuron neuron : layers.get(layers.size() - 1).neurons) {
			Error = TargetOutput.get(layers.get(layers.size() - 1).neurons.indexOf(neuron)) - neuron.getValue();
			neuron.setError(Error);
			Output = neuron.getValue();
			if (Drawing.outputPoints.size() > 30) {
				Drawing.outputPoints.remove(0);
			}
		}
		Double sum = 0d;
		for (Neuron neuron : layers.get(layers.size() - 1).neurons) {
			sum = sum + Math.pow(
					(neuron.getValue() - TargetOutput.get(layers.get(layers.size() - 1).neurons.indexOf(neuron))), 2);
		}
		
		if (minError == null) {
			minError = sum / layers.get(layers.size() - 1).neurons.size();
			maxError = sum / layers.get(layers.size() - 1).neurons.size();
		}
		
		if ((sum / layers.get(layers.size() - 1).neurons.size()) > maxError) {
			maxError = sum / layers.get(layers.size() - 1).neurons.size();
		}
		
		if ((sum / layers.get(layers.size() - 1).neurons.size()) < minError) {
			minError = (sum / layers.get(layers.size() - 1).neurons.size());
		}
		
		Drawing.graphPoints.add(new Vector2D(PointX,
				sum / layers.get(layers.size() - 1).neurons.size()));
		
		if (minOutput == null) {
			minOutput = Output;
			maxOutput = Output;
		}
		
		if (Output > maxOutput) {
			maxOutput = Output;
		}
		
		if (Output < minError) {
			minOutput = Output;
		}
		
		Drawing.outputPoints.add(Output);
		
		PointX++;
		Drawing.pointNumber++;

		Collections.reverse(connections);
		for (Connection connection : connections) {
			connection.N1.setError((connection.N1.getError() + connection.N2.getError() * connection.getValue()));
		}
		Collections.reverse(connections);
		for (Connection connection : connections) {
			connection.setValue(connection.getValue() + (TeachingRate * connection.N2.getError()
					* AFHandler.AFDerivative(Run.AF, connection.N2.getValue(), MinRange, MaxRange)
					* connection.N1.getValue()));
		}
	}

	public Double getMinRange() {
		return MinRange;
	}

	public void setMinRange(Double minRange) {
		MinRange = minRange;
		for (Layer layer : layers) {
			for (Neuron neuron : layer.neurons) {
				neuron.setMinRange(MinRange);
				;
			}
		}
	}

	public Double getMaxRange() {
		return MaxRange;
	}

	public void setMaxRange(Double maxRange) {
		MaxRange = maxRange;
		for (Layer layer : layers) {
			for (Neuron neuron : layer.neurons) {
				neuron.setMaxRange(MaxRange);
				;
			}
		}
	}
}