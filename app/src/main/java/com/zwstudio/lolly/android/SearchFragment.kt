package com.zwstudio.lolly.android

import android.support.v4.app.Fragment
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.SearchView
import android.widget.Toast
import com.zwstudio.lolly.data.SearchViewModel
import org.androidannotations.annotations.*


@EFragment(R.layout.content_search)
class SearchFragment : Fragment() {

    @ViewById
    lateinit var svWord: SearchView
    @ViewById
    lateinit var wvDictOnline: WebView
    @ViewById
    lateinit var wvDictOffline: WebView

    @Bean
    lateinit var vm: SearchViewModel

    var word = ""
    var webViewFinished = false

    @AfterViews
    fun afterViews() {
        // http://stackoverflow.com/questions/3488664/android-launcher-label-vs-activity-title
        activity?.title = resources.getString(R.string.search)

        configWebView(wvDictOnline)
        configWebView(wvDictOffline)

        wvDictOnline.visibility = View.INVISIBLE
        wvDictOffline.visibility = View.INVISIBLE
        svWord.setQuery(vm.word, false)
        svWord.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchDict()
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
        wv.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Click(R.id.btnGo)
    fun searchDict() {
        word = svWord.query.toString()
        wvDictOnline.visibility = View.VISIBLE
        wvDictOffline.visibility = View.INVISIBLE
        val url = vm.urlString!!
        svWord.post { svWord.clearFocus() }
        wvDictOnline.loadUrl(url)
    }

}
