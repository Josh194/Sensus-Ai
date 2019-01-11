package main;

import java.util.ArrayList;
import java.util.Collections;

import Graphics.Drawing;
import main.NN.AFHandler;
import main.NN.Connection;
import main.NN.Layer;
import main.NN.Neurons.BiasNeuron;
import main.NN.Neurons.HiddenNeuron;
import main.NN.Neurons.InputNeuron;
import main.NN.Neurons.Neuron;
import main.NN.Neurons.OutputNeuron;
import main.util.Vector2D;

public class NeuralNetwork {

	public ArrayList<Double> input = new ArrayList<Double>();
	public ArrayList<Double> targetOutput = new ArrayList<Double>();
	public ArrayList<Layer> layers = new ArrayList<Layer>();
	public ArrayList<Connection> connections = new ArrayList<Connection>();
	public Double teachingRate;
	Double error;
	public Double maxError = null;
	public Double minError = null;
	Double output;
	public Double maxOutput = 0d;
	public Double minOutput = 0d;
	private Double minRange = 0d;
	private Double maxRange = 1d;
	private static int NX;
	private static int NY;
	private static int pointX = 0;
	private static int batchProg = 0;
	private static double batchAverage = 0.0;

	public NeuralNetwork(Double teachingRate) {
		connections.clear();
		layers.clear();
		Drawing.graphPoints.clear();
		this.teachingRate = teachingRate;
		
		for (int i = 0; i < 3; i++) {
			layers.add(new Layer());
		}

		for (Layer layer : layers) {
			if (layers.indexOf(layer) == 0) {
				layer.addNeuron(new InputNeuron(0.0, layer));
				layer.addNeuron(new InputNeuron(0.0, layer));
			} else if (layers.indexOf(layer) == 1) {
				layer.addNeuron(new HiddenNeuron(0.0, layer));
				layer.addNeuron(new HiddenNeuron(0.0, layer));
			} else {
				layer.addNeuron(new OutputNeuron(0.0, layer));
			}

			createConnections();
		}
		
		updateAnim();
	}
	
	public void clear() {
		connections.clear();
		layers.clear();
		Drawing.graphPoints.clear();
	}
	
	public void createConnections() {
		connections.clear();
		
		for (Layer layer : layers) {
			for (Neuron neuron : layer.neurons) {
				if (layers.indexOf(neuron.getLayer()) != 0) {
					for (Neuron lastNeuron : layers.get(layers.indexOf(neuron.getLayer()) - 1).neurons) {
						connections.add(new Connection(lastNeuron, neuron));
					}
				}
			}
		}
	}
	
	public void updateAnim() {
		for (Layer layer : layers) {
			for (Neuron neuron : layer.neurons) {
				NX = Run.CANVAS_WIDTH / (layers.size() + 1) * (layers.indexOf(layer) + 1);
				NY = Run.CANVAS_HEIGHT / (layer.neurons.size() + 1) * (layer.neurons.indexOf(neuron) + 1);
				
				neuron.animationHandler.setLocation(new Vector2D(NX, NY));
				
				if (neuron.getLayer().neurons.size() > 8) {
					neuron.animationHandler.setSize(900 / neuron.getLayer().neurons.size());
				} else {
					neuron.animationHandler.setSize(100);
				}
				
				neuron.setMinRange(getMinRange());
				neuron.setMaxRange(getMaxRange());
			}
		}
	}

	public void setInput(ArrayList<Double> input) {
		this.input = input;
		targetOutput = new ArrayList<Double>(input.subList(
				Math.max(input.size() - layers.get(layers.size() - 1).neurons.size() - layers.get(0).numberOf(BiasNeuron.class), 0),
				input.size()));
		System.out.println(targetOutput.get(0));
		for (Neuron neuron : layers.get(0).neurons) {
			if (!neuron.getType().equals("BiasNeuron")) {
				neuron.setValue(input.get(layers.get(0).neurons.indexOf(neuron)));
			}
		}
	}

