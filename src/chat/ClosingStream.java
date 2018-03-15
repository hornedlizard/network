package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClosingStream {

	public static void closingStream(Socket socket) {
		try {
			if (socket != null && socket.isClosed() == false) {
				socket.close();
				System.out.println("소켓 닫기");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static void closingStream(ServerSocket socket) {
		try {
			if (socket != null && socket.isClosed() == false) {
				socket.close();
				System.out.println("소켓 닫기");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
