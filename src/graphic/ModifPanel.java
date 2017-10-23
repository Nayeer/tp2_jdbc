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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import javax.swing.text.html.parser.Entity;


public class ModifPanel extends JPanel implements ActionListener, PropertyChangeListener {
	
	List<JFormattedTextField> all_fields;
	List<String> tables_name;
	DatabaseMetaData md;
	JPanel actualEntriesPanel;
	GridBagConstraints gbc;
	JComboBox<String> tablesComboBox;
	JButton btnNext, btnValid, btnAnnule;
	JCheckBox addBatch;
	ResultSet valuesRS;
	String selected_table;
	String actualUpdateQuery;
	PreparedStatement actualStatement;
	Connection connection;
	

	public ModifPanel(Connection connection) throws SQLException {
		this.setLayout(new GridBagLayout());
		this.connection=connection;
		gbc = new GridBagConstraints();
		
		md = connection.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        tables_name = new ArrayList<String>();
        all_fields = new ArrayList<JFormattedTextField>();
        valuesRS=null;
        
        //Creation du menu déroulant
        
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
      selected_table = first_table;
      
      //Initialisation de valuesRS pour la première table
      updateValuesRS(selected_table);

      
  	  actualEntriesPanel = entriesComponentPanel(selected_table);
  	  
  	  gbc.gridx = 0; 
  	  gbc.gridy = 1;
  	  gbc.anchor = GridBagConstraints.PAGE_START;
  	  gbc.weighty = 0.8;
      this.add(actualEntriesPanel, gbc);
      

      JPanel panebtn=new JPanel();
      panebtn.setLayout(new GridLayout(0, 1));
      btnNext= new JButton("Next");
      btnNext.addActionListener(this);
      addBatch= new JCheckBox("Batch Mode");
      btnValid=new JButton("Valider");
      btnValid.addActionListener(this);
      btnAnnule=new JButton("Annuler");
      btnAnnule.addActionListener(this);
      panebtn.add(addBatch);
      panebtn.add(btnNext);
      panebtn.add(btnAnnule);
      panebtn.add(btnValid);
      this.add(panebtn);
	
	}
	
	public void updateValuesRS(String selectedTable) {
    	StringBuffer s = new StringBuffer();
		String buffer = "select * from "+ selectedTable;
		s.append(buffer);
		try {
			valuesRS=(MAinWindow.JDBCengine.executeStatement(s));
			valuesRS.next(); //Pour que la première valeur soit accessible
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
	}
	
	public JPanel entriesComponentPanel(String selectedTable) throws SQLException {
		ResultSet rs = md.getColumns(null, null, selectedTable, null);	
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		all_fields.clear();
		
		
		int i=1;

  	  while (rs.next()) {
  		  System.out.println(i);
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
  	  }
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
				
				selected_table = (String) ((JComboBox<?>) source).getSelectedItem();
				updateValuesRS(selected_table);
				changeActualEntriesPanel(selected_table);
				break;
			}
		}
		if (source.getClass()==new JButton().getClass()) {
			switch( ((JButton) source).getText() ) {
				case "Next":
					System.out.println("next");
					try {
						if(valuesRS.next()) {
							actualUpdateQuery=generateUpdateQuery();							
							try {
								actualStatement = connection.prepareStatement(actualUpdateQuery);
								int i;
								for( i=0; i<all_fields.size() ; i++) {
									String fieldValue = all_fields.get(i).getText();
									//Les donnees des champs texte sont, en théorie, déjà vérifiés.
									actualStatement.setObject(i+1, fieldValue.isEmpty() ? null : fieldValue ) ;
								}
								actualStatement.setObject(i+1, all_fields.get(0).getText().isEmpty()?null:all_fields.get(0).getText());
								System.out.println(actualStatement);
								if(addBatch.isSelected()){
									actualStatement.addBatch();
									JOptionPane.showMessageDialog(this, "L'update en mode batch a bien été effectué.");
								}
								else {
									actualStatement.execute();
									actualStatement.clearParameters();
									actualStatement.close();
									JOptionPane.showMessageDialog(this, "L'update a bien été effectué.");
								}
								
							}catch (SQLException esql) {
								JOptionPane.showMessageDialog(this, esql.getMessage());
							}
							finally {
								actualStatement = null;
							}
							changeActualEntriesPanel(selected_table);
		
						} else {
							updateValuesRS(selected_table);
							valuesRS.previous();
							JOptionPane.showMessageDialog(this, "Il n'y a plus d'entrées dans cette table. "
									+ "Appuyez sur le bouton Next pour recommencer depuis le début.");
						};
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case "Valider":
					try {
						if (actualStatement != null) {
							int rows[] = actualStatement.executeBatch();
							JOptionPane.showMessageDialog(this, "Le(s) ajout(s) se sont bien terminés.");
						} else {
							JOptionPane.showMessageDialog(this, "Execution impossible, il n'y a pas eu d'ajout en mode batch !");
							System.out.println("ModifPanel.actionPerformed()");
						}
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(this, "Error : " + e1.getMessage());
					}
					finally {
						actualStatement = null;
					}
					break;
				case "Annuler":
					try {
						if (actualStatement != null ){
							actualStatement.clearBatch();
							actualStatement.close();
							actualStatement = null;
						}
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(this, "Error : " + e1.getMessage());
					}
					break;
			}
			
		}
	}
	
	
	public String generateUpdateQuery() {
		ResultSet rs;
		String query = "";
		HashMap<Integer,String> colnames=new HashMap<Integer,String>();
		int nbColumn = 0;
		try {
			rs = md.getColumns(null, null, selected_table, null);
		    while (rs.next()) {
		    	colnames.put(nbColumn,rs.getString("COLUMN_NAME"));
		        nbColumn = nbColumn + 1;
		    }		    
			Iterator entries = colnames.entrySet().iterator();
		    query="update `"+ selected_table+ "` set ";
		    while(entries.hasNext()){
		    	Entry<Integer, String> next=(Entry<Integer, String>) entries.next();
		    	query= ""+query+" `"+ next.getValue()+ "` = ?";
		    	if(entries.hasNext()){
		    		query=query+", ";
		    	}
		    	
		    }
		    //en supposant que la première colonne soit une clé primaire
		    query=query+" where `"+colnames.get(0)+ "` = ?;";
		    
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(query);
		return query;		
	}
	

	
	
	
	

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
	}
	
	
	public void updateTable(ResultSet res) throws SQLException{
	
		if (addBatch.isSelected()) {
			MAinWindow.JDBCengine.autoCommit();
		}
		
		ResultSetMetaData rsmd=res.getMetaData();
		while(res.next()){
			for(int i=1;i<=rsmd.getColumnCount(); i++){
				
			}
		}
	}
}
