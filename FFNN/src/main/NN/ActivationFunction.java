package main.NN;

public class ActivationFunction {

	public static Double activationFunction(int AF, Double value, Double MinRange, Double MaxRange) {
		Double output = null;
		switch (AF) {
		case 0:
			output = sigmoid(value, MinRange, MaxRange);
			break;
		}
		return output;
	}
	
	public static Double sigmoid(Double value, Double MinRange, Double MaxRange) {
		return ((MaxRange - MinRange) / (1 + (Math.pow(2.71828, -value)))) + MinRange;
	}

}
