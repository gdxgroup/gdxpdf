package com.gdx_group.pdf;

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

		try {
			byte[] portfolio = service.portofolio(PdfDocuments.files());
			assertNotNull(portfolio);
			assertNotEquals(0, portfolio.length);

		} catch (Exception ex) {
			log.warn(ex.getMessage());
		}
	}
}
