package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner sc = new Scanner(System.in);
		
		try {
			InetAddress inetAddress =  InetAddress.getLocalHost();
			
			while(true) {
				
				System.out.println(" >>");
				
				String s = sc.nextLine();
				
//				String hostAddress = inetAddress.getHostAddress();
				
				InetAddress[] allbyname = inetAddress.getAllByName(s);
				
				for(int i = 0; i < allbyname.length; i++) {
					System.out.println(s +":" + allbyname[i]);
				}
				
				if(s.equals("exit")) {
					break;
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
