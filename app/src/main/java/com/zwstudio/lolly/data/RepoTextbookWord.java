package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.TextbookWord;

import java.sql.SQLException;
import java.util.List;

public class RepoTextbookWord {

    DBHelper db;

    public RepoTextbookWord(DBHelper db) {
        this.db = db;
    }

    public List<TextbookWord> getDataByLang(int langid) {
        try {
            return db.getDaoTextbookWord().queryBuilder()
                    .where().eq("LANGID", langid)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
