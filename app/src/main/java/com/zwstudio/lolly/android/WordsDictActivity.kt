package com.zwstudio.lolly.android

import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zwstudio.lolly.data.SearchViewModel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@EActivity(R.layout.activity_word_dict)
class WordsDictActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: SearchViewModel

    @ViewById(R.id.webView)
    lateinit var wv: WebView

    @AfterViews
    fun afterViews() {
        vm.word = intent.getStringExtra("word")
        title = vm.word
        val url = vm.urlString!!
        // http://stackoverflow.com/questions/7746409/android-webview-launches-browser-when-calling-loadurl
        wv.webViewClient = WebViewClient()
        wv.loadUrl(url)
    }
}
