package com.zwstudio.lolly.data;

import android.util.Log;

import com.zwstudio.lolly.domain.DictAll;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;

public class RepoDictAll {

    DBHelper db;

    public RepoDictAll(DBHelper db) {
        this.db = db;
    }

    public List<DictAll> getDataByLang(int langid) {
        try {
            return db.getDaoDictAll().queryBuilder()
                    .where().eq("LANGID", langid)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DictAll getDataByLangDict(int langid, String dictname) {
        try {
            return db.getDaoDictAll().queryBuilder()
                    .where().eq("LANGID", langid)
                    .and().eq("DICTNAME", dictname)
                    .queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String urlString(String url, String word) {
        String wordUrl = null;
        try {
            wordUrl = url.replace("{0}", URLEncoder.encode(word, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("RepoDictAll", "urlString: " + wordUrl);
        return wordUrl;
    }

}
