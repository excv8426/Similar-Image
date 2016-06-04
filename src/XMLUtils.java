import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.BitSet;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
		Encoder encoder=Base64.getEncoder();
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
				imagehash.setTextContent(encoder.encodeToString(images[i].getHash().toByteArray()));
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
		ImageDifference difference=null;
		Set<ImageDifference> differences=new TreeSet<>();
		BitSet imagehash=null;
		Decoder decoder=Base64.getDecoder();
		
	    SAXReader reader = new SAXReader();
		org.dom4j.Document document=null;
		try {
			document = reader.read(path);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
        org.dom4j.Element root = document.getRootElement();
		@SuppressWarnings("rawtypes")
		Iterator i = root.elementIterator();
        @SuppressWarnings("rawtypes")
		Iterator j=null;
        int i_ix=0;
        int j_ix=0;
        org.dom4j.Element element_i=null;
        org.dom4j.Element element_j=null;
        
        while (i.hasNext()) {
        	j_ix=0;
        	element_i = (org.dom4j.Element) i.next();
            /*System.out.println(element_i.attributeValue("name"));
            System.out.println(element_i.getText());*/
            imagehash=BitSet.valueOf(decoder.decode(element_i.getText()));
            j=root.elementIterator();
            while (j.hasNext()) {
				element_j = (org.dom4j.Element) j.next();
				if (i_ix!=j_ix) {
					difference=new ImageDifference();
					difference.setName1(element_i.attributeValue("name"));
					difference.setName2(element_j.attributeValue("name"));
					BitSet hash2=BitSet.valueOf(decoder.decode(element_j.getText()));
					hash2.xor(imagehash);
					difference.setDifference(hash2.cardinality());
					differences.add(difference);
				}
				j_ix++;
			}
            i_ix++;
		}
        
        for (ImageDifference imagedifference : differences) {
			System.out.println(imagedifference.toString());
		}

	}
	
	
}
