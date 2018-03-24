package com.zwstudio.lolly.android

import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.zwstudio.lolly.data.WordsUnitViewModel
import com.zwstudio.lolly.domain.UnitPhrase
import org.androidannotations.annotations.*

@EActivity(R.layout.activity_phrases_unit_detail)
@OptionsMenu(R.menu.menu_save)
class PhrasesUnitDetailActivity : AppCompatActivity() {

    @Bean
    lateinit var vm: WordsUnitViewModel

    @ViewById
    lateinit var tvID: TextView
    @ViewById
    lateinit var etUnit: TextView
    @ViewById
    lateinit var etPart: TextView
    @ViewById
    lateinit var etSeqNum: TextView
    @ViewById
    lateinit var etPhrase: TextView
    @ViewById
    lateinit var etTranslation: TextView

    @AfterViews
    fun afterViews() {
        val item = intent.getSerializableExtra("phrase") as UnitPhrase
        tvID.text = "ID: ${item.id}"
        etUnit.text = "${item.unit}"
        etPart.text = "${item.part}"
        etSeqNum.text = "${item.seqnum}"
        etPhrase.text = item.phrase
        etTranslation.text = item.translation
    }

    @OptionsItem
    fun menuSave() {

    }
}
