package com.owt.test;

import org.hamcrest.Matchers;
import org.junit.Assert;

/**
 * ThrowableAssertion Created by Open Web Technology.
 *
 * @author DBO
 * @since 27 mai 2015
 */
public class ThrowableAssertion {
    protected final Throwable caught;

    /**
     * ThrowableAssertion constructor
     *
     * @param caught, throwable
     * @return ThrowableAssertion
     */
    public ThrowableAssertion(final Throwable caught) {
        this.caught = caught;
    }

    /**
     * assertThrown
     *
     * @param exceptionThrower, ExceptionThrower
     * @return ThrowableAssertion
     */
    public static ThrowableAssertion assertThrown(final ExceptionThrower exceptionThrower) {
        try {
            exceptionThrower.throwException();
        } catch (final Throwable caught) {
            return new ThrowableAssertion(caught);
        }
        throw new ExceptionNotThrownAssertionError();
    }

    /**
     * isInstanceOf
     *
     * @param exceptionClass, class
     * @return ThrowableAssertion
     */
    public <T extends Throwable> ThrowableAssertion isInstanceOf(final Class<T> exceptionClass) {
        /* unchecked cast here */
        Assert.assertThat(caught, Matchers.isA(exceptionClass));
        return this;
    }

    /**
     * hasMessage
     *
     * @param expectedMessage, string
     * @return ThrowableAssertion
     */
    public ThrowableAssertion hasMessage(final String expectedMessage) {
        Assert.assertThat(caught.getMessage(), Matchers.equalTo(expectedMessage));
        return this;
    }

    /**
     * hasNoCause
     *
     * @return ThrowableAssertion
     */
    public ThrowableAssertion hasNoCause() {
        Assert.assertThat(caught.getCause(), Matchers.nullValue());
        return this;
    }

    /**
     * hasCauseInstanceOf
     *
     * @param exceptionClass class
     * @return ThrowableAssertion
     */
    public <T extends Throwable> ThrowableAssertion hasCauseInstanceOf(final Class<T> exceptionClass) {
        Assert.assertThat(caught.getCause(), Matchers.notNullValue());
        /* unchecked cast here */
        Assert.assertThat(caught.getCause(), Matchers.isA(exceptionClass));
        return this;
    }
}
