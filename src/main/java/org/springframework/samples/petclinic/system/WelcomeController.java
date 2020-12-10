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

package org.springframework.samples.petclinic.system;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class WelcomeController {

	private static final String GOOD_RETURN = "Good job! But I don't add any value to this app!";

	private static final String BAD_RETURN = "Bad!";

	@GetMapping("/")
	public String welcome() {
		customMethod();
		return "welcome";
	}

	private String customMethod() {
		Tracer tracer = OpenTelemetry.getGlobalTracer("spring-petclinic-otel-manual-inst", "semver:1.0.0");
		Span span = tracer.spanBuilder("customMethod").startSpan();

		try (Scope scope = span.makeCurrent()) {
			span.setAttribute("good-return", GOOD_RETURN);
			return GOOD_RETURN;
		}
		catch (Throwable t) {
			span.setStatus(StatusCode.ERROR, BAD_RETURN);
		}
		finally {
			// closing the scope does not end the span, this has to be done manually
			span.end();
		}
		return BAD_RETURN;
	}

}
