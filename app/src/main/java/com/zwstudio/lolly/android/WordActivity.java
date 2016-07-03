package com.zwstudio.lolly.android;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.zwstudio.lolly.data.RepoDictAll;
import com.zwstudio.lolly.data.SettingsViewModel;
import com.zwstudio.lolly.domain.DictAll;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by zwvista on 2016/07/03.
 */
@ContentView(R.layout.activity_word)
public class WordActivity extends RoboAppCompatActivity {

    @InjectView(R.id.webView)
    WebView wv;

    protected SettingsViewModel getSettingsViewModel() {
        return ((LollyApplication) getApplicationContext()).getSettingsViewModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String word = intent.getStringExtra("word");
        setTitle(word);
        DictAll m = getSettingsViewModel().getCurrentDict();
        String url = RepoDictAll.urlString(m.url, word);
        wv.loadUrl(url);
    }
}
