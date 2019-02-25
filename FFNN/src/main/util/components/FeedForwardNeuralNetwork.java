package main.util.components;

import graphics.objects.InputSocket;
import graphics.objects.OutputSocket;

public class FeedForwardNeuralNetwork extends Component {

	@SuppressWarnings("unchecked")
	public FeedForwardNeuralNetwork() {
		super("Neural Network", new InputSocket[] {new InputSocket<Double>()}, new OutputSocket[] {new OutputSocket<Double>()});
	}
	
}
