package com.zwstudio.lolly.android;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.SearchView;

public class SearchActivity extends BaseActivity {

    SearchView svWord;
    WebView wvOnline;
    WebView wvOffline;

    String word = "";
    boolean webViewFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        svWord = (SearchView)findViewById(R.id.svWord);
        wvOnline = (WebView)findViewById(R.id.wvOnline);
    }

    private void searchDict(View view) {
        word = svWord.getQuery().toString();
    }

}
