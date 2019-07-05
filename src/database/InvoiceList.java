package database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Scanner;

public class InvoiceList {
	int invoiceId;

	public void listInvoice() {
		Connection conn = null;
		PreparedStatement stmt = null;
		InputStream inputStream = null;
		ResultSet rs = null;
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
			rs = stmt.executeQuery();

			
				System.out.printf("%15s %15s %15s %20s %15s %70s %30s", "InvoiceId", "InvoiceNo", "InvoiceDate",
						"Customer PO", "Amount", "Sold To", "Approval Status");
				System.out.println();
			
			
			while (rs.next()) {
				invoiceId = rs.getInt(1);
				// prop.setProperty("invoiceId", Integer.toString(invoice_id));
				// prop.save(output,"invoiceId");

				System.out.format("%15s %15s %15s %20s %15s %70s %30s", invoiceId, rs.getLong(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getBoolean(7));
				System.out.println();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
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

	public String isApproval() throws IOException {
		System.out.println("Are you want to approve invoice? yes/no");
		String approvalStatus = new Scanner(System.in).nextLine();
		return approvalStatus;
	}

}
