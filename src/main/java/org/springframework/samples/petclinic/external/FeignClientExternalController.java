package org.springframework.samples.petclinic.external;

import feign.Feign;
import feign.Headers;
import feign.RequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FeignClientExternalController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FeignClientExternalController.class);
	//	final String URL = "http://localhost:8080/httpurlconnection/getInputStream/external";
	final String URL = "https://postman-echo.com";

	public FeignClientExternalController() {
	}

	interface EchoAPI {
		@RequestLine("POST /post")
		@Headers({
			"Accept: application/json",
			"Content-Type: application/json"
		})
		String echo(String body);
	}

	@GetMapping("/feign/repro")
	public String testEchoAPI(Model model) {
		EchoAPI api = Feign.builder().target(EchoAPI.class, URL);
		String response = api.echo("response body will be this");
		assert response.contains("response body will be this");
		LOGGER.info("Made external call to: " + URL);
		populateModelAttributes(model, URL, response);
		return "externals/external";
	}

	private void populateModelAttributes(Model model, String url, String response) {
		model.addAttribute("external_url", url);
		model.addAttribute("response", response);
	}

}
