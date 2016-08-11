package com.zwstudio.lolly.android;

import android.widget.ListView;

import roboguice.inject.InjectView;

public class DrawerListActivity extends DrawerActivity {

    @InjectView(R.id.listView)
    ListView lv;

}
