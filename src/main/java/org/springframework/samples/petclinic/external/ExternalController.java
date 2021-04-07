/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.external;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Controller
class ExternalController {

	/**
	 * Make a request to an external URI
	 * @param model used by view
	 * @return externals/external view
	 * @throws IOException in rare cases
	 */
	@GetMapping("/external")
	public String external(Model model) throws IOException {
		String url = "https://example.com/";
		final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		HttpClient client = httpClientBuilder.build();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);

		model.addAttribute("external_url", url);
		model.addAttribute("status_code", response.getStatusLine().getStatusCode());
		model.addAttribute("status_message", response.getStatusLine().getReasonPhrase());
		model.addAttribute("response", response);
		model.addAttribute("response_body", getResponseBody(response));
		model.addAttribute("response_headers", response.getAllHeaders());
		return "externals/external";
	}

	private String getResponseBody(HttpResponse response) throws IOException {
		final StringBuilder output = new StringBuilder();
		// Get the response
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		String line = "";
		while ((line = rd.readLine()) != null) {
			output.append(line);
		}

		return output.toString();
	}
}
