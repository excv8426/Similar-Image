import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ImageSearch {
	private static int w=8;
	private static int h=8;
	
	public static void main(String[] args) {
		String imagepath="C:\\新建文件夹";
		String outputpath="D:\\imagehash.xml";
		/*File dir=new File(imagepath);
		File[] files=dir.listFiles();
		Image[] images=new Image[files.length];
		int i=0;
		for (File file : files) {
			//System.out.println(images[i]);
			images[i]=new Image();
			images[i].setName(file.getName());
			images[i].setHash(ImageHash(file));
			i++;
		}
		XMLUtils.createXML(outputpath);
		XMLUtils.saveHash(images, outputpath);*/
		XMLUtils.readXML(outputpath);
	}
	
	
	public static BitSet ImageHash(File file){
		BufferedImage image=null;
		try {
			image=ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		double sx=(double) w/image.getWidth();
		double sy=(double) h/image.getHeight();
		System.out.println(sx);
		BufferedImage target=new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D graphics2d=target.createGraphics();
		graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2d.drawRenderedImage(image, AffineTransform.getScaleInstance(sx, sy));
		graphics2d.dispose();
		/*try {
			ImageIO.write(target, "jpg", new File("C:\\新建文件夹\\"+UUID.randomUUID().toString()+".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		//System.out.println(image.getRGB(10, 10));
		//System.out.println(target.getRGB(10, 10));
		long gray_avg=0;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				gray_avg=gray_avg+target.getRGB(i, j);
			}
		}
		gray_avg=gray_avg/w/h;
		//byte[][] image_hash=new byte[w][h];
		BitSet image_hash=new BitSet();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (gray_avg>target.getRGB(i, j)) {
					//image_hash[i][j]=0;
				} else {
					//image_hash[i][j]=1;
					image_hash.set((i+1)*(j+1));
				}
				
			}
		}
		
		return image_hash;
	}
	
	public static void savaHash(){
		DocumentBuilderFactory builderFactory=DocumentBuilderFactory.newInstance();
		builderFactory.setIgnoringElementContentWhitespace(true);
		TransformerFactory transformerFactory=TransformerFactory.newInstance();
		Transformer transformer=null;
		DocumentBuilder builder=null;
		try {
			builder=builderFactory.newDocumentBuilder();
			transformer=transformerFactory.newTransformer();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		Document document=builder.newDocument();
		Element hash=document.createElement("filename");
		hash.setTextContent("hash");
		document.appendChild(hash);
		try {
			transformer.transform(new DOMSource(document), new StreamResult(new File("D:\\imagehash.xml")));
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

}
