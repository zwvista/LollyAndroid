package com.zwstudio.lolly.android.misc

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.webkit.WebView
import android.widget.CheckedTextView
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.misc.SearchViewModel
import com.zwstudio.lolly.data.misc.SettingsListener
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.domain.misc.MDictionary
import com.zwstudio.lolly.domain.misc.MLanguage
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
    lateinit var onlineDict: OnlineDict
    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        // http://stackoverflow.com/questions/3488664/android-launcher-label-vs-activity-title
        activity!!.title = resources.getString(R.string.search)

        onlineDict = OnlineDict(wvDictReference, vm, compositeDisposable)
        onlineDict.initWebViewClient()

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
    fun spnLanguageItemSelected(selected: Boolean, selectedItem: MLanguage) {
        if (vm.vmSettings.selectedLang == selectedItem) return
        compositeDisposable.add(vm.vmSettings.setSelectedLang(selectedItem).subscribe())
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
        searchDict()
    }

    @ItemSelect
    fun spnDictReferenceItemSelected(selected: Boolean, selectedItem: MDictionary) {
        if (vm.vmSettings.selectedDictReference == selectedItem) return
        vm.vmSettings.selectedDictReference = selectedItem
        compositeDisposable.add(vm.vmSettings.updateDictReference().subscribe())
        searchDict()
    }

    fun searchDict() {
        vm.word = svWord.query.toString()
        svWord.post { svWord.clearFocus() }
        onlineDict.searchDict()
    }

}
