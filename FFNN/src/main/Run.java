package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Graphics.Drawing;
import main.NN.Connection;
import main.NN.Layer;
import main.NN.Neuron;

@SuppressWarnings("serial")
public class Run extends JFrame {
	public static ArrayList<Shape> shapes = new ArrayList<Shape>();
	public static Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	public static final int CANVAS_WIDTH = screenSize.width - 480;
	public static final int CANVAS_HEIGHT = screenSize.height;
	public static final int GRAPH_WIDTH = 440;
	public static final int GRAPH_HEIGHT = screenSize.height - 610;
	public static final int CONTROL_WIDTH = 480;
	public static final int CONTROL_HEIGHT = 570;
	public static final int OUTPUT_WIDTH = 440;
	public static final int OUTPUT_HEIGHT = 530;
	public static final int NEURON_INFO_WIDTH = 480;
	public static final int NEURON_INFO_HEIGHT = 570;

	public static BufferedImage BasicNeuronImage;
	public static BufferedImage BiasNeuronImage;
	public static BufferedImage InputNeuronImage;
	public static BufferedImage OutputNeuronImage;
	public static int SelectedNeuronType = 0;
	public static boolean FoundType = false;

	public Boolean Paused = true;
	public String InputString = "";
	public static NeuralNetwork neuralNetwork = new NeuralNetwork(new int[] { 2, 4, 6, 5, 3, 1, },
			new int[] { 0, 1, 1, 0, 0, 0, }, 0.01);
	private static int InputLine = 0;
	private static int InputLines;
	private static Double[] Input = new Double[neuralNetwork.layers.get(0).neurons.size() + 1];
	private static Connection connectionToRemove = null;

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
	private JTextField TestInput = new JTextField();
	private JButton InputLocation = new JButton("Load Input");
	private JButton SaveNN = new JButton("Save");
	private JButton LoadNN = new JButton("Load");
	private JButton Pause = new JButton("Pause");
	private JButton SetNN = new JButton("Apply");
	private JButton outputPanelButton = new JButton("Output Graph");
	private JButton neuronPanelButton = new JButton("Inspect");
	private JButton customizationPanelButton = new JButton("Customize");
	private JButton outputPanelButton2 = new JButton("Output Graph");
	private JButton neuronPanelButton2 = new JButton("Inspect");
	private JButton customizationPanelButton2 = new JButton("Customize");
	private JButton Exit = new JButton();

	public Run() throws TransformerException, ParserConfigurationException {
		try {
			BasicNeuronImage = ImageIO.read(new File("src/Graphics/Simple_Neuron.png"));
			BiasNeuronImage = ImageIO.read(new File("src/Graphics/Bias_Neuron.png"));
			InputNeuronImage = ImageIO.read(new File("src/Graphics/Input_Neuron.png"));
			OutputNeuronImage = ImageIO.read(new File("src/Graphics/Output_Neuron.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		Drawing.graphSize = new Dimension(GRAPH_WIDTH, GRAPH_HEIGHT);
		Drawing.graphLocation = new Point(20, GRAPH_HEIGHT);
		Drawing.outputSize = new Dimension(OUTPUT_WIDTH, OUTPUT_HEIGHT);
		Drawing.outputLocation = new Point(20, OUTPUT_HEIGHT);

		Drawing.startAnimation();

		container.setLayout(new GridBagLayout());
		canvas = new DrawCanvas();
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		graphPanel = new GraphPanel();
		graphPanel.setPreferredSize(new Dimension(GRAPH_WIDTH + 40, GRAPH_HEIGHT + 40));
		controlPanel = new JPanel();
		controlPanel.setLayout(new CardLayout());
		customizationPanel = new NNCustomization();
		customizationPanel.setPreferredSize(new Dimension(CONTROL_WIDTH, CONTROL_HEIGHT));
		outputPanel = new OutputPanel();
		outputPanel.setPreferredSize(new Dimension(OUTPUT_WIDTH + 40, OUTPUT_HEIGHT + 40));
		viewNeuron = new ViewNeuron();
		viewNeuron.setPreferredSize(new Dimension(NEURON_INFO_WIDTH, NEURON_INFO_HEIGHT));

		Action LearningRateChange = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				neuralNetwork.TeachingRate = Double.parseDouble(LearningRate.getText());
			}
		};

		Action TestInputAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Double> Input = DoubleStream
						.of(Arrays.stream(TestInput.getText().substring(1, TestInput.getText().length() - 1).split(","))
								.map(String::trim).mapToDouble(Double::parseDouble).toArray())
						.boxed().collect(Collectors.toList());
				neuralNetwork.setInput(new ArrayList<Double>(Input));
				neuralNetwork.feedForward();
			}
		};

		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				super.mouseClicked(me);

