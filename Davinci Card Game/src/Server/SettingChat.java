
package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Sensor
 */
public class SettingChat {
    
    private ServerSocket serverSocket;
    private Socket socket;
    private ChatServer monitor;
    private String message;
    
    private Map<String, DataOutputStream> clientsMap = new HashMap<String, DataOutputStream>();
    

    public final void setGui(ChatServer m){
        this.monitor = m;
    }
    
    public void set() throws IOException{
        
        Collections.synchronizedMap(clientsMap);    // 멀티쓰레드 환경에서 쓰레드들이 데이터에 접근할때, 무결성을 보장해줌.
        serverSocket = new ServerSocket(9999);      // 해당 접속하려고 하는 소켓의 포트번호
        
        while(true){
            monitor.appendMsg("사용자 대기중..." + "\r\n");
            socket = serverSocket.accept();     // 소켓에 사용자가 접속할때 까지 대기 -
            monitor.appendMsg(socket.getInetAddress()+"에서 접속했습니다."+ "\r\n");
            Receiver receiver = new Receiver(socket);   // 해당 소켓을 Receiver 라는 클래스로 보낸다.
            receiver.start();       // 해당 클래스 쓰레드 시작.

        }
        
        
    }

    //맵의 내용(클라이언트) 저장한다.
    public void addClient(String nick, DataOutputStream out) throws IOException {
        sendMessage(nick + "님이 접속했습니다.\r\n");
        clientsMap.put(nick, out);
    }
    
    
    /*    채팅정보   */
    
    //맵에서 클라이언트 정보를 제거한다.
    public void removeClient(String nick) {
        sendMessage(nick + "님이 나갔습니다.\r\n");
        clientsMap.remove(nick);
    }
    
    
    public void sendMessage(String msg) {
        //전송할 메세지가 발생하면,
        //맵에 저장된 모든 클라이언트 정보를 가져와
        Iterator<String> it = clientsMap.keySet().iterator();
        String key = "";
 
        while (it.hasNext()) {
            key = it.next();
            try {//모든 클라이언트에 각각 메세지를 전달한다.
                clientsMap.get(key).writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
        /*    소켓 정보   */
    class Receiver extends Thread {
        private DataInputStream in;     // 해당 소켓으로 들어온 데이터
        private DataOutputStream out;   // 나갈 데이터
        private String nick;
 
        //리시버가 한 일은 자기 혼자서 네트워크 처리. 계속 듣기 처리해주는 것
        //클라이언트와 1:1로 리시버가 연결된다.
        public Receiver(Socket socket) throws IOException {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            nick = in.readUTF();
 
            addClient(nick, out);
        }
        
        //Thread를 상속받았으므로, run() 메소드를 오버라이드해야 한다.
        public void run() {
            try {      //계속 듣기만 한다.
                while (in != null) {
                    //소켓을 통해 데이터를 읽어와서
                    message = in.readUTF();
                    //메세지를 각 클라이언트에 전송한다.
                    sendMessage(message);
                    //서버 채팅창에 메세지를 추가한다.
                    monitor.appendMsg(message);
                }
            }catch (IOException e) {
                //사용자가 접속종료시 여기서 에러발생.
                //이것은 사용자가 나간 것을 의미하므로 
                //여기서 클라이언트를 제거한다.
                removeClient(nick);
            }
        }
    }
   
}





