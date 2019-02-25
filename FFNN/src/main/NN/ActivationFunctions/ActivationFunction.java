package main.NN.ActivationFunctions;

public interface ActivationFunction {

	public Double function(Double value, Double MinRange, Double MaxRange);
	
	public Double derivative(Double value, Double MinRange, Double MaxRange);

}