				Double distanceToLine = 10000d;
				Line2D closestLine = null;

				for (Shape s : shapes) {
					if (s instanceof Line2D && shortestDistance(((Line2D) s).getP1(), ((Line2D) s).getP2(),
							me.getPoint()) < distanceToLine) {
						distanceToLine = shortestDistance(((Line2D) s).getP1(), ((Line2D) s).getP2(), me.getPoint());
						closestLine = ((Line2D) s);
					}
				}

				for (Shape s : shapes) {
					if (s instanceof Ellipse2D && s.contains(me.getPoint())) {
						for (Layer layer : neuralNetwork.layers) {
							for (Neuron neuron : layer.neurons) {
								if (neuron.getLocation().equals(new Point(s.getBounds().getLocation().x + (100 / 2),
										s.getBounds().getLocation().y + (100 / 2)))) {
									SelectedNeuronType = neuron.Type;
									FoundType = true;
								}
							}
						}
					}

					if (distanceToLine < 5 && !FoundType) {
						for (Connection connection : neuralNetwork.connections) {
							if (connection.N1.getLocation().equals((closestLine.getP1()))
									&& connection.N2.getLocation().equals((closestLine.getP2()))) {
								connectionToRemove = connection;
							}
						}
					}
				}

				if (distanceToLine < 5 && !FoundType) {
					Run.neuralNetwork.connections.remove(connectionToRemove);
					shapes.remove(closestLine);
				}

				if (FoundType) {
					FoundType = false;
				} else {
					SelectedNeuronType = 0;
				}

