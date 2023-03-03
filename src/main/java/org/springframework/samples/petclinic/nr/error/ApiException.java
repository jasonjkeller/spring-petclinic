package org.springframework.samples.petclinic.nr.error;

public class ApiException extends Exception {
	//	"error_demo", "error_description_demo", 400
	private String code;
	private String description;
	private int statusCode;

	public ApiException(String code, String description, int statusCode) {
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
