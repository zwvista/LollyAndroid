package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.Book;
import com.zwstudio.lolly.domain.Language;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepoBook {

    DBHelper db;

    public RepoBook(DBHelper db) {
        this.db = db;
    }

    public List<Book> getDataByLang(int langid) {
        try {
            return db.getDaoBook().queryBuilder()
                    .where().eq("LANGID", langid)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
