package cardgame;

import java.awt.Color;
import java.awt.Frame;

import java.awt.Component;
import java.awt.ComponentOrientation;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;


public class Screen extends javax.swing.JFrame {
    
 
    
    /* 
    각 클라이언트들을 처리해줄 Class 
    */
    public class Client extends Thread{
    	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
        private String msg;
        private String Name;
        
        /*
        해당 쓰레드에서 해당 클라이언트
        */
        public void run() {
            
		try {
                        
			socket = new Socket("165.229.89.38", 9999); // IP 127.0.0.1 , 포트 9999 소켓 연결
			chatLog.append("서버에 연결 되었습니다. \r\n");
                        
                        out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());

			out.writeUTF(ID);         // 서버에 ( ) 안의 값을 전달.
			chatLog.append("채팅을 시작하셔도 됩니다. \r\n");   // 현재 클라이언트의 텍스트 필드에 내용 추가.
                        
                        
			while(in!=null){
                            
				msg=in.readUTF();       // 서버에서 클라이언트로 전달된 값을 읽어옴.
                                System.out.println(msg);
				chatLog.append(msg);			// 텍스트 Area에 값 넣기.
			}
                                
                                
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
       
        public void sendMessage(String msg) {
            try {
                out.writeUTF(msg);  // 서버로 값 전송.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        
    }
    
    public class Game extends Thread{
    	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
        private int msg;
        private int Player;
        private int MyTurn = 0;
        private ArrayList Inst = new ArrayList<String>();
        private ArrayList OpenCards = new ArrayList<String>();
        
        public Game(){
            Inst.add("CardSet");
            Inst.add("CardOK");
            Inst.add("CardDraw");
            Inst.add("TurnOver");
            Inst.add("GameOver");
        }  
        public void run() {
            
		try {
                        
			socket = new Socket("165.229.89.38", 8888); // IP 127.0.0.1 , 포트 8888 소켓 연결
                        
                        in = new DataInputStream(socket.getInputStream());
                        out = new DataOutputStream(socket.getOutputStream());
                        
                        
                        out.writeUTF(ID);
                        Player =  Integer.parseInt(in.readUTF());
                        if(Player == 0)
                            MyTurn = 1;
                        
                        if(Player == 0)
                            StartButton.setVisible(true);
                        else
                            StartButton.setVisible(false);
                        
			while(in!=null){
				msg=Integer.parseInt(in.readUTF());       // 서버에서 클라이언트로 전달된 값을 읽어옴.
                                separateInst(msg);
			}
                                
                                
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
        public int returnPlayer(){
            return Player;
        }
        
        public int returnTurn(){
            return MyTurn;
        }
        
        
        public void sendMessage(String msg) {
            try {
                out.writeUTF(msg);  // 서버로 값 전송.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public void separateInst(int Message) throws IOException{
            
            switch(Message){
                
                case 0 : 
                    cardSet();
                    break;
                case 1 :
                    cardOK();
                    break;
                case 2 :
                    cardDraw();
                    break;
                    
                case 3 :
                    turnOver();
                    break;
                case 4 :
                    gameOver();
                    break;
                case 5 :
                    containCard();
                    break;
            }
        }
        
        public void cardSet() throws IOException{
            DrawIndex = 0;
            String plus;
            
            String s = in.readUTF();
            s = s.replace("[", "").replace("]", "");
            String[] temp = s.split(", ");
            
            for(String a : temp)
                Cards.add(a);
            
            int players = Integer.parseInt(in.readUTF());
            
            StartButton.setVisible(false);
            
            
            ArrayList needSort = new ArrayList();
 
            for(int i= 0; i < players; i++){
                
                for(int j = 0; j < 4; j++)
                needSort.add(Cards.get(DrawIndex++)); // Cards 배열로 부터 저장된 4개의 값을 가져옴.

                Collections.sort(needSort); // 가져온 값들을 정렬.


                for(int k = 0; k < 4; k++){
                    JButton button = (JButton)UserCard[i][k];
                    button.setText(needSort.get(k).toString());
                    ImageIcon icon = null;

                    
                    if(i==0 || i == 2)
                        plus = "";
                    else if(i == 1)
                        plus = "_90";
                    else
                        plus = "_270";
                    
                    
                    if(i != Player){
                        
                        if(button.getText().contains("_0"))
                                icon = new ImageIcon(getClass().getResource("/data/whiteb"+plus+".jpg"));
                        else
                                icon = new ImageIcon(getClass().getResource("/data/blackb"+plus+".jpg"));
                    }
                    
                    if(i == Player){
                        if(button.getText().contains("_0")){
                            if(button.getText().contains("A"))
                                icon = new ImageIcon(getClass().getResource("/data/wa"+plus+".jpg"));
                            else if(button.getText().contains("B"))
                                icon = new ImageIcon(getClass().getResource("/data/wb"+plus+".jpg"));
                            else if(button.getText().contains("C"))
                                icon = new ImageIcon(getClass().getResource("/data/wc"+plus+".jpg"));
                            else
                            icon = new ImageIcon(getClass().getResource("/data/w" + button.getText().replace("_0", "") + plus+".jpg"));
                        }
                                
                        else{
                             if(button.getText().contains("A"))
                                icon = new ImageIcon(getClass().getResource("/data/ba"+plus+".jpg"));
                            else if(button.getText().contains("B"))
                                icon = new ImageIcon(getClass().getResource("/data/bb"+plus+".jpg"));
                            else if(button.getText().contains("C"))
                                icon = new ImageIcon(getClass().getResource("/data/bc"+plus+".jpg"));
                            else    
                                icon = new ImageIcon(getClass().getResource("/data/b" + button.getText().replace("_1", "") +plus+".jpg"));
                        }
                    }
                         
                    
                    if(i==1 || i==3)
                    icon.setImage(icon.getImage().getScaledInstance(110, 60, Image.SCALE_SMOOTH));
                    else
                    icon.setImage(icon.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH));

                    button.setIcon(icon);
                    
                    button.setVisible(true);
                }
                needSort.clear(); // 비워줌
            }

            
        
               
        }
        
        public void cardOK() throws IOException{
            String open = in.readUTF();
            OpenCards.add(open);
            if(open.contains("_0"))
                JOptionPane.showMessageDialog(null, "하얀 " + open.replace("_0", "") + "번을 찾았습니다");
            else
                JOptionPane.showMessageDialog(null, "검은 " + open.replace("_1", "") + "번을 찾았습니다");
            
            ImageIcon icon = null;
            int findflag = 0;
            int i=0,j=0;
            
            String plus;
            for(i=0; i<4; i++){
                if(i==0 || i == 2)
                    plus = "";
                else if(i == 1)
                    plus = "_90";
                else
                    plus = "_270";
                for(j=0; j<8; j++){
                    if(UserCard[i][j].getText().equals(open)){
                        findflag = 1;
                        
                        if(UserCard[i][j].getText().contains("_0")){
                            if(UserCard[i][j].getText().contains("A"))
                                icon = new ImageIcon(getClass().getResource("/data/wa"+plus+".jpg"));
                            else if(UserCard[i][j].getText().contains("B"))
                                icon = new ImageIcon(getClass().getResource("/data/wb"+plus+".jpg"));
                            else if(UserCard[i][j].getText().contains("C"))
                                icon = new ImageIcon(getClass().getResource("/data/wc"+plus+".jpg"));
                            else
                            icon = new ImageIcon(getClass().getResource("/data/w" + UserCard[i][j].getText().replace("_0", "") +plus+".jpg"));
                        }
                                
                        else{
                             if(UserCard[i][j].getText().contains("A"))
                                icon = new ImageIcon(getClass().getResource("/data/ba"+plus+".jpg"));
                            else if(UserCard[i][j].getText().contains("B"))
                                icon = new ImageIcon(getClass().getResource("/data/bb"+plus+".jpg"));
                            else if(UserCard[i][j].getText().contains("C"))
                                icon = new ImageIcon(getClass().getResource("/data/bc"+plus+".jpg"));
                            else    
                                icon = new ImageIcon(getClass().getResource("/data/b" + UserCard[i][j].getText().replace("_1", "") + plus +".jpg"));
                        }
                    }
                    if(findflag == 1)
                        break;
                }
                if(findflag == 1){
                    break;
                }
            }
            
            if(i == 1 || i == 3){
                    icon.setImage(icon.getImage().getScaledInstance(110, 60, Image.SCALE_SMOOTH));
                    }
                    else{
                    icon.setImage(icon.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH));
                    }
        
                 UserCard[i][j].setIcon(icon);
                 
                 if(i == Player){
                     if(i == 0)
                         UserCard[i][j].setLocation(UserCard[i][j].getX(), UserCard[i][j].getY()+10);
                     else if(i == 1)
                         UserCard[i][j].setLocation(UserCard[i][j].getX()-10, UserCard[i][j].getY());
                     else if(i == 2)
                         UserCard[i][j].setLocation(UserCard[i][j].getX(), UserCard[i][j].getY()-10);
                     else if(i == 3)
                         UserCard[i][j].setLocation(UserCard[i][j].getX()+10, UserCard[i][j].getY());
                 }
                 
                 
        }
        
        public void cardDraw() throws IOException{
            String plus;
            ImageIcon icon = null;
            int jokerIndex = Integer.parseInt(in.readUTF());
            int openplayer = Integer.parseInt(in.readUTF());
            int opencardIndex = Integer.parseInt(in.readUTF());
            
            JOptionPane.showMessageDialog(null, openplayer +"번 플레이어가 드로우합니다.");
            
            ArrayList temp = new ArrayList();
            
            UserCard[openplayer][opencardIndex].setText(Cards.get(DrawIndex++).toString());
            
            
            for(int i = 0; i <= opencardIndex; i++)
                temp.add(UserCard[openplayer][i].getText());
            
            
            Collections.sort(temp);

            /*
            C_0 또는 C_1 (하얀색 또는 검은색 조커) 가 들어올 경우에 해당 값을 랜덤하게 카드내부에서 위치시킵니다.
            */
            
            if(temp.contains("C_0") || temp.contains("C_1"))
            {
                if(temp.contains("C_0")){
                    temp.add(jokerIndex,"C_0");
                    temp.remove(temp.lastIndexOf("C_0"));
                }

                if(temp.contains("C_1")){
                    temp.add(jokerIndex,"C_1");
                    temp.remove(temp.lastIndexOf("C_1"));
                }
            }
            
            if(openplayer==0 || openplayer == 2)
                        plus = "";
            else if(openplayer == 1)
                        plus = "_90";
            else
                        plus = "_270";

            for(int i = 0; i <= opencardIndex; i++){
                UserCard[openplayer][i].setText(temp.get(i).toString());
                
                if(openplayer != Player){
                    if(OpenCards.contains(UserCard[openplayer][i].getText())){
                        if(UserCard[openplayer][i].getText().contains("_0")){
                            if(UserCard[openplayer][i].getText().contains("A"))
                                icon = new ImageIcon(getClass().getResource("/data/wa"+plus+".jpg"));
                            else if(UserCard[openplayer][i].getText().contains("B"))
                                icon = new ImageIcon(getClass().getResource("/data/wb"+plus+".jpg"));
                            else if(UserCard[openplayer][i].getText().contains("C"))
                                icon = new ImageIcon(getClass().getResource("/data/wc"+plus+".jpg"));
                            else
                            icon = new ImageIcon(getClass().getResource("/data/w" + UserCard[openplayer][i].getText().replace("_0", "") + plus+".jpg"));
                        }
                                
                        else{
                             if(UserCard[openplayer][i].getText().contains("A"))
                                icon = new ImageIcon(getClass().getResource("/data/ba"+plus+".jpg"));
                            else if(UserCard[openplayer][i].getText().contains("B"))
                                icon = new ImageIcon(getClass().getResource("/data/bb"+plus+".jpg"));
                            else if(UserCard[openplayer][i].getText().contains("C"))
                                icon = new ImageIcon(getClass().getResource("/data/bc"+plus+".jpg"));
                            else    
                                icon = new ImageIcon(getClass().getResource("/data/b" + UserCard[openplayer][i].getText().replace("_1", "") +plus+".jpg"));
                        }
                    }
                    else{
                        if(UserCard[openplayer][i].getText().contains("_0"))
                                icon = new ImageIcon(getClass().getResource("/data/whiteb"+plus+".jpg"));
                        else
                                icon = new ImageIcon(getClass().getResource("/data/blackb"+plus+".jpg"));
                    }                      
                    
                }
                else{
                        if(UserCard[openplayer][i].getText().contains("_0")){
                            if(UserCard[openplayer][i].getText().contains("A"))
                                icon = new ImageIcon(getClass().getResource("/data/wa"+plus+".jpg"));
                            else if(UserCard[openplayer][i].getText().contains("B"))
                                icon = new ImageIcon(getClass().getResource("/data/wb"+plus+".jpg"));
                            else if(UserCard[openplayer][i].getText().contains("C"))
                                icon = new ImageIcon(getClass().getResource("/data/wc"+plus+".jpg"));
                            else
                            icon = new ImageIcon(getClass().getResource("/data/w" + UserCard[openplayer][i].getText().replace("_0", "") + plus+".jpg"));
                        }
                                
                        else{
                             if(UserCard[openplayer][i].getText().contains("A"))
                                icon = new ImageIcon(getClass().getResource("/data/ba"+plus+".jpg"));
                            else if(UserCard[openplayer][i].getText().contains("B"))
                                icon = new ImageIcon(getClass().getResource("/data/bb"+plus+".jpg"));
                            else if(UserCard[openplayer][i].getText().contains("C"))
                                icon = new ImageIcon(getClass().getResource("/data/bc"+plus+".jpg"));
                            else    
                                icon = new ImageIcon(getClass().getResource("/data/b" + UserCard[openplayer][i].getText().replace("_1", "") +plus+".jpg"));
                        }
                    
                }
                
                if(openplayer == 1 || openplayer == 3){
                    icon.setImage(icon.getImage().getScaledInstance(110, 60, Image.SCALE_SMOOTH));
                    }
                    else{
                    icon.setImage(icon.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH));
                    }       
                 UserCard[openplayer][i].setIcon(icon);
            
            }           
            UserCard[openplayer][opencardIndex].setVisible(true);            
        }
        
        public void turnOver() throws IOException{
            int tp = Integer.parseInt(in.readUTF());
            
            if(Player == tp)
                MyTurn = 1;
            else
                MyTurn = 0;

            JOptionPane.showMessageDialog(null, tp + "의 차례입니다.");
        }
        
        public void gameOver() throws IOException{
            int win = Integer.parseInt(in.readUTF());
            
            JOptionPane.showMessageDialog(null, win + "의 승리입니다.");
            out.writeUTF("GameOver");
            if(Player == 0)
                StartButton.setVisible(true);
            OpenCards.clear();
            for (JButton button : SouthCard) 
            button.setVisible(false);
            for (JButton button : WestCard) 
            button.setVisible(false);
            for (JButton button : NorthCard) 
            button.setVisible(false);
            for (JButton button : EastCard) 
            button.setVisible(false);       
        }        
        
        public void containCard() throws IOException{
            OpenCardFlag = Integer.parseInt(in.readUTF());
        }       
    }
 private String ID = null;
    private Game g = null;
    private Client c = null;
    
    private PopUp p = new PopUp(this, true);
    private Dimension ScreenSize = null;
    /**
     * Creates new form Screen
     */
    private ArrayList Cards = new ArrayList();

    // Contain
    private Component[] contain = null;

    private JButton[][] UserCard;
    private int OpenCardFlag = 0;
    
    private int DrawIndex; // 나누어 주는 카드 Array 인덱스.

    private JButton[] SouthCard;
    private JButton[] WestCard;
    private JButton[] NorthCard;
    private JButton[] EastCard;

    public Screen() {
        initComponents();
        
        g = new Game();
        g.start();
        c = new Client();
        c.start();
        
        initButton();
        Center();       
        
        ImageIcon start = new ImageIcon(getClass().getResource("/data/start.bmp"));
        start.setImage(start.getImage().getScaledInstance(160, 60, Image.SCALE_SMOOTH));
        StartButton.setIcon(start);
    }
    
    public Screen(String id) {
        
        initComponents();
        ID = id;     
        
        g = new Game();
        g.start();
        c = new Client();
        c.start();
        
        initButton();
        Center();
        
        ImageIcon start = new ImageIcon(getClass().getResource("/data/start.bmp"));
        start.setImage(start.getImage().getScaledInstance(160, 60, Image.SCALE_SMOOTH));
        StartButton.setIcon(start);
    }

   public void Center() {

      ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int cx = (ScreenSize.width / 2) - (this.getWidth() / 2); // 스크린 중앙 배치
      int cy = (ScreenSize.height / 2) - (this.getHeight() / 2);
      this.setLocation(cx, cy);

   }

   /*
    * 카드로 쓸 버튼들을 배열에 넣어서 플레이어별 조작이 용이하도록 초기화
    */
   public void initButton() {

      SouthCard = new JButton[] { s1, s2, s3, s4, s5, s6, s7, s8, };
      WestCard = new JButton[] { w1, w2, w3, w4, w5, w6, w7, w8 };
      NorthCard = new JButton[] { n1, n2, n3, n4, n5, n6, n7, n8 };
      EastCard = new JButton[] { e1, e2, e3, e4, e5, e6, e7, e8 };

      UserCard = new JButton[][] { SouthCard, WestCard, NorthCard, EastCard };

      for (JButton button : SouthCard)
         button.setVisible(false);
      for (JButton button : WestCard)
         button.setVisible(false);
      for (JButton button : NorthCard)
         button.setVisible(false);
      for (JButton button : EastCard)
         button.setVisible(false);
   }

   public void appendMsg(String msg) {
      chatLog.append(msg);
      jScrollPane1.getVerticalScrollBar().setValue(
            jScrollPane1.getVerticalScrollBar().getMaximum());

   }

   /*
    * 다이어로그 PopUp 를 통해 들어온 값이 참인지 거짓인지 판별.
    */
   private void Check(JButton compare) {
      // TODO add your handling code here:
      int kind;
      OpenCardFlag = 0;
      g.sendMessage("ContainOpen");
      g.sendMessage(compare.getText());

      try {
         Thread.sleep(100);
      } catch (InterruptedException ex) {
         Logger.getLogger(Screen.class.getName())
               .log(Level.SEVERE, null, ex);
      }

      if (OpenCardFlag == 1)
         return;

      else {
         p.setVisible(true);

         if (p.ReturnValue().equals(compare.getText())) {
            g.sendMessage("CardOK");
            g.sendMessage(compare.getText());

            kind = JOptionPane.showConfirmDialog(null,
                  "정답입니다. 한번 더 하시겠습니까?", "축하합니다.",
                  JOptionPane.YES_NO_OPTION);
            if (kind == JOptionPane.YES_OPTION)
               return;
            else {
               g.sendMessage("TurnOver");
               g.sendMessage("DrawCard");
            }
         } else if (p.ReturnValue().equals("-1_-1")) {

         } else {
            JOptionPane.showMessageDialog(null, "틀렸습니다");
            g.sendMessage("MyDraw");
            g.sendMessage(g.returnPlayer() + "");
            g.sendMessage("TurnOver");
            g.sendMessage("DrawCard");
         }
      }
   }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        Content = new javax.swing.JPanel();
        StartButton = new javax.swing.JButton();
        w1 = new javax.swing.JButton();
        w2 = new javax.swing.JButton();
        w3 = new javax.swing.JButton();
        w4 = new javax.swing.JButton();
        w5 = new javax.swing.JButton();
        w6 = new javax.swing.JButton();
        w7 = new javax.swing.JButton();
        w8 = new javax.swing.JButton();
        n1 = new javax.swing.JButton();
        n2 = new javax.swing.JButton();
        n3 = new javax.swing.JButton();
        n4 = new javax.swing.JButton();
        n5 = new javax.swing.JButton();
        n6 = new javax.swing.JButton();
        n7 = new javax.swing.JButton();
        n8 = new javax.swing.JButton();
        e1 = new javax.swing.JButton();
        e2 = new javax.swing.JButton();
        e3 = new javax.swing.JButton();
        e4 = new javax.swing.JButton();
        e5 = new javax.swing.JButton();
        e6 = new javax.swing.JButton();
        e7 = new javax.swing.JButton();
        e8 = new javax.swing.JButton();
        s1 = new javax.swing.JButton();
        s2 = new javax.swing.JButton();
        s3 = new javax.swing.JButton();
        s4 = new javax.swing.JButton();
        s5 = new javax.swing.JButton();
        s6 = new javax.swing.JButton();
        s7 = new javax.swing.JButton();
        s8 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        ChatPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatLog = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        Chat = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1400, 840));

