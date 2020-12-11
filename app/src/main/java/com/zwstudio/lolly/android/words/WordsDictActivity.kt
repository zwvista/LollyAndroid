package com.zwstudio.lolly.android.words

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.misc.DictWebViewStatus
import com.zwstudio.lolly.data.misc.SearchViewModel
import com.zwstudio.lolly.data.misc.makeAdapter
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_words_dict)
class WordsDictActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: SearchViewModel

    @ViewById
    lateinit var spnWord: Spinner
    @ViewById
    lateinit var spnDictReference: Spinner
    @ViewById(R.id.webView)
    lateinit var wv: WebView

    var dictStatus = DictWebViewStatus.Ready

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        vm.lstWords = (intent.getSerializableExtra("list") as Array<String>).toMutableList()
        vm.selectedWordIndex = intent.getIntExtra("index", 0)
        selectedWordChanged()

        run {
            val lst = vm.lstWords
            val adapter = makeAdapter(this, android.R.layout.simple_spinner_item, lst) { v, position ->
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = getItem(position)
                v
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spnWord.adapter = adapter

            spnWord.setSelection(vm.selectedWordIndex)
        }

        run {
            val lst = vm.vmSettings.lstDictsReference
            val adapter = makeAdapter(this, R.layout.spinner_item_2, android.R.id.text1, lst) { v, position ->
                val m = getItem(position)!!
                var tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = m.dictname
                (tv as? CheckedTextView)?.isChecked = spnDictReference.selectedItemPosition == position
                tv = v.findViewById<TextView>(android.R.id.text2)
                val item2 = vm.vmSettings.lstDictsReference.firstOrNull { it.dictname == m.dictname }
                tv.text = item2?.url ?: ""
                v
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            spnDictReference.adapter = adapter

            spnDictReference.setSelection(vm.vmSettings.selectedDictReferenceIndex)
        }

        wv.settings.javaScriptEnabled = true
    }

    @ItemSelect
    fun spnWordItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedWordIndex == position) return
        vm.selectedWordIndex = position
        selectedWordChanged()
    }

    @ItemSelect
    fun spnDictReferenceItemSelected(selected: Boolean, position: Int) {
        if (vm.vmSettings.selectedDictReferenceIndex == position) return
        vm.vmSettings.selectedDictReference = vm.vmSettings.lstDictsReference[position]
        Log.d("", String.format("Checked position:%d", position))
        (spnDictReference.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        compositeDisposable.add(vm.vmSettings.updateDictReference().subscribe())
        selectedDictChanged()
    }

    private fun selectedWordChanged() {
        title = vm.selectedWord
        selectedDictChanged()
    }

    private fun selectedDictChanged() {
        val item = vm.vmSettings.selectedDictReference
        val item2 = vm.vmSettings.lstDictsReference.first { it.dictname == item.dictname }
        val url = item2.urlString(vm.selectedWord, vm.vmSettings.lstAutoCorrect)
        if (item2.dicttypename == "OFFLINE") {
            wv.loadUrl("about:blank")
            compositeDisposable.add(vm.getHtml(url).subscribe {
                Log.d("HTML", it)
                val str = item2.htmlString(it, vm.selectedWord, true)
                wv.loadDataWithBaseURL("", str, "text/html", "UTF-8", "")
            })
        } else {
            // http://stackoverflow.com/questions/7746409/android-webview-launches-browser-when-calling-loadurl
            wv.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    if (dictStatus == DictWebViewStatus.Ready) return
                    if (dictStatus == DictWebViewStatus.Automating) {
                        val s = item2.automation.replace("{0}", vm.selectedWord)
                        wv.evaluateJavascript(s) {
                            dictStatus = DictWebViewStatus.Ready
                            if (item2.dicttypename == "OFFLINE-ONLINE")
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
                            val str = item2.htmlString(html, vm.selectedWord, true)
                            wv.loadDataWithBaseURL("", str, "text/html", "UTF-8", "")
                            dictStatus = DictWebViewStatus.Ready
                        }
                    }
                }
            }
            wv.loadUrl(url)
            if (item2.automation.isNotEmpty())
                dictStatus = DictWebViewStatus.Automating
            else if (item2.dicttypename == "OFFLINE-ONLINE")
                dictStatus = DictWebViewStatus.Navigating
        }
    }
}
