package com.zwstudio.lolly.android

import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.SearchView
import android.widget.Toast
import com.zwstudio.lolly.data.SettingsViewModel
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById


@EActivity(R.layout.activity_search)
class SearchActivity : DrawerActivity() {

    @ViewById
    internal lateinit var svWord: SearchView
    @ViewById
    internal lateinit var wvDictOnline: WebView
    @ViewById
    internal lateinit var wvDictOffline: WebView

    @Bean
    internal lateinit var vm: SettingsViewModel

    internal var word = ""
    internal var webViewFinished = false

    @AfterViews
    override fun afterViews() {
        super.afterViews()
        // http://stackoverflow.com/questions/3488664/android-launcher-label-vs-activity-title
        this.title = resources.getString(R.string.search)

        configWebView(wvDictOnline)
        configWebView(wvDictOffline)

        wvDictOnline.visibility = View.INVISIBLE
        wvDictOffline.visibility = View.INVISIBLE
        svWord.setQuery(vm.word, false)
        svWord.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchDict(svWord)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                vm.word = newText
                return false
            }
        })
    }

    private fun configWebView(wv: WebView) {
        wv.settings.javaScriptEnabled = true // enable javascript
        val activity = this
        wv.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun searchDict(view: View?) {
//        word = svWord.query.toString()
//        wvDictOnline.visibility = View.VISIBLE
//        wvDictOffline.visibility = View.INVISIBLE
//        val m = vm.selectedDict
//        val url = RestDictionary.urlString(m.url, word)
//        svWord.post { svWord.clearFocus() }
//        wvDictOnline.loadUrl(url)
    }

}
