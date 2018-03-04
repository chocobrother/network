package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChatServerProcessThread extends Thread{

	private List<Writer> listWriters = new ArrayList<Writer>();
	private Socket socket;
	
	public ChatServerProcessThread(Socket socket, List<PrintWriter> listWriter) {
		super();
		this.socket = socket;
		this.listWriters = listWriters;
	}

	@Override
	public void run() {
		
		BufferedReader br = null;
		PrintWriter pw = null;
		String nickName="";
		try {
			
			br = new BufferedReader(
					new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
			pw = new PrintWriter(
					new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),true);

			while(true) {

				String request = br.readLine();
				if(request == null) {
					System.out.println("클라이언트로부터 연결 끊김");
					break;
				}

				//protocol 분석
				String[] tokens = request.split(":");
				if( "join".equals(tokens[0])) {
					doJoin(tokens[1], pw);
					nickName = tokens[1];
				}else if( "message".equals(tokens[0])) {
					doMessage(tokens[1]);
				}else {
					System.out.println("에러: 알수 없는 요청("+tokens[0]+")");
				}
			}//while
			
		} catch (SocketException e) {
			doQuit(pw,nickName);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			
			try {
				if(br!=null)
					br.close();
				if(pw!=null)
					pw.close();
				if(socket.isClosed() == false && socket!=null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}//finally
		
	}// run()
	
	private void broadcast(String data) {
		synchronized (listWriters) {
			if(data!=null) {
				for(Writer w : listWriters) {
					PrintWriter pw = (PrintWriter)w;
					pw.println(data);
					pw.flush();
				}
			}
		}
	}
	
	private void addWriter(Writer writer) {
		synchronized (listWriters) {
			listWriters.add(writer);
		}
	}
	
	private void doJoin(String nickName, Writer writer) {
		String data = nickName + " 님이 참여하였습니다.";
		broadcast(data);
		
		// writer pool에 저장
		addWriter(writer);
	}
	
	private void doMessage(String message) {
		if(message!=null)
			broadcast(message);
	}
	
	private void doQuit(Writer writer, String nickName) {
		removeWriter(writer);
		String data = nickName + " 님이 퇴장 하셨습니다.";
		broadcast(data);
	}
	
	private void removeWriter(Writer writer) {
		synchronized (listWriters) {
			for(Writer w : listWriters) {
				if(w==writer) {
					listWriters.remove(w);
					break;
				}
			}
		}
	}
}