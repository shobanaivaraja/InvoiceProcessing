package database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class UpdateInvoice {
	
	public long performApproval() throws IOException {
		Connection conn = null;
		PreparedStatement stmt = null;
		InputStream inputStream = null;
		long id = 0;
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			String driver = prop.getProperty("driver");
			String URL = prop.getProperty("URL");
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");
			Class.forName(driver);
			conn = DriverManager.getConnection(URL, username, password);
			System.out.println("Enter Invoice ID");
			id=new Scanner(System.in).nextLong();
			
			stmt = conn.prepareStatement("Update invoice set approval_status=1 where invoiceNum=?");
			stmt.setLong(1, id);
			int isUpdate=stmt.executeUpdate();
			if(isUpdate==0)
				return -1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				inputStream.close();
			} catch (SQLException e) {
				return -1;
			}

		}
		return id;
	}
}
