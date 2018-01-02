package com.owt.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * GsonUtils, utility class for using gson library
 *
 * @author DBO, Open Web Technology
 * @since 1 july 2015
 *
 */
public final class GsonUtils
{
    private static JsonParser jsonParser = new JsonParser();

    private GsonUtils()
    {
    }

    public static <T> JsonArray convertCollectionToJsonArray(final List<T> collection)
    {
        return new Gson().toJsonTree(collection).getAsJsonArray();
    }

    /**
     * Convert Object to Json Object.
     *
     * @param object object to convert
     * @return the object
     */
    public static <T> JsonObject convertObjectToJsonObject(final T object)
    {
        return new Gson().toJsonTree(object).getAsJsonObject();
    }

    /**
     * Convert String to Json.
     *
     * @param str String to convert
     * @return json Object
     */
    public static JsonObject convertStringToJsonObject(final String str)
    {
        return jsonParser.parse(str).getAsJsonObject();
    }

    /**
     * Return an Map from a json object.
     *
     * @param jsonObject
     * @return Map
     */
    public static Object convertJsonObjectToHashMap(final JsonObject jsonObject)
    {
        return new Gson().fromJson(jsonObject, new TypeToken<Map<String, Object>>()
        {
            // Map conversion
        }.getType());
    }

    /**
     * Convert a Json String into a Collection on Typed element.
     *
     * @param strJson String to convert
     * @param klass expected type
     * @return list of Type
     */
    public static <T> List<T> convertJsonStringToCollection(final String strJson, final Class<T> klass)
    {
        final JsonArray array = new JsonParser().parse(strJson).getAsJsonArray();
        final Gson g = new Gson();
        final List<T> lst = new ArrayList<>();
        for (final JsonElement json : array) {
            final T entity = g.fromJson(json, klass);
            lst.add(entity);
        }
        return lst;
    }

    public static JsonElement convertJsonStringToElement(final String strJson)
    {
        return jsonParser.parse(strJson);
    }

    public static Map<String, String> convertJsonStringToMap(final String strJson)
    {
        final JsonElement element = jsonParser.parse(strJson);

        if (element.isJsonArray()) {

            final Map<String, String> data = new HashMap<>();
            final JsonArray jsonarray = element.getAsJsonArray();
            for (int i = 0, size = jsonarray.size(); i < size; i++) {
                final JsonElement el = jsonarray.get(i);
                final Set<Entry<String, JsonElement>> set = el.getAsJsonObject().entrySet();
                for (final Entry<String, JsonElement> entry : set) {
                    data.put(entry.getKey(), entry.getValue().toString());
                }
            }
            return data;

        } else if (element.isJsonObject()) {

            final Map<String, String> data = new HashMap<>();
            final Set<Entry<String, JsonElement>> set = element.getAsJsonObject().entrySet();
            for (final Entry<String, JsonElement> entry : set) {
                data.put(entry.getKey(), entry.getValue().getAsString());
            }
            return data;

        } else {
            return null;
        }
    }

    public static Map<String, Object> convertJsonStringToObjectMap(final String strJson)
    {
        final JsonElement element = jsonParser.parse(strJson);

        if (element.isJsonArray()) {

            final Map<String, Object> data = new HashMap<>();
            final JsonArray jsonarray = element.getAsJsonArray();
            for (int i = 0, size = jsonarray.size(); i < size; i++) {
                final JsonElement el = jsonarray.get(i);
                final Set<Entry<String, JsonElement>> set = el.getAsJsonObject().entrySet();
                for (final Entry<String, JsonElement> entry : set) {
                    data.put(entry.getKey(), entry.getValue().toString());
                }
            }
            return data;

        } else if (element.isJsonObject()) {

            final Map<String, Object> data = new HashMap<>();
            final Set<Entry<String, JsonElement>> set = element.getAsJsonObject().entrySet();
            for (final Entry<String, JsonElement> entry : set) {
                if (entry.getValue().isJsonArray()) {
                    data.put(entry.getKey(), entry.getValue().getAsJsonArray());
                } else {
                    data.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
            return data;

        } else {
            return null;
        }
    }

    public static boolean isEmptyJsonArray(final JsonArray array)
    {
        return array == null || array.size() == 0;
    }

    public static boolean isNotEmptyJsonArray(final JsonArray array)
    {
        return !isEmptyJsonArray(array);
    }
}