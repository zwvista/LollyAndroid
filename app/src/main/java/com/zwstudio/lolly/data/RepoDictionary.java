package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.Dictionary;

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
                    .where().eq("LANGID", langid)
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

}
