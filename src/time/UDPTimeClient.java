package time;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;

public class UDPTimeClient {
	private static final String SERVER_IP = "192.168.1.12";
	private static final int SERVER_PORT = 5000;
	private static final int BUFFER_SIZE = 1024;
	public static void main(String[] args) {
		DatagramSocket socket = null;
		try {			
			socket = new DatagramSocket();
			
			Scanner scanner = new Scanner(System.in);
			
			System.out.print(">> ");
			byte[] message = scanner.nextLine().getBytes("UTF-8");
				
			DatagramPacket sendData = new DatagramPacket(message, message.length, new InetSocketAddress(SERVER_IP, SERVER_PORT));
				
			socket.send(sendData);
				
			DatagramPacket receiveData = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
			socket.receive(receiveData);
			String time = new String(receiveData.getData(), 0, receiveData.getLength(), "UTF-8");
				
			System.out.println("[SERVER] TIME: "+time);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}
	}
}
