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

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import com.newrelic.api.agent.NewRelic;
import java.util.logging.Level;

@Controller
public class ReproTwoTransactions {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReproTwoTransactions.class);

	// FIXME -javaagent:/Users/jkeller/nr/agents/opentelemetry/opentelemetry-javaagent.jar

	// FIXME -Dotel.javaagent.extensions=/Users/jkeller/nr/code/opentelemetry-java-instrumentation/examples/extension/build/libs/opentelemetry-java-instrumentation-extension-demo-1.0-all.jar

	// FIXME -Dotel.javaagent.extensions=/Users/jkeller/nr/code/newrelic-java-agent/newrelic-opentelemetry-agent-extension/build/libs/newrelic-opentelemetry-agent-extension-8.18.0-SNAPSHOT.jar

	// FIXME -Dotel.javaagent.extensions=/Users/jkeller/nr/code/newrelic-java-agent/newrelic-opentelemetry-span-processor-agent-extension/build/libs/newrelic-opentelemetry-span-processor-agent-extension-1.0.jar

	// /Users/jkeller/nr/code/newrelic-java-agent/newrelic-opentelemetry-span-processor-agent-extension/build/libs/newrelic-opentelemetry-span-processor-agent-extension-8.18.0-SNAPSHOT.jar



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
		NewRelic.getAgent().getLogger().log(Level.INFO, "in doOnSuccess");
		HashMap<String, Object> atts = new HashMap<>();
		atts.put("Foo", "Bar");
		NewRelic.getAgent().getInsights().recordCustomEvent("JasonCustomEvent", atts);
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
