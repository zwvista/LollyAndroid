package com.zwstudio.lolly.android.phrases

import android.app.Activity
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zwstudio.lolly.android.R
import com.zwstudio.lolly.data.misc.makeAdapter
import com.zwstudio.lolly.data.phrases.PhrasesUnitViewModel
import com.zwstudio.lolly.domain.wpp.MUnitPhrase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_phrases_textbook_detail)
@OptionsMenu(R.menu.menu_save)
class PhrasesTextbookDetailActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: PhrasesUnitViewModel
    lateinit var item: MUnitPhrase

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
    lateinit var tvPhraseID: TextView
    @ViewById
    lateinit var etPhrase: TextView
    @ViewById
    lateinit var etTranslation: TextView

    val compositeDisposable = CompositeDisposable()

    @AfterViews
    fun afterViews() {
        item = intent.getSerializableExtra("phrase") as MUnitPhrase
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
        tvPhraseID.text = "${resources.getString(R.string.label_phraseid)} ${item.phraseid}"
        etPhrase.text = item.phrase
        etTranslation.text = item.translation
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
        item.phrase = vm.vmSettings.autoCorrectInput(etPhrase.text.toString())
        item.translation = etTranslation.text.toString()
//        compositeDisposable.add(vm.update(item.id, item.langid, item.textbookid, item.unit, item.part, item.seqnum, item.phraseid, item.phrase, item.translation).subscribe())
        setResult(Activity.RESULT_OK)
        finish()
    }
}
