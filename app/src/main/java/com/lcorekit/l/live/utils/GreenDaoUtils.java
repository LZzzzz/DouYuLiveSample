package com.lcorekit.l.live.utils;

import android.database.sqlite.SQLiteDatabase;

import com.lcorekit.l.live.App;
import com.lcorekit.l.live.db.DaoMaster;
import com.lcorekit.l.live.db.DaoSession;


/**
 * Created by l on 17-1-14.
 */

public class GreenDaoUtils {
    private SQLiteDatabase database;
    private DaoSession mDaoSession;

    private static GreenDaoUtils greenDaoUtils;

    private GreenDaoUtils() {

    }

    public static GreenDaoUtils getInstance() {
        if (greenDaoUtils == null) {
            greenDaoUtils = new GreenDaoUtils();
        }
        return greenDaoUtils;
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(App.getCotext(), "attention.db", null);
        database = mHelper.getWritableDatabase();
        DaoMaster mDaoMaster = new DaoMaster(database);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getmDaoSession() {
        if (mDaoSession == null) {
            initGreenDao();
        }
        return mDaoSession;
    }

    public SQLiteDatabase getDatabase() {
        if (database == null) {
            initGreenDao();
        }
        return database;
    }
}
