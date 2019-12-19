package chatApp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	static List<DataOutputStream> listOut;
	public static void main(String[] args) {
		Server myServer = new Server();
		listOut = new ArrayList<DataOutputStream>();
		myServer.start();
	}
	private void start() {

		Socket socket = null;
		ServerSocket server_socket = null;
		try {
			server_socket = new ServerSocket(9999); //열려는 포트
		}catch(IOException e) {
			System.out.println("포트가 열려있습니다.");
		}
		try {
			System.out.println("서버가 열렸습니다.");
			while(true) {
				socket = server_socket.accept();
				
				InetAddress ip = socket.getInetAddress();
				System.out.println(ip+ " 접속 ");
				
				new MultiThread(socket,listOut).start();
			}
						
		}catch(Exception e) {
			
		}
	}
}
class MultiThread extends Thread{
	Socket socket = null;
	String msg = null;
	String mac = null;
	DataInputStream in = null; //수신용 버퍼
	DataOutputStream out = null; // 출력용 버퍼
	List<DataOutputStream> listOut = null;
	
	public MultiThread(Socket socket,List<DataOutputStream> listOut) {
		this.socket = socket;
		this.listOut = listOut;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			synchronized(listOut) {
				listOut.add(out);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try {
			while(in != null) {
				String name = in.readUTF();
				String temp = in.readUTF();
				synchronized(listOut){
					for(DataOutputStream output : listOut) {
						output.writeUTF(name);
						output.writeUTF(temp);
					}
					System.out.println(name+":"+temp);			
					//out.writeUTF(temp); 				
				}
				//sendMsg(temp);
			}
		} catch(Exception e) {
			
		}
	}
	public void sendMsg(String msg) {
		try {
			out.writeUTF(msg); 
		}catch(Exception e){
			
		}
	}
}
