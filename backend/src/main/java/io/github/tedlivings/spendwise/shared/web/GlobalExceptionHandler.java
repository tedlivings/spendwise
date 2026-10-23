package io.github.tedlivings.spendwise.shared.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.github.tedlivings.spendwise.shared.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Single place where exceptions are translated into RFC 9457 problem details.
 */

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String PROBLEM_TYPE_PREFIX = "urn:spendwise:problem:";

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		List<FieldViolation> violations = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> new FieldViolation(error.getField(), error.getDefaultMessage())).toList();

		ProblemDetail problem = ex.getBody();
		problem.setType(problemType("validation-error"));
		problem.setTitle("Validation failed");
		problem.setDetail("One or more fields are invalid");
		problem.setProperty("errors", violations);

		return handleExceptionInternal(ex, problem, headers, status, request);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	ProblemDetail handlerResourceNotFound(ResourceNotFoundException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problem.setType(problemType("resource-not-found"));
		problem.setTitle("Resource not found");
		return problem;
	}

	@ExceptionHandler(Exception.class)
	ProblemDetail handleUnexpected(Exception ex) {
		log.error("Unexpected error", ex);
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
				"An unexpected error occurred");
		problem.setType(problemType("internal-error"));
		problem.setTitle("Internal server error");
		return problem;
	}

	private static URI problemType(String name) {
		return URI.create(PROBLEM_TYPE_PREFIX + name);
	}

}
