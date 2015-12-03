package cardgame;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mysql.jdbc.PreparedStatement;

public class DaVinci extends JFrame implements ActionListener{
	
	Container cp = getContentPane();
	JImagePanel jip = null;
	JTextField id;
	JPasswordField pwd;
	JButton go;
	JButton sign;
	Dimension ScreenSize = null;
	Icon icon1 = new ImageIcon(getClass().getResource("/data/go.jpg"));
	
	public DaVinci(){
		
		super("Davinci Code Online");
		
	    ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    int cx = (ScreenSize.width / 2) - (this.getWidth() / 2); // 스크린 중앙 배치
	    int cy = (ScreenSize.height / 2) - (this.getHeight() / 2);
	    this.setLocation(cx, cy);
		
		this.setSize(568,297);
		this.setResizable(false);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(tk.getImage(getClass().getResource("/data/BackGround.jpg")), 0);
		jip = new JImagePanel(tk.getImage(getClass().getResource("/data/BackGround.jpg")));
		
		jip.setLayout(null);
		jip.setBounds(0,0,100,100);
		cp.add(jip);
		
		id = new JTextField("User Name",15);
		id.setFont(new Font("휴먼모음T", Font.PLAIN, 12));
		id.setBounds(25, 217, 120, 20);
		jip.add(id);
		
		pwd = new JPasswordField("User Password", 10);
		pwd.setBounds(170, 217, 120, 20);
		jip.add(pwd);
		
		go = new JButton(icon1);
		go.setBounds(303,213,25,28);
		go.setOpaque(false);
		go.setBorderPainted(false);
		go.setContentAreaFilled(false);
		go.addActionListener(this);
		jip.add(go);
		
		sign = new JButton("회원가입 하실 분은 여기를 눌러주세요!");
		sign.setBounds(5,246,227,20);
		sign.setBackground(Color.white);
		sign.setOpaque(false);
		sign.setFont(new Font("휴먼모음T", Font.PLAIN, 12));
		sign.setBorderPainted(false);
		sign.setContentAreaFilled(false);
		sign.addActionListener(this);
		jip.add(sign);
                
                this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setVisible(true);
                
	}

	class JImagePanel extends JPanel{
		private java.awt.Image img;
		public JImagePanel(java.awt.Image image){
			this.img = image;
		}
		public void paintComponent(Graphics g){
			g.drawImage(this.img, 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}
	
	public static void main(String args[]){
		new DaVinci();
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==sign){
			new Sign();
		}
		if(e.getSource()==go){
			JDBCgo();
		}
	}
	
	public void JDBCgo(){
            
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Diver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException : " + e.getMessage());
		}
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://165.229.89.38:3619/Davinci", "admin", "12345");
			PreparedStatement ptmt = (PreparedStatement) conn.prepareStatement("SELECT pw from davinci where id = ?");
			ptmt.setString(1, id.getText());
			rs = ptmt.executeQuery();
			
			if(rs.next()){
				if(rs.getString("pw").equals(pwd.getText())){
                                    java.awt.EventQueue.invokeLater(new Runnable() {
                                        public void run() {
                                            new Screen(id.getText()).setVisible(true);
                                        }
                                    });
                                    this.dispose();
                                }
                                
				else
					JOptionPane.showMessageDialog(null, "비밀번호가 틀립니다.", "메시지",JOptionPane.ERROR_MESSAGE);
			}
			else
				JOptionPane.showMessageDialog(null, "아이디가 없습니다.", "메시지",JOptionPane.ERROR_MESSAGE);
			
			
		} catch (SQLException sqle) {
			System.err.println("SQLException : " + sqle);
		}
                    
                    
	}
}