package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.TextBook;

import java.sql.SQLException;
import java.util.List;

public class RepoTextBook {

    DBHelper db;

    public RepoTextBook(DBHelper db) {
        this.db = db;
    }

    public List<TextBook> getDataByLang(int langid) {
        try {
            return db.getDaoTextBook().queryBuilder()
                    .where().eq("LANGID", langid)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
