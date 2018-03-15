package chat;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

public class ChatWindow {

	private Frame frame;
	private Panel pannel;
	private Button buttonSend;
	private TextField textField;
	private TextArea textArea;
	
	private String name;
	private BufferedReader br;
	private PrintWriter pw;

	public ChatWindow(String name, BufferedReader br, PrintWriter pw) {
		frame = new Frame(name);
		pannel = new Panel();
		buttonSend = new Button("Send");
		textField = new TextField();
		textArea = new TextArea(30, 80);
		
		this.name = name;
		this.br = br;
		this.pw = pw;
		new ChatClientReceiveThread().start();
	}

	public void show() {
		// Button
		buttonSend.setBackground(Color.GRAY);
		buttonSend.setForeground(Color.WHITE);
		buttonSend.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent actionEvent ) {
				sendMessage(name, pw);
			}
		});
		

		// Textfield
		textField.setColumns(80);
		textField.addKeyListener( new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				char keyCode = e.getKeyChar();
				if (keyCode == KeyEvent.VK_ENTER) {
					sendMessage(name, pw);
				}
			}
		});

		// Pannel
		pannel.setBackground(Color.LIGHT_GRAY);
		pannel.add(textField);
		pannel.add(buttonSend);
		frame.add(BorderLayout.SOUTH, pannel);

		// TextArea
		textArea.setEditable(false);
		frame.add(BorderLayout.CENTER, textArea);

		// Frame
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
		frame.pack();
	}
	
	private void sendMessage(String name, PrintWriter pw) {
		String message = textField.getText();
		MessagePacket mPacket = new MessagePacket(name);
		if ("quit".equals(message)) {
			mPacket.setProtocol(2);
			pw.print(mPacket);
//			pw.println("quit:"+name);
			return;
		}
		pw.println("message:"+message);
		System.out.println("메시지 보냄");
		
		textField.setText("");
		textField.requestFocus();		
	}
	
	public class ChatClientReceiveThread extends Thread {
//		private static final String SERVER_IP = "192.168.1.12";
//		private static final int SERVER_PORT = 6000;
//		private String name;
//		private BufferedReader br;
//		private PrintWriter pw;
//		
//		public ChatClientReceiveThread(String name, BufferedReader br, PrintWriter pw) {
//			this.name = name;
//			this.br = br;
//			this.pw = pw;
//		}
			@Override
		public void run() {
//			Scanner scanner = new Scanner(System.in);
			Socket socket = new Socket();
			try {
//				socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
//				
//				BufferedReader br = new BufferedReader(
//						new InputStreamReader(
//								socket.getInputStream(), StandardCharsets.UTF_8));
//				PrintWriter pw = new PrintWriter(
//						new OutputStreamWriter(
//								socket.getOutputStream(), StandardCharsets.UTF_8), true);
				pw.println("join:"+name);
				System.out.println("클라이언트 접속");
				while (true) {
					
					String msg = br.readLine();
					
					if( msg == null ) {
						System.out.println( "[client] disconnected by server" );
						break;
					}
					
					System.out.println("받은 메시지:"+msg);
					textArea.append( msg );
					textArea.append("\n");
				}
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				System.out.println("클라쪽");
				ClosingStream.closingStream(socket);
			}
			
		}
	}
}