        Content.setPreferredSize(new java.awt.Dimension(1400, 840));
        Content.setLayout(null);

        StartButton.setText("start");
        StartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartButtonActionPerformed(evt);
            }
        });
        Content.add(StartButton);
        StartButton.setBounds(460, 370, 140, 60);

        w1.setText("jButton18");
        w1.setPreferredSize(new java.awt.Dimension(100, 60));
        w1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                w1ActionPerformed(evt);
            }
        });
        Content.add(w1);
        w1.setBounds(10, 90, 100, 60);

        w2.setText("jButton19");
        w2.setPreferredSize(new java.awt.Dimension(100, 60));
        w2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                w2ActionPerformed(evt);
            }
        });
        Content.add(w2);
        w2.setBounds(10, 170, 100, 60);

        w3.setText("jButton20");
        w3.setPreferredSize(new java.awt.Dimension(100, 60));
        w3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                w3ActionPerformed(evt);
            }
        });
        Content.add(w3);
        w3.setBounds(10, 250, 100, 60);

        w4.setText("jButton21");
        w4.setPreferredSize(new java.awt.Dimension(100, 60));
        w4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                w4ActionPerformed(evt);
            }
        });
        Content.add(w4);
        w4.setBounds(10, 330, 100, 60);

        w5.setText("jButton22");
        w5.setPreferredSize(new java.awt.Dimension(100, 60));
        w5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                w5ActionPerformed(evt);
            }
        });
        Content.add(w5);
        w5.setBounds(10, 410, 100, 60);

        w6.setText("jButton23");
        w6.setPreferredSize(new java.awt.Dimension(100, 60));
        w6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                w6ActionPerformed(evt);
            }
        });
        Content.add(w6);
        w6.setBounds(10, 490, 100, 60);

        w7.setText("jButton24");
        w7.setPreferredSize(new java.awt.Dimension(100, 60));
        w7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                w7ActionPerformed(evt);
            }
        });
        Content.add(w7);
        w7.setBounds(10, 570, 100, 60);

        w8.setText("jButton25");
        w8.setPreferredSize(new java.awt.Dimension(100, 60));
        w8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                w8ActionPerformed(evt);
            }
        });
        Content.add(w8);
        w8.setBounds(10, 650, 100, 60);

        n1.setText("jButton26");
        n1.setPreferredSize(new java.awt.Dimension(60, 100));
        n1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                n1ActionPerformed(evt);
            }
        });
        Content.add(n1);
        n1.setBounds(140, 10, 60, 100);

        n2.setText("jButton27");
        n2.setPreferredSize(new java.awt.Dimension(60, 100));
        n2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                n2ActionPerformed(evt);
            }
        });
        Content.add(n2);
        n2.setBounds(240, 10, 60, 100);

        n3.setText("jButton28");
        n3.setPreferredSize(new java.awt.Dimension(60, 100));
        n3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                n3ActionPerformed(evt);
            }
        });
        Content.add(n3);
        n3.setBounds(340, 10, 60, 100);

        n4.setText("jButton29");
        n4.setPreferredSize(new java.awt.Dimension(60, 100));
        n4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                n4ActionPerformed(evt);
            }
        });
        Content.add(n4);
        n4.setBounds(440, 10, 60, 100);

        n5.setText("jButton30");
        n5.setPreferredSize(new java.awt.Dimension(60, 100));
        n5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                n5ActionPerformed(evt);
            }
        });
        Content.add(n5);
        n5.setBounds(540, 10, 60, 100);

        n6.setText("jButton31");
        n6.setPreferredSize(new java.awt.Dimension(60, 100));
        n6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                n6ActionPerformed(evt);
            }
        });
        Content.add(n6);
        n6.setBounds(640, 10, 60, 100);

        n7.setText("jButton32");
        n7.setPreferredSize(new java.awt.Dimension(60, 100));
        n7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                n7ActionPerformed(evt);
            }
        });
        Content.add(n7);
        n7.setBounds(740, 10, 60, 100);

        n8.setText("jButton33");
        n8.setPreferredSize(new java.awt.Dimension(60, 100));
        n8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                n8ActionPerformed(evt);
            }
        });
        Content.add(n8);
        n8.setBounds(840, 10, 60, 100);

        e1.setText("jButton17");
        e1.setPreferredSize(new java.awt.Dimension(100, 60));
        e1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e1ActionPerformed(evt);
            }
        });
        Content.add(e1);
        e1.setBounds(940, 90, 100, 60);

        e2.setText("jButton16");
        e2.setPreferredSize(new java.awt.Dimension(100, 60));
        e2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e2ActionPerformed(evt);
            }
        });
        Content.add(e2);
        e2.setBounds(940, 170, 100, 60);

        e3.setText("jButton15");
        e3.setPreferredSize(new java.awt.Dimension(100, 60));
        e3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e3ActionPerformed(evt);
            }
        });
        Content.add(e3);
        e3.setBounds(940, 250, 100, 60);

        e4.setText("jButton14");
        e4.setPreferredSize(new java.awt.Dimension(100, 60));
        e4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e4ActionPerformed(evt);
            }
        });
        Content.add(e4);
        e4.setBounds(940, 330, 100, 60);

        e5.setText("jButton13");
        e5.setPreferredSize(new java.awt.Dimension(100, 60));
        e5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e5ActionPerformed(evt);
            }
        });
        Content.add(e5);
        e5.setBounds(940, 410, 100, 60);

        e6.setText("jButton12");
        e6.setPreferredSize(new java.awt.Dimension(100, 60));
        e6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e6ActionPerformed(evt);
            }
        });
        Content.add(e6);
        e6.setBounds(940, 490, 100, 60);

        e7.setText("jButton11");
        e7.setPreferredSize(new java.awt.Dimension(100, 60));
        e7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e7ActionPerformed(evt);
            }
        });
        Content.add(e7);
        e7.setBounds(940, 570, 100, 60);

        e8.setText("jButton10");
        e8.setPreferredSize(new java.awt.Dimension(100, 60));
        e8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                e8ActionPerformed(evt);
            }
        });
        Content.add(e8);
        e8.setBounds(940, 650, 100, 60);

        s1.setText("jButton2");
        s1.setPreferredSize(new java.awt.Dimension(60, 100));
        s1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s1ActionPerformed(evt);
            }
        });
        Content.add(s1);
        s1.setBounds(150, 690, 60, 100);

        s2.setText("jButton3");
        s2.setPreferredSize(new java.awt.Dimension(60, 100));
        s2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s2ActionPerformed(evt);
            }
        });
        Content.add(s2);
        s2.setBounds(250, 690, 60, 100);

        s3.setText("jButton4");
        s3.setPreferredSize(new java.awt.Dimension(60, 100));
        s3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s3ActionPerformed(evt);
            }
        });
        Content.add(s3);
        s3.setBounds(350, 690, 60, 100);

        s4.setText("jButton5");
        s4.setPreferredSize(new java.awt.Dimension(60, 100));
        s4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s4ActionPerformed(evt);
            }
        });
        Content.add(s4);
        s4.setBounds(450, 690, 60, 100);

        s5.setText("jButton6");
        s5.setPreferredSize(new java.awt.Dimension(60, 100));
        s5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s5ActionPerformed(evt);
            }
        });
        Content.add(s5);
        s5.setBounds(550, 690, 60, 100);

        s6.setText("jButton7");
        s6.setPreferredSize(new java.awt.Dimension(60, 100));
        s6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s6ActionPerformed(evt);
            }
        });
        Content.add(s6);
        s6.setBounds(650, 690, 60, 100);

        s7.setText("jButton8");
        s7.setPreferredSize(new java.awt.Dimension(60, 100));
        s7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s7ActionPerformed(evt);
            }
        });
        Content.add(s7);
        s7.setBounds(750, 690, 60, 100);

        s8.setText("jButton9");
        s8.setPreferredSize(new java.awt.Dimension(60, 100));
        s8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s8ActionPerformed(evt);
            }
        });
        Content.add(s8);
        s8.setBounds(850, 690, 60, 100);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/data/back1.png"))); // NOI18N
        Content.add(jLabel1);
        jLabel1.setBounds(0, 0, 1050, 800);

        ChatPanel.setMinimumSize(new java.awt.Dimension(300, 780));
        ChatPanel.setPreferredSize(new java.awt.Dimension(300, 780));
        ChatPanel.setLayout(null);

        chatLog.setEditable(false);
        chatLog.setColumns(20);
        chatLog.setLineWrap(true);
        chatLog.setRows(30);
        jScrollPane1.setViewportView(chatLog);

        ChatPanel.add(jScrollPane1);
        jScrollPane1.setBounds(10, 10, 310, 650);

        Chat.setColumns(20);
        Chat.setLineWrap(true);
        Chat.setRows(1);
        Chat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ChatKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ChatKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(Chat);

        ChatPanel.add(jScrollPane2);
        jScrollPane2.setBounds(12, 670, 310, 120);

        Content.add(ChatPanel);
        ChatPanel.setBounds(1050, 0, 330, 800);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Content, javax.swing.GroupLayout.PREFERRED_SIZE, 1373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Content, javax.swing.GroupLayout.PREFERRED_SIZE, 808, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>                        

    private void StartButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
        
        g.sendMessage("SetCard");
        g.sendMessage("DrawCard");
        
    }                                           

    
    private void s1ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=0 && g.returnTurn() == 1)
            Check(s1);
    }                                  

    private void s2ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=0 && g.returnTurn() == 1)
            Check(s2);
    }                                  

    private void s3ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=0 && g.returnTurn() == 1)
            Check(s3);
    }                                  

    private void s4ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=0 && g.returnTurn() == 1)
            Check(s4);
    }                                  

    private void s5ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=0 && g.returnTurn() == 1)
            Check(s5);
    }                                  

    private void s6ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=0 && g.returnTurn() == 1)
            Check(s6);
    }                                  

    private void s7ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=0 && g.returnTurn() == 1)
            Check(s7);
    }                                  

    private void s8ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=0 && g.returnTurn() == 1)
            Check(s8);
    }                                  

    private void w1ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=1 && g.returnTurn() == 1)
            Check(w1);
    }                                  

    private void w7ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=1 && g.returnTurn() == 1)
            Check(w7);
    }                                  

    private void w2ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=1 && g.returnTurn() == 1)
            Check(w2);
    }                                  

    private void w3ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=1 && g.returnTurn() == 1)
            Check(w3);
    }                                  

    private void w4ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=1 && g.returnTurn() == 1)
            Check(w4);
    }                                  

    private void w5ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=1 && g.returnTurn() == 1)
            Check(w5);
    }                                  

    private void w6ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=1 && g.returnTurn() == 1)
            Check(w6);
    }                                  

    private void w8ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer()!=1 && g.returnTurn() == 1)
            Check(w8);
    }                                  

    private void n1ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 2 && g.returnTurn() == 1)
            Check(n1);
    }                                  

    private void n2ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 2 && g.returnTurn() == 1)
            Check(n2);
    }                                  

    private void n3ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 2 && g.returnTurn() == 1)
            Check(n3);
    }                                  

    private void n4ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 2 && g.returnTurn() == 1)
            Check(n4);
    }                                  

    private void n5ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 2 && g.returnTurn() == 1)
            Check(n5);
    }                                  

    private void n6ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 2 && g.returnTurn() == 1)
            Check(n6);
    }                                  

    private void n7ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 2 && g.returnTurn() == 1)
            Check(n7);
    }                                  

    private void n8ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 2 && g.returnTurn() == 1)
            Check(n8);
    }                                  

    private void e8ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 3 && g.returnTurn() == 1)
            Check(e8);
    }                                  

    private void e7ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 3 && g.returnTurn() == 1)
            Check(e7);
    }                                  

    private void e6ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 3 && g.returnTurn() == 1)
            Check(e6);
    }                                  

    private void e5ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 3 && g.returnTurn() == 1)
            Check(e5);
    }                                  

    private void e4ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 3 && g.returnTurn() == 1)
            Check(e4);
    }                                  

    private void e3ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 3 && g.returnTurn() == 1)
            Check(e3);
    }                                  

    private void e2ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 3 && g.returnTurn() == 1)
            Check(e2);
    }                                  

    private void e1ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        if(g.returnPlayer() != 3 && g.returnTurn() == 1)
            Check(e1);
    }                                  

    private void ChatKeyReleased(java.awt.event.KeyEvent evt) {                                 
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            if(!Chat.getText().equals("")){
                if(!Chat.getText().equals("\n")){
                    String msg = ID + " : " + Chat.getText().replaceAll("\n", "").replaceAll("\r", "") + "\r\n";
                    c.sendMessage(msg);
                    Chat.setText("");
                }

            }
            if(Chat.getText().equals("\n")){
                Chat.setText("");
            }
            jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getMaximum());
        }
    }                                

    /*
    채팅방에서 엔터를 누르고 있을 경우
    텍스트 아레아에 존재하고 있던 값을 서버에 전송시키고,
    서버는 그 값을 클라이언트들에게 뿌려줌.
    */
    private void ChatKeyPressed(java.awt.event.KeyEvent evt) {                                
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            Chat.setText(Chat.getText().replace("\n", ""));
        }
    }                               
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Screen().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JTextArea Chat;
    private javax.swing.JPanel ChatPanel;
    private javax.swing.JPanel Content;
    private javax.swing.JButton StartButton;
    private javax.swing.JTextArea chatLog;
    private javax.swing.JButton e1;
    private javax.swing.JButton e2;
    private javax.swing.JButton e3;
    private javax.swing.JButton e4;
    private javax.swing.JButton e5;
    private javax.swing.JButton e6;
    private javax.swing.JButton e7;
    private javax.swing.JButton e8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton n1;
    private javax.swing.JButton n2;
    private javax.swing.JButton n3;
    private javax.swing.JButton n4;
    private javax.swing.JButton n5;
    private javax.swing.JButton n6;
    private javax.swing.JButton n7;
    private javax.swing.JButton n8;
    private javax.swing.JButton s1;
    private javax.swing.JButton s2;
    private javax.swing.JButton s3;
    private javax.swing.JButton s4;
    private javax.swing.JButton s5;
    private javax.swing.JButton s6;
    private javax.swing.JButton s7;
    private javax.swing.JButton s8;
    private javax.swing.JButton w1;
    private javax.swing.JButton w2;
    private javax.swing.JButton w3;
    private javax.swing.JButton w4;
    private javax.swing.JButton w5;
    private javax.swing.JButton w6;
    private javax.swing.JButton w7;
    private javax.swing.JButton w8;
    // End of variables declaration                   
}