	public void feedForward() {
		for (Connection connection : connections) {
			connection.getSecondNeuron().setValue((connection.getSecondNeuron().getValue() + connection.getFirstNeuron().getValue() * connection.getValue()));
		}

		for (Layer layer : layers) {
			for (Neuron neuron : layer.neurons) {
				neuron.update();
			}
		}
	}

	public void feedBackward() {
		for (Neuron neuron : layers.get(layers.size() - 1).neurons) {
			error = targetOutput.get(layers.get(layers.size() - 1).neurons.indexOf(neuron)) - neuron.getValue();
			neuron.setError(error);
			output = neuron.getValue();
			
			if (Drawing.outputPoints.size() > 30) {
				Drawing.outputPoints.remove(0);
			}
		}
		Double sum = 0d;
		for (Neuron neuron : layers.get(layers.size() - 1).neurons) {
			sum = sum + Math.abs((neuron.getValue() - targetOutput.get(layers.get(layers.size() - 1).neurons.indexOf(neuron))));
		}
		
		if (batchProg == Run.InputLines) {
			if (minError == null) {
				minError = batchAverage / (layers.get(layers.size() - 1).neurons.size() + layers.get(0).neurons.size() - layers.get(0).numberOf(BiasNeuron.class));
				maxError = batchAverage / (layers.get(layers.size() - 1).neurons.size() + layers.get(0).neurons.size() - layers.get(0).numberOf(BiasNeuron.class));
			} else {
				if (batchAverage / (layers.get(layers.size() - 1).neurons.size() + layers.get(0).neurons.size() - layers.get(0).numberOf(BiasNeuron.class)) > maxError) {
					maxError = batchAverage / (layers.get(layers.size() - 1).neurons.size() + layers.get(0).neurons.size() - layers.get(0).numberOf(BiasNeuron.class));
				}
				
				if (batchAverage / (layers.get(layers.size() - 1).neurons.size() + layers.get(0).neurons.size() - layers.get(0).numberOf(BiasNeuron.class)) < minError) {
					minError = batchAverage / (layers.get(layers.size() - 1).neurons.size() + layers.get(0).neurons.size() - layers.get(0).numberOf(BiasNeuron.class));
				}
			}
			
			Drawing.graphPoints.add(new Vector2D(pointX, batchAverage / (layers.get(layers.size() - 1).neurons.size() + layers.get(0).neurons.size() - layers.get(0).numberOf(BiasNeuron.class))));
			
			batchProg = 0;
			batchAverage = 0;
			
			pointX++;
			Drawing.pointNumber++;
		} else {
			batchProg++;
			
			batchAverage += (sum / layers.get(layers.size() - 1).neurons.size());
		}
		
		if (minOutput == null) {
			minOutput = output;
			maxOutput = output;
		}
		
		if (output > maxOutput) {
			maxOutput = output;
		}
		
		if (output < minOutput) {
			minOutput = output;
		}
		
		Drawing.outputPoints.add(output);

		Collections.reverse(connections);
		for (Connection connection : connections) {
			connection.getFirstNeuron().setError((connection.getFirstNeuron().getError() + connection.getSecondNeuron().getError() * connection.getValue()));
		}
		
		Collections.reverse(connections);
		for (Connection connection : connections) {
			connection.setValue(connection.getValue() + (teachingRate * connection.getSecondNeuron().getError()
					* AFHandler.AFDerivative(Run.AF, connection.getSecondNeuron().getValue(), minRange, maxRange)
					* connection.getFirstNeuron().getValue()));
		}
	}

	public Double getMinRange() {
		return minRange;
	}

	public void setMinRange(Double minRange) {
		this.minRange = minRange;
		for (Layer layer : layers) {
			for (Neuron neuron : layer.neurons) {
				neuron.setMinRange(minRange);
				;
			}
		}
	}

	public Double getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(Double maxRange) {
		this.maxRange = maxRange;
		for (Layer layer : layers) {
			for (Neuron neuron : layer.neurons) {
				neuron.setMaxRange(maxRange);
				;
			}
		}
	}
}