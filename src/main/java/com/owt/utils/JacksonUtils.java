package com.owt.utils;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Configurations for Jackson.
 *
 * @author DBO
 */
public final class JacksonUtils
{

    private JacksonUtils()
    {

    }

    public static Jackson2ObjectMapperBuilder getObjectMapperBuilder()
    {
        return new Jackson2ObjectMapperBuilder()
                .indentOutput(false)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
    }

}
