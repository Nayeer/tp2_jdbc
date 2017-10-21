package graphic;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;


public class AddPanel extends JPanel implements ActionListener {
	
	protected List<JFormattedTextField> all_fields;
	protected List<String> tables_name;
	protected DatabaseMetaData md;
	protected JPanel actualEntriesPanel;
	protected GridBagConstraints gbc;
	protected String selectedTable;
	protected Connection connection;
	protected String actualInsertQuery;
	protected JButton btnAdd;
	protected JButton btnAddBatch;
	protected JButton btnExecute;
	protected JButton btnCancel;
	protected PreparedStatement actualStatement;


	public AddPanel(Connection conn) throws SQLException {
		this.connection = conn;
		actualStatement = null;
		
		this.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		md = connection.getMetaData();
	    ResultSet rs = md.getTables(null, null, "%", null);
	    tables_name = new ArrayList<String>();
	    all_fields = new ArrayList<JFormattedTextField>();
	    
	    JComboBox<String> tablesComboBox = new JComboBox<String>();
	    tablesComboBox.setName("ComboBox");
	    
	    while (rs.next()) {
	        tables_name.add(rs.getString("TABLE_NAME"));
	        tablesComboBox.addItem(rs.getString("TABLE_NAME"));
	    }
      
      tablesComboBox.addActionListener(this);
      
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.anchor = GridBagConstraints.PAGE_START;
      gbc.weighty = 0.1;
      gbc.weightx = 0.05;
      gbc.gridwidth = 2;
      this.add(tablesComboBox, gbc);
      
      //Checkbox mode batch
      JCheckBox chckbxBatch = new JCheckBox("Mode Batch");
      chckbxBatch.addActionListener(this);
	  //Elements de disposition
  	  gbc.gridx = 2; 
  	  gbc.gridy = 0;
      gbc.anchor = GridBagConstraints.PAGE_START;
      gbc.gridwidth = 1;
  	  this.add(chckbxBatch, gbc);
      
      
      //Ajout des button
      btnAdd = new JButton("Ajouter maintenant");
      btnAdd.addActionListener(this);
  	  //Elements de disposition
      gbc.anchor = GridBagConstraints.CENTER;
      gbc.insets = new Insets(10,10,10,10);
  	  gbc.gridx = 1; 
  	  gbc.gridy = 2;
      this.add(btnAdd, gbc);
      
      btnAddBatch = new JButton("AddBatch");
      btnAddBatch.addActionListener(this);
      btnAddBatch.setVisible(false);
  	  //Elements de disposition
      gbc.anchor = GridBagConstraints.LAST_LINE_END;
  	  gbc.gridx = 1; 
  	  gbc.gridy = 2;
      gbc.weightx = 0.9;
      this.add(btnAddBatch, gbc);
      
      btnCancel = new JButton("Annuler tout");
      btnCancel.addActionListener(this);
      btnCancel.setVisible(false);
  	  //Elements de disposition
  	  gbc.gridx = 0; 
  	  gbc.gridy = 2;
      gbc.anchor = GridBagConstraints.PAGE_END;
      gbc.weightx = 0.1;
      this.add(btnCancel, gbc);
      
      btnExecute = new JButton("Executer tout");
      btnExecute.addActionListener(this);
      btnExecute.setVisible(false);
  	  //Elements de disposition
  	  gbc.gridx = 2; 
  	  gbc.gridy = 2;
      this.add(btnExecute, gbc);
      
  	  //Elements de disposition
  	  gbc.gridx = 0; 
  	  gbc.gridy = 1;
      gbc.anchor = GridBagConstraints.LINE_END;
  	  gbc.weightx = 1;
      this.add(new JPanel(), gbc);
	
      //Affichage par défaut de la première table
      selectedTable = tables_name.get(0);
  	  actualEntriesPanel = entriesComponentPanel();
  	  //Elements de disposition
      gbc.anchor = GridBagConstraints.LINE_START;
  	  gbc.gridx = 1; 
  	  gbc.gridy = 1;
  	  gbc.weightx = 0;
  	  gbc.gridwidth = 2;
  	  gbc.ipadx = 200;
      this.add(actualEntriesPanel, gbc);
      

	
	}
	
	public JPanel entriesComponentPanel() throws SQLException {
		ResultSet rs = md.getColumns(null, null, selectedTable, null);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		all_fields.clear();
		
		int i = 0;
		  
	  	  while (rs.next()) {
	  		  /*
	  		System.out.println(rs.getString("COLUMN_NAME") + " " 
			+ rs.getString("TYPE_NAME") + " "
			+ rs.getString("COLUMN_SIZE"));
			*/
	  		
	  		  //Ajout du label
	  		JLabel label = new JLabel(rs.getString("COLUMN_NAME") + " (" + rs.getString("TYPE_NAME") + " " + rs.getString("COLUMN_SIZE")+ ")");
	  		panel.add(label);
	  		panel.add(new JPanel()); //comble l'espace dans gridLayout
	  		
	  		//Ajout du champ de texte formatté
	  		JFormattedTextField ft = new JFormattedTextField();	  		
	  		//Verification basique des types int , double/float et date 
	  		
	  		if (rs.getString("TYPE_NAME").equals("INT")) {
	  			ft.setValue(new Integer(0)); //Creation implicite d'un formatter de type Integer
	  			ft.setText("");
	  		    ft.setColumns(rs.getInt("COLUMN_SIZE"));
	  		}
	  		else if (rs.getString("TYPE_NAME").equals("FLOAT") || rs.getString("TYPE_NAME").equals("DOUBLE")) {
	  			ft.setValue(new Double(0.0f)); //Creation implicite d'un formatter de type Float
	  			ft.setText("");
	  			ft.setColumns(rs.getInt("COLUMN_SIZE"));
	  		}
	  		else if (rs.getString("TYPE_NAME").equals("DATE")) {
	  		    DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //Formateur de date
	  		    ft = new JFormattedTextField(format);
	  		    ft.setText("yyyy-MM-dd");
	  		    ft.setToolTipText("In yyyy-MM-dd format");
	  		}
	  		else if (rs.getString("TYPE_NAME").equals("BIT")) {
	  			NumberFormatter nf = new NumberFormatter();
	  			nf.setMinimum(new Integer(0));
	  			nf.setMaximum(new Integer(1));
	  			ft = new JFormattedTextField(nf);
	  		    ft.setToolTipText("Entrer 0 pour FALSE ou 1 pour TRUE ");
	  		}
	  		
	  		all_fields.add(ft);
	  		panel.add(ft);
	  		
	  		//Ajout d'un checkbox pour mettre à null la valeur (quand cela est possible)
	  		if(rs.getString("IS_NULLABLE").equals("YES")) {
	  			JCheckBox cb = new JCheckBox("null");
	  			cb.setName(""+i);
	  			cb.addActionListener(this);
	  			panel.add(cb);
	  		}
	  		else panel.add(new JPanel());
	  		
	  		i++;
	  	  }
		return panel;
	}
	
