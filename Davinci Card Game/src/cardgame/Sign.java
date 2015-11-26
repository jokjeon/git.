package cardgame;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mysql.jdbc.PreparedStatement;

public class Sign extends JFrame implements ActionListener{
	
	Container cp = getContentPane();
	JPanel jp = new JPanel();
	JTextField id;
	JPasswordField pw;
	JPasswordField pwChek;
	JLabel lid;
	JLabel lpw;
	JLabel lpwChek;
	JButton join;
	JButton exit;
       
        
	public Sign() {
            
            
		setTitle("회원가입");
		this.setSize(224,150);
		this.setResizable(false);

		jp.setLayout(null);
		jp.setBounds(0,0,100,100);
		cp.add(jp);
		
		lid = new JLabel("이     름              : ");
		lid.setFont(new Font("맑은고딕", Font.PLAIN, 12));
		lid.setBounds(15, 15, 100, 20);
		jp.add(lid);
		
		lpw = new JLabel("비밀번호           : ");
		lpw.setFont(new Font("맑은고딕", Font.PLAIN, 12));
		lpw.setBounds(15, 40, 100, 20);
		jp.add(lpw);
		
		lpwChek = new JLabel("비밀번호 확인  : ");
		lpwChek.setFont(new Font("맑은고딕", Font.PLAIN, 12));
		lpwChek.setBounds(15, 65, 100, 20);
		jp.add(lpwChek);
		
		join = new JButton("가입");
		join.setBounds(32,95,60,20);
		join.addActionListener(this);
		jp.add(join);
		
		exit = new JButton("닫기");
		exit.setBounds(132,95,60,20);
		exit.addActionListener(this);
		jp.add(exit);
		
		id = new JTextField();
		id.setFont(new Font("맑은고딕", Font.PLAIN, 12));
		id.setBounds(120, 15, 80, 20);
		jp.add(id);
		
		pw = new JPasswordField();
		pw.setFont(new Font("맑은고딕", Font.PLAIN, 12));
		pw.setBounds(120, 40, 80, 20);
		jp.add(pw);

		pwChek = new JPasswordField();
		pwChek.setFont(new Font("맑은고딕", Font.PLAIN, 12));
		pwChek.setBounds(120, 65, 80, 20);
		jp.add(pwChek);
                
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==exit){
			this.setVisible(false);
		}
		
		if(e.getSource()==join){
			if(id.getText().isEmpty()||pw.getText().isEmpty()||pwChek.getText().isEmpty()){
				JOptionPane.showMessageDialog(null, "빈칸을 모두 채워주세요.", "메시지",JOptionPane.ERROR_MESSAGE);
			}
			else if (pw.getText().equals(pwChek.getText())){
				JDBCsign();
			}	
			else{
				JOptionPane.showMessageDialog(null, "비밀번호를 확인해주세요.", "메시지",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void JDBCsign(){
		try {
			Class.forName("com.mysql.jdbc.Diver");
		} catch (ClassNotFoundException e) {
			System.err.println("ClassNotFoundException : " + e.getMessage());
		}
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://165.229.89.38:3619/Davinci", "admin", "12345");
			PreparedStatement ptmt = (PreparedStatement) conn.prepareStatement("INSERT INTO davinci VALUES (?, ?)");
			ptmt.setString(1, id.getText());
			ptmt.setString(2, pw.getText());
			ptmt.execute();
			JOptionPane.showMessageDialog(null, "가입되었습니다.");
		} catch (SQLException sqle) {
			//System.err.println("SQLException : " + sqle);
			JOptionPane.showMessageDialog(null, "이미 가입되어있습니다.");
		}
	}
	
	public static void main(String args[]){
		new Sign();
	}
}
