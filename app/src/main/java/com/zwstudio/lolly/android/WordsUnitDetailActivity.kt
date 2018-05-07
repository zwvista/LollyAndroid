package com.zwstudio.lolly.android

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.zwstudio.lolly.data.WordsUnitViewModel
import com.zwstudio.lolly.domain.UnitWord
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_words_unit_detail)
@OptionsMenu(R.menu.menu_save)
class WordsUnitDetailActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: WordsUnitViewModel
    lateinit var item: UnitWord

    @ViewById
    lateinit var tvID: TextView
    @ViewById
    lateinit var spnUnit: Spinner
    @ViewById
    lateinit var spnPart: Spinner
    @ViewById
    lateinit var etSeqNum: TextView
    @ViewById
    lateinit var etWord: TextView
    @ViewById
    lateinit var etNote: TextView

    @AfterViews
    fun afterViews() {
        vm.lstWords = (intent.getSerializableExtra("list") as Array<UnitWord>).toMutableList()
        item = intent.getSerializableExtra("word") as UnitWord
        tvID.text = "ID: ${item.id}"
        run {
            val lst = vm.vmSettings.lstUnits
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
            spnUnit.adapter = adapter
            spnUnit.setSelection(item.unit - 1)
        }

        run {
            val lst = vm.vmSettings.lstParts
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
            spnPart.adapter = adapter
            spnPart.setSelection(item.part - 1)
        }
        etSeqNum.text = "${item.seqnum}"
        etWord.text = item.word
        etNote.text = item.note
    }

    @OptionsItem
    fun menuSave() {
        item.seqnum = etSeqNum.text.toString().toInt()
        item.word = etWord.text.toString()
        item.note = etNote.text.toString()
        if (item.id == 0) {
            vm.lstWords.add(item)
            vm.create(item.textbookid, item.unit, item.part, item.seqnum, item.word, item.note ?: "").subscribe {
                item.id = it
            }
        } else
            vm.update(item.id, item.textbookid, item.unit, item.part, item.seqnum, item.word, item.note ?: "").subscribe()
        setResult(Activity.RESULT_OK);
        finish()
    }
}
