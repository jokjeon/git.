package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Sensor
 */
public class SettingGame{
    
    private ServerSocket serverSocket;
    private Socket socket;
    private GameServer monitor;
    private String message;
    
    private Map<String, DataOutputStream> clientsMap = new LinkedHashMap<String, DataOutputStream>();

    private ArrayList Cards = new ArrayList<String>();
    private int cardIndex = 0; // ��üī��
    
    private ArrayList<String>[] UserCard = new ArrayList[4];
    private ArrayList<String> OpenCards = new ArrayList<String>();
    private int[] OpenCardsCount = new int[4];
    private int[] cardsIndex = new int[4]; // �� ����ں� ī��
    private int[] OverPlayer = new int[4];
    
    private int turn = 0;
    private int win = 0;
    
    public final void setGui(GameServer m){
        this.monitor = m;
    }
    public void setCards(){
        cardIndex = 0;
        
        
        for(int i=0; i<4; i++){
            UserCard[i] = new ArrayList<String>();
            UserCard[i].clear();
            cardsIndex[i] = 0;
            OpenCardsCount[i] = 0;
            OverPlayer[i] = 0;
        }
        
        for(int i = 0; i < clientsMap.size(); i++){
            for(int j = 0; j < 4; j++){
                UserCard[i].add(Cards.get(cardIndex++).toString());
                cardsIndex[i]++;
            }
        }
    }
    
    public void setDraw(int p){
        UserCard[p].add(Cards.get(cardIndex++).toString());
        cardsIndex[p]++;
    }
    
    public SettingGame(){
        Cards.add("A_0");
        Cards.add("A_1");
        Cards.add("B_0");
        Cards.add("B_1");
        Cards.add("C_0");
        Cards.add("C_1");
        
        for (int i = 0; i < 10; i++) { // shuffle ������ add
            Cards.add(i + "_" + "0");
            Cards.add(i + "_" + "1");
        }
        Collections.shuffle(Cards); // �������� ����
        
    }
    
    public void setGame() throws IOException{
       
                
        Collections.synchronizedMap(clientsMap);    // ��Ƽ������ ȯ�濡�� ��������� �����Ϳ� �����Ҷ�, ���Ἲ�� ��������.
        serverSocket = new ServerSocket(8888);      // �ش� �����Ϸ��� �ϴ� ������ ��Ʈ��ȣ

        while(true){
            
            socket = serverSocket.accept();     // ���Ͽ� ����ڰ� �����Ҷ� ���� ��� -
            Activity act = new Activity(socket);   // �ش� ������ Receiver ��� Ŭ������ ������.
            act.start();       // �ش� Ŭ���� ������ ����.
        }
        
        
    }

    //���� ����(Ŭ���̾�Ʈ) �����Ѵ�.
    public void addClient(String nick, DataOutputStream out) throws IOException {
        clientsMap.put(nick, out);
    }
    
    
    /*    ä������   */
    
    //�ʿ��� Ŭ���̾�Ʈ ������ �����Ѵ�.
    public void removeClient(String nick) {
        clientsMap.remove(nick);
    }
    
    public String separatePacket(String msg){
        
        return null;
    }
    
        /*    ���� ����   */
    class Activity extends Thread {
        private DataInputStream in;     // �ش� �������� ���� ������
        private DataOutputStream out;   // ���� ������
        private String nick;
        private int Player = 0;
 
        //���ù��� �� ���� �ڱ� ȥ�ڼ� ��Ʈ��ũ ó��. ��� ��� ó�����ִ� ��
        //Ŭ���̾�Ʈ�� 1:1�� ���ù��� ����ȴ�.
        public Activity(Socket socket) throws IOException {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            
            nick = in.readUTF();
 
            addClient(nick, out);
        }
        
