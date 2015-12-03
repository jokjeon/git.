
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
        
        Collections.synchronizedMap(clientsMap);    // ��Ƽ������ ȯ�濡�� ��������� �����Ϳ� �����Ҷ�, ���Ἲ�� ��������.
        serverSocket = new ServerSocket(9999);      // �ش� �����Ϸ��� �ϴ� ������ ��Ʈ��ȣ
        
        while(true){
            monitor.appendMsg("����� �����..." + "\r\n");
            socket = serverSocket.accept();     // ���Ͽ� ����ڰ� �����Ҷ� ���� ��� -
            monitor.appendMsg(socket.getInetAddress()+"���� �����߽��ϴ�."+ "\r\n");
            Receiver receiver = new Receiver(socket);   // �ش� ������ Receiver ��� Ŭ������ ������.
            receiver.start();       // �ش� Ŭ���� ������ ����.

        }
        
        
    }

    //���� ����(Ŭ���̾�Ʈ) �����Ѵ�.
    public void addClient(String nick, DataOutputStream out) throws IOException {
        sendMessage(nick + "���� �����߽��ϴ�.\r\n");
        clientsMap.put(nick, out);
    }
    
    
    /*    ä������   */
    
    //�ʿ��� Ŭ���̾�Ʈ ������ �����Ѵ�.
    public void removeClient(String nick) {
        sendMessage(nick + "���� �������ϴ�.\r\n");
        clientsMap.remove(nick);
    }
    
    
    public void sendMessage(String msg) {
        //������ �޼����� �߻��ϸ�,
        //�ʿ� ����� ��� Ŭ���̾�Ʈ ������ ������
        Iterator<String> it = clientsMap.keySet().iterator();
        String key = "";
 
        while (it.hasNext()) {
            key = it.next();
            try {//��� Ŭ���̾�Ʈ�� ���� �޼����� �����Ѵ�.
                clientsMap.get(key).writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
        /*    ���� ����   */
    class Receiver extends Thread {
        private DataInputStream in;     // �ش� �������� ���� ������
        private DataOutputStream out;   // ���� ������
        private String nick;
 
        //���ù��� �� ���� �ڱ� ȥ�ڼ� ��Ʈ��ũ ó��. ��� ��� ó�����ִ� ��
        //Ŭ���̾�Ʈ�� 1:1�� ���ù��� ����ȴ�.
        public Receiver(Socket socket) throws IOException {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            nick = in.readUTF();
 
            addClient(nick, out);
        }
        
        //Thread�� ��ӹ޾����Ƿ�, run() �޼ҵ带 �������̵��ؾ� �Ѵ�.
        public void run() {
            try {      //��� ��⸸ �Ѵ�.
                while (in != null) {
                    //������ ���� �����͸� �о�ͼ�
                    message = in.readUTF();
                    //�޼����� �� Ŭ���̾�Ʈ�� �����Ѵ�.
                    sendMessage(message);
                    //���� ä��â�� �޼����� �߰��Ѵ�.
                    monitor.appendMsg(message);
                }
            }catch (IOException e) {
                //����ڰ� ��������� ���⼭ �����߻�.
                //�̰��� ����ڰ� ���� ���� �ǹ��ϹǷ� 
                //���⼭ Ŭ���̾�Ʈ�� �����Ѵ�.
                removeClient(nick);
            }
        }
    }
   
}





