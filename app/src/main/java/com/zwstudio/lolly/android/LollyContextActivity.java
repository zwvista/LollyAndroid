package com.zwstudio.lolly.android;

import com.zwstudio.lolly.data.DBHelper;
import com.zwstudio.lolly.data.SettingsViewModel;

public class LollyContextActivity extends RoboAppCompatActivity {

    protected LollyApplication getLollyApp() {
        return (LollyApplication)getApplicationContext();
    }

    protected SettingsViewModel getSettingsViewModel() {
        return getLollyApp().getSettingsViewModel();
    }

    protected DBHelper getDBHelper() {
        return getLollyApp().getDBHelper();
    }

}
