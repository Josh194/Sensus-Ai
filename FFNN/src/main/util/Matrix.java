package main.util;

import java.util.Arrays;

public class Matrix {
	
	private int[][] elements = new int[0][0];
	
	public Matrix() {
		
	}
	
	public void setSize(int rows, int columns) {
		elements = new int[rows][columns];
	}
	
	public int rows() {
		return elements.length;
	}
	
	public int columns() {
		return elements[0].length;
	}
	
	public int getElement(int row, int column) {
		if ((row < rows() && row >= 0) && (column < columns() && column >= 0)) {
			return elements[row][column];
		} else {
			return 0;
		}
	}
	
	public void setElement(int value, int row, int column) {
		elements[row][column] = value;
	}
	
	public String toString() {
		return Arrays.deepToString(elements);
	}
}
