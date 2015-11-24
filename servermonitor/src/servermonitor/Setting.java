package servermonitor;

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
public class Setting {
    
    private ServerSocket serverSocket;
    private Socket socket;
    private Server monitor;
    private String message;
    
    private Map<String, DataOutputStream> clientsMap = new HashMap<String, DataOutputStream>();

    public final void setGui(Server m){
        this.monitor = m;
    }
    
    public void set() throws IOException{
        
        Collections.synchronizedMap(clientsMap);
        serverSocket = new ServerSocket(9999);
        
        while(true){
            monitor.appendMsg("사용자 대기중..." + "\r\n");
            socket = serverSocket.accept();
            monitor.appendMsg(socket.getInetAddress()+"에서 접속했습니다.."+ "\r\n");
            
            Receiver receiver = new Receiver(socket);
            receiver.start();
        }
        
        
    }

    //맵의 내용(클라이언트) 저장한다
    public void addClient(String nick, DataOutputStream out) throws IOException {
        sendMessage(nick + "님이 접속했습니다..");
        clientsMap.put(nick, out);
    }
    
    //맵에서 클라이언트 정보를 제거한다.
    public void removeClient(String nick) {
        sendMessage(nick + "님이 나갔습니다.");
        clientsMap.remove(this);
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
    
    class Receiver extends Thread {
        private DataInputStream in;
        private DataOutputStream out;
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
    
    
    public static void main(String[] args) throws IOException{
        Setting server = new Setting();
        server.set();
        
        
    }
}