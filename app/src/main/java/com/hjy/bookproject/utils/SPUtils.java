package com.hjy.bookproject.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Map;
import java.util.Set;

/**
 * 首次使用之前需要先调用createLPPrefUtils()进行创建
 */
public class SPUtils {

    private static SharedPreferences mSP = null;
    private static Editor mEdit = null;

    /**
     * 先在全局类中将工具创建出来，以后就可以直接使用了
     *
     * @param context  上下文
     * @param fileName xml文件名称
     */
    @SuppressLint("CommitPrefEdits")
    public static void createPref(Context context, String fileName) {
        mSP = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        mEdit = mSP.edit();
    }

    /**
     * 保存String类型数值
     *
     * @param key
     * @param value
     */
    public static void putString(String key, String value) {
        mEdit.putString(key, value).commit();
    }

    /**
     * 获取字符串的方法
     *
     * @param key
     * @param defValue
     * @return String
     */
    public static String getString(String key, String defValue) {
        return mSP.getString(key, defValue);
    }

    /**
     * 保存boolean类型数值
     *
     * @param key
     * @param value
     */
    public static void putBoolean(String key, boolean value) {
        mEdit.putBoolean(key, value).commit();
    }

    /**
     * 获取boolean类型数值
     *
     * @param key
     * @param defValue
     * @return boolean
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return mSP.getBoolean(key, defValue);
    }

    /**
     * 保存int类型数值
     *
     * @param key
     * @param value
     */
    public static void putInt(String key, int value) {
        mEdit.putInt(key, value).commit();
    }

    /**
     * 获取int类型数值
     *
     * @param key
     * @param defValue
     * @return int
     */
    public static int getInt(String key, int defValue) {
        return mSP.getInt(key, defValue);
    }

    /**
     * 保存float类型数值
     *
     * @param key
     * @param value
     */
    public static void putFloat(String key, float value) {
        mEdit.putFloat(key, value).commit();
    }

    /**
     * 获取float类型数值
     *
     * @param key
     * @param defValue
     * @return float
     */
    public static float getFloat(String key, float defValue) {
        return mSP.getFloat(key, defValue);
    }

    /**
     * 保存long类型数值
     *
     * @param key
     * @param value
     */
    public static void putLong(String key, Long value) {
        mEdit.putLong(key, value).commit();
    }

    /**
     * 获取long类型数值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static Long getLong(String key, Long defValue) {
        return mSP.getLong(key, defValue);
    }

    /**
     * 保存Set<String>集合数据
     *
     * @param key
     * @param value
     */
    @SuppressLint("NewApi")
    public static void putStringSet(String key, Set<String> value) {
        mEdit.putStringSet(key, value).commit();
    }

    /**
     * 获取Set<String>集合数据
     *
     * @param key
     * @param defValue
     * @return Set<String>
     */
    @SuppressLint("NewApi")
    public static Set<String> getStringSet(String key, Set<String> defValue) {
        return mSP.getStringSet(key, defValue);
    }

    /**
     * 保存对象到xml中
     *
     * @param key
     * @param obj
     */
    public static void putObject(String key, Object obj) {
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            // 将对象写入字节流
            oos.writeObject(obj);

            // 将字节流编码成base64的字符窜
            String base64Data = new String(Base64.encode(baos.toByteArray()));
            mEdit.putString(key, base64Data).commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getObject(String key, T obj) {
        String objBase64 = getString(key, "");
        // 读取字节
        // 读取字节
        byte[] base64 = Base64.decode(objBase64);

        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            // 再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                // 读取对象
                obj = (T) bis.readObject();
                return obj;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取sp中保存的所有数据
     *
     * @return
     */
    public static Map<String, ?> getAll() {
        return mSP.getAll();
    }

    /**
     * 清除SP中保存的所有数据
     */
    public static void clearSP() {
        mEdit.clear().commit();
    }

    /**
     * 清除SP中指定的字段，最少一个字段
     *
     * @param key  字段1
     * @param keys 多字段
     */
    public static void clearSP(String key, String... keys) {
        mEdit.remove(key);
        for (String otherKey : keys) {
            mEdit.remove(otherKey);
        }
        mEdit.commit();
    }

}