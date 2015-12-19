package com.zwstudio.lolly.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SearchView;
import android.widget.Toast;

import com.zwstudio.lolly.data.RepoDictAll;
import com.zwstudio.lolly.domain.DictAll;

public class SearchActivity extends BaseActivity {

    SearchView svWord;
    WebView wvDictOnline;
    WebView wvDictOffline;

    String word = "";
    boolean webViewFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inflateStub(R.layout.content_search);

        svWord = (SearchView) findViewById(R.id.svWord);
        wvDictOnline = (WebView) findViewById(R.id.wvDictOnline);
        wvDictOffline = (WebView) findViewById(R.id.wvDictOffline);
        configWebView(wvDictOnline);
        configWebView(wvDictOffline);

        wvDictOnline.setVisibility(View.INVISIBLE);
        wvDictOffline.setVisibility(View.INVISIBLE);
        svWord.setQuery(getLollyViewModel().word, false);
        svWord.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDict(svWord);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getLollyViewModel().word = newText;
                return false;
            }
        });
    }

    private void configWebView(WebView wv) {
        wv.getSettings().setJavaScriptEnabled(true); // enable javascript
        final Activity activity = this;
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void searchDict(View view) {
        word = svWord.getQuery().toString();
        wvDictOnline.setVisibility(View.VISIBLE);
        wvDictOffline.setVisibility(View.INVISIBLE);
        DictAll m = getLollyViewModel().getCurrentDict();
        String url = RepoDictAll.urlString(m.url, word);
        svWord.post(new Runnable() {
            @Override
            public void run() {
                svWord.clearFocus();
            }
        });
        wvDictOnline.loadUrl(url);
    }

}
