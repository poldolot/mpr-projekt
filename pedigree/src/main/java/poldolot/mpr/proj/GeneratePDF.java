package poldolot.mpr.proj;

import poldolot.mpr.proj.pedigree.*;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GeneratePDF {

	public static Horse rootHorse = null;
	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);

	public GeneratePDF(int id, int deep) throws IOException, DocumentException {
		rootHorse = DAO.readHorseById(id);
		createPdf(deep);
	}

	public void createPdf(Integer deep) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream("rodowod_konia_" + rootHorse.getName().replaceAll("\\W","_") + ".pdf"));
		document.open();
		document.addTitle("Rodowod konia imieniem " + rootHorse.getName());
		document.addAuthor("poldolot");
		document.addCreator("poldolot");

		Paragraph header = new Paragraph("Rodowod konia imieniem " + rootHorse.getName(), catFont);
		header.setAlignment(Element.ALIGN_CENTER);
		document.add(header);
		document.add(new Paragraph(" "));

		document.add(createTable(deep));
		document.close();
	}

	public static PdfPTable createTable(Integer deep) {
		PdfPTable table = new PdfPTable(deep + 1);
		createCell(deep, rootHorse, table);
		return table;
	}

	private static void createCell(Integer deep, Horse horse, PdfPTable table) {
		PdfPCell cell;
		String name;
		if (horse.getName() != null) {
			name = horse.getName() + "\n" + horse.getDob().toString();
		} else {
			name = "---";
		}
		cell = new PdfPCell(new Phrase(name));
		cell.setRowspan((int) Math.pow(2,deep));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);

		if (horse.getSire() != null && deep > 0) {
			createCell(deep - 1, horse.getSire(), table);
		} else if (horse.getSire() == null && deep > 0) {
			createCell(deep - 1, new Horse(), table);
		}
		if (horse.getDam() != null && deep > 0) {
			createCell(deep - 1, horse.getDam(), table);
		} else if (horse.getDam() == null && deep > 0) {
			createCell(deep - 1, new Horse(), table);
		}
	}
}
