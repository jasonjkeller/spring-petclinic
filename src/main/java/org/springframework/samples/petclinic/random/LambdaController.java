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

package org.springframework.samples.petclinic.random;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.TraceLambda;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.function.BiConsumer;

@TraceLambda
@Controller
class LambdaController {

	@GetMapping("/lambda")
	public String executeLambda(Model model) {
		String key = "foo";
		String value = "bar";

		lambdaFunction(key, value);

		model.addAttribute("key", key);
		model.addAttribute("value", value);
		return "externals/lambda";
	}

	// @Trace(dispatcher = true)
	private static void lambdaFunction(String key, String value) {
		BiConsumer<String, String> consumer = (String k, String v) -> {
			NewRelic.addCustomParameter(k, v);
			System.out.printf("Added key: " + k + ", value: " + v);
		};
		consumer.accept(key, value);
	}

}
