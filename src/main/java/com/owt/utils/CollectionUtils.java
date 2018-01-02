package com.owt.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * BooleanUtils, utility class for Collections Extension of org.springframework.util.CollectionUtils
 *
 * @author DBO Open Web Technology
 *
 */
public final class CollectionUtils
{
    private CollectionUtils()
    {
    }

    public static boolean isEmpty(final Collection<?> collection)
    {
        return org.springframework.util.CollectionUtils.isEmpty(collection);
    }

    public static boolean isEmpty(final Map<?, ?> map)
    {
        return org.springframework.util.CollectionUtils.isEmpty(map);
    }

    public static boolean isNotEmpty(final Collection<?> collection)
    {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(final Map<?, ?> map)
    {
        return !isEmpty(map);
    }

    public static <T> List<T> defaultIfEmpty(final List<T> list, final List<T> defaultList)
    {
        return isNotEmpty(list) ? list : defaultList;
    }

    public static <T> List<T> defaultToEmpty(final List<T> list)
    {
        return defaultIfEmpty(list, Collections.emptyList());
    }
}
