package graphic;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ConnectionPanel extends JPanel {
	
	JTextField loginTextField;
	JTextField psswdTextField;
	JTextArea urlTextArea;
	JButton btnConnect;

	public ConnectionPanel() {

		this.setBounds(0, 0, 884, 473);
		//Add connection pane to the window
		//frame.getContentPane().add(panel);
		this.setLayout(null);
		

		int w=this.getWidth();
		JLabel lblConnection = new JLabel("Connection");
		lblConnection.setBounds(w/2-50, 21, 68, 14);
		this.add(lblConnection);
		
		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setBounds(w/2-150, 200, 42, 14);
		this.add(lblLogin);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(w/2-150, 250, 68, 14);
		this.add(lblPassword);
		
		JLabel lblURL = new JLabel("URL:");
		lblURL.setBounds(w/2-150, 100, 68, 14);
		this.add(lblURL);
		

		loginTextField = new JTextField();
		loginTextField.setBounds(w/2+50, 200, 200, 20);
		loginTextField.setColumns(10);
		this.add(loginTextField);
		loginTextField.setText("root");
		
		

		
		urlTextArea= new JTextArea();
		urlTextArea.setBounds(w/2+50, 100, 200, 60);
		//jdbc:mysql://127.0.0.1:3306/cooperative?autoReconnect=true&useSSL=false
		//jdbc:mysql://localhost:3306/coopérative_:@?useSSL=false
		urlTextArea.setText("jdbc:mysql://127.0.0.1:3306/cooperative?autoReconnect=true&useSSL=false");
		urlTextArea.setLineWrap(true);
		this.add(urlTextArea);
		
		psswdTextField = new JTextField();
		psswdTextField.setBounds(w/2+50, 250, 200, 20);
		this.add(psswdTextField);
		
		btnConnect = new JButton("Connect");
		btnConnect.setBounds(w/2-50, 350, 100, 23);
		this.add(btnConnect);
	}
}
