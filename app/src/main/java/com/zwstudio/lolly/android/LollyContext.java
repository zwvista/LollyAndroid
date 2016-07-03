package com.zwstudio.lolly.android;

import android.app.Activity;

import com.zwstudio.lolly.data.DBHelper;
import com.zwstudio.lolly.data.SettingsViewModel;

/**
 * Created by zwvista on 2016/07/03.
 */
public interface LollyContext {

    default LollyApplication getLollyApp() {
        return (LollyApplication)((Activity)this).getApplicationContext();
    }

    default SettingsViewModel getSettingsViewModel() {
        return getLollyApp().getSettingsViewModel();
    }

    default DBHelper getDBHelper() {
        return getLollyApp().getDBHelper();
    }
}
