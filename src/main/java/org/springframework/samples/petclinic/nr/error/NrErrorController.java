package org.springframework.samples.petclinic.nr.error;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NrErrorController {
	@GetMapping("/nr-error")
	public String demoErrorExcepted() throws ApiException {
		throw new ApiException("error_demo", "error_description_demo", 400);
	}

}


