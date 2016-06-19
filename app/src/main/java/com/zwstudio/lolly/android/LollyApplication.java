package com.zwstudio.lolly.android;

import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.zwstudio.lolly.data.DBHelper;
import com.zwstudio.lolly.data.WordsOnlineViewModel;

public class LollyApplication extends Application {
    private DBHelper dbHelper = null;

    private WordsOnlineViewModel wordsOnlineViewModel;

    @Override
    public void onCreate() {
        super.onCreate();
        wordsOnlineViewModel = new WordsOnlineViewModel(getHelper());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (dbHelper != null) {
            OpenHelperManager.releaseHelper();
            dbHelper = null;
        }
    }

    private DBHelper getHelper() {
        if (dbHelper == null)
            dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        return dbHelper;
    }

    public WordsOnlineViewModel getWordsOnlineViewModel() {
        return wordsOnlineViewModel;
    }
}
