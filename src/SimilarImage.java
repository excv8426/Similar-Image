import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimilarImage {

	public static void main(String[] args) {
		String imagepath="C:\\新建文件夹";
		String outputpath="D:\\imagehash.xml";
		ExecutorService executorService=Executors.newSingleThreadExecutor();
		executorService.execute(new CalculateController(imagepath));
		executorService.shutdown();
		XMLUtils.createXML(outputpath);
		XMLUtils.readXML(outputpath);
	}

}
