package main.NN.ActivationFunctions;

public class Gaussian implements ActivationFunction {

	public Double function(Double value, Double MinRange, Double MaxRange) {
		return ((MaxRange-MinRange)*Math.pow(2.718, -(Math.pow(value, 2))/(2)))+MinRange;
	}

	public Double derivative(Double value, Double MinRange, Double MaxRange) {
		return (value*Math.pow(2.718, -(Math.pow(value, 2))/(2)))*(MinRange-MaxRange);
	}

}
