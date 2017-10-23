package graphic;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;


public class ModifPanel extends JPanel implements ActionListener, PropertyChangeListener {
	
	List<JFormattedTextField> all_fields;
	List<String> tables_name;
	DatabaseMetaData md;
	JPanel actualEntriesPanel;
	GridBagConstraints gbc;
	JComboBox<String> tablesComboBox;
	JButton btnNext;
	JCheckBox addBatch;

	public ModifPanel(Connection connection) throws SQLException {
		this.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		
		md = connection.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        tables_name = new ArrayList<String>();
        all_fields = new ArrayList<JFormattedTextField>();
        
        tablesComboBox= new JComboBox<String>();
        tablesComboBox.setName("ComboBox");
        
        while (rs.next()) {
            tables_name.add(rs.getString("TABLE_NAME"));
            tablesComboBox.addItem(rs.getString("TABLE_NAME"));
        }
      
      tablesComboBox.addActionListener(this);
      
      gbc.gridx = gbc.gridy = 0;
      gbc.anchor = GridBagConstraints.PAGE_START;
      gbc.weighty = 0.2;
      this.add(tablesComboBox, gbc);
	
      //Affichage par défaut de la première table
      String first_table = tables_name.get(0);
  	  actualEntriesPanel = entriesComponentPanel(first_table);
  	  
  	  gbc.gridx = 0; 
  	  gbc.gridy = 1;
  	  gbc.anchor = GridBagConstraints.PAGE_START;
  	  gbc.weighty = 0.8;
      this.add(actualEntriesPanel, gbc);
  
      btnNext= new JButton("Next");
      btnNext.addActionListener(this);
      addBatch= new JCheckBox("Batch Mode");
      this.add(btnNext);
      this.add(addBatch);
	
	}
	
	public JPanel entriesComponentPanel(String selectedTable) throws SQLException {
		ResultSet rs = md.getColumns(null, null, selectedTable, null);	
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		all_fields.clear();
		
		
		ResultSet valuesRS=null;
		StringBuffer s = new StringBuffer();
		String buffer = "select * from "+ selectedTable;
			s.append(buffer);
			try {
				valuesRS=(MAinWindow.JDBCengine.executeStatement(s));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			int i=1;

	  	while(valuesRS.next()){
	  	  while (rs.next()) {
	  		
	  		JLabel label = new JLabel(rs.getString("COLUMN_NAME") + " (" + rs.getString("TYPE_NAME") + " " + rs.getString("COLUMN_SIZE")+ ")");
	  		panel.add(label);
	  		JFormattedTextField ft = new JFormattedTextField();
	  		
	  		//Verification basique des types int , double/float et date 

  	
	  			//System.out.println(valuesRS.getString(1));
	  			
	  			
	  		if (rs.getString("TYPE_NAME").equals("INT")) {
	  			ft.setValue(new Integer(0)); //Creation implicite d'un formatter de type Integer
	  			ft.setText(""+valuesRS.getInt(i));
	  		    ft.setColumns(rs.getInt("COLUMN_SIZE"));
	  			ft.addPropertyChangeListener("value",this);
	  		}
	  		else if (rs.getString("TYPE_NAME").equals("FLOAT") || rs.getString("TYPE_NAME").equals("DOUBLE")) {
	  			ft.setValue(new Double(0.0f)); //Creation implicite d'un formatter de type Float
	  			ft.setText(""+valuesRS.getFloat(i));
	  			ft.setColumns(rs.getInt("COLUMN_SIZE"));
	  			//ft.addPropertyChangeListener("value",this);
	  		}
	  		else if (rs.getString("TYPE_NAME").equals("DATE")) {
	  		    DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //Formateur de date
	  		    ft = new JFormattedTextField(format);
	  		    ft.setText(""+valuesRS.getDate(i));
	  		    ft.setToolTipText("In yyyy-MM-dd format");
	  		}
	  		else if (rs.getString("TYPE_NAME").equals("VARCHAR")) {
	  		    ft.setText(""+valuesRS.getString(i));
	  		}
	  		else{
	  			ft.setText(""+valuesRS.getObject(i));
	  		}
	  		
	  		
	  		all_fields.add(ft);
	  		panel.add(ft);
	  		i++;
	  	  }}
		return panel;
	}
	
	public void changeActualEntriesPanel(String selectedTable) {
		this.remove(actualEntriesPanel);
		try {
			actualEntriesPanel = entriesComponentPanel(selectedTable);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.add(actualEntriesPanel, gbc);
		revalidate();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();

		if (source.getClass() == new JComboBox<String>().getClass()) {
			switch ( ((JComboBox<?>) source).getName()) {
			case "ComboBox" :
				
				String selectedTable = (String) ((JComboBox<?>) source).getSelectedItem();
				changeActualEntriesPanel(selectedTable);
				break;
			}
		}
		if (source.getClass()==new JButton().getClass()) {
			
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
	}
}
