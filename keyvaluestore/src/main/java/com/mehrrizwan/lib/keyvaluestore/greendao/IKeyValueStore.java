package com.mehrrizwan.lib.keyvaluestore.greendao;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author : mehrrizwan
 * @version : 1.0
 * @since : 5/24/18 : 7:12 AM
 */
interface IKeyValueStore
{
    boolean put(final String key, final String value);
    boolean remove(String key);
    String get(final String key);
    HashMap<String, String> getAll();
    boolean removeAll();
    boolean batchInsert(ArrayList<Map> entities);
    boolean batchDelete(ArrayList<String> keys);
}
