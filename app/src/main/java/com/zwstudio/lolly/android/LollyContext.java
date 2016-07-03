package com.zwstudio.lolly.android;

import android.app.Activity;

import com.zwstudio.lolly.data.DBHelper;
import com.zwstudio.lolly.data.SettingsViewModel;

/**
 * Created by zwvista on 2016/07/03.
 */
public interface LollyContext {

    default SettingsViewModel getSettingsViewModel() {
        return ((LollyApplication) ((Activity)this).getApplicationContext()).getSettingsViewModel();
    }

    default DBHelper getDBHelper() {
        return ((LollyApplication) ((Activity)this).getApplicationContext()).getDBHelper();
    }
}
