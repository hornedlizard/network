package chat;

import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ChatServerProcessThread extends Thread{
	private Socket socket;
	private static List<Writer> writers = null;
	private String clientName;

	public ChatServerProcessThread(Socket socket, List<Writer> writers) {
		this.socket = socket;
		this.writers = writers;
	}
	
	@Override
	public void run() {
		try {
//			InputStream is = socket.getInputStream();
//			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(
					new OutputStreamWriter(
							// parameter의 true는 autoFlush
							socket.getOutputStream(), "utf-8"), true);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			String request = null;
			checkClientConnection();
			System.out.println("서버소켓연결");
			while (true) {
				request = br.readLine();
				if (request == null) {
					System.out.println("클라 연결종료");
					doQuit(clientName, pw);
					break;
				}
				
				System.out.println("메시지 받음"+request);
				
				String[] tokens = request.split(":");
				if (tokens[0].equals("join")) {
					doJoin(tokens[1], pw);
				} else if (tokens[0].equals("message")) {
					System.out.println(tokens[1]);
					broadcast(clientName+": "+tokens[1]);
				} else if (tokens[0].equals("quit")) {
					doQuit(clientName, pw);
					break;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("서버쪽");
			ClosingStream.closingStream(socket);
		}
		
	}
	private void doJoin(String clientName, Writer writer) throws IOException {
		System.out.println("join");
		this.clientName = clientName;
		broadcast(clientName+"님이 입장하셨습니다.");
		writers.add(writer);
	}
	
	private void broadcast(String data) {
		synchronized (writers) {
			for (Writer writer : writers) {
				System.out.println(writer+"메시지 보내기"+data);
				PrintWriter pw = (PrintWriter) writer;
				pw.println(data);
			}
		}
	}
	
	private void doQuit(String clientName, Writer writer) {
		synchronized (writers) {
			writers.remove(writer);
			broadcast(clientName+"님이 퇴장하였습니다.");
		}
	}
	
	private void checkClientConnection() {
		InetSocketAddress clientAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
		int remoteHostPort = clientAddress.getPort();
		String remoteHostAddress = clientAddress.getAddress().getHostAddress();
		consoleLog("connected from " + remoteHostAddress + " : " + remoteHostPort);
	}
	
	private void consoleLog(String log) {
		System.out.println("[server: " + getId() + "] " + log);
	}
}
