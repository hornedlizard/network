package thread;

public class AlphabatThread extends Thread {

	@Override
	public void run() {
		for (char c = 'a'; c <='z'; c++) {
			System.out.print(c);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
