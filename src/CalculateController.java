import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CalculateController implements Runnable {
	private String imagepath;
	
	public CalculateController(String imagepath){
		this.imagepath=imagepath;
	}
	@Override
	public void run() {
		File dir=new File(imagepath);
		File[] files=dir.listFiles();
		ExecutorService executorService=Executors.newFixedThreadPool(4);
		for (File file : files) {
			executorService.execute(new HashCalculator(file));
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XMLUtils.queueinputEnd();
	}

}
