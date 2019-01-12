package main.util;

public class Matrix {
	
	private Double[][] elements = new Double[0][0];
	
	public Matrix() {
		
	}
	
	public void setSize(int columns, int rows) {
		elements = new Double[rows][columns];
	}
	
	public int rows() {
		return elements[0].length;
	}
	
	public int columns() {
		return elements.length;
	}
	
	public double getElement(int row, int column) {
		return elements[row][column];
	}
	
	public void setElement(double value, int row, int column) {
		elements[row][column] = value;
	}
	
	public String toString() {
		return elements.toString();
	}
}
