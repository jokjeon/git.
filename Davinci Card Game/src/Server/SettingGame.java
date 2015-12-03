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
    private int cardIndex = 0; // 전체카드
    
    private ArrayList<String>[] UserCard = new ArrayList[4];
    private ArrayList<String> OpenCards = new ArrayList<String>();
    private int[] OpenCardsCount = new int[4];
    private int[] cardsIndex = new int[4]; // 각 사용자별 카드
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
        
        for (int i = 0; i < 10; i++) { // shuffle 쓸려고 add
            Cards.add(i + "_" + "0");
            Cards.add(i + "_" + "1");
        }
        Collections.shuffle(Cards); // 램덤으로 섞기
        
    }
    
    public void setGame() throws IOException{
       
                
        Collections.synchronizedMap(clientsMap);    // 멀티쓰레드 환경에서 쓰레드들이 데이터에 접근할때, 무결성을 보장해줌.
        serverSocket = new ServerSocket(8888);      // 해당 접속하려고 하는 소켓의 포트번호

        while(true){
            
            socket = serverSocket.accept();     // 소켓에 사용자가 접속할때 까지 대기 -
            Activity act = new Activity(socket);   // 해당 소켓을 Receiver 라는 클래스로 보낸다.
            act.start();       // 해당 클래스 쓰레드 시작.
        }
        
        
    }

    //맵의 내용(클라이언트) 저장한다.
    public void addClient(String nick, DataOutputStream out) throws IOException {
        clientsMap.put(nick, out);
    }
    
    
    /*    채팅정보   */
    
    //맵에서 클라이언트 정보를 제거한다.
    public void removeClient(String nick) {
        clientsMap.remove(nick);
    }
    
    public String separatePacket(String msg){
        
        return null;
    }
    
        /*    소켓 정보   */
    class Activity extends Thread {
        private DataInputStream in;     // 해당 소켓으로 들어온 데이터
        private DataOutputStream out;   // 나갈 데이터
        private String nick;
        private int Player = 0;
 
        //리시버가 한 일은 자기 혼자서 네트워크 처리. 계속 듣기 처리해주는 것
        //클라이언트와 1:1로 리시버가 연결된다.
        public Activity(Socket socket) throws IOException {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            
            nick = in.readUTF();
 
            addClient(nick, out);
        }
        
        //Thread를 상속받았으므로, run() 메소드를 오버라이드해야 한다.
        public void run() {
            System.out.println(Player);
            for(String PIndex : clientsMap.keySet()){
                if(PIndex.equals(nick))
                    break;
                else
                    Player++;
            }

            try {      //계속 듣기만 한다.
                monitor.appendMsg("Player"+ Player+" 참여 \r\n");
                out.writeUTF(""+Player);
                
                while (in != null) {
                    //소켓을 통해 데이터를 읽어와서
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
                        // turn 에 해당하는 플레이어의 값을 변경시켜줘야함.
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