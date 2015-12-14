package com.zwstudio.lolly.data;


import android.content.Context;
import java.sql.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.zwstudio.lolly.domain.DictAll;
import com.zwstudio.lolly.domain.Dictionary;
import com.zwstudio.lolly.domain.Language;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "lolly.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    private Dao<Language, Integer> daoLanguage = null;
    private Dao<Dictionary, Integer> daoDictionary = null;
    private Dao<DictAll, Integer> daoDictAll = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
    }

    public Dao<Language, Integer> getDaoLanguage() throws SQLException {
        if (daoLanguage == null) {
            daoLanguage = getDao(Language.class);
        }
        return daoLanguage;
    }

    public Dao<Dictionary, Integer> getDaoDictionary() throws SQLException {
        if (daoDictionary == null) {
            daoDictionary = getDao(Dictionary.class);
        }
        return daoDictionary;
    }

    public Dao<DictAll, Integer> getDaoDictAll() throws SQLException {
        if (daoDictAll == null) {
            daoDictAll = getDao(DictAll.class);
        }
        return daoDictAll;
    }

}