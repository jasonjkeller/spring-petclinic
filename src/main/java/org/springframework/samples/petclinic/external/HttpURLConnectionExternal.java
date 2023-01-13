package org.springframework.samples.petclinic.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class HttpURLConnectionExternal {

//	private static final String URL = "https://example.com/";
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpURLConnectionExternal.class);

	/**
	 * GET data using HttpURLConnection
	 */
	@GetMapping("/httpurlconnection/getInputStream/external")
	public String getInputStream(Model model) throws IOException, URISyntaxException {
		final String URL = "http://localhost:8080/httpurlconnection/getInputStream/external";
//		final String URL = "https://example.com/";

		URI uri = new URI(URL);
		URL url = uri.toURL();

		// openConnection does no network IO, it simply creates an HttpURLConnection instance
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// prepare the request
		con.setRequestMethod("GET");

		try {
			// connect can be called explicitly, but it doesn't need to be as it will implicitly be called by other
			// methods that read or write over the connection such as getInputStream, getResponseCode, and getOutputStream.
			// connect doesn't cause a request to be issued does it?????
			con.connect();

			// getInputStream opens a stream with the intention of reading data from the server
			// getInputStream opens a connection, then sends GET & headers
			// con.getInputStream() is what establishes the connection and fires the request
//			StringBuilder stringBuilder = getInputStreamString(con);

			// getResponseCode gets the status code from an HTTP response message.
			// If the request was already made (e.g. because getInputStream was called before it) it will simply return the status code from the response.
			// Otherwise, it will initiate the request itself by calling getInputStream which calls connect (or potentially getOutputStream if streaming).
//			int responseCode = con.getResponseCode();
//			String responseMessage = con.getResponseMessage();

			LOGGER.info("Made external call to: " + URL);
//			LOGGER.info("Response code was: " + responseCode);
//			LOGGER.info("Response message was: " + responseMessage);
//			LOGGER.info("Result of getInputStream: " + stringBuilder);
			LOGGER.info("Header fields: " + con.getHeaderFields());

			populateModelAttributes(model, url, con);
		} catch (IOException e) {
			LOGGER.info("Caught exception: " + e);
			// Returns the error stream if the connection failed but the server sent useful data nonetheless
//			con.getErrorStream();
		} finally {
			// close and release the connection resource
			con.disconnect();
		}

		return "externals/external";
	}

	/**
	 * POST data using HttpURLConnection
	 *
	 * POST http://localhost:8081/httpurlconnection/getOutputStream/external?foo=heyyyya
	 *
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@PostMapping("/httpurlconnection/getOutputStream/external")
	public String getOutputStream(String foo, Model model) throws IOException, URISyntaxException {
		final String URL = "http://localhost:8080/httpurlconnection/getOutputStream/external?foo=" + foo;
//		final String URL = "https://example.com/";

		LOGGER.info("Got message: " + foo);

		URI uri = new URI(URL);
		URL url = uri.toURL();

		// openConnection does no network IO, it simply creates an HttpURLConnection instance
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// prepare the request
		con.setDoOutput(true);
		con.setChunkedStreamingMode(0);

		try {
			// connect can be called explicitly, but it doesn't need to be as it will implicitly be called by other
			// methods that read or write over the connection such as getInputStream, getResponseCode, and getOutputStream.
			con.connect();

			// getOutputStream opens a stream with the intention of writing data to the server but doesn't actually write until you call writer.write
			OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
			String message = URLEncoder.encode(foo, StandardCharsets.UTF_8);
//			// actually generates request
			writer.write("message=" + message);
			writer.close();

			// getInputStream opens a stream with the intention of reading data from the server
			// getInputStream opens a connection, then sends GET & headers
			// con.getInputStream() is what establishes the connection and fires the request
			StringBuilder stringBuilder = getInputStreamString(con);

			// getResponseCode gets the status code from an HTTP response message.
			// If the request was already made (e.g. because getInputStream was called before it) it will simply return the status code from the response.
			// Otherwise, it will initiate the request itself by calling getInputStream which calls connect (or potentially getOutputStream if streaming).
			int responseCode = con.getResponseCode();

			LOGGER.info("Made external call to: " + URL);
			LOGGER.info("Response code was: " + responseCode);
//			LOGGER.info("Result of getInputStream: " + stringBuilder);
			LOGGER.info("Header fields: " + con.getHeaderFields()); // TODO getHeaderFields causes a request
			populateModelAttributes(model, url, con);
		} catch (IOException e) {
			LOGGER.info("Caught exception: " + e);
			// Returns the error stream if the connection failed but the server sent useful data nonetheless
//			con.getErrorStream();
		} finally {
			// close and release the connection resource
			con.disconnect();
		}

		return "externals/external";
	}

	private static StringBuilder getInputStreamString(HttpURLConnection con) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//		InputStream reader2 = new BufferedInputStream(con.getInputStream());
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line).append("\n");
		}
		return stringBuilder;
	}

	private void populateModelAttributes(Model model, URL url, HttpURLConnection connection) throws IOException {
//		StringBuilder stringBuilder = getInputStreamString(connection);
		model.addAttribute("external_url", url);
//		model.addAttribute("status_code", connection.getResponseCode());
//		model.addAttribute("status_message", connection.getResponseMessage());
//		model.addAttribute("response", stringBuilder);
//		model.addAttribute("response_body", "getResponseBody(response)");
//		model.addAttribute("response_headers", connection.getHeaderFields());
	}

}
