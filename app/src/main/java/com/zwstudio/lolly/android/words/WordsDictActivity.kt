package com.zwstudio.lolly.android.words

import android.util.Log
import android.webkit.WebView
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.android.misc.OnlineDict
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.data.words.WordsDictViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_words_dict)
class WordsDictActivity : AppCompatActivity(), TouchListener {

    @ViewById
    lateinit var spnWord: Spinner
    @ViewById
    lateinit var spnDictReference: Spinner
    @ViewById(R.id.webView)
    lateinit var wv: WebView

    @Bean
    lateinit var vm: WordsDictViewModel
    lateinit var onlineDict: OnlineDict
    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        vm.lstWords = (intent.getSerializableExtra("list") as Array<String>).toMutableList()
        vm.selectedWordIndex = intent.getIntExtra("index", 0)

        wv.setOnTouchListener(OnSwipeWebviewTouchListener(this, this))

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
        }

        onlineDict = OnlineDict(wv, vm, compositeDisposable)
        onlineDict.initWebViewClient()
        selectedWordChanged()
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
        vm.vmSettings.updateDictReference()
        selectedDictChanged()
    }

    private fun selectedWordChanged() {
        title = vm.selectedWord
        selectedDictChanged()
    }

    private fun selectedDictChanged() {
        onlineDict.searchDict()
    }

    override fun onSwipeLeft() {
        vm.next(-1);
        selectedWordChanged()
    }

    override fun onSwipeRight() {
        vm.next(1);
        selectedWordChanged()
    }
}
