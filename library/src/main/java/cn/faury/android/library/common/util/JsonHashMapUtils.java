package cn.faury.android.library.common.util;

import java.util.HashMap;

/**
 * 数据格式转化类
 */
public class JsonHashMapUtils extends HashMap<String, Object> {


    private static final long serialVersionUID = 1L;

    private final String NULL = "null";

    private final String ERROR_STRING = "-";

    private int DEFAULTINTVALUE = 0;

    private String DEFAULTSTRINGVALUE = "";

    private Boolean DEFAULTBOOLEANVALUE = false;

    private float DEFAULT_FLOAT_VALUE = 0.0f;


    public int getInt(Object key) {

        return getInt(key, DEFAULTINTVALUE);
    }

    public int getInt(Object key, int defaultValue) {

        int value = 0;
        if (super.containsKey(key)) {
            try {
                value = Integer.parseInt(super.get(key).toString().trim());
            } catch (Exception e) {
                e.printStackTrace();
                value = defaultValue;
            }
        } else {
            value = defaultValue;
        }

        return value;
    }

    public String getString(Object key) {

        return getString(key, DEFAULTSTRINGVALUE);
    }

    public String getString(Object key, String defaultValue) {

        String value = "";
        if (super.containsKey(key)) {
            try {
                value = super.get(key).toString();
            } catch (Exception e) {
                e.printStackTrace();
                value = defaultValue;
            }
            if (NULL.equals(value) || ERROR_STRING.equals(value) || "".equals(value)) {
                value = defaultValue;
            }
        } else {
            value = defaultValue;
        }
        return value;
    }

    public boolean getBoolean(Object key) {

        return getBoolean(key, DEFAULTBOOLEANVALUE);
    }

    public boolean getBoolean(Object key, boolean defaultValue) {

        boolean value = false;
        if (super.containsKey(key)) {
            try {
                value = Boolean.parseBoolean(super.get(key).toString().trim());
            } catch (Exception e) {
                e.printStackTrace();
                value = defaultValue;
            }

        } else {
            value = defaultValue;
        }
        return value;
    }

    public float getFloat(Object key) {

        return getFloat(key, DEFAULT_FLOAT_VALUE);
    }

    public float getFloat(Object key, float defaultValue) {

        float value;
        if (super.containsKey(key)) {
            try {
                value = Float.parseFloat(super.get(key).toString().trim());
            } catch (Exception e) {
                value = defaultValue;
                e.printStackTrace();
            }
        } else {
            value = defaultValue;
        }
        return value;
    }

    @Override
    public Object get(Object key) {

        if (!this.containsKey(key)) {
            return NULL;
        }
        return super.get(key);
    }

}
