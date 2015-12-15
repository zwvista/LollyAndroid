package com.zwstudio.lolly.data;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.zwstudio.lolly.domain.DictAll;

public class RepoDictAll {

    DBHelper db;
	
	public RepoDictAll(DBHelper db) {
        this.db = db;
    }
	public List<DictAll> getDataByLang(int langid) {
		try {
			return db.getDaoDictAll().queryBuilder()
				.where().eq("LANGID", langid)
				.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public DictAll getDataByLangDict(int langid, String dictname) {
		try {
            return db.getDaoDictAll().queryBuilder()
                    .where().eq("LANGID", langid)
                .and().eq("DICTNAME", dictname)
                .queryForFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
