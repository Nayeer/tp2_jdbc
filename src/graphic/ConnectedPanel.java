package graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ConnectedPanel extends JPanel {
	
	JMenuBar menuBar;
	Vector<String> columnNames;
	Vector<Object> data;
	JButton btnGo, btnReset, btnQuit,btnCommit,btnRollback;
	JCheckBox chckbxAutocommit;
	JTextField saisieTextField;
	JTable table;
	JMenuItem mntmModification;
	JMenuItem mntmView;

	public ConnectedPanel() {
		
		this.setLayout(new BorderLayout());
		JPanel tabPanel = new JPanel();
		tabPanel.setBackground(Color.GRAY);
		
		menuBar = new JMenuBar();
		
		JMenu mnEditer = new JMenu("Editer");
		menuBar.add(mnEditer);
		
		mntmView = new JMenuItem("View");
		mnEditer.add(mntmView);
		
		mntmModification = new JMenuItem("Modification");
		mnEditer.add(mntmModification);
		
		
		JLabel lblConnectionResult = new JLabel("Connection Result");
		lblConnectionResult.setFont(new Font("Tahoma", Font.BOLD, 14));
		tabPanel.add(lblConnectionResult);
		
		columnNames = new Vector<String>();
		data = new Vector<Object>();
		table = new JTable(data, columnNames);
		table.setDefaultEditor(Object.class, null);
		JScrollPane scrollPaneTable = new JScrollPane(table);

	
		JPanel controlPane = new JPanel();
		controlPane.setBackground(Color.GRAY);
		controlPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panel_4 = new JPanel();
		controlPane.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));
		
		saisieTextField = new JTextField();
		saisieTextField.setColumns(3);
		panel_4.add(saisieTextField);
		panel_4.setPreferredSize(new Dimension(700,70));
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(Color.GRAY);
		panel_4.add(panel_5);
		
		btnGo = new JButton("Go");
		btnGo.setBackground(Color.WHITE);
		btnGo.setForeground(Color.GREEN);
		panel_5.add(btnGo);
		
		btnReset = new JButton("Reset");
		btnReset.setBackground(Color.WHITE);
		btnReset.setForeground(new Color(255, 153, 0));
		panel_5.add(btnReset);
		
		
		btnQuit = new JButton("Quit");
		btnQuit.setForeground(Color.RED);
		btnQuit.setBackground(Color.WHITE);
		panel_5.add(btnQuit);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.GRAY);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));
		
		
		chckbxAutocommit = new JCheckBox("AutoCommit");
		chckbxAutocommit.setBackground(Color.LIGHT_GRAY);
		chckbxAutocommit.setSelected(true);
		panel_3.add(chckbxAutocommit);
		
		btnCommit = new JButton("Commit");
		btnCommit.setBackground(Color.LIGHT_GRAY);
		panel_3.add(btnCommit);
		
		btnRollback = new JButton("Rollback");
		btnRollback.setBackground(Color.LIGHT_GRAY);
		panel_3.add(btnRollback);

		controlPane.add(panel_3);	
		
		this.add(tabPanel, BorderLayout.NORTH);
		this.add(scrollPaneTable, BorderLayout.CENTER);
		this.add(controlPane, BorderLayout.SOUTH);	
	}
	
	public void afficheResultSet(ResultSet result) throws SQLException  {
		if (result != null) {
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
}
