package chat;

import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ChatServerProcessThread extends Thread{
	private Socket socket;
	private static List<Writer> writers = null;
	private String name;

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
			//String request = null;
			//while (true) {
				System.out.println("서버소켓연결");
				String line = br.readLine();
//				if (line == null||"".equals(line)) {
//					break;
//				}
//				if (request == null) {
//					request = line;
//					break;
//				}
				
			//}
			
			System.out.println(line);
			
			String[] tokens = line.split(":");
			if (tokens[0].equals("join")) {
//				doJoin(os, tokens[1], writer);
				doJoin(tokens[1], pw);
			} else if (tokens[0].equals("message")) {
				broadcast(tokens[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private void doJoin(String name, Writer writer) throws IOException {
		System.out.println("join");
//		os.write((tokens+"님이 입장하셨습니다.").getBytes(StandardCharsets.UTF_8));
//		os.write( "\r\n".getBytes() );
		this.name = name;
		broadcast(name+"님이 입장하셨습니다.");
		writers.add(writer);
	}
	
	private void broadcast(String data) {
		synchronized (writers) {
			for (Writer writer : writers) {
				PrintWriter pw = (PrintWriter) writer;
				pw.println(data);
			}
		}
	}
	
}
