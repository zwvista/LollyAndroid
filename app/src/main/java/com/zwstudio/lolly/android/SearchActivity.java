package com.zwstudio.lolly.android;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.SearchView;

public class SearchActivity extends BaseActivity {

    SearchView svWord;
    WebView wvDictOnline;
    WebView wvDictOffline;

    String word = "";
    boolean webViewFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        svWord = (SearchView)findViewById(R.id.svWord);
        wvDictOnline = (WebView)findViewById(R.id.wvDictOnline);
        wvDictOffline = (WebView)findViewById(R.id.wvDictOffline);

        wvDictOnline.setVisibility(View.INVISIBLE);
        wvDictOffline.setVisibility(View.INVISIBLE);
    }

    private void searchDict(View view) {
        wvDictOnline.setVisibility(View.VISIBLE);
        wvDictOffline.setVisibility(View.INVISIBLE);

        word = svWord.getQuery().toString();
    }

}
