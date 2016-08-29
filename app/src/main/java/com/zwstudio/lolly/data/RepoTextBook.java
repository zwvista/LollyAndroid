package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.Textbook;

import java.sql.SQLException;
import java.util.List;

public class RepoTextbook {

    DBHelper db;

    public RepoTextbook(DBHelper db) {
        this.db = db;
    }

    public List<Textbook> getDataByLang(int langid) {
        try {
            return db.getDaoTextbook().queryBuilder()
                    .where().eq("LANGID", langid)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
