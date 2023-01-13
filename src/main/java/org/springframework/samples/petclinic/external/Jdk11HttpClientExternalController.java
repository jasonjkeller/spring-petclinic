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

import com.newrelic.api.agent.ExternalParameters;
import com.newrelic.api.agent.HttpParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Controller
class Jdk11HttpClientExternalController {

	private static final String EXAMPLE_COM = "https://example.com/";

	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
			.connectTimeout(Duration.ofSeconds(30)).build();

	/**
	 * Make a synchronous request to an external URI using the JDK 11 HttpClient.
	 * @param model used by view
	 * @return externals/external view
	 * @throws IOException in rare cases
	 * @throws InterruptedException in rare cases
	 */
	@GetMapping("/jdk11/sync/external")
	public String syncExternal(Model model) throws IOException, InterruptedException {
		// Make a Http request
		HttpResponse<String> response = HTTP_CLIENT.send(getRequest(), HttpResponse.BodyHandlers.ofString());
		populateModelAttributes(model, response);

		// ExternalParameters params = HttpParameters
		// .library(library)
		// .uri(uri)
		// .procedure(procedure)
		// .inboundHeaders(inboundHeaders)
		// .build();
		//
		// NewRelic.getAgent().getTransaction().getTracedMethod().reportAsExternal(params);
		//
		// Segment segment =
		// NewRelic.getAgent().getTransaction().startSegment("MySegment");
		// segment.reportAsExternal(params);
		// segment.end();

		return "externals/external";
	}

	/**
	 * Make an asynchronous request to an external URI using the JDK 11 HttpClient.
	 * @param model used by view
	 * @return externals/external view
	 * @throws IOException in rare cases
	 * @throws InterruptedException in rare cases
	 * @throws ExecutionException in rare cases
	 * @throws TimeoutException in rare cases
	 */
	@GetMapping("/jdk11/async/external")
	public String asyncExternal(Model model)
			throws IOException, InterruptedException, ExecutionException, TimeoutException {
		CompletableFuture<HttpResponse<String>> httpResponseCompletableFuture = HTTP_CLIENT.sendAsync(getRequest(),
				HttpResponse.BodyHandlers.ofString());

		String result = httpResponseCompletableFuture.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);

		System.out.println(result);

		HttpResponse<String> httpResponse = httpResponseCompletableFuture.get();

		populateModelAttributes(model, httpResponse);
		return "externals/external";
	}

	/**
	 * Populate the Model for the view.
	 * @param model model containing attributes for the view
	 * @param response HttpResponse
	 * @throws IOException in rare cases
	 */
	private void populateModelAttributes(Model model, HttpResponse<String> response) throws IOException {
		model.addAttribute("external_url", response.uri());
		model.addAttribute("status_code", response.statusCode());
		model.addAttribute("status_message", "null"); // FIXME
		model.addAttribute("response", response);
		model.addAttribute("response_body", response.body());
		model.addAttribute("response_headers", response.headers());
	}

	/**
	 * Obtain a HttpRequest object.
	 * @return HttpRequest
	 */
	private HttpRequest getRequest() {
		return HttpRequest.newBuilder().GET().uri(URI.create(EXAMPLE_COM))
				.setHeader("User-Agent", "Java 11 HttpClient Bot").build();
	}

}
