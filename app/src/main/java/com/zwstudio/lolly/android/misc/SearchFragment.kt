package com.zwstudio.lolly.android.misc

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.fragment.app.Fragment
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.misc.SearchViewModel
import com.zwstudio.lolly.data.misc.SettingsListener
import com.zwstudio.lolly.data.misc.makeAdapter
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*


@EFragment(R.layout.content_search)
class SearchFragment : Fragment(), SettingsListener {

    @ViewById
    lateinit var svWord: SearchView
    @ViewById
    lateinit var spnLanguage: Spinner
    @ViewById
    lateinit var spnDictReference: Spinner
    @ViewById
    lateinit var wvDictReference: WebView

    @Bean
    lateinit var vm: SearchViewModel

    var webViewFinished = false

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        // http://stackoverflow.com/questions/3488664/android-launcher-label-vs-activity-title
        activity!!.title = resources.getString(R.string.search)

        wvDictReference.settings.javaScriptEnabled = true // enable javascript
        wvDictReference.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show()
            }
        }

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

        vm.vmSettings.handler = Handler(Looper.getMainLooper())
        vm.vmSettings.settingsListener = this
        compositeDisposable.add(vm.vmSettings.getData().subscribe())
    }

    override fun onGetData() {
        val lst = vm.vmSettings.lstLanguages
        val adapter = makeAdapter(context!!, android.R.layout.simple_spinner_item, lst) { v, position ->
            val ctv = v.findViewById<TextView>(android.R.id.text1)
            ctv.text = lst[position].langname
            ctv.setTextColor(Color.BLUE)
            v
        }
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        spnLanguage.adapter = adapter

        spnLanguage.setSelection(vm.vmSettings.selectedLangIndex)
    }

    @ItemSelect
    fun spnLanguageItemSelected(selected: Boolean, position: Int) {
        if (vm.vmSettings.selectedLangIndex == position) return
        Log.d("", String.format("Checked position:%d", position))
        compositeDisposable.add(vm.vmSettings.setSelectedLang(vm.vmSettings.lstLanguages[position]).subscribe())
    }

    override fun onUpdateLang() {
        val lst = vm.vmSettings.lstDictsReference
        val adapter = makeAdapter(activity!!, R.layout.spinner_item_2, android.R.id.text1, lst) { v, position ->
            val item = getItem(position)!!
            var tv = v.findViewById<TextView>(android.R.id.text1)
            tv.text = item.dictname
            (tv as? CheckedTextView)?.isChecked = spnDictReference.selectedItemPosition == position
            tv = v.findViewById<TextView>(android.R.id.text2)
            tv.text = item.url
            v
        }
        adapter.setDropDownViewResource(R.layout.list_item_2)
        spnDictReference.adapter = adapter

        spnDictReference.setSelection(vm.vmSettings.selectedDictReferenceIndex)
        onUpdateDictReference()
    }

    @ItemSelect
    fun spnDictReferenceItemSelected(selected: Boolean, position: Int) {
        if (vm.vmSettings.selectedDictReferenceIndex == position) return
        vm.vmSettings.selectedDictReference = vm.vmSettings.lstDictsReference[position]
        Log.d("", String.format("Checked position:%d", position))
        (spnDictReference.adapter as ArrayAdapter<*>).notifyDataSetChanged()
        compositeDisposable.add(vm.vmSettings.updateDictReference().subscribe())
        searchDict()
    }

    fun searchDict() {
        vm.word = svWord.query.toString()
        val item = vm.vmSettings.selectedDictReference
        val url = item.urlString(vm.word, vm.vmSettings.lstAutoCorrect)
        svWord.post { svWord.clearFocus() }
        wvDictReference.loadUrl(url)
    }

}
