package tests;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.NN.Neurons.Convolution;

public class ConvolutionTest {
	
	private Convolution convolution = new Convolution();
	
	public ConvolutionTest() {
		try {
			convolution.loadImage(ImageIO.read(getClass().getResourceAsStream("/tests/resources/Test.png")));
			convolution.setFilter(new int[][] {{-1, 0, +1},{-2, 0, 2},{-1, 0, +1}});
			System.out.println(convolution.getPixels());
			convolution.doConvolution();
			System.out.println(convolution);
			convolution.update();
			System.out.println(convolution);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ConvolutionTest();
	}

}
