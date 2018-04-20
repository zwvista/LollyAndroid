package com.zwstudio.lolly.android

import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.Spinner
import android.widget.TextView
import com.zwstudio.lolly.data.SearchViewModel
import com.zwstudio.lolly.domain.DictOnline
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_words_dict)
class WordsDictActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: SearchViewModel

    @ViewById
    lateinit var spnWord: Spinner
    @ViewById
    lateinit var spnDictOnline: Spinner
    @ViewById(R.id.webView)
    lateinit var wv: WebView

    @AfterViews
    fun afterViews() {
        vm.lstWords = (intent.getSerializableExtra("list") as Array<String>).toMutableList()
        vm.selectedWordIndex = intent.getIntExtra("index", 0)
        selectWordChanged()

        run {
            val lst = vm.lstWords
            val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lst) {
                fun convert(v: View, position: Int): View {
                    val tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = getItem(position)
                    return v
                }
                override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getView(position, convertView, parent), position)
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getDropDownView(position, convertView, parent), position)
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spnWord.adapter = adapter

            spnWord.setSelection(vm.selectedWordIndex)
        }

        run {
            val lst = vm.vmSettings.lstDictsOnline
            val adapter = object : ArrayAdapter<DictOnline>(this, R.layout.spinner_item_2, android.R.id.text1, lst) {
                fun convert(v: View, position: Int): View {
                    val m = getItem(position)
                    var tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = m.dictname
                    (tv as? CheckedTextView)?.isChecked = spnDictOnline.selectedItemPosition == position
                    tv = v.findViewById<TextView>(android.R.id.text2)
                    tv.text = m.url
                    return v
                }
                override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getView(position, convertView, parent), position)
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getDropDownView(position, convertView, parent), position)
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            spnDictOnline.adapter = adapter

            spnDictOnline.setSelection(vm.vmSettings.selectedDictOnlineIndex)
        }

    }

    @ItemSelect
    fun spnWordItemSelected(selected: Boolean, position: Int) {
        if (vm.selectedWordIndex == position) return
        vm.selectedWordIndex = position
        selectWordChanged()
    }

    @ItemSelect
    fun spnDictOnlineItemSelected(selected: Boolean, position: Int) {
        if (vm.vmSettings.selectedDictOnlineIndex == position) return
        vm.vmSettings.selectedDictOnlineIndex = position
        Log.d("", String.format("Checked position:%d", position))
        (spnDictOnline.adapter as ArrayAdapter<DictOnline>).notifyDataSetChanged()
        vm.vmSettings.updateDict { }
        selectDictChanged()
    }

    private fun selectWordChanged() {
        title = vm.selectWord
        selectDictChanged()
    }

    private fun selectDictChanged() {
        val url = vm.urlString!!
        // http://stackoverflow.com/questions/7746409/android-webview-launches-browser-when-calling-loadurl
        wv.webViewClient = WebViewClient()
        wv.loadUrl(url)
    }
}