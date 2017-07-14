package com.owt.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;

/**
 * Formats dates to sortable UTC strings in compliance with ISO-8601.
 *
 *
 *
 * @author DBO Open Web Technology
 * @since 1 july 2015
 *
 *
 * @see http ://stackoverflow.com/questions/11294307/convert-java-date-to-utc-string /11294308
 */
// TODO: this class need a serious refactoring
public final class DateUtils
{
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int HOURS_IN_DAY = 24;
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    private static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS zzz";
    private static final String LEGACY_FORMAT = "EEE dd MMM yy hh:mm:ss z";
    public static final long ONE_MINUTE_IN_MILLISECONDS = 60000; //ms
    public static final long ONE_MINUTE_IN_SECONDS = 60; //s

    private DateUtils()
    {
    }

    /**
     * Formats the current time in a sortable ISO-8601 UTC format.
     *
     * @return Current time in ISO-8601 format, e.g. : "2012-07-03T07:59:09.206 UTC"
     */
    @Deprecated
    public static String now()
    {
        return DateUtils.toString(Date.from(Instant.now()));
    }

    /**
     * Formats a given date in the legacy format with ruby Nxt Portal
     *
     * @param date Valid Date object.
     * @return The given date in Legacy Date.toString() format, e.g. ""%a, %e %b %Y %H:%M:%S UTC"
     */
    @Deprecated
    public static String toLegacyString(final Date date)
    {
        final SimpleDateFormat formatter = new SimpleDateFormat(LEGACY_FORMAT, Locale.ENGLISH);
        formatter.setTimeZone(UTC);
        return formatter.format(date);
    }

    /**
     * Formats a given date in a sortable ISO-8601 UTC format.
     *
     * <pre>
     * <code>
     * final Calendar moonLandingCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
     * moonLandingCalendar.set(1969, 7, 20, 20, 18, 0);
     * final Date moonLandingDate = moonLandingCalendar.getTime();
     * System.out.println("UTCDate.toString moon:       " + PrettyDate.toString(moonLandingDate));
     * >>> UTCDate.toString moon:       1969-08-20T20:18:00.209 UTC
     * </code>
     * </pre>
     *
     * @param date Valid Date object.
     * @return The given date in ISO-8601 format.
     *
     */
    @Deprecated
    public static String toString(final Date date)
    {
        final SimpleDateFormat formatter = new SimpleDateFormat(ISO_FORMAT);
        formatter.setTimeZone(UTC);
        return formatter.format(date);
    }

    /**
     * Formats a given date to RFC1123 format.
     *
     * The RFC-1123 date-time formatter, such as 'Tue, 3 Jun 2008 11:05:30 GMT'.
     *
     * @param date Valid Date object.
     * @return The given date in ISO-8601 format.
     *
     */
    @Deprecated
    public static String toRFC1123String(final Date date)
    {
        final ZonedDateTime ldt = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("GMT"));

        return ldt.format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    /**
     * Return a Date object from a String formated in RFC3339 format
     * http://tools.ietf.org/html/rfc3339 "Z" suffix is Zulu UTC time
     *
     * @param date string value
     * @return the Date
     */
    @Deprecated
    public static java.util.Date fromRfc3339ToDateString(final String dateString)
    {
        return Date.from(ZonedDateTime.parse(dateString).toInstant());

    }

    /**
     * Formats a date in any given format at UTC.
     *
     * <pre>
     * <code>
     * final Calendar moonLandingCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
     * moonLandingCalendar.set(1969, 7, 20, 20, 17, 40);
     * final Date moonLandingDate = moonLandingCalendar.getTime();
     * PrettyDate.toString(moonLandingDate, "yyyy-MM-dd")
     * >>> "1969-08-20"
     * </code>
     * </pre>
     *
     *
     * @param date Valid Date object.
     * @param format String representation of the format, e.g. "yyyy-MM-dd"
     * @return The given date formatted in the given format.
     */
    @Deprecated
    public static String toString(final Date date, final String format)
    {
        return toString(date, format, "UTC");
    }

    /**
     * Formats a date at any given format String, at any given Timezone String.
     *
     *
     * @param date Valid Date object
     * @param format String representation of the format, e.g. "yyyy-MM-dd HH:mm"
     * @param timezone String representation of the time zone, e.g. "CST"
     * @return The formatted date in the given time zone.
     */
    @Deprecated
    public static String toString(final Date date, final String format, final String timezone)
    {
        final TimeZone tz = TimeZone.getTimeZone(timezone);
        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(tz);
        return formatter.format(date);
    }

