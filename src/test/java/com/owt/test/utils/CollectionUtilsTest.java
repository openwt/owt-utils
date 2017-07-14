package com.owt.test.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.owt.utils.CollectionUtils;

/**
 * Created by Open Web Technology.
 *
 * @author Loïc Bernollin Open Web Technology
 * @since 3 déc. 2015
 * 
 */
public class CollectionUtilsTest
{

    @Test
    public void testCollectionIsNotEmpty()
    {
        List<String> list = null;
        assertFalse(CollectionUtils.isNotEmpty(list));

        list = new ArrayList<>();
        assertFalse(CollectionUtils.isNotEmpty(list));

        list.add("bdshd");
        assertTrue(CollectionUtils.isNotEmpty(list));
    }

    @Test
    public void testMapIsNotEmpty()
    {
        Map<String, String> map = null;
        assertFalse(CollectionUtils.isNotEmpty(map));

        map = new HashMap<>();
        assertFalse(CollectionUtils.isNotEmpty(map));

        map.put("key", "value");
        assertTrue(CollectionUtils.isNotEmpty(map));
    }

}
