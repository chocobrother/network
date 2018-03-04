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
	import java.net.Socket;
import java.nio.charset.StandardCharsets;
	
	 
	
	public class ChatWindow {
	
	 
	
	private Frame frame;
	
	private Panel pannel;
	
	private Button buttonSend;
	
	private TextField textField;
	
	private TextArea textArea;
	
	 
	
	private String message = null;
	
	private Socket socket = null;
	
	private String nickName = null;
	
	private PrintWriter pw = null;
	
	public ChatWindow(String nickName,Socket socket) {
	
	frame = new Frame(nickName);
	
	pannel = new Panel();
	
	buttonSend = new Button("Send");
	
	textField = new TextField();
	
	textArea = new TextArea(30, 80);
	
	this.socket = socket;
	
	this.nickName = nickName;
	
	this.socket = socket;
	
	}
	
	 
	
	public void show() {
	
	// Button
	
	buttonSend.setBackground(Color.GRAY);
	
	buttonSend.setForeground(Color.WHITE);
	
	buttonSend.addActionListener( new ActionListener() {
	
	@Override
	
	public void actionPerformed( ActionEvent actionEvent ) {
	
	sendMessage();
	
	//new ChatClientReceiveThread(socket,nickName).start();
	
	sendMessage();
	
	}
	
	});
	
	 
	
	// Textfield
	
	textField.setColumns(80);
	
	textField.addKeyListener( new KeyAdapter() {
	
	public void keyReleased(KeyEvent e) {
	
	char keyCode = e.getKeyChar();
	
	if (keyCode == KeyEvent.VK_ENTER) {
	
	sendMessage();
	
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
	
	new ChatClientReceiveThread().start();
	
	try {
	
	pw = new PrintWriter(new OutputStreamWriter
	
	(socket.getOutputStream(),StandardCharsets.UTF_8),true);
	
	} catch (IOException e) {
	
	// TODO Auto-generated catch block
	
	e.printStackTrace();
	
	}
	
	}
	
	private void sendMessage() {
	
	//서버로 메시지를 보낸다.
	
	message = textField.getText();
	
	textArea.append( "message:" +nickName + ":" + message );
	
	textArea.append("\n");
	
	 
	
	textField.setText("");
	
	textField.requestFocus();
	
	if("quit".equals(message)) {
	
	return;
	
	}
	
	String send = "message:"+nickName+":"+message;
	
	pw.println(send);
	
	pw.flush();
	
	}
	
	//show 함수 아래서 .start
	
	private class ChatClientReceiveThread extends Thread{
	
	//소켓 가지고 들어와서 리드하고 쓰면 댐
	
	 
	
	@Override
	
	public void run() {
	
	 
	
	BufferedReader br = null;
	
	 
	
	try {
	
	br = new BufferedReader(new InputStreamReader
	
	(socket.getInputStream(),StandardCharsets.UTF_8));
	
	 
	
	while(true){
	
	String message = br.readLine();
	
	if(message!=null){
	
	textArea.append(message);
	
	textArea.append("\n");
	
	 
	
	}//if
	
	}//whille
	
	 
	
	} catch (IOException e) {
	
	// TODO Auto-generated catch block
	
	e.printStackTrace();
	
	}finally {
	
	try{
	
	if(br!=null)
	
	br.close();
	
	if(socket.isClosed() == false && socket!=null)
	
	socket.close();
	
	}catch(IOException e){
	
	e.printStackTrace();
	
		}
	
		}
		
		}
		
		}
		}