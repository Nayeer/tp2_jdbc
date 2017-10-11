package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import jdbcConsole.JDBCAppConsole;

public class connectedPanel extends JPanel implements ActionListener {
	
	JTextArea textArea;
	actionPanel ap;
	ResultSet results;
	
	JDBCAppConsole JDBCengine;
	
	Vector<String> columnNames;
	Vector<Object> data;
	
	JTable tableau;

	
	public connectedPanel() {

		this.setLayout(new BorderLayout());
		
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(2,0));
		
		textArea = new JTextArea(5, 20);
		textArea.setText("select * from produit");
		JScrollPane scrollPane = new JScrollPane(textArea);
		
		ap = new actionPanel();
		
		

		ap.buttonGo.addActionListener(this);

		ap.buttonCommit.addActionListener(this);
		ap.buttonReset.addActionListener(this);

		ap.buttonQuit.addActionListener(this);

		ap.buttonRollback.addActionListener(this);

		ap.buttonVersion.addActionListener(this);
		
		ap.buttonAutocommit.addActionListener(this);

		
		JDBCengine = new JDBCAppConsole(true);
		columnNames = new Vector<String>();
		data = new Vector<Object>();
		
		p1.add(ap);
		
		tableau = new JTable(data, columnNames);
		tableau.setDefaultEditor(Object.class, null);

		JScrollPane scrollPane1 = new JScrollPane(tableau);
		
		this.add(scrollPane, BorderLayout.NORTH);
		this.add(p1, BorderLayout.CENTER);
		this.add(scrollPane1, BorderLayout.SOUTH);
		
		try {
			JDBCengine.connect();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		results = null;
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
	    
		((DefaultTableModel) tableau.getModel()).fireTableStructureChanged();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if (source.getClass() == new JButton().getClass()) {
			switch( ((JButton) source).getText() ) {
			case "GO" : 
				System.out.println("go");

				StringBuffer s = new StringBuffer();
				String buffer = textArea.getText();
				if (!buffer.isEmpty()) {
					s.append(buffer);
					try {
						afficheResultSet(JDBCengine.executeStatement(s));
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}

				break;
				
			}
		}
	}

}
