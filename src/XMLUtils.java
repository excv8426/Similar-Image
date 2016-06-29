import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.BitSet;
import java.util.Base64.Decoder;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.LinkedBlockingDeque;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class XMLUtils {
	
	public static LinkedBlockingDeque<ImageHash> hashQueue=new LinkedBlockingDeque<>(100);
	
	/**
	 * 把hashQueue保存到XML文件。
	 * @param xmlpath
	 * XML文件路径，例如D:\\imagehash.xml。
	 * */
	public static void createXML(String xmlpath){
		File file=new File(xmlpath);
		FileOutputStream outputStream=null;
		XMLStreamWriter writer=null;
		ImageHash imageHash=null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			outputStream = new FileOutputStream(file);
			writer=XMLOutputFactory.newInstance().createXMLStreamWriter(outputStream);
			writer.writeStartDocument("UTF-8","1.0");
			writer.writeEndDocument();
			writer.writeStartElement("root");
			while (true) {
				imageHash=hashQueue.take();
				if (imageHash.getHash().equals("!")) {
					break;
				} else {
					writer.writeStartElement("imagehash");
					writer.writeAttribute("id", imageHash.getName());
					writer.writeCharacters(imageHash.getHash());
					writer.writeEndElement();
				}
			}
			writer.writeEndElement();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.flush();
				writer.close();
				outputStream.flush();
				outputStream.close();
			} catch (XMLStreamException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
	
	/**
	 * 遍历XML文件，比较hash值并排序输出。
	 * @param path
	 * XML文件路径，例如D:\\imagehash.xml
	 * */
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
            imagehash=BitSet.valueOf(decoder.decode(element_i.getText()));
            j=root.elementIterator();
            while (j.hasNext()) {
				element_j = (org.dom4j.Element) j.next();
				if (i_ix<j_ix) {
					difference=new ImageDifference();
					difference.setName1(element_i.attributeValue("id"));
					difference.setName2(element_j.attributeValue("id"));
					BitSet hash2=BitSet.valueOf(decoder.decode(element_j.getText()));
					hash2.xor(imagehash);
					difference.setDifference(hash2.cardinality());
					differences.add(difference);
				}
				j_ix++;
			}
            i_ix++;
		}
        int out=0;
        for (ImageDifference imagedifference : differences) {
        	out++;
        	if (out==1000000) {
				break;
			}
			System.out.println(imagedifference.toString());
		}

	}
	
	/**
	 * 在hashQueue插入队列结束标识。
	 * name=="" hash=="!"
	 * */
	public static void queueinputEnd(){
		ImageHash imageHash=new ImageHash();
		imageHash.setName("");
		imageHash.setHash("!");
		try {
			hashQueue.put(imageHash);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
