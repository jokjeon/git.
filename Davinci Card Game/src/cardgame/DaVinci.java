package cardgame;

import java.awt.Color;
import java.awt.Container;
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
import com.mysql.jdbc.Statement;


public class DaVinci extends JFrame implements ActionListener{
	
	Container cp = getContentPane();
	JImagePanel jip = null;
	JTextField id;
	JPasswordField pwd;
	JButton go;
	JButton sign;
	Icon icon1 = new ImageIcon("src/data/go.jpg");
	
	public DaVinci(){
		
		super("Davinci Code Online");
		
		this.setSize(568,297);
		this.setResizable(false);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(tk.getImage("src/data/BackGround.jpg"), 0);
		jip = new JImagePanel(tk.getImage("src/data/BackGround.jpg"));
		
		jip.setLayout(null);
		jip.setBounds(0,0,100,100);
		cp.add(jip);
		
		id = new JTextField("User Name",15);
		id.setFont(new Font("�޸ո���T", Font.PLAIN, 12));
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
		
		sign = new JButton("ȸ������ �Ͻ� ���� ���⸦ �����ּ���!");
		sign.setBounds(5,246,227,20);
		sign.setBackground(Color.white);
		sign.setOpaque(false);
		sign.setFont(new Font("�޸ո���T", Font.PLAIN, 12));
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
					JOptionPane.showMessageDialog(null, "��й�ȣ�� Ʋ���ϴ�.", "�޽���",JOptionPane.ERROR_MESSAGE);
			}
			else
				JOptionPane.showMessageDialog(null, "���̵� �����ϴ�.", "�޽���",JOptionPane.ERROR_MESSAGE);
			
			
		} catch (SQLException sqle) {
			System.err.println("SQLException : " + sqle);
		}
                    
                    
	}
}