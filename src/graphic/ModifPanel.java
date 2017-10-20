package graphic;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class ModifPanel extends JPanel {
	
	List<String> tables_name;

	public ModifPanel(Connection connection) throws SQLException {
		 DatabaseMetaData md = connection.getMetaData();
         ResultSet rs = md.getTables(null, null, "%", null);
         tables_name = new ArrayList<String>();
         while (rs.next()) {
             tables_name.add(rs.getString("TABLE_NAME"));
         }
         
         System.out.println(tables_name);
         
         
	}
}
