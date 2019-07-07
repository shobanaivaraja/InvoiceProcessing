package presentation;

import java.io.IOException;
import java.util.Scanner;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import business.MailReceiver;
import business.MailSender;
import database.InvoiceList;
import database.UpdateInvoice;

public class InvoiceProcessing {
	public static void main(String[] args) throws AddressException, MessagingException {
		Scanner scan = new Scanner(System.in);
		String popHost = "pop.gmail.com";
		String port = "995";
		String host = "smtp.gmail.com";
		String senderPort = "587";
		Properties userProperties = new Properties();
		String propFileName = "userCredentials.properties";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		if (inputStream != null) {
			userProperties.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		String userName = userProperties.getProperty("username");
		String password = userProperties.getProperty("password");

		long invoiceId;
		while (true) {
			System.out.println("1.Receive Mail\n2.List invoices to be approved\n3.Approve Invoices\n4.exit");
			System.out.println("Enter your choice:");
			int choice = scan.nextInt();
			switch (choice) {
			case 1:
				MailReceiver mailReceiver = new MailReceiver();
				mailReceiver.receiveEmail(popHost, port, userName, password);
				break;
			case 2:
				InvoiceList invoiceList = new InvoiceList();
				invoiceList.listInvoice();
				break;
			case 3:
				UpdateInvoice invoiceUpdate = new UpdateInvoice();
				try {
					invoiceId = invoiceUpdate.performApproval();
					if (invoiceId != -1) {
						MailSender sender = new MailSender();
						sender.sendPlainTextEmail(host, senderPort, userName, password, userName,
								"Invoice approval - Reg", "Invoice " + Long.toString(invoiceId) + " is approved..");
					} else
						System.out.println("Not updated! Enter a valid invoice id");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 4:
				System.exit(0);
				break;
			default:
				System.out.println("Choose a valid option");
					
			}
		}
	}
}
