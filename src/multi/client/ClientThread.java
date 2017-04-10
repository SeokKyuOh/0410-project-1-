/*
	Ű�Է��� �ؾ� ������ �޼����� �޴� ������ ����� �����Ѵ�.
	���ѷ����� ���� ������ �޼����� ���� ���簡 �ʿ��ϸ� 
	����Ǫ���� �����ؾ� �ϹǷ� ������� �����Ѵ�.
*/
package multi.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JTextArea;

public class ClientThread extends Thread{
	ClientMain main;		//Ŭ���ξ�Ʈ ������ ��ü�� �����غ���.
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	boolean flag = true;
	
	public ClientThread(Socket socket, ClientMain main) {
		this.socket = socket;
		this.main = main;
		
		try {
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void listen(){
		String msg = null;
		try {
			msg = buffr.readLine();
			main.area.append(msg+"\n");	//������ area�� ���� ���ͼ� �ٿ�������
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void send(String msg){	
		try {
			buffw.write(main.nickName+"���� �� : "+msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(flag){
			listen();
		}

	}
}
