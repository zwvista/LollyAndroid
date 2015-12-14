package com.zwstudio.lolly.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.lolly.domain.Language;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepoLanguage {

	Dao<Language, Integer> daoLanguage;

	public RepoLanguage(DatabaseHelper db) {
		try {
			daoLanguage = db.getDaoLanguage();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public List<Language> getData() {
		try {
			return daoLanguage.queryBuilder()
			    .where().gt("LANGID", 0)
                .query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Map<String, String> getIdNameMap() {
		Map<String, String> m = new HashMap<String, String>();
		List<Language> lst = getData();
		for(Language r : lst)
			m.put(Integer.toString(r.langid), r.langname);
		return m;
	}

}
