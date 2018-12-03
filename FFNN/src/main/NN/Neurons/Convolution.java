package main.NN;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import main.util.Matrix;

public class Convolution {

	Matrix neurons = new Matrix();
	
	public Convolution() {
		
	}
	
	public void loadImage(BufferedImage imageToLoad) {
		BufferedImage image = convertToGrayScale(imageToLoad);
		
		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		int iteration = 0;
		
		neurons.setSize(image.getHeight(), image.getWidth());
		
		for (int i = 0; i < image.getHeight(); i++) {
			for (int n = 0; n < image.getWidth(); n++) {
				neurons.setElement(pixels[iteration], i, n);
				
				iteration++;
			}
		}
	}
	
	public static BufferedImage convertToGrayScale(BufferedImage image) {
		  BufferedImage result = new BufferedImage(
		            image.getWidth(),
		            image.getHeight(),
		            BufferedImage.TYPE_BYTE_GRAY);
		  Graphics g = result.getGraphics();
		  g.drawImage(image, 0, 0, null);
		  g.dispose();
		  return result;
	}
	
	public static Matrix doConvolution() {
		return new Matrix();
	}
	
	public Matrix getNeurons() {
		return neurons;
	}
}
