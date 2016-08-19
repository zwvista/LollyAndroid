package com.zwstudio.lolly.data;

import com.zwstudio.lolly.domain.UserSetting;

import java.sql.SQLException;

public class RepoUserSetting {

    DBHelper db;

    public RepoUserSetting(DBHelper db) {
        this.db = db;
    }

    public UserSetting getData() {
        try {
            return db.getDaoUserSetting().queryBuilder()
                    .queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
