package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.LangWord;

import java.sql.SQLException;
import java.util.List;

public class RepoLangWord {

    DBHelper db;

    public RepoLangWord(DBHelper db) {
        this.db = db;
    }

    public List<LangWord> getDataByLang(int langid) {
        try {
            return db.getDaoLangWord().queryBuilder()
                    .where().eq("LANGID", langid)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
