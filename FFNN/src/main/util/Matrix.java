package main.util;

import java.util.ArrayList;

public class Matrix {
	
	ArrayList<ArrayList<Double>> elements = new ArrayList<ArrayList<Double>>();
	
	public Matrix() {
		
	}
	
	public void setSize(int columns, int rows) {
		if(elements.size() != 0) {
			if(columns != elements.size() && rows != elements.get(0).size()) {
				for (int i = 0; i < columns; i++) {
					elements.add(new ArrayList<Double>(rows));
					
					
					for (int n = 0; n < rows; n++) {
						  elements.get(n).add(0.0);
					}
				}
			}
		} else {
			for (int i = 0; i < columns; i++) {
				elements.add(new ArrayList<Double>(rows));
				
				
				for (int n = 0; n < rows; n++) {
					  elements.get(i).add(0.0);
				}
			}
		}
	}
	
	public int rows() {
		return elements.get(0).size();
	}
	
	public int columns() {
		return elements.size();
	}
	
	public double getElement(int column, int row) {
		return elements.get(column).get(row);
	}
	
	public void setElement(double value, int column, int row) {
		elements.get(column).set(row, value);
	}
	
	public String toString() {
		return elements.toString();
	}
}
