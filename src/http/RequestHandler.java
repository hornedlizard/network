package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler extends Thread {
	private static final String DOCUMENT_ROOT = "./webapp";

	private Socket socket;

	public RequestHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			consoleLog("connected from " + inetSocketAddress.getAddress().getHostAddress() + ":"
					+ inetSocketAddress.getPort());

			// get IOStream
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			OutputStream os = socket.getOutputStream();

			String request = null;
			while (true) {
				// 클라이언트(브라우저 접속 시 요청)의 요청을 읽음
				// 라인 단위(readLine())로 읽음
				String line = br.readLine();
				System.out.println("client request : "+line);
				if (line == null || "".equals(line)) {
					// "".equals(line) 는 헤더와 바디 경계를 의미
					// 지금은 프로그램을 실행시키는 건 아니기 때문에 읽지 않음
					break;
				}
				//consoleLog(line);
				// request에는 첫번째 라인만 들어감
				if (request == null) {
					request = line;
					break;
				}
			}
			
			consoleLog(request);
			// request 요청/분석
			String[] tokens = request.split(" ");
			if ("GET".equals(tokens[0])) {
				responseStaticResource(os, tokens[1], tokens[2]);
			} else { // post 방식 요청 처리
//				responseStatic400Error();
				System.out.println("bad request");
			}

			// 예제 응답입니다. (응답 프로토콜)
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
//			os.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
//			os.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
//			os.write( "\r\n".getBytes() );
//			os.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );

		} catch ( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try {
				if ( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
			} catch ( IOException ex)  {
				consoleLog( "error:" + ex );
			}
		}
	}

//	static void responseStatic400Error() {
//		File file = new File(DOCUMENT_ROOT+"/error/400.html");
//		byte[] body = Files.readAllBytes(file.toPath());
//		String mimeType = Files.probeContentType(file.toPath());
//		os.write((protocol+"HTTP/1.1 404 \r\n").getBytes("utf-8"));
//		os.write( ("Content-Type:"+mimeType+"; charset=utf-8\r\n").getBytes( "UTF-8" ) );
//		os.write( "\r\n".getBytes() );
//		os.write(body);
//	}
	
	static void response404Error(
			OutputStream os, String url, String protocol) throws IOException {
		byte[] body =null;
		File file = new File(DOCUMENT_ROOT+"/error/404.html");
		if (file.exists()) {
			body = Files.readAllBytes(file.toPath());			
		}
		String mimeType = Files.probeContentType(file.toPath());
		os.write((protocol+"HTTP/1.1 404 \r\n").getBytes("utf-8"));
		os.write( ("Content-Type:"+mimeType+"; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		os.write( "\r\n".getBytes() );
		os.write(body);
	}
	
	private void responseStaticResource(
			OutputStream os, String url, String protocol) throws IOException {
		// 루트 파일
		if ("/".equals(url)) {
			url = "/index.html";
		}
		
		File file = new File(DOCUMENT_ROOT+"/"+url);
		if (file.exists() == false) {
			response404Error(os, url, protocol);
			return;
		}
		
		// Files 는 nio 클래스
		byte[] body = Files.readAllBytes(file.toPath());
		// 읽어온 경로의 파일의 마임타입(http프로토콜의 content-type)
		String mimeType = Files.probeContentType(file.toPath());
		
		// http 전송 (응답)
		// header 전송
		os.write( (protocol + "HTTP/1.1 200 OK\r\n").getBytes( "UTF-8" ) );
		os.write( ("Content-Type:"+mimeType+"; charset=utf-8\r\n").getBytes( "UTF-8" ) );
		os.write( "\r\n".getBytes() );
		// body 전송
		os.write( body );

	}
	
	private void consoleLog(String message) {
		System.out.println("[RequestHandler#" + getId() + "] " + message);
	}
}