package database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class AddInvoice {
	public void insertToDb(String invoiceDate, Long invoiceNo, String customerPO, String totalInvoice, String soldTo) {
		Connection conn = null;
		PreparedStatement pStatement = null;
		InputStream inputStream = null;
		try {
			Properties database = new Properties();
			String propFileName = "config.properties";
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				database.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			String driver = database.getProperty("driver");
			String URL = database.getProperty("URL");
			String username = database.getProperty("username");
			String password = database.getProperty("password");
			Class.forName(driver);
			conn = DriverManager.getConnection(URL, username, password);
			pStatement = conn.prepareStatement(
					"insert into invoice(invoiceNum,invoiceDate,customer_PO,Amount,SoldTo) values(?,?,?,?,?)");
			pStatement.setLong(1, invoiceNo);
			pStatement.setString(2, invoiceDate);
			pStatement.setString(3, customerPO);
			pStatement.setString(4, totalInvoice);
			pStatement.setString(5, soldTo);
			pStatement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pStatement != null)
					pStatement.close();
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
