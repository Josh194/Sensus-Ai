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
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
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
import main.NN.Neurons.Neuron;

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
	public static final int OUTPUT_HEIGHT = screenSize.height - GRAPH_HEIGHT - 80;
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
	public static int[] NeuronComposition = new int[] { 2, 3, 5, 5, 3, 1 };
	public static int[] BiasComposition = new int[] { 0, 1, 1, 0, 0, 0 };
	public static NeuralNetwork neuralNetwork = new NeuralNetwork(NeuronComposition, BiasComposition, 0.01);
	public static int AF = 0;
	private static int InputLine = 0;
	private static int InputLines;
	private static Double[] Input = new Double[neuralNetwork.layers.get(0).neurons.size() + 1];
	private static Connection connectionToRemove = null;
	private static Integer NeuronLayer = null;
	private static Double MaxRange = 0d;
	private static Double MinRange = 0d;

	// Gui Variables
	private JPanel container = new JPanel();
	private DrawCanvas canvas;
	private GraphPanel graphPanel;
	private JPanel controlPanel;
	private NNCustomization customizationPanel;
	private OutputPanel outputPanel;
	private ViewNeuron viewNeuron;
	private MMenu mMenu;

	private JTextField LearningRate = new JTextField();
	private JTextField TestInput = new JTextField();
	private JButton InputLocation = new JButton("Load Input");
	private JButton SaveNN = new JButton("Save");
	private JButton LoadNN = new JButton("Load");
	private String[] ActivationFunctions = { "Sigmoid", "Gaussian" };
	private JComboBox<String> ChooseActivation = new JComboBox<String>(ActivationFunctions);
	private JButton Pause = new JButton("Pause");
	private JButton outputPanelButton = new JButton("Output Graph");
	private JButton neuronPanelButton = new JButton("Inspect");
	private JButton customizationPanelButton = new JButton("Customize");
	private JButton outputPanelButton2 = new JButton("Output Graph");
	private JButton neuronPanelButton2 = new JButton("Inspect");
	private JButton customizationPanelButton2 = new JButton("Customize");
	private JButton Start = new JButton("Start");
	private JButton ExitMenu = new JButton();
	private JButton Exit = new JButton();

	public Run() throws TransformerException, ParserConfigurationException {

		initGui();

		initMouseListener();

		initActionListeners();

		setTitle("Sensus Ai 1.2.0");
		setSize(CANVAS_WIDTH + 480, CANVAS_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUndecorated(true);
		setResizable(false);
		setVisible(true);
	}

	private void initGui() {

		try {
			BasicNeuronImage = ImageIO.read(getClass().getResourceAsStream("/Graphics/Simple_Neuron.png"));
			BiasNeuronImage = ImageIO.read(getClass().getResourceAsStream("/Graphics/Bias_Neuron.png"));
			InputNeuronImage = ImageIO.read(getClass().getResourceAsStream("/Graphics/Input_Neuron.png"));
			OutputNeuronImage = ImageIO.read(getClass().getResourceAsStream("/Graphics/Output_Neuron.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			ExitMenu.setIcon(new ImageIcon(getClass().getResource("/images/exit.png")));
			Exit.setIcon(new ImageIcon(getClass().getResource("/images/exit.png")));
		} catch (Exception ex) {
			System.out.println(ex);
		}

		Drawing.graphSize = new Dimension(GRAPH_WIDTH, GRAPH_HEIGHT);
		Drawing.graphLocation = new Point(20, GRAPH_HEIGHT);
		Drawing.outputSize = new Dimension(OUTPUT_WIDTH, OUTPUT_HEIGHT);
		Drawing.outputLocation = new Point(20, OUTPUT_HEIGHT);

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
		mMenu = new MMenu();
		mMenu.setPreferredSize(new Dimension(screenSize.width, screenSize.height));

		LearningRate.setBounds(20, 40, 100, 20);
		ChooseActivation.setBounds(20, 290, 200, 20);
		Pause.setBounds(360, 530, 100, 20);
		TestInput.setBounds(20, 530, 100, 20);
		customizationPanelButton.setBounds(CONTROL_WIDTH - 140, 20, 120, 25);
		outputPanelButton.setBounds(CONTROL_WIDTH - 140, 20, 120, 25);
		neuronPanelButton.setBounds(CONTROL_WIDTH - 140, 55, 120, 25);
		customizationPanelButton2.setBounds(CONTROL_WIDTH - 140, 20, 120, 25);
		outputPanelButton2.setBounds(CONTROL_WIDTH - 140, 60, 120, 25);
		neuronPanelButton2.setBounds(CONTROL_WIDTH - 140, 55, 120, 25);
		InputLocation.setBounds(20, 250, 200, 20);
		SaveNN.setBounds(CONTROL_WIDTH - 140, 250, 120, 20);
		LoadNN.setBounds(CONTROL_WIDTH - 140, 290, 120, 20);
		ExitMenu.setBounds(20, 20, 43, 43);
		Exit.setBounds(20, 20, 43, 43);
		Start.setBounds((screenSize.width / 2) - 50, (int) (0.87 * screenSize.height), 100, 20);

		ExitMenu.setOpaque(false);
		ExitMenu.setContentAreaFilled(false);
		ExitMenu.setBorderPainted(false);
		Exit.setOpaque(false);
		Exit.setContentAreaFilled(false);
		Exit.setBorderPainted(false);

		customizationPanel.setLayout(null);
		customizationPanel.add(ChooseActivation);
		customizationPanel.add(Pause);
		customizationPanel.add(LearningRate);
		customizationPanel.add(InputLocation);
		customizationPanel.add(SaveNN);
		customizationPanel.add(LoadNN);
		customizationPanel.add(TestInput);

		JLabel LearningRateLabel = new JLabel("Learning Rate:");
		LearningRateLabel.setBounds(20, 20, 100, 20);
		JLabel TestInputLabel = new JLabel("Test Input:");
		TestInputLabel.setBounds(20, 510, 100, 20);
		JLabel InfoLabel = new JLabel(
				"<html><body style='text-align: center'>For information on how to use this software visit:<br>github.com/Josh194/Ai/wiki</html>");
		InfoLabel.setBounds(115, 480, 250, 100);

		customizationPanel.add(LearningRateLabel);
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

		mMenu.setLayout(null);
		mMenu.add(ExitMenu);
		mMenu.add(Start);

		canvas.setLayout(null);
		canvas.add(Exit);

		controlPanel.add(customizationPanel, "customizationPanel");
		controlPanel.add(outputPanel, "outputPanel");
		controlPanel.add(viewNeuron, "viewNeuron");

		container.add(mMenu);
		container.add(canvas, new GridBagConstraints(0, 0, 2, 3, 0d, 0d, GridBagConstraints.LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		container.add(graphPanel, new GridBagConstraints(2, 1, 1, 1, 0d, 0d, GridBagConstraints.LINE_END,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		container.add(controlPanel, new GridBagConstraints(2, 2, 1, 1, 0d, 0d, GridBagConstraints.FIRST_LINE_END,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		canvas.setVisible(false);
		graphPanel.setVisible(false);
		controlPanel.setVisible(false);

		Container cp = getContentPane();
		cp.add(container);
	}

	public void initMouseListener() {
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
								if (neuron.animationHandler.getLocation().equals(new Point(s.getBounds().getLocation().x + (neuron.animationHandler.getSize() / 2),
										s.getBounds().getLocation().y + (neuron.animationHandler.getSize() / 2)))) {
									if (SwingUtilities.isRightMouseButton(me)) {
										NeuronLayer = neuralNetwork.layers.indexOf(neuron.getLayer());
										SelectedNeuronType = neuron.getType();

										shapes.clear();
										Drawing.Added = false;

										NeuronMenu neuronMenu = new NeuronMenu();
										neuronMenu.show(me.getComponent(), me.getX(), me.getY());
										FoundType = true;
									} else {
										SelectedNeuronType = neuron.getType();
										FoundType = true;
									}
								}
							}
						}
					}

					if (distanceToLine < 5 && !FoundType && SwingUtilities.isRightMouseButton(me)) {
						for (Connection connection : neuralNetwork.connections) {
							if (connection.N1.animationHandler.getLocation().equals((closestLine.getP1()))
									&& connection.N2.animationHandler.getLocation().equals((closestLine.getP2()))) {
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
	}

	public void initActionListeners() {

		ExitMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		Exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mMenu.setVisible(true);
				mMenu.validate();
				canvas.setVisible(false);
				graphPanel.setVisible(false);
				controlPanel.setVisible(false);
			}
		});

		Start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mMenu.setVisible(false);
				mMenu.invalidate();
				canvas.setVisible(true);
				graphPanel.setVisible(true);
				controlPanel.setVisible(true);
				Drawing.startAnimation();
			}
		});

		ChooseActivation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AF = ChooseActivation.getSelectedIndex();
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

		InputLocation.addActionListener(new ActionListener() {

			JFileChooser fileChooser = new JFileChooser();

			@Override
			public void actionPerformed(ActionEvent e) {

				Input = new Double[neuralNetwork.layers.get(0).neurons.size() + 1];

				fileChooser.setFileFilter((new FileNameExtensionFilter("text files (*.txt)", "txt")));
				int returnVal = fileChooser.showOpenDialog(fileChooser);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					InputString = file.getAbsolutePath();
					MinRange = GetNumberFromFile.getSmallestNumber(file.getAbsolutePath());
					MaxRange = GetNumberFromFile.getLargestNumber(file.getAbsolutePath());
					neuralNetwork.setMinRange(MinRange);
					neuralNetwork.setMaxRange(MaxRange);
				} else {
					System.out.println("File access cancelled by user.");
				}
			}
		});

		SaveNN.addActionListener(new ActionListener() {

			JFileChooser fileChooser = new JFileChooser();

			@Override
			public void actionPerformed(ActionEvent e) {

				fileChooser.setFileFilter((new FileNameExtensionFilter("xml files (*.xml)", "xml")));
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
							if (neuron.getType() == 4) {
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

				fileChooser.setFileFilter((new FileNameExtensionFilter("xml files (*.xml)", "xml")));
				int returnVal = fileChooser.showOpenDialog(fileChooser);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
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
						Bias[i] = Integer.parseInt(eElement.getElementsByTagName("HasBias").item(i).getTextContent());
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
				} else {
					System.out.println("File access cancelled by user.");
				}
			}
		});

		Action LearningRateChange = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				neuralNetwork.TeachingRate = Double.parseDouble(LearningRate.getText());
			}
		};

		Action TestInputAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {

				for (Layer layer : neuralNetwork.layers) {
					for (Neuron neuron : layer.neurons) {
						if (neuron.getType() != 4) {
							neuron.setError(0.0);
							neuron.setValue(0.0);
						}
					}
				}

				List<Double> Input = DoubleStream
						.of(Arrays.stream(TestInput.getText().substring(1, TestInput.getText().length() - 1).split(","))
								.map(String::trim).mapToDouble(Double::parseDouble).toArray())
						.boxed().collect(Collectors.toList());
				neuralNetwork.setInput(new ArrayList<Double>(Input));
				neuralNetwork.feedForward();
			}
		};

		LearningRate.addActionListener(LearningRateChange);
		TestInput.addActionListener(TestInputAction);
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
		for (int i = 0; i <= neuralNetwork.layers.get(0).neurons.size() - BiasComposition[0]; i++) {
			try {
				Input[i] = Double.parseDouble(Files.readAllLines(Paths.get(File)).get(InputLine));
				if (InputLine == InputLines) {
					InputLine = 0;
				} else {
					if (InputLine % neuralNetwork.layers.get(0).neurons.size() - BiasComposition[0] == 0) {
						if (InputLine!=0) {
							InputLine += neuralNetwork.layers.get(neuralNetwork.layers.size()-1).neurons.size();
						} else {
							InputLine++;
						}
					} else {
						InputLine++;
					}
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
				Drawing.drawLine(g2, (int) connection.N1.animationHandler.getLocation().getX(), (int) connection.N1.animationHandler.getLocation().getY(),
						(int) connection.N2.animationHandler.getLocation().getX(), (int) connection.N2.animationHandler.getLocation().getY(),
						(int) (connection.getValue() * 5),
						(-2 * neuralNetwork.layers.indexOf(connection.N1.getLayer())) - connection.RandomAnimOffset,
						connection.getColor());
			}

			for (Layer layer : neuralNetwork.layers) {
				for (Neuron neuron : layer.neurons) {
					g2.setColor(neuron.animationHandler.getColor());
					Drawing.drawCircle(g2, neuron.animationHandler.getLocation().x, neuron.animationHandler.getLocation().y, neuron.animationHandler.getSize(),
							-2 * neuralNetwork.layers.indexOf(neuron.getLayer()));
					g2.setColor(new Color(0, 0, 0));
					Drawing.drawText(g2,
							new Rectangle(neuron.animationHandler.getLocation().x - 50, neuron.animationHandler.getLocation().y - 50, 100, 100),
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

			if (Paused == false) {
				for (Layer layer : neuralNetwork.layers) {
					for (Neuron neuron : layer.neurons) {
						if (neuron.getType() != 4) {
							neuron.setError(0.0);
							neuron.setValue(0.0);
						}
					}
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

			g2.drawRect(10, 10, OUTPUT_WIDTH + 20, OUTPUT_HEIGHT + 20);
			g2.setColor(Color.WHITE);
			g2.drawLine(20, OUTPUT_HEIGHT + 20, OUTPUT_WIDTH + 20, OUTPUT_HEIGHT + 20);
			g2.drawLine(20, OUTPUT_HEIGHT + 20, 20, 20);
			int ZeroHeight = 20 + (int) (((0 - neuralNetwork.getMinRange())
					/ (neuralNetwork.getMaxRange() - neuralNetwork.getMinRange())) * OUTPUT_HEIGHT);
			g2.drawLine(20, OUTPUT_HEIGHT - ZeroHeight + 40, OUTPUT_WIDTH + 20, OUTPUT_HEIGHT - ZeroHeight + 40);

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

	private class MMenu extends JPanel {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBackground(new Color(0, 66, 103));

			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHints(
					new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

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

	private class NeuronMenu extends JPopupMenu {
		JMenuItem SimpleN;
		JMenuItem BiasN;
		JMenuItem InputN;
		JMenuItem OutputN;
		JMenuItem RemoveN;
		JMenuItem AddL;
		JMenuItem RemoveL;

		public NeuronMenu() {
			SimpleN = new JMenuItem("Add Hidden Neuron");
			BiasN = new JMenuItem("Add Bias Neuron");
			InputN = new JMenuItem("Add Input Neuron");
			OutputN = new JMenuItem("Add Output Neuron");
			RemoveN = new JMenuItem("Remove Neuron");
			AddL = new JMenuItem("Add Layer");
			RemoveL = new JMenuItem("Remove Layer");

			add(SimpleN);
			add(BiasN);
			add(InputN);
			add(OutputN);
			add(RemoveN);
			add(AddL);
			add(RemoveL);

			SimpleN.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NeuronComposition[NeuronLayer] = NeuronComposition[NeuronLayer] + 1;

					shapes.clear();
					Drawing.Added = false;
					Drawing.animationX = -5d;
					Drawing.animation = new Timer();
					Drawing.startAnimation();

					neuralNetwork = new NeuralNetwork(NeuronComposition, BiasComposition, neuralNetwork.TeachingRate);
				}
			});

			BiasN.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					BiasComposition[NeuronLayer] = BiasComposition[NeuronLayer] + 1;

					shapes.clear();
					Drawing.Added = false;
					Drawing.animationX = -5d;
					Drawing.animation = new Timer();
					Drawing.startAnimation();

					neuralNetwork = new NeuralNetwork(NeuronComposition, BiasComposition, neuralNetwork.TeachingRate);
				}
			});

			InputN.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NeuronComposition[NeuronLayer] = NeuronComposition[NeuronLayer] + 1;

					shapes.clear();
					Drawing.Added = false;
					Drawing.animationX = -5d;
					Drawing.animation = new Timer();
					Drawing.startAnimation();

					neuralNetwork = new NeuralNetwork(NeuronComposition, BiasComposition, neuralNetwork.TeachingRate);
				}
			});

			OutputN.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NeuronComposition[NeuronLayer] = NeuronComposition[NeuronLayer] + 1;

					shapes.clear();
					Drawing.Added = false;
					Drawing.animationX = -5d;
					Drawing.animation = new Timer();
					Drawing.startAnimation();

					neuralNetwork = new NeuralNetwork(NeuronComposition, BiasComposition, neuralNetwork.TeachingRate);
				}
			});

			RemoveN.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					switch (SelectedNeuronType) {
					case 1:
						NeuronComposition[NeuronLayer] = NeuronComposition[NeuronLayer] - 1;
						break;
					case 2:
						NeuronComposition[NeuronLayer] = NeuronComposition[NeuronLayer] - 1;
						break;
					case 3:
						NeuronComposition[NeuronLayer] = NeuronComposition[NeuronLayer] - 1;
						break;
					case 4:
						BiasComposition[NeuronLayer] = BiasComposition[NeuronLayer] - 1;
						break;
					}

					shapes.clear();
					Drawing.Added = false;
					Drawing.animationX = -5d;
					Drawing.animation = new Timer();
					Drawing.startAnimation();

					neuralNetwork = new NeuralNetwork(NeuronComposition, BiasComposition, neuralNetwork.TeachingRate);
				}
			});

			AddL.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					int[] temp = new int[NeuronComposition.length + 1];

					for (int i = 0; i < NeuronComposition.length + 1; i++) {
						if (i == NeuronLayer + 1) {
							temp[i] = 1;
						} else if (i <= NeuronLayer) {
							temp[i] = NeuronComposition[i];
						} else if (i > NeuronLayer) {
							temp[i] = NeuronComposition[i - 1];
						}
					}

					NeuronComposition = temp;
					temp = new int[BiasComposition.length + 1];

					for (int i = 0; i < BiasComposition.length + 1; i++) {
						if (i == NeuronLayer + 1) {
							temp[i] = 0;
						} else if (i <= NeuronLayer) {
							temp[i] = BiasComposition[i];
						} else if (i > NeuronLayer) {
							temp[i] = BiasComposition[i - 1];
						}
					}

					BiasComposition = temp;

					shapes.clear();
					Drawing.Added = false;
					Drawing.animationX = -5d;
					Drawing.animation = new Timer();
					Drawing.startAnimation();

					neuralNetwork = new NeuralNetwork(NeuronComposition, BiasComposition, neuralNetwork.TeachingRate);
				}
			});

			RemoveL.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					int[] temp = new int[NeuronComposition.length - 1];

					for (int i = 0; i < NeuronComposition.length - 1; i++) {
						if (i < NeuronLayer) {
							temp[i] = NeuronComposition[i];
						} else if (i >= NeuronLayer) {
							temp[i] = NeuronComposition[i + 1];
						}
					}

					NeuronComposition = temp;
					temp = new int[BiasComposition.length - 1];

					for (int i = 0; i < BiasComposition.length - 1; i++) {
						if (i < NeuronLayer) {
							temp[i] = BiasComposition[i];
						} else if (i >= NeuronLayer) {
							temp[i] = BiasComposition[i + 1];
						}
					}

					BiasComposition = temp;

					shapes.clear();
					Drawing.Added = false;
					Drawing.animationX = -5d;
					Drawing.animation = new Timer();
					Drawing.startAnimation();

					neuralNetwork = new NeuralNetwork(NeuronComposition, BiasComposition, neuralNetwork.TeachingRate);
				}
			});
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