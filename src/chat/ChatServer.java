package chat;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private final static int SERVER_PORT = 6000;

	public static void main(String[] args) {
		List<Writer> writers = new ArrayList<>();
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket();
			String localhostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhostAddress, SERVER_PORT));
			
			while (true) {
				Socket socket = serverSocket.accept();
				new ChatServerProcessThread(socket, writers).start();
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			ClosingStream.closingStream(serverSocket);
		}
		
	}
	

}
