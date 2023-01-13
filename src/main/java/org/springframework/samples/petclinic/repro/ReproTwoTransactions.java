package org.springframework.samples.petclinic.repro;

import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Controller
public class ReproTwoTransactions {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReproTwoTransactions.class);

//	private final WebClient clientWith10MsTimeout;
//	private final WebClient client;

//	HttpClient httpClient = HttpClient.create()
//		.tcpConfiguration(tcpClient ->
//			tcpClient.doOnConnected(conn ->
//				conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.MILLISECONDS))));
//	ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
//	private final WebClient clientWith10MsTimeout = WebClient.builder().clientConnector(connector).build();
	private final WebClient client = WebClient.builder().build();

//	public ReproTwoTransactions() {
//		HttpClient httpClient = HttpClient.create()
//			.tcpConfiguration(tcpClient ->
//				tcpClient.doOnConnected(conn ->
//					conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.MILLISECONDS))));
//		ReactorClientHttpConnector connector =
//			new ReactorClientHttpConnector(httpClient);
//		clientWith10MsTimeout = WebClient.builder()
//			.clientConnector(connector)
//			.build();
//		client = WebClient.builder()
//			.build();
//	}

	@GetMapping("/repro/success")
	public Mono<ResponseEntity<String>> success() {
		LOGGER.info("in /success handler");
		return client.get()
			.uri("https://www.google.com")
			.retrieve()
			.bodyToMono(String.class)
			.map(s -> ResponseEntity.ok()
				.contentType(MediaType.TEXT_HTML)
				.body(s))
			.doOnSuccess(s -> LOGGER.info("in doOnSuccess"));
	}

	@GetMapping("/repro/hello")
	public Mono<ResponseEntity<String>> hello() {
		LOGGER.info("in hello world handler");
		ResponseEntity<String> response = ResponseEntity.ok()
			.contentType(MediaType.TEXT_HTML)
			.body("Hello World!!!");
		return Mono.just(response)
			.doOnSuccess(s -> LOGGER.info("in doOnSuccess hello world"));
	}
}
