package time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPTimeServer {
	private static final int PORT = 5000;
	private static final int BUFFER_SIZE = 1024;
	public static void main(String[] args) {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(PORT);
			DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
			
			socket.receive(receivePacket);
			String message = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
			System.out.println(message);
			if ("time".equals(message)) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
				byte[] date = format.format(new Date()).getBytes("UTF-8");
				
				DatagramPacket sendData = new DatagramPacket(
						date, 0, date.length, receivePacket.getAddress(), receivePacket.getPort());
				socket.send(sendData);
			}
		} catch (SocketException e) {
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
