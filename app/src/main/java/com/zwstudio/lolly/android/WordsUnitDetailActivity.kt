package com.zwstudio.lolly.android

import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.zwstudio.lolly.data.WordsUnitViewModel
import com.zwstudio.lolly.domain.UnitWord
import org.androidannotations.annotations.AfterViews
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EActivity
import org.androidannotations.annotations.ViewById

@EActivity(R.layout.activity_words_unit_detail)
class WordsUnitDetailActivity : AppCompatActivity() {

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
    lateinit var etWord: TextView
    @ViewById
    lateinit var etNote: TextView

    @AfterViews
    fun afterViews() {
        val item = intent.getSerializableExtra("word") as UnitWord
        tvID.text = "ID: ${item.id}"
        etUnit.text = "${item.unit}"
        etPart.text = "${item.part}"
        etSeqNum.text = "${item.seqnum}"
        etWord.text = item.word
        etNote.text = item.note
    }
}
