package main.util.components;

import graphics.objects.InputSocket;
import graphics.objects.OutputSocket;

public class Component {
	
	private String name;
	private InputSocket<Object>[] inputs;
	private OutputSocket<Object>[] outputs;
	
	public Component(String name, InputSocket<Object>[] inputs, OutputSocket<Object>[] outputs) {
		this.name = name;
		this.inputs = inputs;
		this.outputs = outputs;
	}
	
	public String getName() {
		return name;
	}

	public InputSocket<Object>[] getInputs() {
		return inputs;
	}

	public OutputSocket<Object>[] getOutputs() {
		return outputs;
	}

}
