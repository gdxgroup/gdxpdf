/*
 * e G e d e x: gdxpdf
 * (c) 2025 GDX Group
 * https://gdx-group.com/
 */
package com.gdx_group.pdf;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Response {

	private boolean ok;
	private String message;
	private byte[] data;
}
