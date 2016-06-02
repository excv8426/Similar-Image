import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XMLUtils {
	
	public static void createXML(String path){
		DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
		builderFactory.setIgnoringElementContentWhitespace(true);
		TransformerFactory transformerFactory=TransformerFactory.newInstance();
		Transformer transformer=null;
		DocumentBuilder builder=null;
		try {
			builder=builderFactory.newDocumentBuilder();
			transformer=transformerFactory.newTransformer();
			Document document=builder.newDocument();
			document.appendChild(document.createElement("root"));
			transformer.transform(new DOMSource(document), new StreamResult(new File(path)));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveHash(Image[] images,String path){
		DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
		builderFactory.setIgnoringElementContentWhitespace(true);
		TransformerFactory transformerFactory=TransformerFactory.newInstance();
		Transformer transformer=null;
		DocumentBuilder builder=null;
		Document document=null;
		try {
			builder=builderFactory.newDocumentBuilder();
			transformer=transformerFactory.newTransformer();
			document=builder.parse(path);
			Node rootnode=document.getElementsByTagName("root").item(0);
			Element imagehash=null;
			for (int i = 0; i < images.length; i++) {
				imagehash=document.createElement("imagehash");
				System.out.println(imagehash);
				System.out.println(i);
				System.out.println(images[i]);
				imagehash.setAttribute("name", images[i].getName());
				imagehash.setTextContent(new String(images[i].getHash().toByteArray(), "UTF-8"));
				rootnode.appendChild(imagehash);
			}
			transformer.transform(new DOMSource(document), new StreamResult(new File(path)));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public static void readXML(String path){
	       SAXReader reader = new SAXReader();
	       try {
			org.dom4j.Document document = reader.read(path);

	        org.dom4j.Element root = document.getRootElement();
	        for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
	        	org.dom4j.Element element = (org.dom4j.Element) i.next();
	            System.out.println(element.toString());
	            // do something
	        }
            



		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	
}
