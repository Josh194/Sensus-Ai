package main.NN;

import java.awt.Color;
import java.awt.Point;

public class Neuron {

    Double Value;
    Double Error = 0.0;
    Double MinRange;
    Double MaxRange;
    public int Type; //1: FF, 2: Bias, 3: Recursive
    Layer Layer;
    Color Color;
    Point Location;

    public Neuron(Double value, Layer layer, int type, Color color) {
        Value = value;
        Layer = layer;
        Type = type;
        Color = color;
    }
    
    public void update() {
    		if (Type==1) {
    			Value = ((MaxRange - MinRange) / (1 + (Math.pow(2.71828, -Value)))) + MinRange;
    		}
    }

    public void setValue(Double value) {
        Value = value;
    }

    public Double getValue() {
        return (Value);
    }

    public void setError(Double error) {
        Error = error;
    }

    public Double getError() {
        return (Error);
    }

    public void setLocation(Point location) {
        Location = location;
    }

    public Point getLocation() {
        return (Location);
    }

    public Color getColor() {
        return (Color);
    }

    public void setLayer(Layer layer) {
        Layer = layer;
    }

    public Layer getLayer() {
        return (Layer);
    }

    public void setMinRange(Double minRange) {
        MinRange = minRange;
    }

    public void setMaxRange(Double maxRange) {
        MaxRange = maxRange;
    }
}