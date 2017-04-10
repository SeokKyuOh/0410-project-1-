/*
	접속자마다 클라이언트와 1:1로 대화를 나눌 쓰레드
	
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
	
	//서버로부터 소켓을 받아오자
	public ServerThread(Socket socket, ServerMain main) {		//이미 생성된 소켓을 받아와야하기 때문에 매개변수로 만들자
		this.socket = socket;
		this.main = main;
				
		try {
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//클라이언트의 메세지 받기
	public void listen(){
		String msg = null;
		try {
			msg = buffr.readLine();	//듣기
			main.area.append(msg+"\n");	//화면에 뿌리기
			send(msg);		//다시 보내기
		} catch (IOException e) {
			flag = false;		//현재 쓰레드 죽이기.
			
			//Vector에서 이 쓰레드를 제거 
			main.list.remove(this);		//나 자신 쓰레드를 제거한다.
			
			main.area.append("현재 접속자는 "+main.list.size()+"명 \n"); 
			System.out.println("읽기불가");
			//e.printStackTrace();
			
		}
		
	}
	
	public void send(String msg){
		//기존엔 자기와 연결된 한명의 클라이언트에게마나 대화를 보내고 있다. 
		//이를 현재 접속한자 전부에게 보내자
		
		//접속한 사람들의 리스트는 들락날락하기 때문에 매번 오라클에 저장하지 말고 메모리 상에 저장하여 사용하자.
		//메모리상 리스트는 배열과 컬렉션이 있는데. 이 중 숫자로 한정되어버리는 배열은 사용하지 않는다.
		//그중 벡터와 어레이가 있는데 어레이는 정형화 되어 있기 때문에 동시접근에 대한 가능성을 보장하지 않는다.
		//그래서 지금같은 경우엔 벡터를 활용하는 것이 좋다.(벡터가 처리는 안정적이지만 느림. )
		try {
			//현재 접속한자 전부.
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