	public void changeActualEntriesPanel() {
		this.remove(actualEntriesPanel);
		try {
			actualEntriesPanel = entriesComponentPanel();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.add(actualEntriesPanel, gbc);
		revalidate();
		repaint();
	}
	
	public String generateInsertQuery() {
		ResultSet rs;
		String query = "";
		String colnames = "";
		int nbColumn = 0;
		try {
			colnames = colnames + "(";
			rs = md.getColumns(null, null, selectedTable, null);
		    while (rs.next()) {
		        colnames = colnames + "`" + rs.getString("COLUMN_NAME")  + "`, ";
		        nbColumn = nbColumn + 1;
		    }
		    colnames = colnames.substring(0,colnames.length() - 2) + ")";
		    
			query = "INSERT INTO " + selectedTable +" "+ colnames + " VALUES " + generateQuestionMarks(nbColumn) + ";";

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return query;		
	}
	
	
	public String generateQuestionMarks(int nb) {
		String qm = "(";
		for (int i = 0; i<nb-1; i++) {
			qm = qm + "?,";
		}
		qm = qm + "?)";
		
		return qm;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();

		if (source.getClass() == new JComboBox<String>().getClass()) {
			switch ( ((JComboBox<?>) source).getName()) {
			case "ComboBox" :
				selectedTable = (String) ((JComboBox<?>) source).getSelectedItem();
				actualInsertQuery = generateInsertQuery();
				changeActualEntriesPanel();
				break;
			}
		}
		else if (source.getClass() == new JButton().getClass()) {

			switch( ((JButton) source).getText() ) {
			case "Ajouter maintenant":
				try {
					actualStatement = connection.prepareStatement(actualInsertQuery);
	
					for(int i=0 ; i<all_fields.size() ; i++) {
						String fieldValue = all_fields.get(i).getText();
						//Les donnees des champs texte sont, en théorie, déjà vérifiés.
						actualStatement.setObject(i+1, fieldValue.isEmpty() ? null : fieldValue ) ;
					}
					actualStatement.execute();
					actualStatement.clearParameters();
					actualStatement.close();
					JOptionPane.showMessageDialog(this, "L'ajout a bien été effectué.");
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(this, e1.getMessage());
				}
				finally {
					actualStatement = null;
				}
				break;
			case "AddBatch":
				try {
					actualStatement = connection.prepareStatement(actualInsertQuery);
					for(int i=0 ; i<all_fields.size() ; i++) {
						String fieldValue = all_fields.get(i).getText();
						//Les donnees des champs texte sont, en théorie, déjà vérifiés.
						actualStatement.setObject(i+1, fieldValue.isEmpty() ? null : fieldValue ) ;
					}
					actualStatement.addBatch();
					JOptionPane.showMessageDialog(this, "L'ajout en mode batch a bien été effectué.");
				}
				catch(BatchUpdateException e1) {
					actualStatement = null;
					JOptionPane.showMessageDialog(this, "Erreur : nombre de lignes modifiées" +
					                              e1.getUpdateCounts().length);

				}
				catch(SQLException e1) {
					actualStatement = null;
					JOptionPane.showMessageDialog(this, e1.getMessage());
				}
				break;
			case "Executer tout":
				try {
					if (actualStatement != null) {
						int rows[] = actualStatement.executeBatch();
						JOptionPane.showMessageDialog(this, "Le(s) ajout(s) se sont bien terminés.");
					} else {
						JOptionPane.showMessageDialog(this, "Execution impossible, il n'y a pas eu d'ajout en mode batch !");
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(this, "Error : " + e1.getMessage());
				}
				finally {
					actualStatement = null;
				}
				break;
			case "Annuler tout":
				try {
					actualStatement.clearBatch();
					actualStatement.close();
					actualStatement = null;
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(this, "Error : " + e1.getMessage());
				}
				break;
			}
		}
		else if (source.getClass() == new JCheckBox().getClass()) {
			JCheckBox cb = (JCheckBox) source;
			switch ( cb.getText()) {
			case "null" :
				int field_indice = Integer.parseInt(cb.getName());
				all_fields.get(field_indice).setEditable(!cb.isSelected());
				all_fields.get(field_indice).setText("");
				break;
			case "Mode Batch" :
				btnAdd.setVisible(!cb.isSelected());
				btnAddBatch.setVisible(cb.isSelected());
				btnExecute.setVisible(cb.isSelected());
				btnCancel.setVisible(cb.isSelected());
				break;
			}
		}
	}
}
