package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.WordUnit;

import java.sql.SQLException;
import java.util.List;

public class RepoWordUnit {

    DBHelper db;

    public RepoWordUnit(DBHelper db) {
        this.db = db;
    }

    public List<WordUnit> getDataByBookUnitParts(int bookid, int unitpartfrom, int unitpartto) {
        try {
            return db.getdaoWordUnit().queryBuilder()
                    .where().eq("BOOKID", bookid)
                    .and().raw(String.format("UNIT*10+PART>=%d", unitpartfrom))
                    .and().raw(String.format("UNIT*10+PART<=%d", unitpartto))
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
