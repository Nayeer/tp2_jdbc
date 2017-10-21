package graphic;

import java.awt.EventQueue;

import javax.swing.JFrame;
import jdbcConsole.JDBCAppConsole;

import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;

public class MAinWindow implements ActionListener {

	private JFrame frame;

	JDBCAppConsole JDBCengine;
	Vector<String> columnNames;
	Vector<Object> data;
	JButton btnConnect;
	JCheckBox chckbxAutocommit;
	JScrollPane scrollPaneTable;
	Connection connection;
	ModifPanel modifPanel;
	AddPanel addPanel;
	ConnectionPanel connectionPanel;
	ConnectedPanel connectedPanel;
	CardLayout cardLayout;
	JPanel cards;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MAinWindow window = new MAinWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MAinWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("JDBC");
		frame.setSize(900, 500);
		//frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		JDBCengine = new JDBCAppConsole(true);
		columnNames = new Vector<String>();
		data = new Vector<Object>();
		
		connectionPanel = new ConnectionPanel();
		//A supprimer
		connectionPanel.valentin.addActionListener(this);
		//
		connectionPanel.btnConnect.addActionListener(this);
		
		connectedPanel = new ConnectedPanel();
		connectedPanel.setVisible(false);
		connectedPanel.btnGo.addActionListener(this);
		connectedPanel.btnQuit.addActionListener(this);
		connectedPanel.btnReset.addActionListener(this);
		connectedPanel.btnCommit.addActionListener(this);
		connectedPanel.btnRollback.addActionListener(this);
		connectedPanel.chckbxAutocommit.addActionListener(this);
		
		//Menu bar
		connectedPanel.mntmModification.addActionListener(this);
		connectedPanel.mntmView.addActionListener(this);
		connectedPanel.mntmAjouts.addActionListener(this);

		
		
		modifPanel = null;
		addPanel = null;
		
		

		//afficheResult();
		//afficheConnect();
	//	removeConnectionPane();
		cards = new JPanel(new CardLayout());
		cardLayout = (CardLayout) cards.getLayout();
		cards.add(connectionPanel, "connectionPanel");
		cards.add(connectedPanel, "connectedPanel");
		
		frame.getContentPane().add(cards);
	}
	
	public void connection(){
		JDBCengine.setURL(connectionPanel.urlTextArea.getText());
		JDBCengine.setLog(connectionPanel.loginTextField.getText());
		JDBCengine.setPass(connectionPanel.psswdTextField.getText());
		try {
			connection = JDBCengine.connect();
			modifPanel = new ModifPanel(connection);
			addPanel = new AddPanel(connection);
			cards.add(modifPanel, "modifPanel");
			cards.add(addPanel, "addPanel");
			showConnectedPanel();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showConnectionPanel() {
		cardLayout.show(cards, "connectionPanel");
		frame.setJMenuBar(null);
	}
	
	public void showConnectedPanel() {
		cardLayout.show(cards, "connectedPanel");
		frame.setJMenuBar(connectedPanel.menuBar);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();

		if (source.getClass() == new JButton().getClass()) {

			switch( ((JButton) source).getText() ) {
			case "Go" : 
				System.out.println("go");

				StringBuffer s = new StringBuffer();
				String buffer = connectedPanel.saisieTextField.getText();
				if (!buffer.isEmpty()) {
					s.append(buffer);
					try {
						connectedPanel.afficheResultSet(JDBCengine.executeStatement(s));
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				break;
			case "Commit":
				JDBCengine.commit();
				break;
			case "Rollback":
				JDBCengine.rollback();
				break;
			case "Connect":
				connection();
				break;
			case "Quit":
				showConnectionPanel();
				try {
					JDBCengine.connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case "Reset":
				connectedPanel.saisieTextField.setText("");
				break;
				
			}
		}
		else if (source.getClass() == new JCheckBox().getClass()) {
			switch( ((JCheckBox) source).getText() ) {
			case "AutoCommit" : 
				JDBCengine.autoCommit();
				break;
			//A SUPPRIMER
			case "Valentin" : 
				if ( ((JCheckBox)source).isSelected()) {
					connectionPanel.urlTextArea.setText("jdbc:mysql://localhost:3306/coopérative_:@?useSSL=false");
				}
				else {
					connectionPanel.urlTextArea.setText("jdbc:mysql://127.0.0.1:3306/cooperative?autoReconnect=true&useSSL=false");
				}
			}
		}
		else if (source.getClass() == new JMenuItem().getClass()) {
			switch ( ((JMenuItem) source).getText()) {
			case "Modification" :
				cardLayout.show(cards, "modifPanel");
				System.out.println("modif");
				break;
			case "View" : 
				showConnectedPanel();
				break;
			case "Ajouts" : 
				cardLayout.show(cards, "addPanel");
				break;
			}
		}
			
	}
}
