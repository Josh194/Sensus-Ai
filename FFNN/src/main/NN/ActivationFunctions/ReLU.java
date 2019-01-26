package main.NN.ActivationFunctions;

public class ReLU implements ActivationFunction {

	@Override
	public Double function(Double value, Double MinRange, Double MaxRange) {
		return Math.max(0.0, value);
	}

	@Override
	public Double derivative(Double value, Double MinRange, Double MaxRange) {
		if (value < 0) {
			return 0.0;
		} else {
			return 1.0;
		}
	}

}
