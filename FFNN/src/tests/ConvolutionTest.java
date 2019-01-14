package tests;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.NN.Neurons.Convolution;

public class ConvolutionTest {
	
	private Convolution convolution = new Convolution();
	
	public ConvolutionTest() {
		try {
			convolution.loadImage(ImageIO.read(getClass().getResourceAsStream("/tests/resources/Test.png")));
			System.out.println(convolution.getPixels());
			System.out.println(convolution.doConvolution());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ConvolutionTest();
	}

}
