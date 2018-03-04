package chat;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatClientApp {

	private static final String SERVER_IP = "192.168.1.6";
	private static final int PORT = 6000;
	
	public static void main(String[] args) {
		
			String nickName = null;
			
			Socket socket = null;
			
			ChatWindow chatWindow = null;
			
			PrintWriter pw = null;
			
			Scanner	scanner = new Scanner(System.in);
		
		
		try {
			
			socket = new Socket();
			
			socket.connect(new InetSocketAddress(SERVER_IP, PORT));

			while( true ) {
					
				System.out.println("대화명을 입력하세요.");
				System.out.print(">>> ");
				nickName = scanner.nextLine();
				
				if (nickName.isEmpty() == false ) {
					break;
				}
				
				System.out.println("대화명은 한글자 이상 입력해야 합니다.\n");
			}
			//대화명 등록 join
			pw = new PrintWriter(
					new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),true);
			pw.println("join:"+nickName);
			pw.flush();
			//채팅창 생성
			chatWindow = new ChatWindow(nickName,socket);
			chatWindow.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {	
				if(socket.isClosed() == false && socket!=null && chatWindow==null)
					socket.close();
				if(scanner!=null)
					scanner.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}//finally
		
	}//main
}