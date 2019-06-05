package com.zwstudio.lolly.android.phrases

import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.PhrasesUnitViewModel
import com.zwstudio.lolly.domain.MSelectItem
import com.zwstudio.lolly.domain.MUnitPhrase
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_phrases_unit_detail)
@OptionsMenu(R.menu.menu_save)
class PhrasesUnitDetailActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: PhrasesUnitViewModel
    lateinit var item: MUnitPhrase

    @ViewById
    lateinit var tvID: TextView
    @ViewById
    lateinit var spnUnit: Spinner
    @ViewById
    lateinit var spnPart: Spinner
    @ViewById
    lateinit var etSeqNum: TextView
    @ViewById
    lateinit var tvPhraseID: TextView
    @ViewById
    lateinit var etPhrase: TextView
    @ViewById
    lateinit var etTranslation: TextView

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("phrase") as MUnitPhrase
        tvID.text = "${getResources().getString(R.string.label_id)} ${item.id}"
        run {
            val lst = vm.vmSettings.lstUnits
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
            spnUnit.setSelection(vm.vmSettings.lstUnits.indexOfFirst { it.value == item.unit })
        }

        run {
            val lst = vm.vmSettings.lstParts
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
        tvPhraseID.text = "${getResources().getString(R.string.label_phraseid)} ${item.phraseid}"
        etPhrase.text = item.phrase
        etTranslation.text = item.translation
    }

    @ItemSelect
    fun spnUnitItemSelected(selected: Boolean, position: Int) {
        item.unit = vm.vmSettings.lstUnits[position].value
    }

    @ItemSelect
    fun spnPartItemSelected(selected: Boolean, position: Int) {
        item.part = vm.vmSettings.lstParts[position].value
    }

    @OptionsItem
    fun menuSave() {
        item.seqnum = etSeqNum.text.toString().toInt()
        item.phrase = vm.vmSettings.autoCorrectInput(etPhrase.text.toString())
        item.translation = etTranslation.text.toString()
        if (item.id == 0)
            compositeDisposable.add(vm.create(item.langid, item.textbookid, item.unit, item.part, item.seqnum, item.phraseid, item.phrase, item.translation).subscribe {
                item.id = it
            })
        else
            compositeDisposable.add(vm.update(item.id, item.langid, item.textbookid, item.unit, item.part, item.seqnum, item.phraseid, item.phrase, item.translation).subscribe())
    }
}
