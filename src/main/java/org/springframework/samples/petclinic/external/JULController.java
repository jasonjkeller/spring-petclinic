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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
class JULController {

	private static final Logger LOGGER = Logger.getLogger(JULController.class.getName());

	@GetMapping("/jul")
	public String logUsingJavaUtilLogger(Model model) {
		String logMessage = "Hello world!";
		LOGGER.setLevel(Level.ALL);

		// * <li>SEVERE (highest value)
		// * <li>WARNING
		// * <li>INFO
		// * <li>CONFIG
		// * <li>FINE
		// * <li>FINER
		// * <li>FINEST (lowest value)

		LOGGER.log(Level.SEVERE, logMessage);
		LOGGER.log(Level.WARNING, logMessage);
		LOGGER.log(Level.INFO, logMessage);
		LOGGER.log(Level.CONFIG, logMessage);
		LOGGER.log(Level.FINE, logMessage);
		LOGGER.log(Level.FINER, logMessage);
		LOGGER.log(Level.FINEST, logMessage);

		model.addAttribute("message", logMessage);
		return "externals/logging";
	}

}
