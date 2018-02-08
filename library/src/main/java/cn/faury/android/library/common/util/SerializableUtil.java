package cn.faury.android.library.common.util;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 序列化工具类
 */

public class SerializableUtil {

    /**
     * 序列化对象到Base64字符串
     *
     * @param obj 对象
     * @param <T> 可序列化类型
     * @return 序列化后字符串
     */
    public static <T extends Serializable> String serializeToBase64(T obj) {
        if (obj == null) {
            return null;
        }

        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } catch (IOException e) {
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException ignored) {
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }

    /**
     * 反序列化对象
     *
     * @param data base64数据
     * @param <T>  可序列化类型
     * @return 反序列化后的对象
     */
    public static <T> T deserializeFromBase64(String data) {
        if (StringUtils.isEmpty(data)) {
            return null;
        }

        byte[] bytes = Base64.decode(data, Base64.DEFAULT);
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            Object obj = ois.readObject();
            if (obj instanceof Serializable) {
                return (T) obj;
            }
        } catch (IOException | ClassNotFoundException ignored) {
        } finally {
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException ignored) {
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }
}