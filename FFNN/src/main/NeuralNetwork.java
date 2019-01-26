package main;

import java.util.ArrayList;
import java.util.Arrays;

import graphics.Drawing;
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
		clear();
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
		layers.clear();
		Drawing.graphPoints.clear();
	}

	public void createConnections() {
		for (int i = 0; i < layers.size() - 1; i++) {
		layers.get(i).connections.clear();
			for (Neuron neuron : layers.get(i).neurons) {
				for (Neuron nextNeuron : layers.get(i + 1).neurons) {
					if (!nextNeuron.getType().equals("BiasNeuron")) {
						layers.get(i).connections.add(new Connection(neuron, nextNeuron));
					}
				}
			}
		}
	}
	
	public int numberOfConnections() {
		int size = 0;
		
		for (Layer layer : layers) {
			for (Connection connection : layer.connections) {
				size++;
			}
		}
		
		return size;
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
		targetOutput = new ArrayList<Double>(input.subList(input.size() - layers.get(layers.size() - 1).neurons.size(), input.size()));
		for (Neuron neuron : layers.get(0).neurons) {
			if (!neuron.getType().equals("BiasNeuron")) {
				neuron.setValue(input.get(layers.get(0).neurons.indexOf(neuron)));
			}
		}
	}

	public void feedForward() {
		for (Layer layer : layers) {
			for (Connection connection : layer.connections) {
				connection.getSecondNeuron().setValue((connection.getSecondNeuron().getValue()
						+ connection.getFirstNeuron().getValue() * connection.getValue()));
			}

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
			sum = sum + Math
					.abs((neuron.getValue() - targetOutput.get(layers.get(layers.size() - 1).neurons.indexOf(neuron))));
		}
		
		if (batchProg == ((Run.InputLines + 1) / (layers.get(0).numberOf(InputNeuron.class) + layers.get(layers.size() - 1).neurons.size())) - 1) {
			batchAverage += (sum / layers.get(layers.size() - 1).neurons.size());
			
			if (minError == null) {
				minError = batchAverage / (((Run.InputLines + 1)
						/ (layers.get(0).numberOf(InputNeuron.class) + layers.get(layers.size() - 1).neurons.size())) - 1);
				maxError = batchAverage / (((Run.InputLines + 1)
						/ (layers.get(0).numberOf(InputNeuron.class) + layers.get(layers.size() - 1).neurons.size())) - 1);
			} else {
				if (batchAverage / (((Run.InputLines + 1)
						/ (layers.get(0).numberOf(InputNeuron.class) + layers.get(layers.size() - 1).neurons.size())) - 1) > maxError) {
					maxError = batchAverage / (((Run.InputLines + 1)
							/ (layers.get(0).numberOf(InputNeuron.class) + layers.get(layers.size() - 1).neurons.size())) - 1);
				}

				if (batchAverage / (((Run.InputLines + 1)
						/ (layers.get(0).numberOf(InputNeuron.class) + layers.get(layers.size() - 1).neurons.size())) - 1) < minError) {
					minError = batchAverage / (((Run.InputLines + 1)
							/ (layers.get(0).numberOf(InputNeuron.class) + layers.get(layers.size() - 1).neurons.size())) - 1);
				}
			}
			
			Drawing.graphPoints.add(new Vector2D(pointX, batchAverage /
					(((Run.InputLines + 1) / (layers.get(0).numberOf(InputNeuron.class) + layers.get(layers.size() - 1).neurons.size())) - 1)));

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
		
		for (int i = layers.size() - 1; i >= 0; i--) {
			for (Connection connection : layers.get(i).connections) {
				connection.getFirstNeuron().setError((connection.getFirstNeuron().getError()
						+ connection.getSecondNeuron().getError() * connection.getValue()));
			}
		}
		
		for (Layer layer : layers) {
			for (Connection connection : layer.connections) {
				connection.setValue(connection.getValue() + (teachingRate * connection.getSecondNeuron().getError()
						* AFHandler.AFDerivative(Run.AF, connection.getSecondNeuron().getValue(), minRange, maxRange)
						* connection.getFirstNeuron().getValue()));
			}
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