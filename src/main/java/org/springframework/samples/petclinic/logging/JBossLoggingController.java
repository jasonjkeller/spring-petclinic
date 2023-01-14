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

package org.springframework.samples.petclinic.logging;

import org.jboss.logging.Logger;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class JBossLoggingController {

//	private static final Logger LOGGER = Logger.getLogger(JBossLoggingController.class.getName());
private static final Logger LOGGER = Logger.getLogger(JBossLoggingController.class.getName());

	@GetMapping("/jboss")
	public String logUsingJavaUtilLogger(Model model) {
		String logMessage = "Hello world, from JBoss Logging API!";

		MDC.put("foo", "bar");
		MDC.put("baz", true);
		LOGGER.info(logMessage);

		model.addAttribute("message", logMessage);
		return "externals/logging";
	}

}
