package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
	private static final String SERVER_IP = "192.168.1.12";
	private static final int SERVER_PORT = 6000;
	
	public static void main(String[] args) {
		Socket socket = new Socket();
		Scanner scanner = new Scanner(System.in);
		
		try {
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			
			// try 밖으로 뺴지 않아도 소캣을 닫으면서 닫힘
			BufferedReader br = new BufferedReader(
							new InputStreamReader(
									//System.in, "utf-8"));
									socket.getInputStream(), "utf-8"));
			// 개행 처리를 위해, 문자로 변환해줌
			PrintWriter pw = new PrintWriter(
							new OutputStreamWriter(
									// parameter의 true는 autoFlush
									socket.getOutputStream(), "utf-8"), true);
			
			while (true) {
				System.out.print(">> ");
				//String message = br.readLine();
				String message = scanner.nextLine();
				if ("exit".equals(message)) {
					break;
				}
				
				// exit가 아니면 소켓(서버)으로 보내기
				pw.println(message);
				
				// 서버에서 보낸 데이터 읽기
				// readLine()으로 한번 읽으면 buffer가 비워지는 듯
				String echoMessage = br.readLine();
				// 연결 확인
				if (echoMessage == null) {
					System.out.println("[client] disconnected by server.");
					break;
				}
				
				System.out.println("<< "+echoMessage);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket.isClosed() == false) {
					System.out.println("클라이언트 소켓 닫기");
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			scanner.close();
		}
		
	}
}
