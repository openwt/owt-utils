package com.owt.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.owt.utils.WebUtils;

/**
 * Created by Open Web Technology.
 *
 * @author DBO Open Web Technology
 * @since 26 October 2015
 *
 */
public class WebUtilsTest
{

    @Test
    public void testHtmlEncode()
    {
        final String serviceUnavailable = WebUtils.htmlEncode("الخدمة غير متوفرة");
        final String serviceUnavailableEncoded = "&#x627;&#x644;&#x62e;&#x62f;&#x645;&#x629; &#x63a;&#x64a;&#x631; &#x645;&#x62a;&#x648;&#x641;&#x631;&#x629;";

        final String body = WebUtils.htmlEncode("لم يتم الوصول إلى ارتفاع التحليق بعد أو لا يوجد تغطية أقمار صناعية حاليا.");
        final String bodyEncoded = "&#x644;&#x645; &#x64a;&#x62a;&#x645; &#x627;&#x644;&#x648;&#x635;&#x648;&#x644; &#x625;&#x644;&#x649;"
                + " &#x627;&#x631;&#x62a;&#x641;&#x627;&#x639; &#x627;&#x644;&#x62a;&#x62d;&#x644;&#x64a;&#x642; &#x628;&#x639;&#x62f;"
                + " &#x623;&#x648; &#x644;&#x627; &#x64a;&#x648;&#x62c;&#x62f; &#x62a;&#x63a;&#x637;&#x64a;&#x629; &#x623;&#x642;&#x645;&#x627;&#x631;"
                + " &#x635;&#x646;&#x627;&#x639;&#x64a;&#x629; &#x62d;&#x627;&#x644;&#x64a;&#x627;.";

        assertEquals(serviceUnavailableEncoded, serviceUnavailable);
        assertEquals(bodyEncoded, body);
    }

    @Test
    public void testValidEmail()
    {
        assertTrue(WebUtils.isValidEmail("test@test.fr"));
        assertTrue(WebUtils.isValidEmail("test75@test.fr"));
        assertTrue(WebUtils.isValidEmail("q@q--q.qq"));
        assertTrue(WebUtils.isValidEmail("q@qq.qqqqq"));
        assertTrue(WebUtils.isValidEmail("-_#.q@q.qq"));
        assertTrue(WebUtils.isValidEmail("q.q.q.q.q.q@q.qq"));

        assertFalse(WebUtils.isValidEmail("afada@sad.fr@"));
        assertFalse(WebUtils.isValidEmail("aa@aa.@ff.fr"));
        assertFalse(WebUtils.isValidEmail("hhoo_qqaa@hotmail.com husain7#"));
        assertFalse(WebUtils.isValidEmail("karen@bbrann#elly.net"));
        assertFalse(WebUtils.isValidEmail("karen@bbrannelly.net#"));
        assertFalse(WebUtils.isValidEmail("q@q.q q"));

    }
}
