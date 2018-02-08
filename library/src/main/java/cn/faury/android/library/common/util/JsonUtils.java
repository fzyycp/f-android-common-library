package cn.faury.android.library.common.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;


/**
 * 这是一个简易的Json-JsonHashMapUtils转换工具，
 * 可以将普通的json数据（字符串） 转换为一个JsonHashMapUtils&lt;Srting,
 * Object&gt;表格，也可以反过来操作。此外还支 持将json数据格式化。
 */
public class JsonUtils {

    /**
     * 将指定的json数据转成JsonHashMapUtils&lt;String, Object&gt;对象
     *
     * @param jsonStr json字符串
     * @return JsonHashMapUtils对象
     */
    public static JsonHashMapUtils fromJson(String jsonStr) {
        try {
            if (jsonStr.startsWith("[") && jsonStr.endsWith("]")) {
                jsonStr = "{\"fakelist\":" + jsonStr + "}";
            }
            JSONObject json = new JSONObject(jsonStr);
            return fromJson(json);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return new JsonHashMapUtils();
    }

    private static JsonHashMapUtils fromJson(JSONObject json) throws JSONException {
        JsonHashMapUtils map = new JsonHashMapUtils();
        Iterator<String> iKey = json.keys();
        while (iKey.hasNext()) {
            String key = iKey.next();
            Object value = json.opt(key);
            if (JSONObject.NULL.equals(value)) {
                value = null;
            }
            if (value != null) {
                if (value instanceof JSONObject) {
                    value = fromJson((JSONObject) value);
                } else if (value instanceof JSONArray) {
                    value = fromJson((JSONArray) value);
                }
                map.put(key, value);
            }
        }
        return map;
    }

    private static ArrayList<Object> fromJson(JSONArray array) throws JSONException {
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0, size = array.length(); i < size; i++) {
            Object value = array.opt(i);
            if (value instanceof JSONObject) {
                value = fromJson((JSONObject) value);
            } else if (value instanceof JSONArray) {
                value = fromJson((JSONArray) value);
            }
            list.add(value);
        }
        return list;
    }

    /**
     * 将指定的 JsonHashMapUtils&lt;String, Object&gt;对象转成json数据
     *
     * @param map 输入参数
     * @return 格式化字符串
     */
    public static String fromJsonHashMapUtils(JsonHashMapUtils map) {
        try {
            return getJSONObject(map).toString();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    private static JSONObject getJSONObject(JsonHashMapUtils map) throws JSONException {
        JSONObject json = new JSONObject();
        for (Entry<String, Object> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof JsonHashMapUtils) {
                value = getJSONObject((JsonHashMapUtils) value);
            } else if (value instanceof ArrayList<?>) {
                value = getJSONArray((ArrayList<Object>) value);
            }
            json.put(entry.getKey(), value);
        }
        return json;
    }

    @SuppressWarnings("unchecked")
    private static JSONArray getJSONArray(ArrayList<Object> list) throws JSONException {
        JSONArray array = new JSONArray();
        for (Object value : list) {
            if (value instanceof JsonHashMapUtils) {
                value = getJSONObject((JsonHashMapUtils) value);
            } else if (value instanceof ArrayList<?>) {
                value = getJSONArray((ArrayList<Object>) value);
            }
            array.put(value);
        }
        return array;
    }

    /**
     * 格式化一个json串
     *
     * @param jsonStr json字符串
     * @return 格式化的字符串
     */
    public static String format(String jsonStr) {
        try {
            return format("", fromJson(jsonStr));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return "";
    }


    private static String format(String sepStr, JsonHashMapUtils map) {
        StringBuffer sb = new StringBuffer();
        sb.append("{\n");
        String mySepStr = sepStr + "\t";
        int i = 0;
        for (Entry<String, Object> entry : map.entrySet()) {
            if (i > 0) {
                sb.append(",\n");
            }
            sb.append(mySepStr).append('\"').append(entry.getKey())
                    .append("\":");
            Object value = entry.getValue();
            if (value instanceof JsonHashMapUtils) {
                sb.append(format(mySepStr, (JsonHashMapUtils) value));
            } else if (value instanceof ArrayList<?>) {
                sb.append(format(mySepStr, (ArrayList<Object>) value));
            } else if (value instanceof String) {
                sb.append('\"').append(value).append('\"');
            } else {
                sb.append(value);
            }
            i++;
        }
        sb.append('\n').append(sepStr).append('}');
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    private static String format(String sepStr, ArrayList<Object> list) {
        StringBuffer sb = new StringBuffer();
        sb.append("[\n");
        String mySepStr = sepStr + "\t";
        int i = 0;
        for (Object value : list) {
            if (i > 0) {
                sb.append(",\n");
            }
            sb.append(mySepStr);
            if (value instanceof JsonHashMapUtils) {
                sb.append(format(mySepStr, (JsonHashMapUtils) value));
            } else if (value instanceof ArrayList<?>) {
                sb.append(format(mySepStr, (ArrayList<Object>) value));
            } else if (value instanceof String) {
                sb.append('\"').append(value).append('\"');
            } else {
                sb.append(value);
            }
            i++;
        }
        sb.append('\n').append(sepStr).append(']');
        return sb.toString();
    }

}
