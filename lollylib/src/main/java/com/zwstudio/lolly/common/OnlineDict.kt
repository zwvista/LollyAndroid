package com.zwstudio.lolly.common

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient

enum class DictWebViewStatus {
    Ready, Navigating, Automating
}

interface IOnlineDict {
    val getWord: String
}

class OnlineDict {

    lateinit var wv: WebView
    lateinit var iOnlineDict: IOnlineDict
    var dictStatus = DictWebViewStatus.Ready

    suspend fun searchDict() {
        val item = vmSettings.selectedDictReference
        val url = item.urlString(iOnlineDict.getWord, vmSettings.lstAutoCorrect)
        if (item.dicttypename == "OFFLINE") {
            wv.loadUrl("about:blank")
            val s = vmSettings.getHtml(url)
            Log.d("HTML", s)
            val str = item.htmlString(s, iOnlineDict.getWord, true)
            wv.loadDataWithBaseURL("", str, "text/html", "UTF-8", "")
        } else {
            wv.loadUrl(url)
            if (item.automation.isNotEmpty())
                dictStatus = DictWebViewStatus.Automating
            else if (item.dicttypename == "OFFLINE-ONLINE")
                dictStatus = DictWebViewStatus.Navigating
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun initWebViewClient() {
        wv.settings.javaScriptEnabled = true // enable javascript
        // http://stackoverflow.com/questions/7746409/android-webview-launches-browser-when-calling-loadurl
        wv.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (dictStatus == DictWebViewStatus.Ready) return
                val item = vmSettings.selectedDictReference
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