				distanceToLine = 10000d;
			}
		});

		try {
			Exit.setIcon(new ImageIcon(ImageIO.read(new File("src/images/exit.png"))));
		} catch (Exception ex) {
			System.out.println(ex);
		}

		Exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

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
				if (!CustomNeurons.getText().equals("") && !CustomBias.getText().equals("")
						&& !LearningRate.getText().equals("")) {
					shapes.clear();
					Drawing.Added = false;
					Drawing.animationX = -5d;
					Drawing.animation = new Timer();
					Drawing.startAnimation();

					NeuronComposition = Arrays.stream(
							CustomNeurons.getText().substring(1, CustomNeurons.getText().length() - 1).split(","))
							.map(String::trim).mapToInt(Integer::parseInt).toArray();
					BiasComposition = Arrays
							.stream(CustomBias.getText().substring(1, CustomBias.getText().length() - 1).split(","))
							.map(String::trim).mapToInt(Integer::parseInt).toArray();
					neuralNetwork = new NeuralNetwork(NeuronComposition, BiasComposition,
							Double.parseDouble(LearningRate.getText()));
				}
			}
		});

		InputLocation.addActionListener(new ActionListener() {

			JFileChooser fileChooser = new JFileChooser();

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(fileChooser);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					InputString = file.getAbsolutePath();
				} else {
					System.out.println("File access cancelled by user.");
				}
			}
		});

		SaveNN.addActionListener(new ActionListener() {

			JFileChooser fileChooser = new JFileChooser();

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(fileChooser);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					int[] Neurons = new int[neuralNetwork.layers.size()];
					for (Layer layer : neuralNetwork.layers) {
						Neurons[neuralNetwork.layers.indexOf(layer)] = layer.neurons.size();
					}

					int[] Bias = new int[neuralNetwork.layers.size()];
					int i = 0;
					for (Layer layer : neuralNetwork.layers) {
						for (Neuron neuron : layer.neurons) {
							if (neuron.Type == 4) {
								Bias[i + 1] = 1;
								i++;
							} else {
								Bias[i + 1] = 0;
							}
						}
					}

					Double[] Connections = new Double[neuralNetwork.connections.size()];
					for (Connection connection : neuralNetwork.connections) {
						Connections[neuralNetwork.connections.indexOf(connection)] = connection.getValue();
					}
					try {
						XMLsave.writeDocumentToFile(Neurons, Bias, Connections, new File(file.getAbsolutePath()));
					} catch (TransformerException e1) {
						e1.printStackTrace();
					} catch (ParserConfigurationException e1) {
						e1.printStackTrace();
					}
				} else {
					System.out.println("File access cancelled by user.");
				}
			}
		});

		LoadNN.addActionListener(new ActionListener() {

			JFileChooser fileChooser = new JFileChooser();

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(fileChooser);

				String extension = "";

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();

					int l = file.getAbsolutePath().lastIndexOf('.');
					if (l >= 0) {
						extension = file.getAbsolutePath().substring(l + 1);
					}

					if (extension.equals("xml")) {
						Document InputFile = null;
						try {
							InputFile = XMLsave.asXML(file);
						} catch (SAXException | IOException | ParserConfigurationException e2) {
							e2.printStackTrace();
						}
						InputFile.getDocumentElement().normalize();
						NodeList nList = null;
						try {
							nList = XMLsave.asXML(file).getElementsByTagName("Neurons");
						} catch (SAXException | IOException | ParserConfigurationException e1) {
							e1.printStackTrace();
						}
						Node node = nList.item(0);
						Element eElement = (Element) node;

						int[] Neurons = new int[eElement.getElementsByTagName("NumNeurons").getLength()];
						for (int i = 0; i < Neurons.length; i++) {
							Neurons[i] = Integer
									.parseInt(eElement.getElementsByTagName("NumNeurons").item(i).getTextContent());
						}

						try {
							nList = XMLsave.asXML(file).getElementsByTagName("Bias");
						} catch (SAXException | IOException | ParserConfigurationException e1) {
							e1.printStackTrace();
						}
						node = nList.item(0);
						eElement = (Element) node;

						int[] Bias = new int[eElement.getElementsByTagName("HasBias").getLength()];
						for (int i = 0; i < Bias.length; i++) {
							Bias[i] = Integer
									.parseInt(eElement.getElementsByTagName("HasBias").item(i).getTextContent());
						}

						neuralNetwork = new NeuralNetwork(Neurons, Bias, 0.01);

						try {
							nList = XMLsave.asXML(file).getElementsByTagName("ConnectionValues");
						} catch (SAXException | IOException | ParserConfigurationException e1) {
							e1.printStackTrace();
						}
						node = nList.item(0);
						eElement = (Element) node;

						for (Connection connection : neuralNetwork.connections) {
							connection.setValue(Double.parseDouble(eElement.getElementsByTagName("ConnectionValue")
									.item(neuralNetwork.connections.indexOf(connection)).getTextContent()));
						}
					}
				} else {
					System.out.println("File access cancelled by user.");
				}
			}
		});

		LearningRate.addActionListener(LearningRateChange);
		TestInput.addActionListener(TestInputAction);

		CustomBias.setBounds(20, 140, 100, 20);
		CustomNeurons.setBounds(20, 100, 100, 20);
		LearningRate.setBounds(20, 40, 100, 20);
		Pause.setBounds(360, 530, 100, 20);
		TestInput.setBounds(20, 260, 100, 20);
		customizationPanelButton.setBounds(CONTROL_WIDTH - 140, 20, 120, 25);
		outputPanelButton.setBounds(CONTROL_WIDTH - 140, 20, 120, 25);
		neuronPanelButton.setBounds(CONTROL_WIDTH - 140, 55, 120, 25);
		customizationPanelButton2.setBounds(CONTROL_WIDTH - 140, 20, 120, 25);
		outputPanelButton2.setBounds(CONTROL_WIDTH - 140, 60, 120, 25);
		neuronPanelButton2.setBounds(CONTROL_WIDTH - 140, 55, 120, 25);
		SetNN.setBounds(20, 530, 100, 20);
		InputLocation.setBounds(20, 180, 100, 20);
		SaveNN.setBounds(CONTROL_WIDTH - 140, 250, 120, 20);
		LoadNN.setBounds(CONTROL_WIDTH - 140, 290, 120, 20);
		Exit.setBounds(20, 20, 43, 43);

		Exit.setOpaque(false);
		Exit.setContentAreaFilled(false);
		Exit.setBorderPainted(false);

		customizationPanel.setLayout(null);
		customizationPanel.add(Pause);
		customizationPanel.add(LearningRate);
		customizationPanel.add(CustomNeurons);
		customizationPanel.add(CustomBias);
		customizationPanel.add(SetNN);
		customizationPanel.add(InputLocation);
		customizationPanel.add(SaveNN);
		customizationPanel.add(LoadNN);
		customizationPanel.add(TestInput);

		JLabel LearningRateLabel = new JLabel("Learning Rate:");
		LearningRateLabel.setBounds(20, 20, 100, 20);
		JLabel CustomNeuronsLabel = new JLabel("Neuron Layout:");
		CustomNeuronsLabel.setBounds(20, 80, 100, 20);
		JLabel CustomBiasLabel = new JLabel("Bias Layout:");
		CustomBiasLabel.setBounds(20, 120, 100, 20);
		JLabel TestInputLabel = new JLabel("Test Input:");
		TestInputLabel.setBounds(20, 240, 100, 20);
		JLabel InfoLabel = new JLabel(
				"<html><body style='text-align: center'>For information on how to use this software visit:<br>github.com/Josh194/Ai/wiki</html>");
		InfoLabel.setBounds(115, 480, 250, 100);

		customizationPanel.add(LearningRateLabel);
		customizationPanel.add(CustomNeuronsLabel);
		customizationPanel.add(CustomBiasLabel);
		customizationPanel.add(TestInputLabel);
		customizationPanel.add(InfoLabel);
		customizationPanel.add(outputPanelButton);
		customizationPanel.add(neuronPanelButton);

		outputPanel.setLayout(null);
		outputPanel.add(customizationPanelButton);
		outputPanel.add(neuronPanelButton2);

		viewNeuron.setLayout(null);
		viewNeuron.add(customizationPanelButton2);
		viewNeuron.add(outputPanelButton2);

		canvas.setLayout(null);
		canvas.add(Exit);

		controlPanel.add(customizationPanel, "customizationPanel");
		controlPanel.add(outputPanel, "outputPanel");
		controlPanel.add(viewNeuron, "viewNeuron");

		container.add(canvas, new GridBagConstraints(0, 0, 2, 3, 0d, 0d, GridBagConstraints.LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		container.add(graphPanel, new GridBagConstraints(2, 1, 1, 1, 0d, 0d, GridBagConstraints.LINE_END,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		container.add(controlPanel, new GridBagConstraints(2, 2, 1, 1, 0d, 0d, GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		Container cp = getContentPane();
		cp.add(container);

		setTitle("Sensus Ai 1.0.3");
		setSize(CANVAS_WIDTH + 480, CANVAS_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUndecorated(true);
		setResizable(false);
		setVisible(true);
	}

	private double shortestDistance(Point2D a, Point2D b, Point2D p) {
		double px = b.getX() - a.getX();
		double py = b.getY() - a.getY();
		double temp = (px * px) + (py * py);
		double u = ((p.getX() - a.getX()) * px + (p.getY() - a.getY()) * py) / (temp);
		if (u > 1) {
			u = 1;
		} else if (u < 0) {
			u = 0;
		}
		double x = a.getX() + u * px;
		double y = a.getY() + u * py;

		double dx = x - p.getX();
		double dy = y - p.getY();
		double dist = Math.sqrt(dx * dx + dy * dy);
		return dist;

	}

	public static int getLines(String File) throws IOException {
		try (FileReader input = new FileReader(File); LineNumberReader count = new LineNumberReader(input);) {
			while (count.skip(Long.MAX_VALUE) > 0) {
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

			if (Paused == false) {
				try {
					InputLines = getLines(InputString);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				loadInput(InputString);
				neuralNetwork.setInput(new ArrayList<Double>(Arrays.asList(Input)));
				neuralNetwork.feedForward();
				neuralNetwork.feedBackward();
			}

			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHints(
					new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

			g2.setColor(Color.BLACK);
			g2.drawRect(10, 10, CANVAS_WIDTH - 20, CANVAS_HEIGHT - 20);

			for (Connection connection : neuralNetwork.connections) {
				Drawing.drawLine(g2, (int) connection.N1.getLocation().getX(), (int) connection.N1.getLocation().getY(),
						(int) connection.N2.getLocation().getX(), (int) connection.N2.getLocation().getY(),
						(int) (connection.getValue() * 5), connection.getColor());
			}

			for (Layer layer : neuralNetwork.layers) {
				for (Neuron neuron : layer.neurons) {
					g2.setColor(neuron.getColor());
					Drawing.drawCircle(g2, neuron.getLocation().x, neuron.getLocation().y, 100);
					g2.setColor(new Color(0, 0, 0));
					Drawing.drawText(g2,
							new Rectangle(neuron.getLocation().x - 50, neuron.getLocation().y - 50, 100, 100),
							Double.toString(((double) Math.round(neuron.getValue() * 100d) / 100d)),
							new Font("Monaco", 1, 20));
				}
			}

			Drawing.Added = true;

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Layer layer : neuralNetwork.layers) {
				for (Neuron neuron : layer.neurons) {
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
			g2.setRenderingHints(
					new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

			g2.drawRect(10, 10, GRAPH_WIDTH + 20, GRAPH_HEIGHT + 20);
			g2.setColor(Color.WHITE);
			g2.drawLine(20, GRAPH_HEIGHT + 20, GRAPH_WIDTH + 20, GRAPH_HEIGHT + 20);
			g2.drawLine(20, GRAPH_HEIGHT + 20, 20, 20);

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
			g2.setRenderingHints(
					new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

			g2.drawRect(10, 10, GRAPH_WIDTH + 20, GRAPH_HEIGHT + 110);
			g2.setColor(Color.WHITE);
			g2.drawLine(20, GRAPH_HEIGHT + 110, GRAPH_WIDTH + 20, GRAPH_HEIGHT + 110);
			g2.drawLine(20, GRAPH_HEIGHT + 110, 20, 20);

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
			g2.setRenderingHints(
					new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

			g2.drawRect(10, 10, CONTROL_WIDTH - 20, CONTROL_HEIGHT - 20);

			repaint();
		}
	}

	private class ViewNeuron extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(new Color(0, 66, 103));

			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHints(
					new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

			switch (SelectedNeuronType) {
			case 0:
				g2.drawString("Nothing Selected", 20, 30);
				break;
			case 1:
				g2.drawImage(BasicNeuronImage, null, 0, 0);
				break;
			case 2:
				g2.drawImage(InputNeuronImage, null, 0, 0);
				break;
			case 3:
				g2.drawImage(OutputNeuronImage, null, 0, 0);
				break;
			case 4:
				g2.drawImage(BiasNeuronImage, null, 0, 0);
				break;
			}
			g2.drawRect(10, 10, NEURON_INFO_WIDTH - 20, NEURON_INFO_HEIGHT - 20);

			repaint();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new Run();
				} catch (TransformerException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				}
			}
		});
	}
}