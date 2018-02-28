package chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatClientApp {
	private static final String SERVER_IP = "192.168.1.12";
	private static final int SERVER_PORT = 6000;
	
	public static void main(String[] args) {
		String name = "";
		Scanner scanner = new Scanner(System.in);

		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream(), StandardCharsets.UTF_8));
			PrintWriter pw = new PrintWriter(
					new OutputStreamWriter(
							socket.getOutputStream(), StandardCharsets.UTF_8), true);
			
			while( true ) {
				
				System.out.println("대화명을 입력하세요.");
				System.out.print(">>> ");
				name = scanner.nextLine();
				
				if (name.isEmpty() == false ) {
					break;
				}
				
				System.out.println("대화명은 한글자 이상 입력해야 합니다.\n");
			}
			pw.print("join:"+name);
			scanner.close();
			
			new ChatWindow(name).show();
			
			/*while (true) {
				br.readLine();
			}*/
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
