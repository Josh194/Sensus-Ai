package main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Graphics.Drawing;
import main.NN.Connection;
import main.NN.Layer;
import main.NN.Neuron;

@SuppressWarnings("serial")
public class Window extends JFrame {
    // Define constants
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int CANVAS_WIDTH = (int) screenSize.getWidth();
    public static final int CANVAS_HEIGHT = (int) screenSize.getHeight();
    public static NeuralNetwork neuralNetwork = new NeuralNetwork(new int[] {
        2,
        4,
        6,
        5,
        3,
        1,
    }, 0.01);
    private static int NX;
    private static int NY;
    private static int InputLine = 0;
    private static int InputLines;
    private static Double[] Input = new Double[neuralNetwork.layers.get(0).neurons.size() + 1];

    // Declare an instance of the drawing canvas,
    // which is an inner class called DrawCanvas extending javax.swing.JPanel.
    private DrawCanvas canvas;

    // Constructor to set up the GUI components and event handlers
    public Window() {
        canvas = new DrawCanvas(); // Construct the drawing canvas
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // Set the Drawing JPanel as the JFrame's content-pane
        Container cp = getContentPane();
        cp.add(canvas);
        // or "setContentPane(canvas);"

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            setUndecorated(true);
            gd.setFullScreenWindow(this);
        } else {
            System.err.println("Full screen not supported");
            setSize(100, 100); // just something to let you see the window
            setVisible(true);
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Handle the CLOSE button
        pack(); // Either pack() the components; or setSize()
        setVisible(true); // "super" JFrame show
        setTitle("Feed Forward Neural Network 1.0.0"); // "super" JFrame sets the title  
    }

    public static int getLines(String File) throws IOException {
        try (
            FileReader input = new FileReader(File); LineNumberReader count = new LineNumberReader(input);
        ) {
            while (count.skip(Long.MAX_VALUE) > 0) {
                // Loop just in case the file is > Long.MAX_VALUE or skip() decides to not read the entire file
            }

            return (count.getLineNumber());
        }
    }

    public static void loadInput(String File) {
        for (int i = 0; i <= neuralNetwork.layers.get(0).neurons.size(); i++) {
            try {
                Input[i] = Double.parseDouble(Files.readAllLines(Paths.get(File)).get(InputLine));
                if (InputLine == InputLines) {
                    InputLine = 0;
                } else {
                    InputLine++;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Define inner class DrawCanvas, which is a JPanel used for custom drawing.
     */
    private class DrawCanvas extends JPanel {
        // Override paintComponent to perform your own painting
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g); // paint parent's background
            setBackground(new Color(0, 66, 103)); // set background color for this JPanel

            loadInput("/Users/admin/Documents/Josh Files/Coding/FFNN/Input");

            neuralNetwork.setInput(new ArrayList < Double > (Arrays.asList(Input)));
            neuralNetwork.feedForward();
            neuralNetwork.feedBackward();

            //Create a Graphics2D
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));
            for (Layer layer: neuralNetwork.layers) {
                for (Neuron neuron: layer.neurons) {
                    NX = CANVAS_WIDTH / (neuralNetwork.layers.size() + 1) * (neuralNetwork.layers.indexOf(layer) + 1);
                    NY = CANVAS_HEIGHT / (layer.neurons.size() + 1) * (layer.neurons.indexOf(neuron) + 1);
                    neuron.setLocation(new Point(NX, NY));
                    g2.setColor(neuron.getColor());
                }
            }

            for (Connection connection: neuralNetwork.connections) {
                Drawing.drawLine(g2,
                    (int) connection.N1.getLocation().getX(),
                    (int) connection.N1.getLocation().getY(),
                    (int) connection.N2.getLocation().getX(),
                    (int) connection.N2.getLocation().getY(),
                    (int)(connection.getValue() * 5),
                    connection.getColor());
            }

            for (Layer layer: neuralNetwork.layers) {
                for (Neuron neuron: layer.neurons) {
                    NX = CANVAS_WIDTH / (neuralNetwork.layers.size() + 1) * (neuralNetwork.layers.indexOf(layer) + 1);
                    NY = CANVAS_HEIGHT / (layer.neurons.size() + 1) * (layer.neurons.indexOf(neuron) + 1);
                    neuron.setLocation(new Point(NX, NY));
                    g2.setColor(neuron.getColor());
                    Drawing.drawCircle(g2, NX, NY, 100);
                    g2.setColor(new Color(0, 0, 0));
                    Drawing.drawText(g2, new Rectangle(NX - 50, NY - 50, 100, 100), Double.toString(((double) Math.round(neuron.getValue() * 100d) / 100d)), new Font("Monaco", 1, 20));
                }
            }
            // Your custom painting codes
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            for (Layer layer: neuralNetwork.layers) {
                for (Neuron neuron: layer.neurons) {
                    neuron.setValue(0.0);
                    neuron.setError(0.0);
                }
            }
            repaint();
        }
    }

    // The entry main method
    public static void main(String[] args) {
        // Run the GUI codes on the Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    InputLines = getLines("/Users/admin/Documents/Josh Files/Coding/FFNN/Input");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new Window(); // Let the constructor do the job
            }
        });
    }
}