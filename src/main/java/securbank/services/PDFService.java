package securbank.services;

import java.io.ByteArrayOutputStream;

import securbank.models.CreditCardStatement;
import securbank.models.User;

import com.itextpdf.text.Document;

public interface PDFService {
	public Document createStatementPDF(String file, User user);
	public ByteArrayOutputStream convertPDFToByteArrayOutputStream(String fileName);
	public Document createCreditCardStatementPDF(String file, CreditCardStatement statement);
}
