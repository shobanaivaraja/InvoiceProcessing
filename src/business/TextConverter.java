package business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import database.AddInvoice;

public class TextConverter {
	
	void convertToTextFile(String fileName) throws FileNotFoundException {
		File pdfFileName = null;
		PDDocument document = null;
		PrintWriter writer = null;
		File textFile = null;
		BufferedReader buffer = null;
		try {
			String file = "D:/attachments/" + fileName;
			pdfFileName = new File(file);
			document = PDDocument.load(pdfFileName); // put path to your input pdf file here
			String text = new PDFTextStripper().getText(document);
			textFile = new File("D:/invoice.txt");
			writer = new PrintWriter(new FileWriter(textFile, false));
			writer.write(text);
			writer.close();
			buffer = new BufferedReader(new FileReader("D:/invoice.txt"));
			String line = "";
			String invoiceDate = "", total = "", customerPO = "", address = "";
			long invoiceNo = 0;
			buffer.mark(4048);
			line = buffer.readLine();
			while (line != null) {
				String[] words = line.split("\n");
				for (String word : words) {
					if (word.equals("Invoice Date")) {
						invoiceDate = buffer.readLine();
						// System.out.println("Date"+invoiceDate);
					}
					if (word.equals("Invoice No")) {
						invoiceNo = Long.parseLong(buffer.readLine());
						// System.out.println("Date"+invoiceNo);
					}
					if (word.equals("Customer P.O.")) {
						customerPO = buffer.readLine();
						// System.out.println("Date"+customer);
					}
					if (word.equals("Sold To")) {
						address = buffer.readLine() + buffer.readLine() + buffer.readLine();
						// System.out.println("Date"+address);
					}
					if (word.startsWith("$")) {
						total = word;
						// System.out.println("Date"+total);
					}
				}

				line = buffer.readLine();
			}
			AddInvoice addInvoice=new AddInvoice();
			addInvoice.insertToDb(invoiceDate, invoiceNo, customerPO, total, address);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				document.close();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
