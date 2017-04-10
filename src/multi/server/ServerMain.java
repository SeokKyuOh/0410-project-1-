/*
	서버는 수많은 접속자들의 접속만 관리해야한다.
	그래서 대화를 전달하는 역할은 쓰레드에게 맡기자.
*/

package multi.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerMain extends JFrame implements ActionListener, Runnable{
	JPanel p_north; 
	JTextField t_port;
	JButton bt_start;
	JTextArea area;
	JScrollPane scroll;
	int port=7777;
	Thread thread;	//서버 가동용 쓰레드
	ServerSocket server;
	
	Socket socket;		//대화를 위한 소켓
	
	//BufferedReader buffr;
	//BufferedWriter buffw;
	
	//멀티캐스팅을 위해서는 현재서버에 몇명이 들어오고 나가는지를 체크할 저장소가 필요하며
	//유연해야 하므로 컬렉션 계열로 선언하자.
	Vector<ServerThread> list = new Vector<ServerThread>();			//ServerThread는 접속자를 뜻한다.
	
	public ServerMain() {
		p_north = new JPanel();
		t_port = new JTextField(Integer.toString(port) ,10);
		bt_start = new JButton("가동");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		p_north.add(t_port);
		p_north.add(bt_start);
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		
		bt_start.addActionListener(this);
		
		setBounds(600,100,300,400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	//서버 가동 메서드
	public void startServer(){
		try {
			port = Integer.parseInt(t_port.getText());
			server = new ServerSocket(port);
			area.append("서버생성 \n");
			
			while(true){		//이부분만 while문으로 돌리자
				socket = server.accept();		//서버에 소켓끼우기
				String ip = socket.getInetAddress().getHostAddress();
				area.append(ip+"접속자 발견 \n");
				
				//접속자마다 쓰레드를 하나씩 할당해서 대화를 나누게 해준다.
				ServerThread st = new ServerThread(socket, this);
				st.start();
				
				//접속자가 발견되면, 이 접속자와 대화를 나눌 쓰레드를 Vector에 담는다.
				list.add(st);
				area.append("현재 접속자는 "+list.size()+"\n");	
				
			}
			
			
			/*아래의 내용은 서버가 직접 운영하는 것이 아닌 객체로 뺀다.
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String msg = null;
			msg = buffr.readLine();	//클라이언트 메세지 청취
			buffw.write(msg+"\n");	//클라이언트에 메세지 보내기
			buffw.flush();
			*/
			
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void actionPerformed(ActionEvent e) {
		thread = new Thread(this);
		thread.start();
	}
	
	public void run() {
		startServer();
		
	}
	
	public static void main(String[] args) {
		new ServerMain();
	}
}