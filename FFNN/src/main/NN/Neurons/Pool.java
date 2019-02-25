package main.NN.Neurons;

import main.util.Matrix;

public class Pool {
	
	private Matrix pixels = new Matrix();
	
	public Pool() {
		
	}
	
	public void downSample() {
		Matrix sampled = new Matrix();
		
		sampled.setSize(2, 2);
		
		int max = 0;
		
		for (int i = 0; i < sampled.rows(); i++) {
			for (int n = 0; n < sampled.columns(); n++) {
				for (int r = 2 * i; r < (2 * i) + 2; r++) {
					for (int c = 2 * n; c < (2 * n) + 2; c++) {
						if (pixels.getElement(r, c) > max) {
							max = pixels.getElement(r, c);
						}
					}
				}
				
				sampled.setElement(max, i, n);
				max = 0;
			}
		}
		
		pixels = sampled;
	}
	
	public void setElement(int value, int row, int column) {
		pixels.setElement(value, row, column);
	}
	
	public void setSize(int rows, int columns) {
		pixels.setSize(rows, columns);
	}
	
	public String toString() {
		return pixels.toString();
	}
	
}
