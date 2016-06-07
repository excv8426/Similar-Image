import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.BitSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

public class SimilarImage {
	private static int w=16;
	private static int h=16;
	
	public static void main(String[] args) {
		String imagepath="C:\\新建文件夹";
		String outputpath="D:\\imagehash.xml";
		ExecutorService executorService=Executors.newSingleThreadExecutor();
		executorService.execute(new CalculateController(imagepath));
		executorService.shutdown();
		System.out.println("写入XML");
		XMLUtils.createXML(outputpath);
		XMLUtils.readXML(outputpath);
	}
	
	
	public static String ImageHash(File file){
		Encoder encoder=Base64.getEncoder();
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

		long gray_avg=0;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				gray_avg=gray_avg+target.getRGB(i, j);
			}
		}
		gray_avg=gray_avg/w/h;
		BitSet image_hash=new BitSet();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (gray_avg>target.getRGB(i, j)) {
				} else {
					image_hash.set((i+1)*(j+1));
				}
				
			}
		}
		return encoder.encodeToString(image_hash.toByteArray());
	}

}
