package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import Graphics.Drawing;
import main.NN.Connection;
import main.NN.Layer;
import main.NN.Neuron;

@SuppressWarnings("serial")
public class Run extends JFrame {
    public static ArrayList<Shape> shapes = new ArrayList<Shape>();
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int CANVAS_WIDTH = 1200;
    public static final int CANVAS_HEIGHT = 1050;
    public static final int GRAPH_WIDTH = 440;
    public static final int GRAPH_HEIGHT = 440;
    public static final int CONTROL_WIDTH = 480;
    public static final int CONTROL_HEIGHT = 570;
    public static final int OUTPUT_WIDTH = 440;
    public static final int OUTPUT_HEIGHT = 530;
    public static final int NEURON_INFO_WIDTH = 480;
    public static final int NEURON_INFO_HEIGHT = 570;
    
    public static BufferedImage BasicNeuronImage;
    
    public Boolean Paused = false;
    public static NeuralNetwork neuralNetwork = new NeuralNetwork(new int[] {
        2,
        4,
        6,
        5,
        3,
        1,
    }, new int[] {
        0,
        1,
        1,
        0,
        0,
        0,
    }, 0.01);
    private static int NX;
    private static int NY;
    private static int InputLine = 0;
    private static int InputLines;
    private static Double[] Input = new Double[neuralNetwork.layers.get(0).neurons.size() + 1];

    private JPanel container = new JPanel();
    private DrawCanvas canvas;
    private GraphPanel graphPanel;
    private JPanel controlPanel;
    private NNCustomization customizationPanel;
    private OutputPanel outputPanel;
    private ViewNeuron viewNeuron;
    
    private int[] NeuronComposition;
    private int[] BiasComposition;
    
    private JTextField LearningRate = new JTextField();
    private JTextField CustomNeurons = new JTextField();
    private JTextField CustomBias = new JTextField();
    private JButton Pause = new JButton("Pause");
    private JButton SetNN = new JButton("Apply");
    private JButton outputPanelButton = new JButton("Output Graph");
    private JButton neuronPanelButton = new JButton("Inspect");
    private JButton customizationPanelButton = new JButton("Customize");
    private JButton outputPanelButton2 = new JButton("Output Graph");
    private JButton neuronPanelButton2 = new JButton("Inspect");
    private JButton customizationPanelButton2 = new JButton("Customize");

