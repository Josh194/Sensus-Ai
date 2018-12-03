package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XMLsave {

	public static Document save;

	public static void writeDocumentToFile(ArrayList<ArrayList<String>> Neurons, Double[] Connections, File file)
			throws TransformerException, ParserConfigurationException {
		makeDoc(Neurons, Connections);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(save);
		StreamResult result = new StreamResult(file);
		transformer.transform(source, result);
	}

	private static void makeDoc(ArrayList<ArrayList<String>> Neurons, Double[] Connections) throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		save = builder.newDocument();

		Element root = save.createElement("root");
		save.appendChild(root);

		Element neurons = save.createElement("Neurons");
		root.appendChild(neurons);

		for (ArrayList<String> layer: Neurons) {
			Element typeLayer = save.createElement("Layer");
			neurons.appendChild(typeLayer);
			
			for (String neuronType : layer) {
				Element neuron = save.createElement("Neuron");
				typeLayer.appendChild(neuron);
				neuron.insertBefore(save.createTextNode(neuronType), neuron.getLastChild());
			}
		}

		Element connections = save.createElement("Connections");
		root.appendChild(connections);

		for (Double ConnectionValue : Connections) {
			Element Value = save.createElement("Connection");
			connections.appendChild(Value);
			Value.insertBefore(save.createTextNode(Double.toString(ConnectionValue)), Value.getLastChild());
		}
	}

	public static Document asXML(File file) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();

		return builder.parse(file);
	}
}
