package com.zwstudio.lolly.android;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.SearchView;

import com.zwstudio.lolly.data.RepoDictAll;
import com.zwstudio.lolly.domain.DictAll;

import java.io.UnsupportedEncodingException;

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
        svWord.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDict(svWord);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void searchDict(View view) {
        wvDictOnline.setVisibility(View.VISIBLE);
        wvDictOffline.setVisibility(View.INVISIBLE);

        word = svWord.getQuery().toString();
        DictAll m = getLollyViewModel().getCurrentDict();
        String url = RepoDictAll.urlString(m.url, word);
        wvDictOnline.loadUrl(url);
    }

}
