package com.gdx_group.pdf;

import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class PdfServiceTest {

	@Autowired
	private PdfService service;

	@Test
	public void testPortofolio() {
		assertNotNull(service);

//		try {
//			byte[] portfolio = service.portofolio(PdfDocuments.files());
//			assertNotNull(portfolio);
//			assertNotEquals(0, portfolio.length);
//
//		} catch (Exception ex) {
//			log.warn(ex.getMessage());
//		}
	}

	@Test
	public void protect() {
		assertNotNull(service);

//		try {
////			byte[] data = Files.readAllBytes(Paths.get("/temp/documentos/desencriptado.pdf"));
//			byte[] data = Files.readAllBytes(Paths.get("/temp/documentos/permitirFirma_firmado.pdf"));
//			assertNotNull(data);
//
//			byte[] protectedData = service.protect(data);
//			assertNotNull(protectedData);
//
////			Files.write(Paths.get("/temp/documentos/permitirFirma.pdf"), protectedData);
//			Files.write(Paths.get("/temp/documentos/permitirFirma_firmado_protegido.pdf"), protectedData);
//		} catch (Exception ex) {
//			log.warn(ex.getMessage());
//		}
	}

	@Test
	public void padesltv() {
		assertNotNull(service);

		try {
			byte[] data = Files.readAllBytes(Paths.get("/temp/apc/fulgencio_original.pdf"));
			assertNotNull(data);

			String tsaUrl = "http://tss.accv.es:8318/tsa";
			byte[] seal = service.padesltv(data, tsaUrl);
			assertNotNull(seal);

			Files.write(Paths.get("/temp/apc/fulgencio_original.seal.pdf"), seal);
		} catch (Exception ex) {
			log.warn(ex.getMessage());
		}
	}
}
