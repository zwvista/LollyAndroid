package com.zwstudio.lolly.android

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.zwstudio.lolly.data.PhrasesTextbookViewModel
import com.zwstudio.lolly.domain.TextbookPhrase
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_phrases_textbook_detail)
@OptionsMenu(R.menu.menu_save)
class PhrasesTextbookDetailActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: PhrasesTextbookViewModel
    lateinit var item: TextbookPhrase

    @ViewById
    lateinit var tvTextbookName: TextView
    @ViewById
    lateinit var tvEntryID: TextView
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
        item = intent.getSerializableExtra("phrase") as TextbookPhrase
        tvTextbookName.text = "${getResources().getString(R.string.label_textbook)} ${item.textbookname}"
        tvEntryID.text = "${getResources().getString(R.string.label_entryid)} ${item.entryid}"
        run {
            val lst = item.lstUnits
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
            val lst = item.lstParts
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
        tvPhraseID.text = "${getResources().getString(R.string.label_phraseid)} ${item.phraseid}"
        etPhrase.text = item.phrase
        etTranslation.text = item.translation
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
