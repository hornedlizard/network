package echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class EchoServerReceiveThread extends Thread {
	private Socket socket;
	public EchoServerReceiveThread(Socket socket) {
		// 서버에서 클라이언트의 연결을 accept() 해서 생성한 socket을 받음
		this.socket = socket;
	}
	@Override
	public void run() {
		// 4. 연결 성공 시
		InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
		int remoteHostPort = remoteSocketAddress.getPort();
		String remoteHostAddress = remoteSocketAddress.getAddress().getHostAddress();
		consoleLog("connected from " + remoteHostAddress + " : " + remoteHostPort);
		
		try {
			// 5. IO Stream 받아오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			while (true) {
				// 6. 데이터 읽기(read)
				byte[] buffer = new byte[256];
				// read() 하면서 blocking이 됨,
				// 이때 서버에 새로운 연결요청이 들어와도 할 수 없음 (쓰레드로 구현해야함)
				int readByteCount = is.read(buffer);
				// 정상종료 시 (read하면 -1이 반환됨)
				if (readByteCount == -1) {
					consoleLog("[server] disconnected by client");
					break;
				}

				// 받은 byte열을 인코딩
				String data = new String(buffer, 0, readByteCount, "utf-8");
				consoleLog("[server] received: " + data);

				// 7. 데이터 쓰기 (telnet에 에코)
				os.write(data.getBytes("utf-8"));

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null && socket.isClosed() == false) {
					System.out.println("소켓 닫기");
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void consoleLog(String log) {
		System.out.println("[server: " + getId() + "] " + log);
	}
}
