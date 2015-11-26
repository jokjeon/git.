import java.awt.Frame;

import java.awt.Component;
import java.awt.ComponentOrientation;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;

/**
 *
 * @author Sensor
 */
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
                        
			socket = new Socket("127.0.0.1", 9999); // IP 127.0.0.1 , 포트 9999 소켓 연결
			chatLog.append("서버에 연결 되었습니다. \r\n");
                        
                        out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());

			out.writeUTF("쑤수수수발");         // 서버에 ( ) 안의 값을 전달.
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
    private Client c = null;
    private Dimension ScreenSize = null;
    private PopUp p = new PopUp(this, true);
    /**
     * Creates new form Screen
     */
        private ArrayList Cards = new ArrayList();

    // Contain
    private Component[] contain = null;

    private JButton[][] UserCard;

    private int DrawIndex; // 나누어 주는 카드 Array 인덱스.

    private int[] CIndex;   // 각 유저별 카드 인덱스.

    private JButton[] SouthCard;
    private int sIndex;
    private ArrayList OpenScard = new ArrayList();  // 열린 카드 문자열 모아 놓는곳.

    private JButton[] WestCard;
    private int wIndex;
    private ArrayList OpenWcard = new ArrayList();

    private JButton[] NorthCard;
    private int nIndex;
    private ArrayList OpenNcard = new ArrayList();

    private JButton[] EastCard;
    private int eIndex;
    private ArrayList OpenEcard = new ArrayList();
    
    public Screen() {
        initComponents();
        c = new Client();
        c.start();
        initButton();
        Center();
    }
   
    /*
    폼을 중앙에 일치시키기 위함.
    */
    
    public void Center() {

        ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int cx = (ScreenSize.width / 2) - (this.getWidth() / 2); // 스크린 중앙 배치
        int cy = (ScreenSize.height / 2) - (this.getHeight() / 2);
        this.setLocation(cx, cy);

    }
    
    /*
    카드로 쓸 버튼들을 배열에 넣어서 플레이어별 조작이 용이하도록 초기화
    */
    public void initButton() {

        DrawIndex = 0;

        
         SouthCard = new JButton[]{s1,s2,s3,s4,s5,s6,s7,s8,};
         WestCard = new JButton[]{w1,w2,w3,w4,w5,w6,w7,w8};
         NorthCard = new JButton[]{n1,n2,n3,n4,n5,n6,n7,b1};
         EastCard = new JButton[]{e1,e2,e3,e4,e5,e6,e7,e8};
        
         UserCard = new JButton[][]{SouthCard,WestCard,NorthCard,EastCard};
        
         
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

        for (JButton button : SouthCard) {
            button.setVisible(false);
        }
        for (JButton button : WestCard) {
            button.setVisible(false);
        }
        for (JButton button : NorthCard) {
            button.setVisible(false);
        }
        for (JButton button : EastCard) {
            button.setVisible(false);
        }
        
        ChatPanel.setVisible(false);
        DrawButton.setVisible(false);

    }
    
    public void appendMsg(String msg) {
        chatLog.append(msg);
    }
    
    /*
    다이어로그 PopUp 를 통해 들어온 값이 참인지 거짓인지 판별.
    */
    private void Check(JButton compare) {
        // TODO add your handling code here:
        p.setVisible(true);

        if (p.ReturnValue().equals(compare.getText())) {
            JOptionPane.showMessageDialog(null, "맞아");
        } else if (p.ReturnValue().equals("-1_-1")) {
            JOptionPane.showMessageDialog(null, "취소 버튼");
        } else {
            JOptionPane.showMessageDialog(null, "틀려");
        }
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        Content = new javax.swing.JPanel();
        West = new javax.swing.JPanel();
        w1 = new javax.swing.JButton();
        w2 = new javax.swing.JButton();
        w3 = new javax.swing.JButton();
        w4 = new javax.swing.JButton();
        w5 = new javax.swing.JButton();
        w6 = new javax.swing.JButton();
        w7 = new javax.swing.JButton();
        w8 = new javax.swing.JButton();
        North = new javax.swing.JPanel();
        n1 = new javax.swing.JButton();
        n2 = new javax.swing.JButton();
        n3 = new javax.swing.JButton();
        n4 = new javax.swing.JButton();
        n5 = new javax.swing.JButton();
        n6 = new javax.swing.JButton();
        n7 = new javax.swing.JButton();
        b1 = new javax.swing.JButton();
        ChatPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatLog = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        Chat = new javax.swing.JTextArea();
        East = new javax.swing.JPanel();
        e8 = new javax.swing.JButton();
        e7 = new javax.swing.JButton();
        e6 = new javax.swing.JButton();
        e5 = new javax.swing.JButton();
        e4 = new javax.swing.JButton();
        e3 = new javax.swing.JButton();
        e2 = new javax.swing.JButton();
        e1 = new javax.swing.JButton();
        South = new javax.swing.JPanel();
        s1 = new javax.swing.JButton();
        s2 = new javax.swing.JButton();
        s3 = new javax.swing.JButton();
        s4 = new javax.swing.JButton();
        s5 = new javax.swing.JButton();
        s6 = new javax.swing.JButton();
        s7 = new javax.swing.JButton();
        s8 = new javax.swing.JButton();
        Main = new javax.swing.JPanel();
        StartButton = new javax.swing.JButton();
        DrawButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        w1.setText("jButton18");
        w1.setPreferredSize(new java.awt.Dimension(100, 60));

        w2.setText("jButton19");
        w2.setPreferredSize(new java.awt.Dimension(100, 60));

        w3.setText("jButton20");
        w3.setPreferredSize(new java.awt.Dimension(100, 60));

        w4.setText("jButton21");
        w4.setPreferredSize(new java.awt.Dimension(100, 60));

        w5.setText("jButton22");
        w5.setPreferredSize(new java.awt.Dimension(100, 60));

        w6.setText("jButton23");
        w6.setPreferredSize(new java.awt.Dimension(100, 60));

        w7.setText("jButton24");
        w7.setPreferredSize(new java.awt.Dimension(100, 60));

        w8.setText("jButton25");
        w8.setPreferredSize(new java.awt.Dimension(100, 60));

        javax.swing.GroupLayout WestLayout = new javax.swing.GroupLayout(West);
        West.setLayout(WestLayout);
        WestLayout.setHorizontalGroup(
            WestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WestLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(WestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(w3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(w4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(w1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(w6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(w7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(w8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(w5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(w2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        WestLayout.setVerticalGroup(
            WestLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WestLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(w1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(w2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(w3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(w4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(w5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(w6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(w7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(w8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        n1.setText("jButton26");
        n1.setPreferredSize(new java.awt.Dimension(60, 100));

        n2.setText("jButton27");
        n2.setPreferredSize(new java.awt.Dimension(60, 100));

        n3.setText("jButton28");
        n3.setPreferredSize(new java.awt.Dimension(60, 100));

        n4.setText("jButton29");
        n4.setPreferredSize(new java.awt.Dimension(60, 100));

        n5.setText("jButton30");
        n5.setPreferredSize(new java.awt.Dimension(60, 100));

        n6.setText("jButton31");
        n6.setPreferredSize(new java.awt.Dimension(60, 100));

        n7.setText("jButton32");
        n7.setPreferredSize(new java.awt.Dimension(60, 100));

        b1.setText("jButton33");
        b1.setPreferredSize(new java.awt.Dimension(60, 100));

        javax.swing.GroupLayout NorthLayout = new javax.swing.GroupLayout(North);
        North.setLayout(NorthLayout);
        NorthLayout.setHorizontalGroup(
            NorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NorthLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(n1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(n2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(n3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(n4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(n5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(n6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(n7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(b1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        NorthLayout.setVerticalGroup(
            NorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NorthLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(NorthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(n1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(n2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(n3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(n4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(n5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(n6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(n7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        chatLog.setEditable(false);
        chatLog.setColumns(20);
        chatLog.setLineWrap(true);
        chatLog.setRows(32);
        jScrollPane1.setViewportView(chatLog);

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

        javax.swing.GroupLayout ChatPanelLayout = new javax.swing.GroupLayout(ChatPanel);
        ChatPanel.setLayout(ChatPanelLayout);
        ChatPanelLayout.setHorizontalGroup(
            ChatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ChatPanelLayout.createSequentialGroup()
                .addGap(0, 17, Short.MAX_VALUE)
                .addGroup(ChatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addGap(0, 0, 0))
        );
        ChatPanelLayout.setVerticalGroup(
            ChatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ChatPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 664, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
        );

        e8.setText("jButton10");
        e8.setPreferredSize(new java.awt.Dimension(100, 60));

        e7.setText("jButton11");
        e7.setPreferredSize(new java.awt.Dimension(100, 60));

        e6.setText("jButton12");
        e6.setPreferredSize(new java.awt.Dimension(100, 60));

        e5.setText("jButton13");
        e5.setPreferredSize(new java.awt.Dimension(100, 60));

        e4.setText("jButton14");
        e4.setPreferredSize(new java.awt.Dimension(100, 60));

        e3.setText("jButton15");
        e3.setPreferredSize(new java.awt.Dimension(100, 60));

        e2.setText("jButton16");
        e2.setPreferredSize(new java.awt.Dimension(100, 60));

        e1.setText("jButton17");
        e1.setPreferredSize(new java.awt.Dimension(100, 60));

        javax.swing.GroupLayout EastLayout = new javax.swing.GroupLayout(East);
        East.setLayout(EastLayout);
        EastLayout.setHorizontalGroup(
            EastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EastLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(EastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(e7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(e6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(e5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(e4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(e3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(e2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(e1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(e8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        EastLayout.setVerticalGroup(
            EastLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EastLayout.createSequentialGroup()
                .addGap(133, 133, 133)
                .addComponent(e8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(e7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(e6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(e5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(e4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(e3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(e2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(e1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        s1.setText("jButton2");
        s1.setPreferredSize(new java.awt.Dimension(60, 100));
        s1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                s1ActionPerformed(evt);
            }
        });

        s2.setText("jButton3");
        s2.setPreferredSize(new java.awt.Dimension(60, 100));

        s3.setText("jButton4");
        s3.setPreferredSize(new java.awt.Dimension(60, 100));

        s4.setText("jButton5");
        s4.setPreferredSize(new java.awt.Dimension(60, 100));

        s5.setText("jButton6");
        s5.setPreferredSize(new java.awt.Dimension(60, 100));

        s6.setText("jButton7");
        s6.setPreferredSize(new java.awt.Dimension(60, 100));

        s7.setText("jButton8");
        s7.setPreferredSize(new java.awt.Dimension(60, 100));

        s8.setText("jButton9");
        s8.setPreferredSize(new java.awt.Dimension(60, 100));

        javax.swing.GroupLayout SouthLayout = new javax.swing.GroupLayout(South);
        South.setLayout(SouthLayout);
        SouthLayout.setHorizontalGroup(
            SouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SouthLayout.createSequentialGroup()
                .addGap(190, 190, 190)
                .addComponent(s1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(s2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(s3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(s4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(s5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(s6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(s7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(s8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125))
        );
        SouthLayout.setVerticalGroup(
            SouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SouthLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SouthLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(s1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(s2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(s3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(s4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(s5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(s6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(s7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(s8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        StartButton.setText("start");
        StartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartButtonActionPerformed(evt);
            }
        });

        DrawButton.setText("jButton1");
        DrawButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DrawButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MainLayout = new javax.swing.GroupLayout(Main);
        Main.setLayout(MainLayout);
        MainLayout.setHorizontalGroup(
            MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(DrawButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(StartButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(583, Short.MAX_VALUE))
        );
        MainLayout.setVerticalGroup(
            MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StartButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(DrawButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout ContentLayout = new javax.swing.GroupLayout(Content);
        Content.setLayout(ContentLayout);
        ContentLayout.setHorizontalGroup(
            ContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(ContentLayout.createSequentialGroup()
                        .addGroup(ContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(ContentLayout.createSequentialGroup()
                                .addComponent(West, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Main, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(North, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(East, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(South, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ChatPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        ContentLayout.setVerticalGroup(
            ContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ContentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ContentLayout.createSequentialGroup()
                        .addGroup(ContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(East, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(ContentLayout.createSequentialGroup()
                                .addComponent(North, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(ContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(West, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Main, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(South, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ChatPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1282, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(Content, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 825, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Content, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>                        

    private void StartButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:

        North.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        ArrayList needSort = new ArrayList();
        CIndex = new int[4]; // 0 남, 1 서, 2 북, 3 동

        for(int i = 0; i < 4; i++){ // 플레이어 4명

            for(int j = 0; j < 4; j++)
            needSort.add(Cards.get(DrawIndex++)); // Cards 배열로 부터 저장된 4개의 값을 가져옴.

            Collections.sort(needSort); // 가져온 값들을 정렬.

            /*
            4명이 다 들어있다는 가정하에,
            각 버튼 별 데이터를 입력해줌.
            위에서 정렬한대로 그대로 각 버튼의 값을 set.
            */
            for(int k = 0; k < 4; k++){
                JButton button = (JButton)UserCard[i][k];
                button.setText(needSort.get(k).toString());
                /* 카드 사진 추가
                ImageIcon icon = new ImageIcon(getClass().getResource("/data/bb.jpg"));

                if(i==1 || i==3)
                icon.setImage(icon.getImage().getScaledInstance(110, 60, Image.SCALE_SMOOTH));
                else
                icon.setImage(icon.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH));

                button.setIcon(icon);
                */
                button.setVisible(true);
                CIndex[i]++;
            }

            needSort.clear(); // 비워줌
        }

        ChatPanel.setVisible(true);
        StartButton.setVisible(false);
        DrawButton.setVisible(true);
    }                                           

    private void DrawButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
        int index;

        //UserCard[유저][버튼순서];
        // 유저, 남쪽 - 0, 서쪽 - 1, 북쪽 - 2, 동쪽 - 3;
        // --> 소켓 처리할때 인덱스 활용해서 처리해주면됨.

        // 남쪽놈 예시
        if(DrawIndex == 25) // 26장 다 하면 아무에게도 안보임. ( 공통요소 )
        DrawButton.setVisible(false);

        if(CIndex[0] >= 8){

            // 해당 유저에게만 DrawButton 이 안보이도록. 하는 코드가 필요함. (단독 요소)
            return;
        }
        ArrayList temp = new ArrayList();

        UserCard[0][CIndex[0]].setText(Cards.get(DrawIndex++).toString());
        // .setText(Cards.get(DrawIndex++).toString() 공통부분.
            UserCard[0][CIndex[0]++].setVisible(true);  // 드로우를 통해 들어온 값 버튼에 세팅.

            for(int i = 0; i < CIndex[0]; i++)
            temp.add(UserCard[0][i].getText());

            Collections.sort(temp);

            
            /*
            C_0 또는 C_1 (하얀색 또는 검은색 조커) 가 들어올 경우에 해당 값을 랜덤하게 카드내부에서 위치시킵니다.
            */
            if(temp.contains("C_0") || temp.contains("C_1"))
            {
                if(temp.contains("C_0")){
                    index = (int)(Math.random() * (temp.size() - 2));
                    temp.add(index,"C_0");
                    temp.remove(temp.lastIndexOf("C_0"));
                }

                if(temp.contains("C_1")){
                    index = (int)(Math.random() * (temp.size() - 2));
                    temp.add(index,"C_1");
                    temp.remove(temp.lastIndexOf("C_1"));
                }
            }

            for(int i = 0; i < CIndex[0]; i++)
            UserCard[0][i].setText(temp.get(i).toString());
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

    private void ChatKeyReleased(java.awt.event.KeyEvent evt) {                                 
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            if(!Chat.getText().equals("")){
                if(!Chat.getText().equals("\n")){
                    String msg = "Nick Name" + ":" + Chat.getText().replaceAll("\n", "").replaceAll("\r", "") + "\r\n";
                    c.sendMessage(msg);
                    Chat.setText("");
                }

            }
            if(Chat.getText().equals("\n")){
                Chat.setText("");
            }
        }
    }                                

    private void s1ActionPerformed(java.awt.event.ActionEvent evt) {                                   
        // TODO add your handling code here:
        Check(s1);
    }                                  

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
    private javax.swing.JButton DrawButton;
    private javax.swing.JPanel East;
    private javax.swing.JPanel Main;
    private javax.swing.JPanel North;
    private javax.swing.JPanel South;
    private javax.swing.JButton StartButton;
    private javax.swing.JPanel West;
    private javax.swing.JButton b1;
    private javax.swing.JTextArea chatLog;
    private javax.swing.JButton e1;
    private javax.swing.JButton e2;
    private javax.swing.JButton e3;
    private javax.swing.JButton e4;
    private javax.swing.JButton e5;
    private javax.swing.JButton e6;
    private javax.swing.JButton e7;
    private javax.swing.JButton e8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton n1;
    private javax.swing.JButton n2;
    private javax.swing.JButton n3;
    private javax.swing.JButton n4;
    private javax.swing.JButton n5;
    private javax.swing.JButton n6;
    private javax.swing.JButton n7;
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