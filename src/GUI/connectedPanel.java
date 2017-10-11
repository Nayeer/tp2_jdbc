package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jdbcConsole.JDBCAppConsole;

public class connectedPanel extends JPanel implements ActionListener {
	
	JTextArea textArea;
	actionPanel ap;
	
	JDBCAppConsole JDBCengine;

	
	public connectedPanel() {

		this.setLayout(new BorderLayout());
		
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(10,0));
		
		textArea = new JTextArea(5, 20);
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

		p1.add(ap);
		
		
		
		this.add(scrollPane, BorderLayout.NORTH);
		this.add(p1, BorderLayout.CENTER);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if (source.getClass() == new JButton().getClass()) {
			switch( ((JButton) source).getText() ) {
			case "GO" : 
				System.out.println("go");
				try {
					JDBCengine.connect();
					StringBuffer s = new StringBuffer();
					String buffer = textArea.getText();
					System.out.println("hello" + buffer + "eh");
					if (!buffer.isEmpty()) {
						s.append(buffer);
						JDBCengine.executeStatement(s);
					}
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
				
			}
		}
	}

}
