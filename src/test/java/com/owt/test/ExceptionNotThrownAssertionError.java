package com.owt.test;

/**
 * ExceptionNotThrownAssertionError Created by Open Web Technology.
 *
 * @author DBO
 * @since 27 mai 2015
 */
public class ExceptionNotThrownAssertionError extends AssertionError {
    private static final long serialVersionUID = -4570197424953206656L;

    public ExceptionNotThrownAssertionError() {
        super("Expected exception was not thrown.");
    }
}
