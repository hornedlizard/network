package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {

	private static final int SERVER_PORT = 5000;
	public static void main(String[] args) {
		
		ServerSocket serverSocket = null;
		try {
			// 1. 서버소켓 생성
			serverSocket = new ServerSocket();
			
			// 2. 바인딩(binding) 소켓을 호스트의 포트와 연결
			String localhostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhostAddress, SERVER_PORT));
			System.out.println("[server] binding "+localhostAddress+" : "+SERVER_PORT);
			
			// 3. 연결 요청 기다림(accept) blocking 되어서 대기 중
			Socket socket = serverSocket.accept();
			
			// 4. 연결 성공 시
			InetSocketAddress remoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
			int remoteHostPort = remoteSocketAddress.getPort();
			String remoteHostAddress = remoteSocketAddress.getAddress().getHostAddress();
			System.out.println("[server] connected from "+remoteHostAddress+" : "+remoteHostPort);
			
			try {
				// 5. IO Stream 받아오기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				while (true) {
					// 6. 데이터 읽기(read)
					byte[] buffer = new byte[256];
					// read() 하면서 blocking이 됨,
					// 이때 서버에 새로운 연결요청이 들어와도 연결 할 수 없음 (쓰레드로 구현해야함)
					int readByteCount = is.read(buffer); // 클라이언트에서 보낸 문자열 받음
					System.out.println((char)buffer[0]);
					// 정상종료 시 (read하면 -1이 반환됨)
					if (readByteCount == -1) {
						System.out.println("[server] disconnected by client");
						break;
					}
					// 받은 byte열을 인코딩
					String data = new String(buffer, 0, readByteCount, "utf-8");
					System.out.println("[server] received: "+data);
					
					// 7. 데이터 쓰기 (telnet에 에코)
					os.write(data.getBytes("utf-8"));
					
				}
				
			} catch (SocketException e) {
				// 정상적으로 소켓을 닫지 않고 종료했을 때 read하면 socketException
				System.out.println("[server] sudden closed by client");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