    public Run() {
    		
    			try {
				BasicNeuronImage = ImageIO.read(new File("src/Graphics/Simple_Neuron.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    	
    		Drawing.graphSize = new Dimension(GRAPH_WIDTH,GRAPH_HEIGHT);
    		Drawing.graphLocation = new Point(20,GRAPH_HEIGHT);
    		Drawing.outputSize = new Dimension(OUTPUT_WIDTH,OUTPUT_HEIGHT);
    		Drawing.outputLocation = new Point(20,OUTPUT_HEIGHT);
    		
    		Drawing.startAnimation();
    		
    		container.setLayout(new GridBagLayout());
        canvas = new DrawCanvas();
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        graphPanel = new GraphPanel();
        graphPanel.setPreferredSize(new Dimension(GRAPH_WIDTH+40, GRAPH_HEIGHT+40));
        controlPanel = new JPanel();
        controlPanel.setLayout(new CardLayout());
        customizationPanel = new NNCustomization();
        customizationPanel.setPreferredSize(new Dimension(CONTROL_WIDTH, CONTROL_HEIGHT));
        outputPanel = new OutputPanel();
        outputPanel.setPreferredSize(new Dimension(OUTPUT_WIDTH+40, OUTPUT_HEIGHT+40));
        viewNeuron = new ViewNeuron();
        viewNeuron.setPreferredSize(new Dimension(NEURON_INFO_WIDTH, NEURON_INFO_HEIGHT));
        
        Action LearningRateChange = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                neuralNetwork.TeachingRate = Double.parseDouble(LearningRate.getText());
            }
        };
        
        Pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        			Paused = !Paused;
            }
        });
        
        neuronPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        			((CardLayout) (controlPanel.getLayout())).show(controlPanel, "viewNeuron");
            }
        });
        
        outputPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        			((CardLayout) (controlPanel.getLayout())).show(controlPanel, "outputPanel");
            }
        });
        
        customizationPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            		((CardLayout) (controlPanel.getLayout())).show(controlPanel, "customizationPanel");
            }
        });
        
        neuronPanelButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        			((CardLayout) (controlPanel.getLayout())).show(controlPanel, "viewNeuron");
            }
        });
        
        outputPanelButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        			((CardLayout) (controlPanel.getLayout())).show(controlPanel, "outputPanel");
            }
        });
        
        customizationPanelButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            		((CardLayout) (controlPanel.getLayout())).show(controlPanel, "customizationPanel");
            }
        });
        
        SetNN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            		NeuronComposition = Arrays.stream(CustomNeurons.getText().substring(1, CustomNeurons.getText().length()-1).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
            		BiasComposition = Arrays.stream(CustomBias.getText().substring(1, CustomBias.getText().length()-1).split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
            		neuralNetwork = new NeuralNetwork(NeuronComposition, BiasComposition, Double.parseDouble(LearningRate.getText()));
            }
        });
        
        LearningRate.addActionListener(LearningRateChange);
        
        CustomBias.setBounds(20, 140, 100, 20);
        CustomNeurons.setBounds(20, 100, 100, 20);
        LearningRate.setBounds(20, 40, 100, 20);
        Pause.setBounds(360, 530, 100, 20);
        customizationPanelButton.setBounds(CONTROL_WIDTH-140, 20, 120, 25);
        outputPanelButton.setBounds(CONTROL_WIDTH-140, 20, 120, 25);
        neuronPanelButton.setBounds(CONTROL_WIDTH-140, 55, 120, 25);
        customizationPanelButton2.setBounds(CONTROL_WIDTH-140, 20, 120, 25);
        outputPanelButton2.setBounds(CONTROL_WIDTH-140, 60, 120, 25);
        neuronPanelButton2.setBounds(CONTROL_WIDTH-140, 55, 120, 25);
        SetNN.setBounds(20, 530, 100, 20);
        
        customizationPanel.setLayout(null);
        customizationPanel.add(Pause);
        customizationPanel.add(LearningRate);
        customizationPanel.add(CustomNeurons);
        customizationPanel.add(CustomBias);
        customizationPanel.add(SetNN);
        
        JLabel LearningRateLabel = new JLabel("Learning Rate:");
        LearningRateLabel.setBounds(20, 20, 100, 20);	
        JLabel CustomNeuronsLabel = new JLabel("Neuron Layout:");
        CustomNeuronsLabel.setBounds(20, 80, 100, 20);	
        JLabel CustomBiasLabel = new JLabel("Bias Layout:");
        CustomBiasLabel.setBounds(20, 120, 100, 20);	
        
        customizationPanel.add(LearningRateLabel);
        customizationPanel.add(CustomNeuronsLabel);
        customizationPanel.add(CustomBiasLabel);
        customizationPanel.add(outputPanelButton);
        customizationPanel.add(neuronPanelButton);
        
        outputPanel.setLayout(null);
        outputPanel.add(customizationPanelButton);
        outputPanel.add(neuronPanelButton2);
        
        viewNeuron.setLayout(null);
        viewNeuron.add(customizationPanelButton2);
        viewNeuron.add(outputPanelButton2);
        
        controlPanel.add(customizationPanel, "customizationPanel");
        controlPanel.add(outputPanel, "outputPanel");
        controlPanel.add(viewNeuron, "viewNeuron");
        
        container.add(canvas, new GridBagConstraints(0, 0, 2, 3, 0d, 0d, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        container.add(graphPanel, new GridBagConstraints(2, 1, 1, 1, 0d, 0d, GridBagConstraints.LINE_END, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        container.add(controlPanel, new GridBagConstraints(2, 2, 1, 1, 0d, 0d, GridBagConstraints.FIRST_LINE_END, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        Container cp = getContentPane();
        cp.add(container);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            setUndecorated(true);
            gd.setFullScreenWindow(this);
        } else {
            System.err.println("Full screen not supported");
            setSize(100, 100);
            setVisible(true);
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setTitle("Feed Forward Neural Network 2.2.0");
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                for (Shape s : shapes) {
                    if (s.contains(me.getPoint())) {
                        if (s instanceof Ellipse2D) {
                        		
                        }
                    }
                }
            }
        });
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
    
    private class DrawCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(new Color(0, 66, 103));

            loadInput("/Users/admin/Documents/Josh Files/Coding/FFNN/Input");
            
            if (Paused == false) {
                neuralNetwork.setInput(new ArrayList < Double > (Arrays.asList(Input)));
                neuralNetwork.feedForward();
                neuralNetwork.feedBackward();
            }
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));
            
            g2.setColor(Color.BLACK);
            g2.drawRect(10, 10, CANVAS_WIDTH-20, CANVAS_HEIGHT-20);
            
            for (Layer layer: neuralNetwork.layers) {
                for (Neuron neuron: layer.neurons) {
                    NX = CANVAS_WIDTH / (neuralNetwork.layers.size() + 1) * (neuralNetwork.layers.indexOf(layer) + 1);
                    NY = CANVAS_HEIGHT / (layer.neurons.size() + 1) * (layer.neurons.indexOf(neuron) + 1);
                    	neuron.setLocation(new Point(NX, NY));
                }
            }

            for (Connection connection: neuralNetwork.connections) {
                Drawing.drawLine(g2,
                    (int) connection.N1.getLocation().getX(),
                    (int) connection.N1.getLocation().getY(),
                    (int) connection.N2.getLocation().getX(),
                    (int) connection.N2.getLocation().getY(),
                    (int) (connection.getValue() * 5),
                    connection.getColor());
            }

            for (Layer layer: neuralNetwork.layers) {
                for (Neuron neuron: layer.neurons) {
                    g2.setColor(neuron.getColor());
                    Drawing.drawCircle(g2, neuron.getLocation().x, neuron.getLocation().y, 100);
                    g2.setColor(new Color(0, 0, 0));
                    Drawing.drawText(g2, new Rectangle(neuron.getLocation().x - 50, neuron.getLocation().y - 50, 100, 100), Double.toString(((double) Math.round(neuron.getValue() * 100d) / 100d)), new Font("Monaco", 1, 20));
                }
            }
            
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Layer layer: neuralNetwork.layers) {
                for (Neuron neuron: layer.neurons) {
                    neuron.setError(0.0);
                }
            }
            
            repaint();
        }
    }
    
    private class GraphPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(new Color(0, 66, 103));
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));
            
            g2.drawRect(10, 10, GRAPH_WIDTH+20, GRAPH_HEIGHT+20);
            g2.setColor(Color.WHITE);
            g2.drawLine(20, GRAPH_HEIGHT+20, GRAPH_WIDTH+20, GRAPH_HEIGHT+20);
            g2.drawLine(20, GRAPH_HEIGHT+20, 20, 20);
            
            Drawing.update(g2);
            
            repaint();
        }
    }
    
    private class OutputPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(new Color(0, 66, 103));
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));
            
            g2.drawRect(10, 10, GRAPH_WIDTH+20, GRAPH_HEIGHT+110);
            g2.setColor(Color.WHITE);
            g2.drawLine(20, GRAPH_HEIGHT+110, GRAPH_WIDTH+20, GRAPH_HEIGHT+110);
            g2.drawLine(20, GRAPH_HEIGHT+110, 20, 20);
            
            Drawing.updateOutput(g2);
            
            repaint();
        }
    }
    
    private class NNCustomization extends JPanel { 	
    		@Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(new Color(0, 66, 103));
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));
            
            g2.drawRect(10, 10, CONTROL_WIDTH-20, CONTROL_HEIGHT-20);
            
            repaint();
        }
    }
    
    private class ViewNeuron extends JPanel { 	
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
        		setBackground(new Color(0, 66, 103));
        
        		Graphics2D g2 = (Graphics2D) g;
        		g2.setRenderingHints(new RenderingHints(
        			RenderingHints.KEY_ANTIALIASING,
            		RenderingHints.VALUE_ANTIALIAS_ON));
        		
        		g2.drawImage(BasicNeuronImage, null, 0, 0);
        		g2.drawRect(10, 10, NEURON_INFO_WIDTH-20, NEURON_INFO_HEIGHT-20);
        
        		repaint();
		}
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    InputLines = getLines("/Users/admin/Documents/Josh Files/Coding/FFNN/Input");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new Run();
            }
        });
    }
}