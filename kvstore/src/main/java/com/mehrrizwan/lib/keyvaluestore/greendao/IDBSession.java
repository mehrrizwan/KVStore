package com.mehrrizwan.lib.keyvaluestore.greendao;


import de.greenrobot.dao.async.AsyncSession;

/**
 * @author : mehrrizwan
 * @version : 1.0
 * @since : 5/24/18 : 6:55 AM
 */
interface IDBSession
{
    DaoSession getSession();
    DaoSession getNewSession();
    AsyncSession getNewAsyncSession();
}
