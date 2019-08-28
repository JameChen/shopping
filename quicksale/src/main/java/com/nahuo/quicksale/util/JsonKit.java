package com.nahuo.quicksale.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Created by jame on 2018/5/7.
 */

public class JsonKit {
    /**
     * 将list转为json
     * @param lists
     * @param sb
     * @return
     */
    public static StringBuilder listToJson(List<?> lists, StringBuilder sb) {
        if (sb == null) sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < lists.size(); i++) {
            Object o = lists.get(i);
            if (o instanceof Map<?, ?>) {
                Map<?, ?> map = (Map<?, ?>) o;
                mapToJson(map, sb);
            } else if (o instanceof List<?>) {
                List<?> l = (List<?>) o;
                listToJson(l, sb);
            } else {
                sb.append("\"" + o + "\"");
            }
            if (i != lists.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb;
    }

    /**
     * 将map转为json
     * @param map
     * @param sb
     * @return
     */
    public static StringBuilder mapToJson(Map<?, ?> map, StringBuilder sb) {
        if (sb == null) sb = new StringBuilder();
        sb.append("{");
        Iterator<?> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
           Entry<?, ?> entry = (Entry<?, ?>) iter.next();
            String key = entry.getKey() != null ? entry.getKey().toString() : "";
            sb.append("\"" + stringToJson(key) + "\":");
            Object o = entry.getValue();
            if (o instanceof List<?>) {
                List<?> l = (List<?>) o;
                listToJson(l, sb);
            } else if (o instanceof Map<?, ?>) {
                Map<?, ?> m = (Map<?, ?>) o;
                mapToJson(m, sb);
            } else {
                String val = entry.getValue() != null ? entry.getValue().toString() : "";
                sb.append("\"" + stringToJson(val) + "\"");
            }
            if (iter.hasNext()) sb.append(",");
        }
        sb.append("}");
        return sb;
    }

    /**
     * 将字符串转为json数据
     * @param str 数据字符串
     * @return json字符串
     */
    private static String stringToJson(String str) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * JsonArray转List
     * @param jsonArr
     * @return
     * @throws JSONException
     */
    public static List<Object> jsonToList(JSONArray jsonArr)
            throws JSONException {
        List<Object> jsonToMapList = new ArrayList<Object>();
        for (int i = 0; i < jsonArr.length(); i++) {
            Object object = jsonArr.get(i);
            if (object instanceof JSONArray) {
                jsonToMapList.add(jsonToList((JSONArray) object));
            } else if (object instanceof JSONObject) {
                jsonToMapList.add(jsonToMap((JSONObject) object));
            } else {
                jsonToMapList.add(object);
            }
        }
        return jsonToMapList;
    }

    /**
     * JsonObject转Map
     * @param jsonObj
     * @return
     * @throws JSONException
     */
    public static Map<String, Object> jsonToMap(JSONObject jsonObj)
            throws JSONException {
        Map<String, Object> jsonMap = new TreeMap<String, Object>();
        Iterator<?> jsonKeys = jsonObj.keys();
        while (jsonKeys.hasNext()) {
            String jsonKey = (String) jsonKeys.next();
            Object jsonValObj = jsonObj.get(jsonKey);
            if (jsonValObj instanceof JSONArray) {
                jsonMap.put(jsonKey, jsonToList((JSONArray) jsonValObj));
            } else if (jsonValObj instanceof JSONObject) {
                jsonMap.put(jsonKey, jsonToMap((JSONObject) jsonValObj));
            } else {
                jsonMap.put(jsonKey, jsonValObj);
            }
        }
        return jsonMap;
    }
}
