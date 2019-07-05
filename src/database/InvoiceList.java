package database;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

public class InvoiceList {
	int invoiceId;

	public void listInvoice() {
		Connection conn = null;
		PreparedStatement pStatement = null;
		InputStream inputStream = null;
		ResultSet result = null;
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
			pStatement = conn.prepareCall("select * from invoice where approval_status=0");
			result = pStatement.executeQuery();

			if(!result.isBeforeFirst()){
				System.out.println("No invoices to be approved");
			}
			else{
				System.out.printf("%15s %15s %15s %20s %15s %70s %30s", "InvoiceId", "InvoiceNo", "InvoiceDate",
						"Customer PO", "Amount", "Sold To", "Approval Status");
				System.out.println();
				
			while (result.next()) {
				invoiceId = result.getInt(1);

				System.out.format("%15s %15s %15s %20s %15s %70s %30s", invoiceId, result.getLong(2), result.getString(3),
						result.getString(4), result.getString(5), result.getString(6), result.getBoolean(7));
				System.out.println();
			}}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null)
					result.close();
				if (pStatement != null)
					pStatement.close();
				if (conn != null)
					conn.close();
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
