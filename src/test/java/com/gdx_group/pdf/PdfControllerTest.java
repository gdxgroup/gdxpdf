package com.gdx_group.pdf;

import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.web.client.RestTemplate;

public class PdfControllerTest {

	private final RestTemplate client = new RestTemplate();

	@Test
	public void portofolio() throws Exception {
		String url = "https://gdxpdf.srv.egedex.com/api/v1/pdf/portfolio";
		
		Request request = new Request();
		request.setFiles(PdfDocuments.local());

		Response response = client.postForObject(url, request, Response.class);
		assertNotNull(response);
		assertNotNull(response.getData());
		assertNotEquals(0, response.getData().length);

		Files.write(Paths.get("/temp/facturas/portfolio.pdf"), response.getData());
	}

}
