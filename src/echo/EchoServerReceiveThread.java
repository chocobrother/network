package echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread {

	private Socket socket;
	
	public EchoServerReceiveThread(Socket socket) {
		
	}
	@Override
	public void run() {
		
		 //4. 연결 성공
		 InetSocketAddress remoteSocketAddress =
				 (InetSocketAddress)socket.getRemoteSocketAddress();//클라이언트 쪽의 포트랑 ip주소 알아옴
		 
		 int remoteHostPort = remoteSocketAddress.getPort();
		 
		 String remoteHostAddress = remoteSocketAddress.getAddress().getHostAddress();
		 
		 consoleLog(" connected from " + remoteHostAddress + " : " + remoteHostPort);
		
		
		 //데이터 통신 소켓과 accept 소켓 예외를 나누기 위해서 try구문 한번 더 사용
		 try {
			//5.I/0 Stream 받아오기
			 InputStream is = socket.getInputStream();
			 OutputStream os = socket.getOutputStream();
			 
			 
			 while(true) {
			 //6. 데이터 읽기
			 byte[] buffer = new byte[256];
			 
			 int readByteCount = is.read(buffer); //Blocking
			 
			 if(readByteCount == -1) { //정상적으로 close를 호출한 경우//정상종료
				 consoleLog(" disconnected by client");
				 break;
			 }
			 
			 String data = new String(buffer,0,readByteCount,"utf-8");
			 
			 consoleLog(" recevied : " + data);
			 
			 //7. 데이터 쓰기
			 

			 os.write(data.getBytes("utf-8"));
			 
			 }
	}catch(SocketException e) {
		
		consoleLog(" sudden closed by client");
	}catch(IOException e) {
			 e.printStackTrace();
		 }finally {
			 
				 if(socket!=null && socket.isClosed() == false)
					try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 
		 
		 }	 
	}
		
	

	private void consoleLog(String log) {
		System.out.println("[server: " + getId() + "] " + log);
	}
}
