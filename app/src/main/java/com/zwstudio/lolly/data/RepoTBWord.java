package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.TBWord;

import java.sql.SQLException;
import java.util.List;

public class RepoTBWord {

    DBHelper db;

    public RepoTBWord(DBHelper db) {
        this.db = db;
    }

    public List<TBWord> getDataByLang(int langid) {
        try {
            return db.getDaoTBWord().queryBuilder()
                    .where().eq("LANGID", langid)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
