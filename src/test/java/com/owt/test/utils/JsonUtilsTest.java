package com.owt.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.owt.utils.JsonUtils;

/**
 * Created by Open Web Technology.
 *
 * @author DBO Open Web Technology
 * @since 15 July 2015
 * 
 */
public class JsonUtilsTest
{
    public class DataSample
    {
        private String firstName;
        private String lastName;
        private String phone;

        public String getFirstName()
        {
            return firstName;
        }

        public void setFirstName(final String firstName)
        {
            this.firstName = firstName;
        }

        public String getLastName()
        {
            return lastName;
        }

        public void setLastName(final String lastName)
        {
            this.lastName = lastName;
        }

        public String getPhone()
        {
            return phone;
        }

        public void setPhone(final String phone)
        {
            this.phone = phone;
        }

    }

    @Test
    public void convertObjectToJsonBytesTest()
    {
        try {

            final DataSample sample = new DataSample();
            sample.setFirstName("TEST");
            sample.setLastName("TEST");
            sample.setPhone("010101010101");

            final byte[] result = JsonUtils.convertObjectToJsonBytes(sample);

            assertEquals("{\"firstName\":\"TEST\",\"lastName\":\"TEST\",\"phone\":\"010101010101\"}", new String(result, StandardCharsets.UTF_8));
            assertEquals(61, result.length);
        }
        catch (final IOException e) {
            fail("throw exception" + e.getMessage());
        }
    }

    @Test
    public void convertObjectToJsonStringTest()
    {
        try {

            final DataSample sample = new DataSample();
            sample.setFirstName("TEST");
            sample.setLastName("TEST");
            sample.setPhone("010101010101");

            final String result = JsonUtils.convertObjectToJsonString(sample);
            assertEquals("{\"firstName\":\"TEST\",\"lastName\":\"TEST\",\"phone\":\"010101010101\"}", result);
            assertEquals(61, result.length());
        }
        catch (final IOException e) {
            fail("throw exception" + e.getMessage());
        }
    }

}
