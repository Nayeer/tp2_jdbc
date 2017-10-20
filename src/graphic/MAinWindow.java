package graphic;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import jdbcConsole.JDBCAppConsole;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JEditorPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MAinWindow implements ActionListener {

	private JFrame frame;
	ResultSet results;

	private JLabel lab1,lab2;
	private JTextField field1, field2;
	private JTextField textField;
	private JTextField textField_1;
	private JTextArea textField_u;
	private JPanel panel;
	private JTable table;
	private JTextField textField_2;
	JPanel tabPanel;
	JPanel controlPane;
	JDBCAppConsole JDBCengine;
	Vector<String> columnNames;
	Vector<Object> data;
	JButton btnConnect;
	JScrollPane scrollPaneTable;
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

		//afficheResult();
		afficheConnect();
	//	removeConnectionPane();
	}
	
	public void afficheConnect(){
		frame.getContentPane().setLayout(new BorderLayout());
		panel = new JPanel();
		panel.setBounds(0, 0, 884, 473);
		//Add connection pane to the window
		//frame.getContentPane().add(panel);
		panel.setLayout(null);
		int w=panel.getWidth();
		JLabel lblConnection = new JLabel("Connection");
		lblConnection.setBounds(w/2-50, 21, 68, 14);
		panel.add(lblConnection);
		
		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setBounds(w/2-150, 200, 42, 14);
		panel.add(lblLogin);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(w/2-150, 250, 68, 14);
		panel.add(lblPassword);
		
		JLabel lblURL = new JLabel("URL:");
		lblURL.setBounds(w/2-150, 100, 68, 14);
		panel.add(lblURL);
		
		
		textField = new JTextField();
		textField.setBounds(w/2+50, 200, 200, 20);
		textField.setColumns(10);
		panel.add(textField);
		textField.setText("root");
		
		
		textField_u= new JTextArea();
		textField_u.setBounds(w/2+50, 100, 200, 60);
		textField_u.setText("jdbc:mysql://localhost:3306/coopérative_:@?useSSL=false");
		textField_u.setLineWrap(true);
		panel.add(textField_u);
		
		textField_1 = new JTextField();
		textField_1.setBounds(w/2+50, 250, 200, 20);
		panel.add(textField_1);
		
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(this);
		btnConnect.setBounds(w/2-50, 350, 100, 23);
		panel.add(btnConnect);
		frame.getContentPane().add(panel);
		
		
	}
		
	public void afficheResult(){
		
		tabPanel = new JPanel();
		tabPanel.setBackground(Color.GRAY);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnEditer = new JMenu("Editer");
		menuBar.add(mnEditer);
		
		JMenuItem mntmView = new JMenuItem("View");
		mnEditer.add(mntmView);
		
		JMenuItem mntmModification = new JMenuItem("Modification");
		mnEditer.add(mntmModification);
		
		
		JLabel lblConnectionResult = new JLabel("Connection Result");
		lblConnectionResult.setFont(new Font("Tahoma", Font.BOLD, 14));
		tabPanel.add(lblConnectionResult);
		
		table = new JTable(data, columnNames);
		table.setDefaultEditor(Object.class, null);
		scrollPaneTable = new JScrollPane(table);

	
		controlPane = new JPanel();
		controlPane.setBackground(Color.GRAY);
		controlPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel_4 = new JPanel();
		controlPane.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));
		
		textField_2 = new JTextField();
		textField_2.setColumns(3);
		panel_4.add(textField_2);
		panel_4.setPreferredSize(new Dimension(700,70));
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(Color.GRAY);
		panel_4.add(panel_5);
		
		JButton btnGo = new JButton("Go");
		btnGo.setBackground(Color.WHITE);
		btnGo.setForeground(Color.GREEN);
		btnGo.addActionListener(this);
		panel_5.add(btnGo);
		
		JButton btnReset = new JButton("Reset");
		btnReset.setBackground(Color.WHITE);
		btnReset.setForeground(new Color(255, 153, 0));
		btnReset.addActionListener(this);
		panel_5.add(btnReset);
		
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.setForeground(Color.RED);
		btnQuit.setBackground(Color.WHITE);
		btnQuit.addActionListener(this);
		panel_5.add(btnQuit);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.GRAY);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));
		
		
		JCheckBox chckbxAutocommit = new JCheckBox("AutoCommit");
		chckbxAutocommit.setBackground(Color.LIGHT_GRAY);
		panel_3.add(chckbxAutocommit);
		
		JButton btnCommit = new JButton("Commit");
		btnCommit.setBackground(Color.LIGHT_GRAY);
		panel_3.add(btnCommit);
		
		JButton btnRollback = new JButton("RollBack");
		btnRollback.setBackground(Color.LIGHT_GRAY);
		panel_3.add(btnRollback);

		controlPane.add(panel_3);
		

		
		results = null;
		

		frame.getContentPane().add(tabPanel, BorderLayout.NORTH);
		frame.getContentPane().add(scrollPaneTable, BorderLayout.CENTER);
		frame.getContentPane().add(controlPane, BorderLayout.SOUTH);		
	}
	
	//enlever le panel de connection de la fenetre
	public void removeConnectionPane(){
		afficheResult();

		btnConnect.removeActionListener(null);
		frame.getContentPane().remove(panel);
		SwingUtilities.updateComponentTreeUI(frame);
	}
	
	//enlever le panel de connection de la fenetre
	public void displayConnectionPane(){
		frame.getContentPane().remove(tabPanel);
		frame.getContentPane().remove(scrollPaneTable);
		frame.getContentPane().remove(controlPane);
		afficheConnect();
		SwingUtilities.updateComponentTreeUI(frame);
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
				String buffer = textField_2.getText();
				if (!buffer.isEmpty()) {
					s.append(buffer);
					try {
						afficheResultSet(JDBCengine.executeStatement(s));
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}

				break;
			case "Connect":
				connection();
				break;
			case "Quit":
				displayConnectionPane();
				try {
					JDBCengine.connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case "Reset":
				textField_2.setText("");
				break;
				
			}
		}
	}
	
	
	public void connection(){
		JDBCengine.setURL(textField_u.getText());
		JDBCengine.setLog(textField.getText());
		JDBCengine.setPass(textField_1.getText());
		try {
			JDBCengine.connect();
			removeConnectionPane();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void afficheResultSet(ResultSet result) throws SQLException  {
	    columnNames.clear();
	    data.clear();
	    ResultSetMetaData rsmd = result.getMetaData();
	    int nb_column = rsmd.getColumnCount();   
	    boolean first_time = true;

	    while(result.next()) {
	        Vector<Object> row = new Vector<Object>(nb_column);
	        for(int i=1; i<=nb_column; i++) {
	        	if (first_time) {
	        		columnNames.addElement(rsmd.getColumnLabel(i));
	        	}
	        	
	            row.addElement(result.getObject(i));
	        }
	        first_time = false;
	        data.addElement(row);
	    }
	    
		((DefaultTableModel) table.getModel()).fireTableStructureChanged();
	}
}
