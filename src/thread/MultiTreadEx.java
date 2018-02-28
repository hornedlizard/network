package thread;

public class MultiTreadEx {

	public static void main(String[] args) {		
//		for (int i = 0; i < 10; i++) {
//			System.out.print(i);
//		}
//		for (char c = 'a'; c <='z'; c++) {
//			System.out.print(c);
//		}
		
		// 스레드 상속 받은 객체
		Thread thread1 = new AlphabatThread();
		// runnable을 구현한 객체
		Thread thread2 = new Thread(new DigitThread());
		
		thread1.start();
		thread2.start();
		
	}
}
