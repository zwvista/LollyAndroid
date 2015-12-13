package com.zwstudio.lolly.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zwstudio.lolly.domain.Dictionary;
import com.zwstudio.lolly.domain.DictionaryId;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepoDictionary {

	Dao<Dictionary, DictionaryId> daoDictionary;

	public RepoDictionary(DatabaseHelper db) {
		try {
			daoDictionary = db.getDaoDictionary();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public List<Dictionary> getDataByLang(int langid) {
		try {
			return daoDictionary.queryBuilder()
			    .where().eq("LANGID", langid)
                .query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public List<String> getNamesByLang(int langid) {
        List<Dictionary> lst = getDataByLang(langid);
		List<String> lst2 = new ArrayList<String>();
        for(Dictionary r : lst)
            lst2.add(r.getId().getDictname());
        return lst2;
	}

}
