package main.util.components;

import graphics.objects.InputSocket;
import graphics.objects.OutputSocket;

public class TextFileInput extends Component {
	
	@SuppressWarnings("unchecked")
	public TextFileInput() {
		super("Text File", new InputSocket[0], new OutputSocket[] {new OutputSocket<Double>()});
	}

}
