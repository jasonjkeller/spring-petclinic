package org.springframework.samples.petclinic.nr.error;

import com.newrelic.api.agent.NewRelic;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
	@ExceptionHandler(ApiException.class)
	protected ResponseEntity<ApiError> handleApiException(ApiException e) {
		boolean expected = true;
		NewRelic.noticeError(e, expected);
		ApiError apiError = new ApiError(e.getCode(), e.getDescription(), e.getStatusCode());
		return ResponseEntity.status(apiError.getStatusCode()).body(apiError);
	}

	private class ApiError {
		private final String code;
		private final String description;
		private final int statusCode;

		ApiError(String code, String description, int statusCode) {

			this.code = code;
			this.description = description;
			this.statusCode = statusCode;
		}

		public String getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}

		public int getStatusCode() {
			return statusCode;
		}
	}

}
