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

package org.springframework.samples.petclinic;

import com.newrelic.telemetry.Attributes;
import com.newrelic.telemetry.opentelemetry.export.NewRelicExporters;
import com.newrelic.telemetry.opentelemetry.export.NewRelicSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.TracerSdkProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

import static com.newrelic.telemetry.opentelemetry.export.AttributeNames.SERVICE_NAME;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@SpringBootApplication(proxyBeanMethods = false)
public class PetClinicApplication {

	public static void main(String[] args) {
		NewRelicSpanExporter exporter = NewRelicSpanExporter.newBuilder().apiKey(System.getenv("INSIGHTS_INSERT_KEY"))
				.commonAttributes(new Attributes().put(SERVICE_NAME, "spring-petclinic-otel-manual-inst")).build();

		BatchSpanProcessor spanProcessor = BatchSpanProcessor.newBuilder(exporter).build();
		TracerSdkProvider tracerSdkProvider = OpenTelemetrySdk.getTracerProvider();
		tracerSdkProvider.addSpanProcessor(spanProcessor);

		SpringApplication.run(PetClinicApplication.class, args);

	}

	@Component
	public static class CleanupBean {

		@PreDestroy
		public void destroy() {
			System.out.println("Callback triggered - @PreDestroy.");
			// FIXME throws NPE
			NewRelicExporters.shutdown();
		}

	}

}
