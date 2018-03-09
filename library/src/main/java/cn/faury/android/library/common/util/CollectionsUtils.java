package cn.faury.android.library.common.util;

import android.content.ContentValues;

import java.util.Collection;
import java.util.Map;

/**
 * Util for Collections
 */
public class CollectionsUtils {

    /**
     * Returns true if the collection is null or 0-length.
     *
     * @param collection the collection to be examined
     * @return true if str collection null or zero length
     */
    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null || collection.isEmpty() || collection.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if the array is null or 0-length.
     *
     * @param array the array to be examined
     * @return true if array is null or zero length
     */
    public static boolean isEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if the collection is null or 0-length.
     *
     * @param map the map to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(Map<?, ?> map) {
        if (map == null || map.isEmpty() || map.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(ContentValues values) {
        if (values == null || values.size() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
