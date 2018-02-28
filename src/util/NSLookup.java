package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {
   /*
   	[과제1-1]  NSLookup 구현 하기
   	1. Scanner와  nextLine() 메소드를 사용해서  계속 입력받은 도메인을 입력 받는다.
   	2. 입력받은  도메인의 IP 주소를  출력한다.
   	3. “exit” 를 입력 받아  프로그램을 종료할  때 까지 계속  프로그램을 실행한다.
   	4. InetAddress의  static 메서드  getAllByName( String host ) 를 사용한다. 
   */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		try {
			while (true) {
				String domain = sc.nextLine();
				if (domain.equals("exit")) {
					break;
				}
				InetAddress[] address = InetAddress.getAllByName(domain);
				for (int i = 0; i < address.length; i++) {
					String ip = address[i].getHostAddress();
					System.out.println(domain+" : "+ip);
				}
//				InetAddress hostAddress = InetAddress.getByName(domain);
//				byte[] address = hostAddress.getAddress();
//				System.out.print(domain+" : ");
//				for (int i = 0; i < address.length; i++) {
//					System.out.print(address[i] & 0x000000ff);
//					if (i<3) {
//						System.out.print(".");
//					}
//				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
			
}
