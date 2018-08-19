package main.NN.ActivationFunctions;

public class Sigmoid implements ActivationFunction {

	public Double function(Double value, Double MinRange, Double MaxRange) {
		return ((MaxRange - MinRange) / (1 + (Math.pow(2.71828, -value)))) + MinRange;
	}

	public Double derivative(Double value, Double MinRange, Double MaxRange) {
		return (((MaxRange - MinRange) * Math.pow(2.71828, value)) / Math.pow((Math.pow(2.71828, value) + 1), 2));
	}

}
