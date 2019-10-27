package com.zwstudio.lolly.android

import androidx.fragment.app.Fragment
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import com.zwstudio.lolly.data.SearchViewModel
import com.zwstudio.lolly.domain.MDictItem
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.*


@EFragment(R.layout.content_search)
class SearchFragment : Fragment() {

    @ViewById
    lateinit var svWord: SearchView
    @ViewById
    lateinit var spnDictItem: Spinner
    @ViewById
    lateinit var wvDictReference: WebView
    @ViewById
    lateinit var wvDictOffline: WebView

    @Bean
    lateinit var vm: SearchViewModel

    var word = ""
    var webViewFinished = false

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        // http://stackoverflow.com/questions/3488664/android-launcher-label-vs-activity-title
        activity!!.title = resources.getString(R.string.search)

        configWebView(wvDictReference)
        configWebView(wvDictOffline)

        wvDictReference.visibility = View.INVISIBLE
        wvDictOffline.visibility = View.INVISIBLE
        svWord.setQuery(word, false)
        svWord.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchDict()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                word = newText
                return false
            }
        })

        compositeDisposable.add(vm.app.initializeObject.subscribe {
            val lst = vm.vmSettings.lstDictItems
            val adapter = object : ArrayAdapter<MDictItem>(context, R.layout.spinner_item_2, android.R.id.text1, lst) {
                fun convert(v: View, position: Int): View {
                    val m = getItem(position)!!
                    var tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = m.dictname
                    (tv as? CheckedTextView)?.isChecked = spnDictItem.selectedItemPosition == position
                    tv = v.findViewById<TextView>(android.R.id.text2)
                    val item2 = vm.vmSettings.lstDictsReference.firstOrNull { it.dictname == m.dictname }
                    tv.text = item2?.url ?: ""
                    return v
                }
                override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getView(position, convertView, parent), position)
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                    convert(super.getDropDownView(position, convertView, parent), position)
            }
            adapter.setDropDownViewResource(R.layout.list_item_2)
            spnDictItem.adapter = adapter

            spnDictItem.setSelection(vm.vmSettings.selectedDictItemIndex)
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

    @ItemSelect
    fun spnDictItemItemSelected(selected: Boolean, position: Int) {
        if (vm.vmSettings.selectedDictItemIndex == position) return
        vm.vmSettings.selectedDictItem = vm.vmSettings.lstDictItems[position]
        Log.d("", String.format("Checked position:%d", position))
        (spnDictItem.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        compositeDisposable.add(vm.vmSettings.updateDictItem().subscribe())
        searchDict()
    }

    fun searchDict() {
        word = svWord.query.toString()
        wvDictReference.visibility = View.VISIBLE
        wvDictOffline.visibility = View.INVISIBLE
        val item = vm.vmSettings.selectedDictItem
        val item2 = vm.vmSettings.lstDictsReference.first { it.dictname == item.dictname }
        val url = item2.urlString(word, vm.vmSettings.lstAutoCorrect)
        svWord.post { svWord.clearFocus() }
        wvDictReference.loadUrl(url)
    }

}
