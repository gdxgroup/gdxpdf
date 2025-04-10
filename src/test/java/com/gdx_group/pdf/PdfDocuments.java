package com.gdx_group.pdf;

import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PdfDocuments {

	static {
		// From: https://stackoverflow.com/questions/19540289/how-to-fix-the-java-security-cert-certificateexception-no-subject-alternative/19542614#19542614
		try {
			// Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}
			};

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = (String hostname, SSLSession session) -> true;

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, byte[]> files() {
		String[] urls = new String[]{
			"https://www.juntadeandalucia.es/agenciadeserviciossocialesydependencia/images/solicitudes/solicituddependencia.pdf",
			"https://www.puertoalicante.com/wp-content/uploads/filebase/static_web_files/regelectronico/comoFirmarDocumentos.pdf",
			"https://www.mugeju.es/sites/default/files/archivos/impresos/prestaciones/Modelo_145_rellenable.pdf"
		};

		Map<String, byte[]> files = new HashMap<>();

		for (String url : urls) {
			try {
				URI uri = new URI(url);
				URL u = uri.toURL();
				files.put(u.getFile(), u.openStream().readAllBytes());

			} catch (Exception ex) {
				log.warn(ex.getMessage());
			}
		}
		return files;
	}
}
