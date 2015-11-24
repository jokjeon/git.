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
            monitor.appendMsg("�궗�슜�옄 ��湲곗쨷..." + "\r\n");
            socket = serverSocket.accept();
            monitor.appendMsg(socket.getInetAddress()+"�뿉�꽌 �젒�냽�뻽�뒿�땲�떎."+ "\r\n");
            
            Receiver receiver = new Receiver(socket);
            receiver.start();
        }
        
        
    }

    //留듭쓽 �궡�슜(�겢�씪�씠�뼵�듃) ���옣�븳�떎.
    public void addClient(String nick, DataOutputStream out) throws IOException {
        sendMessage(nick + "�떂�씠 �젒�냽�뻽�뒿�땲�떎.");
        clientsMap.put(nick, out);
    }
    
    //留듭뿉�꽌 �겢�씪�씠�뼵�듃 �젙蹂대�� �젣嫄고븳�떎.
    public void removeClient(String nick) {
        sendMessage(nick + "�떂�씠 �굹媛붿뒿�땲�떎.");
        clientsMap.remove(this);
    }
    
    
    public void sendMessage(String msg) {
        //�쟾�넚�븷 硫붿꽭吏�媛� 諛쒖깮�븯硫�,
        //留듭뿉 ���옣�맂 紐⑤뱺 �겢�씪�씠�뼵�듃 �젙蹂대�� 媛��졇��
        Iterator<String> it = clientsMap.keySet().iterator();
        String key = "";
 
        while (it.hasNext()) {
            key = it.next();
            try {//紐⑤뱺 �겢�씪�씠�뼵�듃�뿉 媛곴컖 硫붿꽭吏�瑜� �쟾�떖�븳�떎.
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
 
        //由ъ떆踰꾧� �븳 �씪�� �옄湲� �샎�옄�꽌 �꽕�듃�썙�겕 泥섎━. 怨꾩냽 �뱽湲� 泥섎━�빐二쇰뒗 寃�
        //�겢�씪�씠�뼵�듃�� 1:1濡� 由ъ떆踰꾧� �뿰寃곕맂�떎.
        public Receiver(Socket socket) throws IOException {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            nick = in.readUTF();
 
            addClient(nick, out);
        }
        
        //Thread瑜� �긽�냽諛쏆븯�쑝誘�濡�, run() 硫붿냼�뱶瑜� �삤踰꾨씪�씠�뱶�빐�빞 �븳�떎.
        public void run() {
            try {      //怨꾩냽 �뱽湲곕쭔 �븳�떎.
                while (in != null) {
                    //�냼耳볦쓣 �넻�빐 �뜲�씠�꽣瑜� �씫�뼱���꽌
                    message = in.readUTF();
                    //硫붿꽭吏�瑜� 媛� �겢�씪�씠�뼵�듃�뿉 �쟾�넚�븳�떎.
                    sendMessage(message);
                    //�꽌踰� 梨꾪똿李쎌뿉 硫붿꽭吏�瑜� 異붽��븳�떎.
                    monitor.appendMsg(message);
                }
            }catch (IOException e) {
                //�궗�슜�옄媛� �젒�냽醫낅즺�떆 �뿬湲곗꽌 �뿉�윭諛쒖깮.
                //�씠寃껋� �궗�슜�옄媛� �굹媛� 寃껋쓣 �쓽誘명븯誘�濡� 
                //�뿬湲곗꽌 �겢�씪�씠�뼵�듃瑜� �젣嫄고븳�떎.
                removeClient(nick);
            }
        }
    }
    
    
    public static void main(String[] args) throws IOException{
        Setting server = new Setting();
        server.set();
        
        
    }
}