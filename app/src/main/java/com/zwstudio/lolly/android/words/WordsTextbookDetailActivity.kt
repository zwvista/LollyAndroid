package com.zwstudio.lolly.android.words

import android.app.Activity
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.data.words.WordsUnitViewModel
import com.zwstudio.lolly.domain.misc.MSelectItem
import com.zwstudio.lolly.domain.wpp.MUnitWord
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_words_textbook_detail)
@OptionsMenu(R.menu.menu_save)
class WordsTextbookDetailActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: WordsUnitViewModel
    lateinit var item: MUnitWord

    @ViewById
    lateinit var tvTextbookName: TextView
    @ViewById
    lateinit var tvID: TextView
    @ViewById
    lateinit var spnUnit: Spinner
    @ViewById
    lateinit var spnPart: Spinner
    @ViewById
    lateinit var etSeqNum: TextView
    @ViewById
    lateinit var tvWordID: TextView
    @ViewById
    lateinit var etWord: TextView
    @ViewById
    lateinit var etNote: TextView
    @ViewById
    lateinit var tvFamiID: TextView
    @ViewById
    lateinit var tvAccuracy: TextView

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("word") as MUnitWord
        tvTextbookName.text = "${resources.getString(R.string.label_textbook)} ${item.textbookname}"
        tvID.text = "${resources.getString(R.string.label_id)} ${item.id}"
        run {
            val lst = item.textbook.lstUnits
            val adapter = makeAdapter(this, android.R.layout.simple_spinner_item, lst) { v, position ->
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = getItem(position)!!.label
                v
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spnUnit.adapter = adapter
            spnUnit.setSelection(item.textbook.lstUnits.indexOfFirst { it.value == item.unit })
        }

        run {
            val lst = item.textbook.lstParts
            val adapter = makeAdapter(this, android.R.layout.simple_spinner_item, lst) { v, position ->
                val tv = v.findViewById<TextView>(android.R.id.text1)
                tv.text = getItem(position)!!.label
                v
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spnPart.adapter = adapter
            spnPart.setSelection(vm.vmSettings.lstParts.indexOfFirst { it.value == item.part })
        }
        etSeqNum.text = "${item.seqnum}"
        tvWordID.text = "${resources.getString(R.string.label_wordid)} ${item.wordid}"
        etWord.text = item.word
        etNote.text = item.note
        tvFamiID.text = "${resources.getString(R.string.label_famiid)} ${item.famiid}"
        tvAccuracy.text = "${resources.getString(R.string.label_accuracy)} ${item.accuracy}"
    }

    @ItemSelect
    fun spnUnitItemSelected(selected: Boolean, selectedItem: MSelectItem) {
        item.unit = selectedItem.value
    }

    @ItemSelect
    fun spnPartItemSelected(selected: Boolean, selectedItem: MSelectItem) {
        item.part = selectedItem.value
    }

    @OptionsItem
    fun menuSave() {
        item.seqnum = etSeqNum.text.toString().toInt()
        item.word = vm.vmSettings.autoCorrectInput(etWord.text.toString())
        item.note = etNote.text.toString()
        setResult(Activity.RESULT_OK)
        finish()
    }
}
