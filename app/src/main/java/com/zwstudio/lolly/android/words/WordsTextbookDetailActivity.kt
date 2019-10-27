package com.zwstudio.lolly.android.words

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.WordsUnitViewModel
import com.zwstudio.lolly.domain.MSelectItem
import com.zwstudio.lolly.domain.MUnitWord
import io.reactivex.disposables.CompositeDisposable
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
    lateinit var etLevel: TextView
    @ViewById
    lateinit var tvAccuracy: TextView

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("word") as MUnitWord
        tvTextbookName.text = "${getResources().getString(R.string.label_textbook)} ${item.textbookname}"
        tvID.text = "${getResources().getString(R.string.label_id)} ${item.id}"
        run {
            val lst = item.textbook.lstUnits
            val adapter = object : ArrayAdapter<MSelectItem>(this, android.R.layout.simple_spinner_item, lst) {
                fun convert(v: View, position: Int): View {
                    val tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = getItem(position)!!.label
                    return v
                }
                override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
                        convert(super.getView(position, convertView, parent), position)
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                        convert(super.getDropDownView(position, convertView, parent), position)
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spnUnit.adapter = adapter
            spnUnit.setSelection(item.textbook.lstUnits.indexOfFirst { it.value == item.unit })
        }

        run {
            val lst = item.textbook.lstParts
            val adapter = object : ArrayAdapter<MSelectItem>(this, android.R.layout.simple_spinner_item, lst) {
                fun convert(v: View, position: Int): View {
                    val tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.text = getItem(position)!!.label
                    return v
                }
                override fun getView(position: Int, convertView: View?, parent: ViewGroup) =
                        convert(super.getView(position, convertView, parent), position)
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
                        convert(super.getDropDownView(position, convertView, parent), position)
            }
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spnPart.adapter = adapter
            spnPart.setSelection(vm.vmSettings.lstParts.indexOfFirst { it.value == item.part })
        }
        etSeqNum.text = "${item.seqnum}"
        tvWordID.text = "${getResources().getString(R.string.label_wordid)} ${item.wordid}"
        etWord.text = item.word
        etNote.text = item.note
        tvFamiID.text = "${getResources().getString(R.string.label_famiid)} ${item.famiid}"
        etLevel.text = item.level.toString()
        tvAccuracy.text = "${getResources().getString(R.string.label_accuracy)} ${item.accuracy}"
    }

    @ItemSelect
    fun spnUnitItemSelected(selected: Boolean, position: Int) {
        item.unit = item.textbook.lstUnits[position].value
    }

    @ItemSelect
    fun spnPartItemSelected(selected: Boolean, position: Int) {
        item.part = item.textbook.lstParts[position].value
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
