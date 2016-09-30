import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.BitSet;
import java.util.Base64.Encoder;

import javax.imageio.ImageIO;

public class HashCalculator implements Runnable {
	private File file;
	private int w=16;//缩略图宽度。
	private int h=16;//缩略图高度。
	
	public HashCalculator(File file,int width,int height){
		this.file=file;
		this.w=width;
		this.h=height;
	}

	@Override
	public void run() {
		ImageHash imageHash=new ImageHash();
		imageHash.setName(file.getName());
		imageHash.setHash(CalculateImageHash(file));
		try {
			XMLUtils.hashQueue.put(imageHash);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * 计算图片hash值。
	 * @param file
	 * 图片文件。
	 * */
	private String CalculateImageHash(File file){
		Encoder encoder=Base64.getEncoder();
		BufferedImage image=null;
		try {
			image=ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//缩小图片，颜色转换为灰度。
		double sx=(double) w/image.getWidth();
		double sy=(double) h/image.getHeight();
		BufferedImage target=new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D graphics2d=target.createGraphics();
		graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2d.drawRenderedImage(image, AffineTransform.getScaleInstance(sx, sy));
		graphics2d.dispose();

		//计算灰度平均值。
		long gray_avg=0;
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				gray_avg=gray_avg+target.getRGB(i, j);
			}
		}
		gray_avg=gray_avg/w/h;
		BitSet image_hash=new BitSet();
		//计算hash值，灰度大于平均值为1.
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
