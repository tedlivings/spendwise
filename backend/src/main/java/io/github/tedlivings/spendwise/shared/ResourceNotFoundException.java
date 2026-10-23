package io.github.tedlivings.spendwise.shared;

/**
 * Thrown when a requested resource does not exist (or is not visible to the caller).
 * Translated to HTTP 404 by the global exception handler.
 */
public class ResourceNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(String resourceName, Object id) {
		super("%s with id '%s' was not found".formatted(resourceName, id));
	}

}
