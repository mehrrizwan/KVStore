package com.mehrrizwan.lib.keyvaluestore.greendao;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class KVStore
{
    private static final String DB_SYNC_MUTEX = "DB_SYNC_MUTEX";
    private static final String GSON_DB_SYNC_MUTEX = "GSON_DB_SYNC_MUTEX";
    private static Gson mGson = new Gson();
    private static HashMap<String, String> _allValuesMap = null;
    public static boolean isInitialized = false;
    private static KVStoreCore kvStoreCore = null;

    public static void initialize(Context context)
    {
        kvStoreCore = new KVStoreCore(context);
        _allValuesMap = kvStoreCore.getAll();
        isInitialized = true;
    }

    public static void put(String name, String value)
    {
        putInternal(name, value);
    }

    public static void putString(String name, String value)
    {
        putInternal(name, value);
    }

    public static void putInt(String name, int value)
    {
        try
        {
            putInternal(name, Integer.toString(value));
        }
        catch (Exception e)
        {
            Log.w("KV Store", e);
        }
    }

    public static void putFloat(String name, float value)
    {
        try
        {
            putInternal(name, Float.toString(value));
        }
        catch (Exception e)
        {
            Log.w("KV Store", e);
        }
    }

    public static void putBoolean(String name, Boolean value)
    {
        try
        {
            putInternal(name, Boolean.toString(value));
        }
        catch (Exception e)
        {
            Log.w("KV Store", e);
        }
    }

    public static void putLong(String name, long value)
    {
        try
        {
            putInternal(name, Long.toString(value));
        }
        catch (Exception e)
        {
            Log.w("KV Store", e);
        }
    }

    public static void remove(String name)
    {
        synchronized (DB_SYNC_MUTEX)
        {
            try
            {
                _allValuesMap.remove(name);
                kvStoreCore.remove(name);
            }
            catch (Exception e)
            {
                Log.w("KV Store", e);
            }
        }
    }

    public static void clear(ArrayList<String> persistKeys)
    {
        synchronized (DB_SYNC_MUTEX)
        {
            try
            {
                kvStoreCore.removeAll();

                if(persistKeys != null && persistKeys.size() > 0)
                {
                    HashMap<String, String> persistMap = new HashMap<>();
                    ArrayList<Map> persistMaps = new ArrayList<>();
                    for(String item : persistKeys)
                    {
                        persistMap.put(item, _allValuesMap.get(item));
                        persistMaps.add(new Map(item, _allValuesMap.get(item)));
                    }

                    _allValuesMap.clear();
                    _allValuesMap.putAll(persistMap);

                    kvStoreCore.batchInsert(persistMaps);
                }
                else
                {
                    _allValuesMap.clear();
                }
            }
            catch (Exception e)
            {
                Log.w("KV Store", e);
            }
        }
    }

    public static void deleteKeys(ArrayList<String> keys)
    {
        synchronized (DB_SYNC_MUTEX)
        {

            if (keys == null || keys.size() == 0)
                return;

            try
            {
                for (String key : keys)
                {
                    _allValuesMap.remove(key);
                }
                kvStoreCore.batchDelete(keys);
            } catch (Exception e)
            {
                Log.w("KV Store", e);
            }
        }
    }

    public static void synchronize()
    {
    }

    public static String getString(String key)
    {
        String result = "";
        try
        {
            result = _allValuesMap.get(key);
        }
        catch (Exception e)
        {
            Log.w("KV Store",e);
        }

        if(result == null)
            result = "";

        return result;
    }

    public static String getString(String key, String defaultValue)
    {
        String result = "";

        try
        {
            result = _allValuesMap.get(key);
        }
        catch (Exception e)
        {
            Log.w("KV Store", e);
        }

        if(result == null)
        {
            return defaultValue;
        }

        return result;
    }

    public static int getInt(String key)
    {
        try
        {
            String value = _allValuesMap.get(key);
            if (null != value)
                return Integer.valueOf(value);
        }
        catch (Exception e)
        {
            Log.w("KV Store", e);
        }

        return 0;
    }

    public static long getLong(String key)
    {
        try
        {
            String value = _allValuesMap.get(key);
            if (null != value)
                return Long.valueOf(value);
        }
        catch (Exception e)
        {
            Log.w("KV Store", e);
        }

        return 0;
    }

    public static float getFloat(String key)
    {
        try
        {
            if(key == null)
                return 0;

            String value = _allValuesMap.get(key);
            if (null != value)
                return Float.valueOf(value);
        }
        catch (Exception e)
        {
            Log.w("KV Store", e);
        }

        return 0;
    }

    public static boolean containsKey(String key)
    {
        try
        {
            if(key == null)
                return false;

            return _allValuesMap.containsKey(key);
        }
        catch (Exception e)
        {
            Log.w("KV Store", e);
        }

        return false;
    }

    public static boolean getBoolean(String key)
    {
        try
        {
            String value = _allValuesMap.get(key);
            if (null != value)
                return Boolean.valueOf(value);
        }
        catch (Exception e)
        {
            Log.w("KV Store", e);
        }

        return false;
    }

    public static <T> T getObjectValueForType(String key, Type typeOfT)
    {
        try
        {
            String value = getString(key);

            if (!TextUtils.isEmpty(value))
            {
                return fromJson(value, typeOfT);
            }
        }
        catch (Exception e)
        {
            Log.w("KV Store", e);
        }

        return null;
    }

    public static <T> T getObjectValueForType(String key, Class<T> classOfT)
    {
        try
        {
            String value = getString(key);

            if (!TextUtils.isEmpty(value))
            {
                return fromJson(value, classOfT);
            }
        }
        catch (Exception e)
        {
            Log.w("KV Store", e);
        }

        return null;
    }

    public static void setObjectValue(String key, Object object)
    {
        try
        {
            String value = toJsonString(object);

            if (!TextUtils.isEmpty(value))
            {
                put(key, value);
            }
        }
        catch (Exception e)
        {
            Log.w("KV Store", e);
        }
    }

    public static void setObjectValue(String key, Object object, Type typeOfObject)
    {
        try
        {
            String value = toJsonString(object, typeOfObject);

            if (!TextUtils.isEmpty(value))
            {
                put(key, value);
            }
        }
        catch (Exception e)
        {
            Log.w("KV Store", e);
        }
    }

    public static String toJsonString(Object object, Type typeOfObject)
    {
        synchronized(GSON_DB_SYNC_MUTEX)
        {
            try
            {
                return mGson.toJson(object, typeOfObject);
            } catch (Exception e)
            {
                Log.w("KV Store", e);
            }

            return "";
        }
    }

    public static String toJsonString(Object object)
    {
        synchronized(GSON_DB_SYNC_MUTEX)
        {
            try
            {
                return mGson.toJson(object);
            } catch (Exception e)
            {
                Log.w("KV Store", e);
            }

            return "";
        }
    }

    public static <T> T fromJson(String value, Type typeOfObject)
    {
        synchronized(GSON_DB_SYNC_MUTEX)
        {
            try
            {
                if (!TextUtils.isEmpty(value))
                {
                    return mGson.fromJson(value, typeOfObject);
                }
            } catch (Exception e)
            {
                Log.w("KV Store", e);
            }

            return null;
        }
    }

    private static void putInternal(final String key, final String value)
    {
        //Log.d("UserDefaults put - "+key+", "+value);
        synchronized (DB_SYNC_MUTEX)
        {
            try
            {
                _allValuesMap.put(key, value);
                kvStoreCore.put(key, value);
            }
            catch (Exception e)
            {
                Log.w("KV Store", e);
            }
        }
    }
}
