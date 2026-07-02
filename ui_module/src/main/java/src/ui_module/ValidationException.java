package src.ui_module;

import java.util.List;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

	private final List<String> errorMessages;

	public ValidationException(String message, List<String> errorMessages) {
		super(message);
		this.errorMessages = errorMessages;
	}
}