package graphics.screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;

import graphics.objects.ComponentSlot;
import main.util.components.FeedForwardNeuralNetwork;
import main.util.components.TextFileInput;

public class Overview extends Screen {

	private static final long serialVersionUID = 5495399203106707019L;

	private ArrayList<ComponentSlot> componentSlots = new ArrayList<ComponentSlot>();

	public Overview() {
		setBackground(new Color(0, 66, 103));
		
		for (int i = 0; i < 15; i++) {
			ComponentSlot componentSlot = new ComponentSlot(new TextFileInput());
			componentSlot.setLocation(new Point((((i + 1) % 3) * (450 + 52)) + 200 + ((i % 2) * (502 / 2)), (((i + 1) % 4) * (130 + 15)) + 200));
			
			componentSlots.add(componentSlot);
		}
		
		ComponentSlot componentSlot = new ComponentSlot(new FeedForwardNeuralNetwork());
		componentSlot.setLocation(new Point((((15 + 1) % 3) * (450 + 52)) + 200 + ((15 % 2) * (502 / 2)), (((15 + 1) % 4) * (130 + 15)) + 200));
		
		componentSlots.add(componentSlot);
		
		componentSlots.get(1).getComponent().getOutputs()[0].setConnection(componentSlots.get(15).getComponent().getInputs()[0]);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

		for (ComponentSlot componentSlot : componentSlots) {
			componentSlot.draw(g2);
		}
		
		for (ComponentSlot componentSlot : componentSlots) {
			componentSlot.drawConnections(g2);
		}

		repaint();
	}

}
