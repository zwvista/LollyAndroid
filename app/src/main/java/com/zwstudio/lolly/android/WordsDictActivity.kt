package com.zwstudio.lolly.android

import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import com.zwstudio.lolly.data.SettingsViewModel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@EActivity(R.layout.activity_word)
class WordsDictActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: SettingsViewModel

    @ViewById(R.id.webView)
    lateinit var wv: WebView

    @AfterViews
    fun afterViews() {

//        val intent = intent
//        val word = intent.getStringExtra("word")
//        title = word
//        val m = vm.selectedDict
//        val url = RestDictionary.urlString(m.url, word)
//        // http://stackoverflow.com/questions/7746409/android-webview-launches-browser-when-calling-loadurl
//        wv.webViewClient = WebViewClient()
//        wv.loadUrl(url)
    }
}
