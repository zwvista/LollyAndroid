package com.zwstudio.lolly.android;

import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.zwstudio.lolly.data.DBHelper;
import com.zwstudio.lolly.data.SettingsViewModel;

public class LollyApplication extends Application {
    private DBHelper dbHelper = null;

    private SettingsViewModel settingsViewModel;

    @Override
    public void onCreate() {
        super.onCreate();
        settingsViewModel = new SettingsViewModel(getDBHelper());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }

    public DBHelper getDBHelper() {
        if (dbHelper == null)
            dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        return dbHelper;
    }

    public SettingsViewModel getSettingsViewModel() {
        return settingsViewModel;
    }
}
