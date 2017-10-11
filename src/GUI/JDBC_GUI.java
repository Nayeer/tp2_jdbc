package GUI;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import GUI.connectedPanel;

public class JDBC_GUI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	connectedPanel cP;
	
	public JDBC_GUI() {
		super("JDBC Connect");
		setLocation(200,100);
		setSize(900,600);
		
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		
		Container pane = this.getContentPane();
		pane.setLayout(new GridLayout(1,1));
		
		cP = new connectedPanel();
		pane.add(cP);
		
		
		
		setVisible(true);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame fen = new JDBC_GUI();
	}

}
