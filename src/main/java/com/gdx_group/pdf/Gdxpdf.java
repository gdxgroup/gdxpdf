/*
 * e G e d e x: gdxpdf
 * (c) 2025 GDX Group
 * https://gdx-group.com/
 */
package com.gdx_group.pdf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
 * PDF utilities.
 */
@SpringBootApplication
@EnableScheduling
public class Gdxpdf {

  public static void main(String[] args) {
    SpringApplication.run(Gdxpdf.class, "");
  }
}
