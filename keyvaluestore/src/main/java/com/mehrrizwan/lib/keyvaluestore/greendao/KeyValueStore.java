package com.mehrrizwan.lib.keyvaluestore.greendao;


import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author : mehrrizwan
 * @version : 1.0
 * @since : 5/24/18 : 7:24 AM
 */

public final class KeyValueStore implements IKeyValueStore
{
    private MapDao mapDao = null;
    private static KeyValueStore instance;

    private KeyValueStore(IDBSession dbSession)
    {
        mapDao = dbSession.getSession().getMapDao();
    }

    public static void createInstance(Context context)
    {
        if (instance == null)
        {
            instance = new KeyValueStore(DBSession.getInstance(context));
        }
    }

    public static KeyValueStore getInstance()
    {
        if (instance == null)
        {
            throw new RuntimeException("No instance found, please call createInstance(Context context) first!");
        }

        return instance;
    }

    @Override
    public boolean put(final String key, final String value)
    {
        try
        {
            long rowID = mapDao.insertOrReplace(new Map(key, value));
            return rowID > 0;
        } catch (Exception e)
        {
//            Log.w(e);
            return false;
        }
    }

    @Override
    public boolean remove(final String key)
    {
        try
        {
            mapDao.deleteByKey(key);
            return true;
        } catch (Exception e)
        {
//            Log.w(e);
            return false;
        }
    }

    @Override
    public String get(final String key)
    {
        try
        {
            return mapDao.load(key).getValue();
        } catch (Exception e)
        {
//            Log.w(e);
            return null;
        }
    }

    @Override
    public HashMap<String, String> getAll()
    {
        try
        {
            List<Map> allValues = mapDao.loadAll();

//            Log.d("Uncomment below to view contents of DB init");
            HashMap<String, String> hashMap = new HashMap<>();
            for (Map map : allValues)
            {
                String key = map.getKey();
                String value = map.getValue();
                hashMap.put(key, value);
                //Log.d("UserDefaults init - "+key+", "+value);
            }

            return hashMap;
        }
        catch (Exception e)
        {
//            Log.e(e);
        }

        return null;
    }

    @Override
    public boolean removeAll()
    {
        try
        {
            mapDao.deleteAll();
            return true;
        } catch (Exception e)
        {
//            Log.w(e);
            return false;
        }
    }

    @Override
    public boolean batchInsert(ArrayList<Map> entities)
    {
        try
        {
            mapDao.insertOrReplaceInTx(entities);
            return true;
        }
        catch (Exception e)
        {
//            Log.w(e);
            return false;
        }
    }

    @Override
    public boolean batchDelete(ArrayList<String> keys)
    {
        try
        {
            mapDao.deleteByKeyInTx(keys);
            return true;
        }
        catch (Exception e)
        {
//            Log.w(e);
            return false;
        }
    }
}
