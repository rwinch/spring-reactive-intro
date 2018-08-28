package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Rob Winch
 */
public class Main {
	public static void main(String[] args) throws Exception {
		int NUM_THREADS = 15000;
		long SLEEP = TimeUnit.MINUTES.toMillis(1);
		List<Thread> threads = new ArrayList<>();
		for(int i = 0; i< NUM_THREADS; i++) {
			if(i % 100 == 0) {
				System.out.println("Created " + i + " threads");
			}
			Thread t = new Thread(() -> {
				try {
					Thread.sleep(SLEEP);
				}
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			});
			t.start();
			threads.add(t);
		}
	}
}