    /**
     * Get in seconds the duration between two dates (substract the first parameter by the second,
     * the result can be negative)
     *
     * @param Date firstDate
     * @param Date secondDate
     * @return long
     */
    public static long getSecondsBetweenDates(final Date firstDate, final Date secondDate)
    {
        return Duration.between(firstDate.toInstant(), secondDate.toInstant()).getSeconds();
    }

    public static String formatDate(final String format, final long datetime)
    {
        if (format != null && datetime > 0) {
            final SimpleDateFormat dt = new SimpleDateFormat(format);
            return dt.format(new Date(datetime));
        }
        return "";
    }

    public static String format(final TemporalAccessor date, final String pattern, final Locale locale)
    {
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneOffset.UTC).withLocale(locale).format(date);
    }

    public static String format(final TemporalAccessor date, final String pattern)
    {
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneOffset.UTC).withLocale(Locale.ENGLISH).format(date);
    }

    public static CronExpression buildCronExpressionFromPattern(final String pattern) throws ParseException
    {
        CronExpression expression = null;
        if (StringUtils.isNotEmpty(pattern)) {
            expression = new CronExpression(pattern);
        }
        return expression;
    }

    public static Date toDate(final LocalDate localDate)
    {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(final LocalDateTime localDateTime)
    {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate toLocalDate(final Date date)
    {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(final Date date)
    {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime dateAndHourOfWeekFromCronExpression(final CronExpression expression)
    {
        // TODO assert cronExpression contains DayOfWeek
        final Date later = expression.getTimeAfter(toDate(LocalDate.now()));
        return toLocalDateTime(later);
    }

    public static int compareDatesWithinWeek(final LocalDateTime ldt1, final LocalDateTime ldt2)
    {
        int compare = ldt1.getDayOfWeek().compareTo(ldt2.getDayOfWeek());
        if (compare == 0) {
            compare = secondsOf(ldt1.getHour(), ldt1.getMinute(), ldt1.getSecond()) - secondsOf(ldt2.getHour(), ldt2.getMinute(), ldt2.getSecond());
        }
        return compare;
    }

    private static int secondsOf(final int hour, final int minute, final int second)
    {
        return hour * HOURS_IN_DAY * MINUTES_IN_HOUR + minute * SECONDS_IN_MINUTE + second;
    }

    public static boolean isDateBeforeCronExpression(final LocalDateTime toCompare, final CronExpression expression)
    {
        if (expression == null) {
            return true;
        }

        final Date later = expression.getNextValidTimeAfter(toDate(LocalDateTime.now()));
        final LocalDateTime laterDateTime = toLocalDateTime(later);

        // first, compare days
        final int compare = toCompare.getDayOfWeek().compareTo(laterDateTime.getDayOfWeek());
        boolean isBefore = compare < 0;

        // if days are same, compare hour and minutes.
        if (compare == 0) {
            isBefore = toCompare.getHour() * MINUTES_IN_HOUR + toCompare.getMinute() < laterDateTime.getHour() * MINUTES_IN_HOUR + laterDateTime.getMinute();
        }
        return isBefore;
    }

    public static boolean isDateEqualsCronExpression(final LocalDateTime toCompare, final CronExpression expression)
    {
        if (expression == null) {
            return true;
        }

        final Date later = expression.getNextValidTimeAfter(toDate(LocalDateTime.now()));
        final LocalDateTime laterDateTime = toLocalDateTime(later);

        // compare days before
        boolean isEquals = laterDateTime.getDayOfWeek().equals(toCompare.getDayOfWeek());

        // compare hours and minutes if days are same.
        if (isEquals) {
            isEquals = laterDateTime.getHour() == toCompare.getHour() && laterDateTime.getMinute() == toCompare.getMinute();
        }

        return isEquals;
    }

    public static boolean isDateAfterCronExpression(final LocalDateTime toCompare, final CronExpression expression)
    {
        if (expression == null) {
            return true;
        }

        return !isDateBeforeCronExpression(toCompare, expression) && !isDateEqualsCronExpression(toCompare, expression);
    }

    public static boolean isDateBeforeCronExpression(final LocalDateTime toCompare, final String expression) throws ParseException
    {
        return isDateBeforeCronExpression(toCompare, buildCronExpressionFromPattern(expression));
    }

    public static boolean isDateEqualsCronExpression(final LocalDateTime toCompare, final String expression) throws ParseException
    {
        return isDateEqualsCronExpression(toCompare, buildCronExpressionFromPattern(expression));
    }

    public static boolean isDateAfterCronExpression(final LocalDateTime toCompare, final String expression) throws ParseException
    {
        return isDateAfterCronExpression(toCompare, buildCronExpressionFromPattern(expression));
    }

    public static String convertLocaleDateTimeToString(final LocalDateTime dateTime, final String pattern)
    {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

}
