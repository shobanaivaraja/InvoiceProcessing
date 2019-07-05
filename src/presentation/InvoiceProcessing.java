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
		System.out.println("1.Receive Mail\n2.List invoices to be approved\n3.Approve Invoices\n4.exit");
		Scanner scan=new Scanner(System.in);
		String popHost = "pop.gmail.com";
		String port = "995";
		final String userName = "shobanak16115@gmail.com";
		final String password = "********";
		String host = "smtp.gmail.com";
		String senderPort = "587";
		
		long invoiceId;
		while(true){
			System.out.println("Enter your choice:");
		int choice=scan.nextInt();
		switch(choice){
		case 1:
			MailReceiver mailReceiver=new MailReceiver();
			mailReceiver.receiveEmail(popHost, port, userName, password);
			break;
		case 2:
			InvoiceList appList = new InvoiceList();
			appList.listInvoice();
			break;
		case 3:
				UpdateInvoice appUpdate = new UpdateInvoice();
			try {
				invoiceId = appUpdate.performApproval();
				if (invoiceId != -1) {
					MailSender sender = new MailSender();
					sender.sendPlainTextEmail(host, senderPort, userName, password, userName, "Approved Invoice",Long.toString(invoiceId) + " is approved");
				} else
					System.out.println("Update not done! Enter a valid invoice id");
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			break;
		case 4:
			System.exit(0);
			break;
		}}
	}
}
