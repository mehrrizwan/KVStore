package com.mehrrizwan.lib.keyvaluestore.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import de.greenrobot.dao.async.AsyncSession;

/**
 * @author : mehrrizwan
 * @version : 1.0
 * @since : 5/24/18 : 6:58 AM
 */

final class DBSession implements IDBSession
{
    public static final String USERDEFAULTS_DB_NAME = "keyvaluestore";
    public static final int SCHEMA_VERSION = 1;
    private final DaoMaster daoMaster;
    private final SQLiteDatabase db;
    private final DaoSession daoSession;

    private static DBSession instance;

    //to fetch db
    //adb shell 'run-as com.testbed.root cat /data/data/com.testbed.root/databases/testbed-db' > /Users/rizwan/Desktop/testbed-db


    public static DBSession getInstance(Context context)
    {
        if (instance == null)
            instance = new DBSession(context);

        return instance;
    }

    private DBSession(Context context)
    {
        DBOpenHelper helper = new DBOpenHelper(context, USERDEFAULTS_DB_NAME, null);
        db = helper.getWritableDatabase();

        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public class DBOpenHelper extends DaoMaster.OpenHelper
    {
        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory)
        {
            super(context, name, factory);
        }

        @Override
        public void onConfigure(SQLiteDatabase db)
        {
            super.onConfigure(db);
//            db.setVersion(SCHEMA_VERSION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            for (int migrateVersion = oldVersion + 1; migrateVersion <= newVersion; migrateVersion++)
                upgrade(db, migrateVersion);
        }

        private void upgrade(SQLiteDatabase db, int migrateVersion)
        {
//            switch (migrateVersion)
//            {
//                case 2:
//                    break;
//                case 3:
//                    break;
//            }
        }
    }

    public DaoSession getSession()
    {
        return daoSession;
    }

    public DaoSession getNewSession()
    {
        return daoMaster.newSession();
    }

    public AsyncSession getNewAsyncSession()
    {
        return daoSession.startAsyncSession();
    }
}
