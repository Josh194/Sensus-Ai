package main.NN;

import main.NN.ActivationFunctions.ActivationFunction;
import main.NN.ActivationFunctions.*;

public class AFHandler {

	private final static ActivationFunction[] functions = new ActivationFunction[] {new Sigmoid(), new Gaussian(), new ReLU()};

	public static Double activationFunction(int AF, Double value, Double MinRange, Double MaxRange) {
		return functions[AF].function(value, MinRange, MaxRange);
	}
	
	public static Double AFDerivative(int AF, Double value, Double MinRange, Double MaxRange) {
		return functions[AF].derivative(value, MinRange, MaxRange);
	}

}
