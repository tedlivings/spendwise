package io.github.tedlivings.spendwise.shared.web;

/** A single invalid field, as exposed in the "errors" member of a validation problem. */
public record FieldViolation (String field, String message) {}
