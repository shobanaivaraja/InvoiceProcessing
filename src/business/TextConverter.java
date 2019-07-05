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

	public static void convertToTextFile(String fileName) throws FileNotFoundException {
		File pdf = null;
		PDDocument doc = null;
		PrintWriter pw = null;
		File textFile = null;
		try {
			String file = "D:/attachments/" + fileName;
			pdf = new File(file);
			doc = PDDocument.load(pdf); // put path to your input pdf file here
			String text = new PDFTextStripper().getText(doc);
			textFile = new File("D:/invoice.txt");
			pw = new PrintWriter(new FileWriter(textFile, false));
			pw.write(text);
			pw.close();
			BufferedReader br = new BufferedReader(new FileReader("D:/invoice.txt"));
			String line = "";
			String invoiceDate = "", total = "", customer = "", address = "";
			long invoiceNo = 0;
			br.mark(4048);
			line = br.readLine();
			while (line != null) {
				String[] words = line.split("\n");
				for (String word : words) {
					if (word.equals("Invoice Date")) {
						invoiceDate = br.readLine();
						// System.out.println("Date"+invoiceDate);
					}
					if (word.equals("Invoice No")) {
						invoiceNo = Long.parseLong(br.readLine());
						// System.out.println("Date"+invoiceNo);
					}
					if (word.equals("Customer P.O.")) {
						customer = br.readLine();
						// System.out.println("Date"+customer);
					}
					if (word.equals("Sold To")) {
						address = br.readLine() + br.readLine() + br.readLine();
						// System.out.println("Date"+address);
					}
					if (word.startsWith("$")) {
						total = word;
						// System.out.println("Date"+total);
					}
				}

				line = br.readLine();
			}

			AddInvoice add = new AddInvoice();
			add.insertToDb(invoiceDate, invoiceNo, customer, total, address);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				doc.close();
				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