        //Thread�� ��ӹ޾����Ƿ�, run() �޼ҵ带 �������̵��ؾ� �Ѵ�.
        public void run() {
            System.out.println(Player);
            for(String PIndex : clientsMap.keySet()){
                if(PIndex.equals(nick))
                    break;
                else
                    Player++;
            }

            try {      //��� ��⸸ �Ѵ�.
                monitor.appendMsg("Player"+ Player+" ���� \r\n");
                out.writeUTF(""+Player);
                
                while (in != null) {
                    //������ ���� �����͸� �о�ͼ�
                    message = in.readUTF();
                    
                    if(Player == 0 && clientsMap.size() >= 2){
                        if(message.equals("SetCard")){
                            setCards();
                            for(String key : clientsMap.keySet()){
                                clientsMap.get(key).writeUTF("0");
                                clientsMap.get(key).writeUTF(Cards.toString());
                                clientsMap.get(key).writeUTF(""+clientsMap.size());
                            }
                        }
                    }
                    if(message.equals("ContainOpen")){
                        if(OpenCards.contains(in.readUTF())){
                            out.writeUTF("5");
                            out.writeUTF("1");
                        }
                        
                        
                    }
                    
                    if(message.equals("CardOK")){
                        int i = 0;
                        int exist = 0;
                        
                        String open = in.readUTF();
                        OpenCards.add(open);
                        for(i=0; i<clientsMap.size(); i++){
                            if(UserCard[i].contains(open)){
                                OpenCardsCount[i]++;
                                break;
                            }
                        }
                        
                        if(cardsIndex[i] == OpenCardsCount[i]){
                            OverPlayer[i] = 1;
                        }

                        for(int j=0; j<clientsMap.size(); j++){
                            if(j == turn)
                                continue;
                            
                            if(OverPlayer[j] == 1)
                                exist++;
                        }
                        
                        if(exist == (clientsMap.size()-1)){
                            for(String key : clientsMap.keySet()){
                                    clientsMap.get(key).writeUTF("4");
                                    clientsMap.get(key).writeUTF(turn + "");
                            }
                            continue;
                        }
                            
                            
                        
                        for(String key : clientsMap.keySet()){
                                clientsMap.get(key).writeUTF("1");
                                clientsMap.get(key).writeUTF(open);
                        }
                        
                    }
                    if(message.equals("MyDraw")){
                        int who = Integer.parseInt(in.readUTF());
                        int i = 0;
                        
                        for(i=0; i < cardsIndex[who]; i++){
                            if(!OpenCards.contains(UserCard[who].get(i))){
                                OpenCardsCount[who]++;
                                OpenCards.add(UserCard[who].get(i));
                                break;
                            }
                        }
                        
                        for(String key : clientsMap.keySet()){
                                clientsMap.get(key).writeUTF("1");
                                clientsMap.get(key).writeUTF(UserCard[who].get(i));
                        }
                        
                    }
                    
                    
                    if(message.equals("DrawCard")){
                        if(win == 1 || clientsMap.size() == 1)
                            continue;
                        
                        if(cardsIndex[turn] < 8){
                            int index = (int)(Math.random() * (cardsIndex[turn] - 2));
                            for(String key : clientsMap.keySet()){
                                    clientsMap.get(key).writeUTF("2");
                                    clientsMap.get(key).writeUTF(index + "");
                                    clientsMap.get(key).writeUTF(turn + "");
                                    clientsMap.get(key).writeUTF(cardsIndex[turn] + "");

                            }
                            setDraw(turn);
                        }
                    }
                    
                    if(message.equals("TurnOver")){
                        // turn �� �ش��ϴ� �÷��̾��� ���� ������������.
                        int firstTurn = turn;
                        turn = (turn + 1) % clientsMap.size();
                        
                        while(OverPlayer[turn] == 1){
                            turn = (turn + 1) % clientsMap.size();
                            if(turn == firstTurn){
                                for(String key : clientsMap.keySet()){
                                    clientsMap.get(key).writeUTF("4");
                                    clientsMap.get(key).writeUTF(turn + "");
                                }
                                break;
                            }
                        }
                        
                        if(turn == firstTurn){
                            win = 1;
                            continue;
                        }
                        
                        for(String key : clientsMap.keySet()){
                                clientsMap.get(key).writeUTF("3");
                                clientsMap.get(key).writeUTF(turn + "");
                        }
                    
                    }
                    
                    if(message.equals("GameOver")){
                        removeClient(nick);
                        turn = 0;
                        Player = 0;
                        Collections.shuffle(Cards);
                        setCards();
                        OpenCards.clear();
                    }
                    
                    
                }
            }catch (IOException e) {
                removeClient(nick);
                turn = 0;
                Player = 0;
                Collections.shuffle(Cards);
                setCards();
                OpenCards.clear();
            }
        }
    }
   
}