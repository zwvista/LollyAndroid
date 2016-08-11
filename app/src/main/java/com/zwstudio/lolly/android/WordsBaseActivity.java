package com.zwstudio.lolly.android;

import android.widget.ListView;

import roboguice.inject.InjectView;

public class WordsBaseActivity extends DrawerActivity {

    @InjectView(R.id.listView)
    ListView lv;

}
