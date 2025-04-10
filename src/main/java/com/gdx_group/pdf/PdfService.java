/*
 * e G e d e x: gdxpdf
 * (c) 2025 GDX Group
 * https://gdx-group.com/
 */
package com.gdx_group.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.collection.PdfCollection;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.layout.Document;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PdfService {
	private static final PdfName MIME_PDF = new PdfName("application/pdf");

	public byte[] portofolio(Map<String, byte[]> files) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfDocument document = new PdfDocument(new PdfWriter(baos));
		
		try (Document doc = new Document(document)) {
			PdfCollection collection = new PdfCollection();
			collection.setView(PdfCollection.TILE);
			document.getCatalog().setCollection(collection);

			int i = 0;
			for (Map.Entry<String, byte[]> entry : files.entrySet()) {
				String name = entry.getKey();
				byte[] data = entry.getValue();

				PdfFileSpec fileSpec = PdfFileSpec.createEmbeddedFileSpec(document, data, "", name, MIME_PDF, null, new PdfName("" + (i++)));
				document.addFileAttachment(name, fileSpec);
			}
		}

		return baos.toByteArray();
	}

}
