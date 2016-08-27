package com.zwstudio.lolly.android;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zwstudio.lolly.data.RepoDictionary;
import com.zwstudio.lolly.domain.Dictionary;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created by zwvista on 2016/07/03.
 */
@ContentView(R.layout.activity_word)
public class WordsDictActivity extends RoboAppCompatActivity implements LollyContext {

    @InjectView(R.id.webView)
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String word = intent.getStringExtra("word");
        setTitle(word);
        Dictionary m = getSettingsViewModel().getCurrentDict();
        String url = RepoDictionary.urlString(m.url, word);
        // http://stackoverflow.com/questions/7746409/android-webview-launches-browser-when-calling-loadurl
        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl(url);
    }
}
