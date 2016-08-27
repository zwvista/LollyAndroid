package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.UnitPhrase;

import java.sql.SQLException;
import java.util.List;

public class RepoUnitPhrase {

    DBHelper db;

    public RepoUnitPhrase(DBHelper db) {
        this.db = db;
    }

    public List<UnitPhrase> getDataByTextbookUnitParts(int textbookid, int unitpartfrom, int unitpartto) {
        try {
            return db.getDaoUnitPhrase().queryBuilder()
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
