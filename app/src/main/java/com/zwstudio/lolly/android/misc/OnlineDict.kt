package com.zwstudio.lolly.android.misc

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zwstudio.lolly.data.misc.DictWebViewStatus
import com.zwstudio.lolly.data.misc.IOnlineDict
import io.reactivex.rxjava3.disposables.CompositeDisposable

class OnlineDict(val wv: WebView, val iOnlineDict: IOnlineDict, val compositeDisposable: CompositeDisposable) {

    var dictStatus = DictWebViewStatus.Ready

    fun searchDict() {
        val item = iOnlineDict.getDict
        val url = iOnlineDict.getUrl
        if (item.dicttypename == "OFFLINE") {
            wv.loadUrl("about:blank")
            compositeDisposable.add(iOnlineDict.getHtml(url).subscribe {
                Log.d("HTML", it)
                val str = item.htmlString(it, iOnlineDict.getWord, true)
                wv.loadDataWithBaseURL("", str, "text/html", "UTF-8", "")
            })
        } else {
            wv.loadUrl(url)
            if (item.automation.isNotEmpty())
                dictStatus = DictWebViewStatus.Automating
            else if (item.dicttypename == "OFFLINE-ONLINE")
                dictStatus = DictWebViewStatus.Navigating
        }
    }

    fun initWebViewClient() {
        wv.settings.javaScriptEnabled = true // enable javascript
        // http://stackoverflow.com/questions/7746409/android-webview-launches-browser-when-calling-loadurl
        wv.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (dictStatus == DictWebViewStatus.Ready) return
                val item = iOnlineDict.getDict
                if (dictStatus == DictWebViewStatus.Automating) {
                    val s = item.automation.replace("{0}", iOnlineDict.getWord)
                    wv.evaluateJavascript(s) {
                        dictStatus = DictWebViewStatus.Ready
                        if (item.dicttypename == "OFFLINE-ONLINE")
                            dictStatus = DictWebViewStatus.Navigating
                    }
                } else if (dictStatus == DictWebViewStatus.Navigating) {
                    wv.evaluateJavascript("document.documentElement.outerHTML.toString()") {
                        val html = it.replace("\\u003C", "<")
                                .replace("\\\"", "\"")
                                .replace("\\n", "\n")
                                .replace("\\r", "\r")
                                .replace("\\t", "\t")
                        Log.d("HTML", html)
                        val str = item.htmlString(html, iOnlineDict.getWord, true)
                        wv.loadDataWithBaseURL("", str, "text/html", "UTF-8", "")
                        dictStatus = DictWebViewStatus.Ready
                    }
                }
            }
        }
    }
}