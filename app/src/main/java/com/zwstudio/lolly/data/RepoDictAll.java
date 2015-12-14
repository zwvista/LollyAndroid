package com.zwstudio.lolly.data;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.zwstudio.lolly.domain.DictAll;

public class RepoDictAll {
	
	Dao<DictAll, Integer> daoDictAll;
	
	public RepoDictAll(DatabaseHelper db) {
		try {
			daoDictAll = db.getDaoDictAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public List<DictAll> getDataByLang(int langid) {
		try {
			return daoDictAll.queryBuilder()
				.where().eq("LANGID", langid)
				.query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public DictAll getDataByLangDict(int langid, String dictname) {
		try {
            return daoDictAll.queryBuilder()
                .where().eq("LANGID", langid)
                .and().eq("DICTNAME", dictname)
                .queryForFirst();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
