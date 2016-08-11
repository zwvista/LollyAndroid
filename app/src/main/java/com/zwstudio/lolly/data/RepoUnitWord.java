package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.UnitWord;

import java.sql.SQLException;
import java.util.List;

public class RepoUnitWord {

    DBHelper db;

    public RepoUnitWord(DBHelper db) {
        this.db = db;
    }

    public List<UnitWord> getDataByTextBookUnitParts(int textbookid, int unitpartfrom, int unitpartto) {
        try {
            return db.getDaoUnitWord().queryBuilder()
                    .where().eq("TEXTBOOKID", textbookid)
                    .and().raw(String.format("UNIT*10+PART>=%d", unitpartfrom))
                    .and().raw(String.format("UNIT*10+PART<=%d", unitpartto))
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
