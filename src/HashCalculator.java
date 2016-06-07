import java.io.File;

public class HashCalculator implements Runnable {
	private File file;
	
	public HashCalculator(File file){
		this.file=file;
	}

	@Override
	public void run() {
		ImageHash imageHash=new ImageHash();
		imageHash.setName(file.getName());
		imageHash.setHash(SimilarImage.ImageHash(file));
		try {
			XMLUtils.hashQueue.put(imageHash);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
