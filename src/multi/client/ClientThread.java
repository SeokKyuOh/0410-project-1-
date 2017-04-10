/*
	키입력을 해야 서버의 메세지를 받는 현재의 기능을 보완한다.
	무한루프를 돌명서 서버의 메세지를 받을 존재가 필요하며 
	무한푸르를 실행해야 하므로 쓰레드로 정의한다.
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
	ClientMain main;		//클라인언트 프레임 자체를 보유해보자.
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
			main.area.append(msg+"\n");	//메인의 area를 직접 얻어와서 붙여버리기
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void send(String msg){	
		try {
			buffw.write(main.nickName+"님의 말 : "+msg+"\n");
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
