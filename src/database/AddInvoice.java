package database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class AddInvoice {
	public void insertToDb(String invoiceDate, Long invoiceNo, String customerPO, String totalInvoice, String soldTo) {
		//if(invoiceDate!="" && totalInvoice != "" &&  soldTo != "" && customerPO!="" && invoiceNo!=0 ){
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement st = null;
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
			st = conn.prepareStatement(
					"insert into invoice(invoiceNum,invoiceDate,customer_PO,Amount,SoldTo) values(?,?,?,?,?)");
			st.setLong(1, invoiceNo);
			st.setString(2, invoiceDate);
			st.setString(3, customerPO);
			st.setString(4, totalInvoice);
			st.setString(5, soldTo);
			st.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (conn != null)
					conn.close();
				inputStream.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		//}
	}}

}
