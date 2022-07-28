package org.springframework.samples.petclinic.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Controller
public class HttpURLConnectionExternal {
	private static final String URL = "https://example.com/";

	// private static final String URL =
	// "http://localhost:8090/httpurlconnection/external";
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpURLConnectionExternal.class);
	@GetMapping("/httpurlconnection/external")
	public String httpUrlConn(Model model) throws IOException, URISyntaxException {
		URI uri = new URI(URL);
		URL url = uri.toURL();
		java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line).append("\n");
		}
		System.out.println(stringBuilder);
		LOGGER.info("@GetMapping(\"/httpurlconnection/external\")");
		LOGGER.info(stringBuilder.toString());

		// Make a Http request
		populateModelAttributes(model);
		return "externals/external";
	}

	private void populateModelAttributes(Model model) throws IOException {
		model.addAttribute("external_url", "URL");
		model.addAttribute("status_code", "response.getStatusLine().getStatusCode()");
		model.addAttribute("status_message", "response.getStatusLine().getReasonPhrase()");
		model.addAttribute("response", "response");
		model.addAttribute("response_body", "getResponseBody(response)");
		model.addAttribute("response_headers", "response.getAllHeaders()");
	}

}
