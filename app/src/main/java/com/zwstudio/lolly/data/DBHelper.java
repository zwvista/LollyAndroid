package com.zwstudio.lolly.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.zwstudio.lolly.domain.Dictionary;
import com.zwstudio.lolly.domain.LangPhrase;
import com.zwstudio.lolly.domain.LangWord;
import com.zwstudio.lolly.domain.Language;
import com.zwstudio.lolly.domain.TBWord;
import com.zwstudio.lolly.domain.TextBook;
import com.zwstudio.lolly.domain.UnitPhrase;
import com.zwstudio.lolly.domain.UnitWord;
import com.zwstudio.lolly.domain.UserSetting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "LollyCore.db";
    private static String databasePath;

    private static final int DATABASE_VERSION = 1;

    private Dao<Dictionary, Integer> daoDictionary;
    private Dao<LangPhrase, Integer> daoLangPhrase;
    private Dao<Language, Integer> daoLanguage;
    private Dao<LangWord, Integer> daoLangWord;
    private Dao<TBWord, Integer> daoTBWord;
    private Dao<TextBook, Integer> daoTextBook;
    private Dao<UnitPhrase, Integer> daoUnitPhrase;
    private Dao<UnitWord, Integer> daoUnitWord;
    private Dao<UserSetting, Integer> daoUserSetting;

    public DBHelper(Context context) throws IOException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        databasePath = context.getDatabasePath(DATABASE_NAME).getParent() + "/";
        boolean dbexist = checkdatabase();
        if (!dbexist) {
            // If database did not exist, try copying existing database from assets folder.
            try {
                File dir = new File(databasePath);
                dir.mkdirs();
                InputStream myinput = context.getAssets().open(DATABASE_NAME);
                String outfilename = databasePath + DATABASE_NAME;
                Log.i(DBHelper.class.getName(), "DB Path : " + outfilename);
                OutputStream myoutput = new FileOutputStream(outfilename);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myinput.read(buffer)) > 0) {
                    myoutput.write(buffer, 0, length);
                }
                myoutput.flush();
                myoutput.close();
                myinput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * Check whether or not database exist
    */
    private boolean checkdatabase() {
        boolean checkdb = false;

        String myPath = databasePath + DATABASE_NAME;
        File dbfile = new File(myPath);
        checkdb = dbfile.exists();

        Log.i(DBHelper.class.getName(), "DB Exist : " + checkdb);

        return checkdb;
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
        daoDictionary = null;
        daoLanguage = null;
        daoTextBook = null;
        daoUnitWord = null;
        daoLangWord = null;
        daoTBWord = null;
        daoLangPhrase = null;
        daoUnitPhrase = null;
        daoUserSetting = null;
    }

    public Dao<Language, Integer> getDaoLanguage() throws SQLException {
        if (daoLanguage == null)
            daoLanguage = getDao(Language.class);
        return daoLanguage;
    }

    public Dao<Dictionary, Integer> getDaoDictionary() throws SQLException {
        if (daoDictionary == null)
            daoDictionary = getDao(Dictionary.class);
        return daoDictionary;
    }

    public Dao<TextBook, Integer> getDaoTextBook() throws SQLException {
        if (daoTextBook == null)
            daoTextBook = getDao(TextBook.class);
        return daoTextBook;
    }

    public Dao<UnitWord, Integer> getDaoUnitWord() throws SQLException {
        if (daoUnitWord == null)
            daoUnitWord = getDao(UnitWord.class);
        return daoUnitWord;
    }

    public Dao<LangWord, Integer> getDaoLangWord() throws SQLException {
        if (daoLangWord == null)
            daoLangWord = getDao(LangWord.class);
        return daoLangWord;
    }

    public Dao<TBWord, Integer> getDaoTBWord() throws SQLException {
        if (daoTBWord == null)
            daoTBWord = getDao(TBWord.class);
        return daoTBWord;
    }

    public Dao<LangPhrase, Integer> getDaoLangPhrase() throws SQLException {
        if (daoLangPhrase == null)
            daoLangPhrase = getDao(LangPhrase.class);
        return daoLangPhrase;
    }

    public Dao<UnitPhrase, Integer> getDaoUnitPhrase() throws SQLException {
        if (daoUnitPhrase == null)
            daoUnitPhrase = getDao(UnitPhrase.class);
        return daoUnitPhrase;
    }

    public Dao<UserSetting, Integer> getDaoUserSetting() throws SQLException {
        if (daoUserSetting == null)
            daoUserSetting = getDao(UserSetting.class);
        return daoUserSetting;
    }

}