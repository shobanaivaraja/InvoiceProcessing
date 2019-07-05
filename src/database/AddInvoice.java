package database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public interface AddInvoice {
	default void insertToDb(String invoiceDate, Long invoiceNo, String customerPO, String totalInvoice, String soldTo) {
		Connection conn = null;
		PreparedStatement stmt = null;
		InputStream inputStream = null;
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
			stmt = conn.prepareStatement(
					"insert into invoice(invoiceNum,invoiceDate,customer_PO,Amount,SoldTo) values(?,?,?,?,?)");
			stmt.setLong(1, invoiceNo);
			stmt.setString(2, invoiceDate);
			stmt.setString(3, customerPO);
			stmt.setString(4, totalInvoice);
			stmt.setString(5, soldTo);
			stmt.execute();
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
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
