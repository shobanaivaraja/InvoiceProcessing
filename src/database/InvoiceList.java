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
		PreparedStatement stmt = null;
		InputStream inputStream = null;
		ResultSet result = null;
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
			stmt = conn.prepareCall("select * from invoice where approval_status=0");
			result = stmt.executeQuery();

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
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
