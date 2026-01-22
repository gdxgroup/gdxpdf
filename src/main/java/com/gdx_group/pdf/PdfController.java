/*
 * e G e d e x: gdxpdf
 * (c) 2025 GDX Group
 * https://gdx-group.com/
 */
package com.gdx_group.pdf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 *
 */
@RestController
@RequestMapping(path = Constants.API_PDF)
@RequiredArgsConstructor
@Slf4j
public class PdfController {

	@Value("${gdxpdf.apikey}")
	private String apikey;

	private final PdfService service;

	@PostMapping(Constants.PORTFOLIO)
	public Response portfolio(@RequestBody Request request) throws InterruptedException {
		Response response = new Response();

		if (apikey.equals(request.getApikey())) {
			response.setMessage("Not authorized.");
			log.warn(response.getMessage());
			return response;
		}

		if (request.getFiles() == null || request.getFiles().isEmpty()) {
			response.setMessage("Request without params.");
			log.warn(response.getMessage());
			return response;
		}

		response.setData(service.portofolio(request.getFiles()));
		response.setOk(response.getData() != null && response.getData().length > 0);

		return response;
	}
	
	@PostMapping(Constants.PROTECT)
	public Response protect(@RequestBody Request request) throws InterruptedException {
		Response response = new Response();

		if (apikey.equals(request.getApikey())) {
			response.setMessage("Not authorized.");
			log.warn(response.getMessage());
			return response;
		}

		if (request.getFiles() == null || request.getFiles().isEmpty()) {
			response.setMessage("Request without params.");
			log.warn(response.getMessage());
			return response;
		}

		byte[] data = request.getFiles().values().stream().findFirst().orElse(null);
		response.setData(service.protect(data));
		response.setOk(response.getData() != null && response.getData().length > 0);

		return response;
	}
	
	@PostMapping(Constants.PADESLTV)
	public Response padesltv(@RequestBody Request request) throws InterruptedException {
		Response response = new Response();

		if (apikey.equals(request.getApikey())) {
			response.setMessage("Not authorized.");
			log.warn(response.getMessage());
			return response;
		}

		if (request.getFiles() == null || request.getFiles().isEmpty()) {
			response.setMessage("Request without params.");
			log.warn(response.getMessage());
			return response;
		}

		byte[] data = request.getFiles().values().stream().findFirst().orElse(null);
		response.setData(service.padesltv(data, Constants.TSA_ACCV));
		response.setOk(response.getData() != null && response.getData().length > 0);

		return response;
	}
}
