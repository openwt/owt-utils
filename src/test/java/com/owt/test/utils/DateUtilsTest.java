package com.owt.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;
import org.quartz.CronExpression;

import com.owt.utils.DateUtils;

/**
 * DateUtilsTest Created by Open Web Technology.
 *
 * @author DBO Open Web Technology
 * @since 15 July 2015
 * 
 */
public class DateUtilsTest
{
    @Test
    public void testFromString()
    {
        // "2014-12-12T10:39:40Z"
        final String dates = "2013-07-04T23:37:46Z";
        // "yyyy-MM-dd'T'HH:mm:ss.SSS zzz"

        final Date date = DateUtils.fromRfc3339ToDateString(dates);

        assertNotNull(date);
        final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(date);

        assertEquals(cal.get(Calendar.YEAR), 2013);
        assertEquals(cal.get(Calendar.MONTH), Calendar.JULY);
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), 4);
        assertEquals(cal.get(Calendar.HOUR_OF_DAY), 23);
        assertEquals(cal.get(Calendar.MINUTE), 37);
        assertEquals(cal.get(Calendar.SECOND), 46);

    }

    @Test
    public void testFromLegacy()
    {
        // "yyyy-MM-dd'T'HH:mm:ss.SSS zzz"
        final String sDate = "2013-07-04T23:37:46Z";
        // EEE dd MMM yy hh:mm:ss z"
        final String assertDate = "Thu 04 Jul 13 11:37:46 UTC";

        final Date date = DateUtils.fromRfc3339ToDateString(sDate);

        final String convertedDate = DateUtils.toLegacyString(date).toString();

        assertEquals(convertedDate, assertDate);

    }

    @Test
    public void testDateBetween()
    {
        final String sDate1 = "2015-07-04T14:37:46Z";
        final String sDate2 = "2015-07-04T23:48:32Z";

        final Date date1 = DateUtils.fromRfc3339ToDateString(sDate1);
        assertNotNull(date1);
        final Date date2 = DateUtils.fromRfc3339ToDateString(sDate2);
        assertNotNull(date2);

        final long result = DateUtils.getSecondsBetweenDates(date1, date2);

        assertEquals(33046, result, 0.1);
    }

    @Test
    public void testDateBetweenHours()
    {
        final String sDate1 = "2015-07-04T14:37:46Z";
        final String sDate2 = "2015-07-04T15:37:46Z";

        final Date date1 = DateUtils.fromRfc3339ToDateString(sDate1);
        assertNotNull(date1);
        final Date date2 = DateUtils.fromRfc3339ToDateString(sDate2);
        assertNotNull(date2);

        final long result = DateUtils.getSecondsBetweenDates(date1, date2);

        assertEquals(3600, result, 0.1);
    }

    @Test
    public void testDateBetweenDay()
    {
        final String sDate1 = "2015-07-04T14:37:46Z";
        final String sDate2 = "2015-07-05T14:37:46Z";

        final Date date1 = DateUtils.fromRfc3339ToDateString(sDate1);
        assertNotNull(date1);
        final Date date2 = DateUtils.fromRfc3339ToDateString(sDate2);
        assertNotNull(date2);

        final long result = DateUtils.getSecondsBetweenDates(date1, date2);

        assertEquals(86400, result, 0.1);
    }

    @Test
    public void testDateBetweenMonth()
    {
        final String sDate1 = "2015-07-04T14:37:46Z";
        final String sDate2 = "2015-08-04T14:37:46Z";

        final Date date1 = DateUtils.fromRfc3339ToDateString(sDate1);
        assertNotNull(date1);
        final Date date2 = DateUtils.fromRfc3339ToDateString(sDate2);
        assertNotNull(date2);

        final long result = DateUtils.getSecondsBetweenDates(date1, date2);

        assertEquals(2678400, result, 0.1);
    }

    @Test
    public void testDateBetweenYear()
    {
        final String sDate1 = "2015-07-04T14:37:46Z";
        final String sDate2 = "2016-07-04T14:37:46Z";

        final Date date1 = DateUtils.fromRfc3339ToDateString(sDate1);
        assertNotNull(date1);
        final Date date2 = DateUtils.fromRfc3339ToDateString(sDate2);
        assertNotNull(date2);

        final long result = DateUtils.getSecondsBetweenDates(date1, date2);

        assertEquals(31622400, result, 0.1);
    }

    @Test
    public void testDateBetweenMinutes()
    {
        final String sDate1 = "2015-07-04T14:37:46Z";
        final String sDate2 = "2015-07-04T14:38:46Z";

        final Date date1 = DateUtils.fromRfc3339ToDateString(sDate1);
        assertNotNull(date1);
        final Date date2 = DateUtils.fromRfc3339ToDateString(sDate2);
        assertNotNull(date2);

        final long result = DateUtils.getSecondsBetweenDates(date1, date2);

        assertEquals(60, result, 0.1);
    }

    @Test
    public void testDateBetweenSeconds()
    {
        final String sDate1 = "2015-07-04T14:37:46Z";
        final String sDate2 = "2015-07-04T14:37:47Z";

        final Date date1 = DateUtils.fromRfc3339ToDateString(sDate1);
        assertNotNull(date1);
        final Date date2 = DateUtils.fromRfc3339ToDateString(sDate2);
        assertNotNull(date2);

        final long result = DateUtils.getSecondsBetweenDates(date1, date2);

        assertEquals(1, result, 0.1);
    }

    @Test
    public void testFormatDate()
    {
        final GregorianCalendar date = new GregorianCalendar(2015, 10, 12, 10, 52, 30);
        final String formatedDate = DateUtils.formatDate("YYYY-MM-DD HH:mm:ss", date.getTimeInMillis());
        assertNotNull(formatedDate);
        assertEquals(formatedDate,
                date.get(Calendar.YEAR)
                        + "-" + String.valueOf(date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_YEAR) + " " + date.get(Calendar.HOUR_OF_DAY) + ":"
                        + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND));
    }

    @Test
    public void testCronExpressionNullPattern() throws Exception
    {
        assertTrue(DateUtils.isDateBeforeCronExpression(LocalDateTime.now(), ""));
        assertTrue(DateUtils.isDateBeforeCronExpression(LocalDateTime.now(), (String) null));
        assertTrue(DateUtils.isDateBeforeCronExpression(LocalDateTime.now(), (CronExpression) null));
        assertTrue(DateUtils.isDateEqualsCronExpression(LocalDateTime.now(), ""));
        assertTrue(DateUtils.isDateEqualsCronExpression(LocalDateTime.now(), (String) null));
        assertTrue(DateUtils.isDateEqualsCronExpression(LocalDateTime.now(), (CronExpression) null));
        assertTrue(DateUtils.isDateAfterCronExpression(LocalDateTime.now(), ""));
        assertTrue(DateUtils.isDateAfterCronExpression(LocalDateTime.now(), (String) null));
        assertTrue(DateUtils.isDateAfterCronExpression(LocalDateTime.now(), (CronExpression) null));
    }

    @Test
    public void testCronExpressionBefore() throws Exception
    {
        final LocalDateTime tuesday = LocalDateTime.of(2016, 3, 22, 10, 00);
        // pattern : wednesday 11:00
        assertTrue(DateUtils.isDateBeforeCronExpression(tuesday, "0 0 11 ? * WED *"));
        // pattern : wednesday 10:00
        assertTrue(DateUtils.isDateBeforeCronExpression(tuesday, "0 0 10 ? * WED *"));
        // pattern : wednesday 9:00
        assertTrue(DateUtils.isDateBeforeCronExpression(tuesday, "0 0 9 ? * WED *"));
        // pattern : tuesday 11:00
        assertTrue(DateUtils.isDateBeforeCronExpression(tuesday, "0 0 11 ? * TUE *"));
        // pattern : tuesday 10:01
        assertTrue(DateUtils.isDateBeforeCronExpression(tuesday, "0 1 10 ? * TUE *"));
        // pattern : tuesday 10:00
        assertFalse(DateUtils.isDateBeforeCronExpression(tuesday, "0 0 10 ? * TUE *"));
        // pattern : tuesday 09:59
        assertFalse(DateUtils.isDateBeforeCronExpression(tuesday, "0 59 9 ? * TUE *"));
        // pattern : tuesday 09:00
        assertFalse(DateUtils.isDateBeforeCronExpression(tuesday, "0 0 9 ? * TUE *"));
        // pattern : monday 11:00
        assertFalse(DateUtils.isDateBeforeCronExpression(tuesday, "0 0 11 ? * MON *"));
        // pattern : monday 10:00
        assertFalse(DateUtils.isDateBeforeCronExpression(tuesday, "0 0 10 ? * MON *"));
        // pattern : monday 09:00
        assertFalse(DateUtils.isDateBeforeCronExpression(tuesday, "0 0 9 ? * MON *"));
    }

    @Test
    public void testCronExpressionEquals() throws Exception
    {
        final LocalDateTime tuesday = LocalDateTime.of(2016, 3, 22, 10, 00);
        // pattern : wednesday 11:00
        assertFalse(DateUtils.isDateEqualsCronExpression(tuesday, "0 0 11 ? * WED *"));
        // pattern : wednesday 10:00
        assertFalse(DateUtils.isDateEqualsCronExpression(tuesday, "0 0 10 ? * WED *"));
        // pattern : wednesday 9:00
        assertFalse(DateUtils.isDateEqualsCronExpression(tuesday, "0 0 9 ? * WED *"));
        // pattern : tuesday 11:00
        assertFalse(DateUtils.isDateEqualsCronExpression(tuesday, "0 0 11 ? * TUE *"));
        // pattern : tuesday 10:01
        assertFalse(DateUtils.isDateEqualsCronExpression(tuesday, "0 1 10 ? * TUE *"));
        // pattern : tuesday 10:00
        assertTrue(DateUtils.isDateEqualsCronExpression(tuesday, "0 0 10 ? * TUE *"));
        // pattern : tuesday 09:59
        assertFalse(DateUtils.isDateEqualsCronExpression(tuesday, "0 59 9 ? * TUE *"));
        // pattern : tuesday 09:00
        assertFalse(DateUtils.isDateEqualsCronExpression(tuesday, "0 0 9 ? * TUE *"));
        // pattern : monday 11:00
        assertFalse(DateUtils.isDateEqualsCronExpression(tuesday, "0 0 11 ? * MON *"));
        // pattern : monday 10:00
        assertFalse(DateUtils.isDateEqualsCronExpression(tuesday, "0 0 10 ? * MON *"));
        // pattern : monday 09:00
        assertFalse(DateUtils.isDateEqualsCronExpression(tuesday, "0 0 9 ? * MON *"));
    }

    @Test
    public void testCronExpressionAfter() throws Exception
    {
        final LocalDateTime tuesday = LocalDateTime.of(2016, 3, 22, 10, 00);
        // pattern : wednesday 11:00
        assertFalse(DateUtils.isDateAfterCronExpression(tuesday, "0 0 11 ? * WED *"));
        // pattern : wednesday 10:00
        assertFalse(DateUtils.isDateAfterCronExpression(tuesday, "0 0 10 ? * WED *"));
        // pattern : wednesday 9:00
        assertFalse(DateUtils.isDateAfterCronExpression(tuesday, "0 0 9 ? * WED *"));
        // pattern : tuesday 11:00
        assertFalse(DateUtils.isDateAfterCronExpression(tuesday, "0 0 11 ? * TUE *"));
        // pattern : tuesday 10:01
        assertFalse(DateUtils.isDateAfterCronExpression(tuesday, "0 1 10 ? * TUE *"));
        // pattern : tuesday 10:00
        assertFalse(DateUtils.isDateAfterCronExpression(tuesday, "0 0 10 ? * TUE *"));
        // pattern : tuesday 09:59
        assertTrue(DateUtils.isDateAfterCronExpression(tuesday, "0 59 9 ? * TUE *"));
        // pattern : tuesday 09:00
        assertTrue(DateUtils.isDateAfterCronExpression(tuesday, "0 0 9 ? * TUE *"));
        // pattern : monday 11:00
        assertTrue(DateUtils.isDateAfterCronExpression(tuesday, "0 0 11 ? * MON *"));
        // pattern : monday 10:00
        assertTrue(DateUtils.isDateAfterCronExpression(tuesday, "0 0 10 ? * MON *"));
        // pattern : monday 09:00
        assertTrue(DateUtils.isDateAfterCronExpression(tuesday, "0 0 9 ? * MON *"));
    }

    @Test
    public void conExpressionTest() throws ParseException
    {
        final LocalDateTime wednesday = LocalDateTime.of(2016, 3, 23, 10, 00);
        assertTrue(DateUtils.isDateAfterCronExpression(wednesday, "* 3 5 ? * 3"));
        assertTrue(DateUtils.isDateBeforeCronExpression(wednesday, "* 9 2 ? * 5"));

    }

    @Test(expected = ParseException.class)
    public void conExpressionTestFailure() throws ParseException
    {
        // YOU MUST NOT USE '*' FOR DAYS_OF_MONTH
        final LocalDateTime wednesday = LocalDateTime.of(2016, 3, 23, 10, 00);
        assertTrue(DateUtils.isDateAfterCronExpression(wednesday, "* 3 5 * * 3"));
        assertTrue(DateUtils.isDateBeforeCronExpression(wednesday, "* 9 2 * * 5"));

    }

}
