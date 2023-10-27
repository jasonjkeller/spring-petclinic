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

import org.eclipse.jetty.client.HttpClient;
//import org.eclipse.jetty.client.Response;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpVersion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
class JettyHttpClientExternalController {

	private static final String URL = "http://localhost:8080/jetty-continuations-1.0-SNAPSHOT/ContinuationsExampleServlet";

	/**
	 * Make a request to an external URI
	 * @param model used by view
	 * @return externals/external view
	 * @throws IOException in rare cases
	 */
	@GetMapping("/jetty-sync-external")
	public String syncExternal(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpClient client = new HttpClient();
		client.start();

		ContentResponse res = client.GET(URL);
//		Continuation cont = ContinuationSupport.getContinuation(request);
		res.getRequest().abort(new Throwable("Jason Says Request Aborted"));

		System.out.println(res.getContentAsString());

		client.stop();

		populateModelAttributes(model, res);
		return "externals/external";
	}


//	Continuation cont = ContinuationSupport.getContinuation(request);
//
//        response.setContentType("text/plain");
//        response.getWriter().println("Request id is : " + requestId + " start: " + new Date());
//
//        cont.setTimeout(3000);
//
//	//        try {
//	makeApacheHttpRequest();
////        } catch (Exception e) {
////            System.out.println("Exception was caught: " + e.getMessage());
////        }
//
//	// SUSPEND
//        cont.suspend();
//
//	// This seems to cause an infinite loop of Jetty suspending/resuming the request
////        cont.undispatch();
//
//
//
//        response.getWriter().println("Request id is : " + requestId + " end: " + new Date());
//
//        if (!cont.isInitial()) {
//		cont.complete();
//	}


	/**
	 * Make a Http request
	 * @return HttpResponse
	 * @throws IOException in rare cases
	 */
//	private HttpResponse makeHttpRequest() throws IOException {
//		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//		HttpClient client = httpClientBuilder.build();
//		HttpGet request = new HttpGet(URL);
//		return client.execute(request);
//	}

	/**
	 * Populate the Model for the view.
	 * @param model model containing attributes for the view
	 * @param response HttpResponse
	 * @throws IOException in rare cases
	 */
	private void populateModelAttributes(Model model, ContentResponse response) throws IOException {
		model.addAttribute("external_url", URL);
		model.addAttribute("status", response.getStatus());
//		model.addAttribute("status_message", response.getStatusLine().getReasonPhrase());
		model.addAttribute("response", response);
//		model.addAttribute("response_body", getResponseBody(response));
		model.addAttribute("response_headers", response.getHeaders());
	}

	/**
	 * Get a String representing the HttpResponse body
	 * @param response HttpResponse
	 * @return HttpResponse body String
	 * @throws IOException in rare cases
	 */
//	private String getResponseBody(Response response) throws IOException {
//		final StringBuilder output = new StringBuilder();
//
//		// Get the response
//		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//
//		String line;
//		while ((line = rd.readLine()) != null) {
//			output.append(line);
//		}
//
//		return output.toString();
//	}

}
