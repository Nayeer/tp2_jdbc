package GUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import jdbcConsole.JDBCAppConsole;

public class actionPanel extends JPanel  {
	
	protected JButton buttonGo , buttonCommit,
	buttonReset, buttonQuit,
	buttonRollback, buttonVersion;
	
	JCheckBox buttonAutocommit;
	
	
	
	public actionPanel() {
		
		this.setLayout(new GridLayout(1,0));
		
		buttonGo = new JButton("GO");
		
		
		buttonCommit = new JButton("Commit");

		buttonReset = new JButton("Reset");

		buttonQuit = new JButton("Quit");

		buttonRollback = new JButton("Rollback");

		buttonVersion = new JButton("Version");

		buttonAutocommit = new JCheckBox("Commit");


		this.add(buttonGo);this.add(buttonCommit);this.add(buttonReset);this.add(buttonQuit);
		this.add(buttonRollback);this.add(buttonVersion);this.add(buttonAutocommit);
		
	}

}
