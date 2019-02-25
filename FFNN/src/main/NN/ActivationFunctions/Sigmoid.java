package main.NN.ActivationFunctions;

public class Sigmoid implements ActivationFunction {

	public Double function(Double value, Double MinRange, Double MaxRange) {
		return ((MaxRange - MinRange) / (1 + (Math.pow(Math.E, -value)))) + MinRange;
	}

	public Double derivative(Double value, Double MinRange, Double MaxRange) {
		return (((MaxRange - MinRange) * Math.pow(Math.E, value)) / Math.pow((Math.pow(Math.E, value) + 1), 2));
	}

}
