package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Localhost {

	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			String hostname = inetAddress.getHostName();
			String hostAddress = inetAddress.getHostAddress();
			byte[] addresses = inetAddress.getAddress();
			
			System.out.println(hostname);
			System.out.println(hostAddress);
			for (int i = 0; i < addresses.length; i++) {
				
				// byte 오버플로우 해결 방법
				System.out.print(addresses[i] & 0x000000ff);
				if (i<3) {
					System.out.print(".");
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
}
