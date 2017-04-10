/*
	�����ڸ��� Ŭ���̾�Ʈ�� 1:1�� ��ȭ�� ���� ������
	
*/
package multi.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JTextArea;

public class ServerThread extends Thread{
	BufferedReader buffr;
	BufferedWriter buffw;
	Socket socket;
	ServerMain main;
	//Vector<ServerThread> list;
	boolean flag = true;
	
	//�����κ��� ������ �޾ƿ���
	public ServerThread(Socket socket, ServerMain main) {		//�̹� ������ ������ �޾ƿ;��ϱ� ������ �Ű������� ������
		this.socket = socket;
		this.main = main;
				
		try {
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//Ŭ���̾�Ʈ�� �޼��� �ޱ�
	public void listen(){
		String msg = null;
		try {
			msg = buffr.readLine();	//���
			main.area.append(msg+"\n");	//ȭ�鿡 �Ѹ���
			send(msg);		//�ٽ� ������
		} catch (IOException e) {
			flag = false;		//���� ������ ���̱�.
			
			//Vector���� �� �����带 ���� 
			main.list.remove(this);		//�� �ڽ� �����带 �����Ѵ�.
			
			main.area.append("���� �����ڴ� "+main.list.size()+"�� \n"); 
			System.out.println("�б�Ұ�");
			//e.printStackTrace();
			
		}
		
	}
	
	public void send(String msg){
		//������ �ڱ�� ����� �Ѹ��� Ŭ���̾�Ʈ���Ը��� ��ȭ�� ������ �ִ�. 
		//�̸� ���� �������� ���ο��� ������
		
		//������ ������� ����Ʈ�� ��������ϱ� ������ �Ź� ����Ŭ�� �������� ���� �޸� �� �����Ͽ� �������.
		//�޸𸮻� ����Ʈ�� �迭�� �÷����� �ִµ�. �� �� ���ڷ� �����Ǿ������ �迭�� ������� �ʴ´�.
		//���� ���Ϳ� ��̰� �ִµ� ��̴� ����ȭ �Ǿ� �ֱ� ������ �������ٿ� ���� ���ɼ��� �������� �ʴ´�.
		//�׷��� ���ݰ��� ��쿣 ���͸� Ȱ���ϴ� ���� ����.(���Ͱ� ó���� ������������ ����. )
		try {
			//���� �������� ����.
			for(int i=0;i<main.list.size();i++){
				ServerThread st = main.list.elementAt(i);
				st.buffw.write(msg+"\n");
				st.buffw.flush();
			}
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
