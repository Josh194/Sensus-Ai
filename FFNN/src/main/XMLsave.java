package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

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

	public static void writeDocumentToFile(int[] Neurons, int[] Bias, Double[] Connection, File file)
			throws TransformerException, ParserConfigurationException {
		makeDoc(Neurons, Bias, Connection);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		DOMSource source = new DOMSource(save);
		StreamResult result = new StreamResult(file);
		transformer.transform(source, result);
	}

	private static void makeDoc(int[] Neurons, int[] Bias, Double[] Connections) throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		save = builder.newDocument();

		Element root = save.createElement("root");
		save.appendChild(root);

		Element neurons = save.createElement("Neurons");
		root.appendChild(neurons);

		for (int NumNeurons : Neurons) {
			Element LayerNeurons = save.createElement("NumNeurons");
			neurons.appendChild(LayerNeurons);
			LayerNeurons.insertBefore(save.createTextNode(Integer.toString(NumNeurons)), LayerNeurons.getLastChild());
		}

		Element bias = save.createElement("Bias");
		root.appendChild(bias);

		for (int HasBias : Bias) {
			Element BiasNeurons = save.createElement("HasBias");
			bias.appendChild(BiasNeurons);
			BiasNeurons.insertBefore(save.createTextNode(Integer.toString(HasBias)), BiasNeurons.getLastChild());
		}

		Element connections = save.createElement("ConnectionValues");
		root.appendChild(connections);

		for (Double ConnectionValue : Connections) {
			Element Value = save.createElement("ConnectionValue");
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
