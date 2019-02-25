package main.NN.Neurons;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import main.NN.AFHandler;
import main.util.Matrix;

public class Convolution {

	private Matrix pixels = new Matrix();
	private int[][] filter = new int[][] {{1, 0, 1}, {0, 1, 0}, {1, 0, 1}};

	public Convolution() {
		
	}
	
	public void update() {
		for (int i = 0; i < pixels.rows(); i++) {
			for (int n = 0; n < pixels.columns(); n++) {
				pixels.setElement(AFHandler.activationFunction(2, Double.valueOf(pixels.getElement(i, n)), 0.0, 0.0).intValue(), i, n);
			}
		}
	}
	
	public void loadImage(BufferedImage imageToLoad) {
		BufferedImage image = convertToGrayScale(imageToLoad);
		
		byte[] pixelArray = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		int iteration = 0;
		
		pixels.setSize(image.getWidth(), image.getHeight());
		
		for (int i = 0; i < image.getHeight(); i++) {
			for (int n = 0; n < image.getWidth(); n++) {
				pixels.setElement(pixelArray[iteration] & 0xff, i, n);
				
				iteration++;
			}
		}
	}
	
	public BufferedImage convertToGrayScale(BufferedImage image) {
		  BufferedImage result = new BufferedImage(
		            image.getWidth(),
		            image.getHeight(),
		            BufferedImage.TYPE_BYTE_GRAY);
		  Graphics g = result.getGraphics();
		  g.drawImage(image, 0, 0, null);
		  g.dispose();
		  return result;
	}
	
	public void doConvolution() {
		Matrix featureMap = new Matrix();
		featureMap.setSize(pixels.rows(), pixels.columns());
		
		for (int i = 0; i < pixels.rows(); i++) {
			for (int n = 0; n < pixels.columns(); n++) {
				int sum = 0;
				
				for (int r = 0; r < filter.length; r++) {
					for (int c = 0; c < filter[0].length; c++) {
						sum += pixels.getElement(i + (r - ((filter.length - 1) / 2)), n + (c - ((filter[0].length - 1) / 2))) * filter[r][c];
					}
				}
				
				featureMap.setElement(sum, i, n);
			}
		}
		
		pixels = featureMap;
	}
	
	public int[][] getFilter() {
		return filter;
	}

	public void setFilter(int[][] filter) {
		this.filter = filter;
	}
	
	public Matrix getPixels() {
		return pixels;
	}
	
	public String toString() {
		return pixels.toString();
	}
}
