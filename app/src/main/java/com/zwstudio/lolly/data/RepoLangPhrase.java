package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.LangPhrase;

import java.sql.SQLException;
import java.util.List;

public class RepoLangPhrase {

    DBHelper db;

    public RepoLangPhrase(DBHelper db) {
        this.db = db;
    }

    public List<LangPhrase> getDataByLang(int langid) {
        try {
            return db.getDaoLangPhrase().queryBuilder()
                    .where().eq("LANGID", langid)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
