package com.zwstudio.lolly.android

import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.zwstudio.lolly.data.PhrasesUnitViewModel
import com.zwstudio.lolly.domain.UnitPhrase
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_phrases_unit_detail)
@OptionsMenu(R.menu.menu_save)
class PhrasesUnitDetailActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: PhrasesUnitViewModel
    lateinit var item: UnitPhrase

    @ViewById
    lateinit var tvID: TextView
    @ViewById
    lateinit var spnUnit: Spinner
    @ViewById
    lateinit var spnPart: Spinner
    @ViewById
    lateinit var etSeqNum: TextView
    @ViewById
    lateinit var etPhrase: TextView
    @ViewById
    lateinit var etTranslation: TextView

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        vm.lstPhrases = (intent.getSerializableExtra("list") as Array<UnitPhrase>).toMutableList()
        item = intent.getSerializableExtra("phrase") as UnitPhrase
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
        etPhrase.text = item.phrase
        etTranslation.text = item.translation
    }

    @OptionsItem
    fun menuSave() {
        item.seqnum = etSeqNum.text.toString().toInt()
        item.phrase = vm.vmSettings.autoCorrectInput(etPhrase.text.toString())
        item.translation = etTranslation.text.toString()
        if (item.id == 0) {
            vm.lstPhrases.add(item)
            compositeDisposable.add(vm.create(item.langid, item.textbookid, item.unit, item.part, item.seqnum, item.langphraseid, item.phrase, item.translation).subscribe {
                item.id = it
            })
        } else
            compositeDisposable.add(vm.update(item.id, item.langid, item.textbookid, item.unit, item.part, item.seqnum, item.langphraseid, item.phrase, item.translation).subscribe())
    }
}
