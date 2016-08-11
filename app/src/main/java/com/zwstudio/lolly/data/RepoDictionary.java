package com.zwstudio.lolly.data;

import android.util.Log;

import com.zwstudio.lolly.domain.Dictionary;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepoDictionary {

    DBHelper db;

    public RepoDictionary(DBHelper db) {
        this.db = db;
    }

    public List<Dictionary> getDataByLang(int langid) {
        try {
            return db.getDaoDictionary().queryBuilder()
                    .where().eq("LANGIDFROM", langid)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getNamesByLang(int langid) {
        List<Dictionary> lst = getDataByLang(langid);
        List<String> lst2 = new ArrayList<String>();
        for (Dictionary r : lst)
            lst2.add(r.dictname);
        return lst2;
    }

    public Dictionary getDataByLangDict(int langid, String dictname) {
        try {
            return db.getDaoDictionary().queryBuilder()
                    .where().eq("LANGIDFROM", langid)
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
        Log.d("RepoDictionary", "urlString: " + wordUrl);
        return wordUrl;
    }


}
