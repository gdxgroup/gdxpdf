/*
 * e G e d e x: gdxpdf
 * (c) 2025-2026 GDX Group
 * https://gdx-group.com/
 */
package com.gdx_group.pdf;

import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.collection.PdfCollection;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.layout.Document;
import com.itextpdf.signatures.CrlClientOnline;
import com.itextpdf.signatures.ICrlClient;
import com.itextpdf.signatures.IOcspClient;
import com.itextpdf.signatures.ITSAClient;
import com.itextpdf.signatures.LtvVerification;
import com.itextpdf.signatures.OcspClientBouncyCastle;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.SignatureUtil;
import com.itextpdf.signatures.TSAClientBouncyCastle;
import java.io.ByteArrayInputStream;
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

	public byte[] protect(byte[] pdf) {
		int permissions = EncryptionConstants.ALLOW_FILL_IN; // fill-in forms and digital signature
		WriterProperties props = new WriterProperties()
				.setStandardEncryption(
						null, // user password
						"GDX-GROUP".getBytes(), // owner password
						permissions,
						EncryptionConstants.ENCRYPTION_AES_256
				);

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfReader reader = new PdfReader(new ByteArrayInputStream(pdf));
			PdfWriter writer = new PdfWriter(baos, props);
			PdfDocument pdfdoc = new PdfDocument(reader, writer);
			pdfdoc.close();
			return baos.toByteArray();
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}

		return null;
	}

	public byte[] padesltv(byte[] pdf, String tsaUrl) {
		byte[] padesltv = null;
		
		try {
			PdfReader reader = new PdfReader(new ByteArrayInputStream(pdf));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			PdfSigner signer = new PdfSigner(reader, baos, new StampingProperties().useAppendMode());

			PdfDocument pdfDoc = signer.getDocument();
			SignatureUtil signUtil = new SignatureUtil(pdfDoc);

			if (signUtil.getSignatureNames().isEmpty()) {
				throw new IllegalArgumentException("El PDF no contiene firmas");
			}

			// Tomamos la última firma (la más reciente)
			String sigName = signUtil.getSignatureNames().get(signUtil.getSignatureNames().size() - 1);

			// OCSP + CRL
			IOcspClient ocsp = new OcspClientBouncyCastle();
			ICrlClient crl = new CrlClientOnline();

			// Construir DSS (Long-Term Validation)
			LtvVerification ltv = new LtvVerification(pdfDoc);
			ltv.addVerification(
					sigName,
					ocsp,
					crl,
					LtvVerification.CertificateOption.WHOLE_CHAIN,
					LtvVerification.Level.OCSP_CRL,
					LtvVerification.CertificateInclusion.YES
			);
			ltv.merge();

			// Sello de tiempo RFC-3161 (DocTimeStamp)
			ITSAClient tsa = new TSAClientBouncyCastle(tsaUrl);
			signer.timestamp(tsa, null);

			padesltv = baos.toByteArray();

		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
		
		return padesltv;
	}
}
