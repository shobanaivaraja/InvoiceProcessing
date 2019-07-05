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

	private Scanner scanner;

	public long performApproval() throws IOException {
		Connection conn = null;
		PreparedStatement pStatement = null;
		InputStream inputStream = null;
		long invoiceId = 0;
		scanner = new Scanner(System.in);
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
			System.out.println("Enter Invoice ID");
			invoiceId = scanner.nextLong();

			pStatement = conn.prepareStatement("Update invoice set approval_status=1 where invoiceNum=?");
			pStatement.setLong(1, invoiceId);
			int isUpdate = pStatement.executeUpdate();
			if (isUpdate == 0)
				return -1;
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
				return -1;
			}

		}
		return invoiceId;
	}
}
