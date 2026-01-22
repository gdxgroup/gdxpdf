/*
 * e G e d e x: gdxpdf
 * (c) 2025 GDX Group
 * https://gdx-group.com/
 */
package com.gdx_group.pdf;

import java.util.Map;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Request {
	
	private String apikey;
	private Map<String, byte[]> files;
}
